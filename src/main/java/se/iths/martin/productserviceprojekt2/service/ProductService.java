package se.iths.martin.productserviceprojekt2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.iths.martin.productserviceprojekt2.dto.ProductRequestDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductResponseDTO;
import se.iths.martin.productserviceprojekt2.mapper.ProductMapper;
import se.iths.martin.productserviceprojekt2.model.Product;
import se.iths.martin.productserviceprojekt2.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Hämta alla produkter
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    // Hämta specifik produkt
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toResponseDTO(product);
    }

    // Ta bort produkt

    // Skapa produkt
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product newProduct = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(newProduct);
        return productMapper.toResponseDTO(savedProduct);
    }

    // Minska lagersaldo
}
