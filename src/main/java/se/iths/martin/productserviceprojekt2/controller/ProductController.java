package se.iths.martin.productserviceprojekt2.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.iths.martin.productserviceprojekt2.dto.ProductInfoDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductRequestDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductResponseDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductStockRequest;
import se.iths.martin.productserviceprojekt2.service.ProductService;

import java.util.List;


/**
 * REST-controller för hantering av produkter. Samtiliga endpoints kräver en giltig JWT. Skapandet och borttagning av produkter kräver dessutom rollen {@code ADMIN}.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "authorization")
public class ProductController {

    private final ProductService productService;

    /**
     * Skapar en ny produkt.
     * <p>
     * Kräver rollen {@code ADMIN}. Indata valideras enligt constraints
     * i {@link ProductRequestDTO}.
     *
     * @param requestDTO produkt data från klienten.
     * @return den skapade produkten med ett id.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO responseDTO = productService.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Hämtar samtliga produkter.
     *
     * @return lista med alla produkter, tom om inga produkter finns.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> responseDTOS = productService.getAllProducts();
        return ResponseEntity.ok(responseDTOS);
    }

    /**
     * Hämtar en produkt via id.
     *
     * @param id produktens id
     * @return produkten
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Tar bort produkt via id. Kräver rollen {@code AMDIN}
     *
     * @param id produktens id
     * @return {@code 204 No Content} vid lyckad borttagning
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Minskar lagersaldoför en eller flera produkter. Samtliga produkter i förfrågan valideras innan något lager minskas. Antingen lyckas hela anropet eller inget.
     *
     * @param requests lista med produkter av data typen {@link ProductStockRequest}
     * @return skickar tillbaka en lista av uppdaterad produkter ({@link ProductInfoDTO})
     */
    @PostMapping("/stock/decrease")
    public ResponseEntity<List<ProductInfoDTO>> decreaseStock(
            @Valid @RequestBody List<ProductStockRequest> requests) {
        List<ProductInfoDTO> result = productService.decreaseStock(requests);
        return ResponseEntity.ok(result);
    }
}

