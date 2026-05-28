package se.iths.martin.productserviceprojekt2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.martin.productserviceprojekt2.dto.ProductRequestDTO;
import se.iths.martin.productserviceprojekt2.dto.ProductStockRequest;
import se.iths.martin.productserviceprojekt2.model.Product;
import se.iths.martin.productserviceprojekt2.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;


    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    // hjälp metod
    private Product saveProduct(String productName, BigDecimal price, int stock) {
        return productRepository.save(Product.builder()
                .name(productName)
                .description("A test product")
                .price(price)
                .stock(stock)
                .build());
    }

    @Test
    @DisplayName("Testing create product as ADMIN")
    @WithMockUser(roles = "ADMIN")
    public void createProductAdmin() throws Exception {
        ProductRequestDTO requestDTO = ProductRequestDTO.builder()
                .name("Test Product")
                .price(new BigDecimal("199.99"))
                .stock(10)
                .description("Test description")
                .build();

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(199.99))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.description").value("Test description"));
    }

    @Test
    @DisplayName("Testing create product as USER (fail)")
    @WithMockUser(roles = "USER")
    public void createProductUser() throws Exception {
        ProductRequestDTO requestDTO = ProductRequestDTO.builder()
                .name("Test Product")
                .price(new BigDecimal("199.99"))
                .stock(10)
                .description("Test description")
                .build();

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Testing create product as ADMIN with blank values")
    public void createProductAdminWithBlankValues() throws Exception {
        ProductRequestDTO requestDTO = ProductRequestDTO.builder()
                .name("")
                .price(new BigDecimal("199.99"))
                .stock(0)
                .description("Test description")
                .build();

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Testing fetching all products")
    public void fetchAllProducts() throws Exception {
        saveProduct("Keyboard", new BigDecimal("499"), 20);
        saveProduct("Mouse", new BigDecimal("199"), 50);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Keyboard", "Mouse")));
    }

    @Test
    @DisplayName("Testing fetching product by id")
    public void fetchProductById() throws Exception {
        Product saved = saveProduct("Keyboard", new BigDecimal("499"), 20);

        mockMvc.perform(get("/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Keyboard"))
                .andExpect(jsonPath("$.price").value(499))
                .andExpect(jsonPath("$.stock").value(20));
    }

    @Test
    @DisplayName("Testing fetching product by id that does not exist")
    public void fetchProductByIdNotFound() throws Exception {
        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found with id: 99"));
    }

    @Test
    @DisplayName("Testing deleting product as ADMIN")
    @WithMockUser(roles = "ADMIN")
    public void deleteProductAdmin() throws Exception {
        Product saved = saveProduct("Keyboard", new BigDecimal("499"), 20);

        mockMvc.perform(delete("/products/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Testing deleting product as USER (fail)")
    @WithMockUser(roles = "USER")
    public void deleteProductUser() throws Exception {
        Product saved = saveProduct("Keyboard", new BigDecimal("499"), 20);

        mockMvc.perform(delete("/products/" + saved.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Testing deleting product that does not exist")
    public void deleteProductNotFound() throws Exception {
        mockMvc.perform(delete("/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testing decreasing product stock")
    public void decreaseProductStock() throws Exception {
        Product saved1 = saveProduct("Keyboard", new BigDecimal("499"), 20);
        Product saved2 = saveProduct("Mouse", new BigDecimal("299"), 10);

        List<ProductStockRequest> requests = List.of(
                new ProductStockRequest(saved1.getId(), 5),
                new ProductStockRequest(saved2.getId(), 3)
        );

        mockMvc.perform(post("/products/stock/decrease")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].stock").value(15))
                .andExpect(jsonPath("$[1].stock").value(7));
    }

    @Test
    @DisplayName("Testing decreasing product stock with insufficient stock")
    public void decreaseProductStockInsufficient() throws Exception {
        Product saved1 = saveProduct("Keyboard", new BigDecimal("499"), 20);
        Product saved2 = saveProduct("Mouse", new BigDecimal("299"), 10);

        List<ProductStockRequest> requests = List.of(
                new ProductStockRequest(saved1.getId(), 5),
                new ProductStockRequest(saved2.getId(), 13)
        );

        mockMvc.perform(post("/products/stock/decrease")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Insufficient stock for product '" + saved2.getName() + "': requested " + 13 + ", available " + saved2.getStock()));
    }

    @Test
    @DisplayName("Testing decreasing product stock that does not exist")
    public void decreaseProductStockNotFound() throws Exception {
        List<ProductStockRequest> requests = List.of(
                new ProductStockRequest(99L, 5),
                new ProductStockRequest(12L, 3)
        );

        mockMvc.perform(post("/products/stock/decrease")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isNotFound());
    }
}
