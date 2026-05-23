package se.iths.martin.productserviceprojekt2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.iths.martin.productserviceprojekt2.dto.ProductRequestDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductResponseDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductStockRequest;
import se.iths.martin.productserviceprojekt2.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Create product endpoint (ADMIN only)
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO responseDTO = productService.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // List all products endpoint
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> responseDTOS = productService.getAllProducts();
        return ResponseEntity.ok(responseDTOS);
    }

    // Get a single product endpoint
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Delete a product endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    // Decrease stock endpoint
    @PostMapping("/stock/decrease")
    public ResponseEntity<List<ProductResponseDTO>> decreaseStock(
            @Valid @RequestBody List<ProductStockRequest> requests) {
        List<ProductResponseDTO> result = productService.decreaseStock(requests);
        return ResponseEntity.ok(result);
    }
}

