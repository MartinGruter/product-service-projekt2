package se.iths.martin.productserviceprojekt2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.iths.martin.productserviceprojekt2.dto.ProductRequestDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductResponseDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductStockRequest;
import se.iths.martin.productserviceprojekt2.exception.InsufficientStockException;
import se.iths.martin.productserviceprojekt2.exception.ProductNotFoundException;
import se.iths.martin.productserviceprojekt2.mapper.ProductMapper;
import se.iths.martin.productserviceprojekt2.model.Product;
import se.iths.martin.productserviceprojekt2.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    // Hämta alla produkter
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    // Hämta specifik produkt
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponseDTO(product);
    }

    // Ta bort produkt
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    // Skapa produkt
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product newProduct = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(newProduct);
        return productMapper.toResponseDTO(savedProduct);
    }

    // Minska lagersaldo
    @Transactional
    public List<ProductResponseDTO> decreaseStock(List<ProductStockRequest> stockRequests) {
        // hämta alla produkter
        // kontrollera att varje produkt verkligen finns och att stock räcker
        List<Product> products = stockRequests.stream().map(req -> {
            // kasta exception om inte en produkt finns eller stock inte räcker
            Product pro = productRepository.findById(req.getProductId()).orElseThrow(() -> new ProductNotFoundException(req.getProductId()));
            if (pro.getStock() < req.getQuantity()) {
                throw new InsufficientStockException(pro.getName(), req.getQuantity(), pro.getStock());
            }
            return pro;
        }).toList();

        // minska stock
        List<Product> updated = stockRequests.stream()
                .map(request -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(request.getProductId()))
                            .findFirst()
                            .get();

                    product.setStock(product.getStock() - request.getQuantity());
                    return productRepository.save(product);
                })
                .toList();

        // returnera produktinfo
        return updated.stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }
}
