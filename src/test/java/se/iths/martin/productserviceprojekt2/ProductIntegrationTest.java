package se.iths.martin.productserviceprojekt2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.martin.productserviceprojekt2.model.Product;
import se.iths.martin.productserviceprojekt2.repository.ProductRepository;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
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
    public void createProductAdmin() {
    }

    @Test
    @DisplayName("Testing create product as USER (fail)")
    public void createProductUser() {
    }

    @Test
    @DisplayName("Testing create product as ADMIN with blank values")
    public void createProductAdminWithBlankValues() {
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
    public void deleteProductAdmin() {
    }

    @Test
    @DisplayName("Testing deleting product as USER (fail)")
    public void deleteProductUser() {
    }

    @Test
    @DisplayName("Testing deleting product that does not exist")
    public void deleteProductNotFound() {
    }

    @Test
    @DisplayName("Testing decreasing product stock")
    public void decreaseProductStock() {
    }

    @Test
    @DisplayName("Testing decreasing product stock with insufficient stock")
    public void decreaseProductStockInsufficient() {
    }

    @Test
    @DisplayName("Testing decreasing product stock that does not exist")
    public void decreaseProductStockNotFound() {
    }
}
