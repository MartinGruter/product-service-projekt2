package se.iths.martin.productserviceprojekt2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.iths.martin.productserviceprojekt2.dto.ProductRequestDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductResponseDTO;
import se.iths.martin.productserviceprojekt2.mapper.ProductMapper;
import se.iths.martin.productserviceprojekt2.model.Product;
import se.iths.martin.productserviceprojekt2.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Hämta alla produkter

    // Hämta specifik produkt

    // Ta bort produkt

    // Skapa produkt
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product newProduct = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(newProduct);
        return productMapper.toResponseDTO(savedProduct);
    }

    // Minska lagersaldo
}
