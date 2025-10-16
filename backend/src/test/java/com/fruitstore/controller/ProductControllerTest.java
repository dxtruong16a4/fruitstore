package com.fruitstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitstore.dto.request.product.CreateProductRequest;
import com.fruitstore.dto.request.product.ProductFilterRequest;
import com.fruitstore.dto.request.product.UpdateProductRequest;
import com.fruitstore.dto.response.product.ProductListResponse;
import com.fruitstore.dto.response.product.ProductResponse;
import com.fruitstore.dto.response.category.CategoryResponse;
import com.fruitstore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for ProductController
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "app.jwt.secret=testSecretKeyForJWTTokenGenerationAndValidationInTests",
    "app.jwt.expiration=86400000",
    "app.cors.allowed-origins=http://localhost:3000",
    "app.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS",
    "app.cors.allowed-headers=*",
    "app.cors.allow-credentials=true"
})
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private CategoryResponse category;
    private ProductResponse product1;
    private ProductResponse product2;
    private CreateProductRequest createRequest;
    private UpdateProductRequest updateRequest;
    private ProductFilterRequest filterRequest;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();

        category = new CategoryResponse();
        category.setCategoryId(1L);
        category.setName("Trái cây tươi");
        category.setDescription("Các loại trái cây tươi ngon");
        category.setIsActive(true);

        product1 = new ProductResponse();
        product1.setProductId(1L);
        product1.setName("Táo Fuji Nhật Bản");
        product1.setDescription("Táo Fuji nhập khẩu từ Nhật Bản");
        product1.setPrice(new BigDecimal("150000.00"));
        product1.setStockQuantity(100);
        product1.setImageUrl("/images/products/apple-fuji.jpg");
        product1.setCategory(category);
        product1.setIsActive(true);
        product1.setCreatedAt(LocalDateTime.now());
        product1.setUpdatedAt(LocalDateTime.now());

        product2 = new ProductResponse();
        product2.setProductId(2L);
        product2.setName("Cam Úc");
        product2.setDescription("Cam tươi nhập khẩu từ Úc");
        product2.setPrice(new BigDecimal("80000.00"));
        product2.setStockQuantity(150);
        product2.setImageUrl("/images/products/orange.jpg");
        product2.setCategory(category);
        product2.setIsActive(true);
        product2.setCreatedAt(LocalDateTime.now());
        product2.setUpdatedAt(LocalDateTime.now());

        createRequest = new CreateProductRequest();
        createRequest.setName("Nho Mỹ");
        createRequest.setDescription("Nho xanh không hạt nhập khẩu từ Mỹ");
        createRequest.setPrice(new BigDecimal("200000.00"));
        createRequest.setStockQuantity(60);
        createRequest.setImageUrl("/images/products/grape.jpg");
        createRequest.setCategoryId(1L);
        createRequest.setIsActive(true);

        updateRequest = new UpdateProductRequest();
        updateRequest.setName("Táo Fuji Nhật Bản cập nhật");
        updateRequest.setDescription("Mô tả cập nhật");
        updateRequest.setPrice(new BigDecimal("160000.00"));
        updateRequest.setStockQuantity(120);
        updateRequest.setImageUrl("/images/products/updated.jpg");
        updateRequest.setCategoryId(1L);
        updateRequest.setIsActive(true);

        filterRequest = new ProductFilterRequest();
        filterRequest.setCategoryId(1L);
        filterRequest.setMinPrice(new BigDecimal("100000.00"));
        filterRequest.setMaxPrice(new BigDecimal("300000.00"));
        filterRequest.setKeyword("táo");
        filterRequest.setPage(0);
        filterRequest.setSize(10);
        filterRequest.setSortBy("name");
        filterRequest.setSortDirection("asc");
    }

    @Test
    public void testGetAllProducts() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(product1, product2);
        Page<ProductResponse> productPage = new PageImpl<>(products, PageRequest.of(0, 20), 2);
        when(productService.getAllProducts(any())).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.content[0].productId").value(1))
                .andExpect(jsonPath("$.data.content[0].name").value("Táo Fuji Nhật Bản"));
    }

    @Test
    public void testGetAllProductsWithPagination() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(product1);
        Page<ProductResponse> productPage = new PageImpl<>(products, PageRequest.of(0, 1), 2);
        when(productService.getAllProducts(any())).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sortBy", "price")
                        .param("sortDirection", "desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    public void testGetProductById() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(product1);

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productId").value(1))
                .andExpect(jsonPath("$.data.name").value("Táo Fuji Nhật Bản"))
                .andExpect(jsonPath("$.data.price").value(150000.00))
                .andExpect(jsonPath("$.data.stockQuantity").value(100));
    }

    @Test
    public void testGetActiveProductById() throws Exception {
        // Given
        when(productService.getActiveProductById(1L)).thenReturn(product1);

        // When & Then
        mockMvc.perform(get("/api/products/1/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productId").value(1))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    public void testGetProductsByCategory() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(product1, product2);
        Page<ProductResponse> productPage = new PageImpl<>(products, PageRequest.of(0, 20), 2);
        when(productService.getProductsByCategory(anyLong(), any())).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products/category/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    public void testSearchProducts() throws Exception {
        // Given
        ProductListResponse productList = new ProductListResponse(
                Arrays.asList(product1), 1, 1L, 0, 10);
        when(productService.searchProducts(any(ProductFilterRequest.class))).thenReturn(productList);

        // When & Then
        mockMvc.perform(get("/api/products/search")
                        .param("categoryId", "1")
                        .param("minPrice", "100000.00")
                        .param("maxPrice", "300000.00")
                        .param("keyword", "táo")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.products").isArray())
                .andExpect(jsonPath("$.data.products.length()").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    public void testSearchProductsByName() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(product1);
        Page<ProductResponse> productPage = new PageImpl<>(products, PageRequest.of(0, 20), 1);
        when(productService.searchProductsByName(anyString(), any())).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products/search/name")
                        .param("name", "táo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    public void testGetProductsByPriceRange() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(product1);
        when(productService.getProductsByPriceRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/price-range")
                        .param("minPrice", "100000.00")
                        .param("maxPrice", "200000.00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].productId").value(1));
    }

    @Test
    public void testGetProductsByCategories() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(product1, product2);
        Page<ProductResponse> productPage = new PageImpl<>(products, PageRequest.of(0, 20), 2);
        when(productService.getProductsByCategories(anyList(), any())).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products/categories")
                        .param("categoryIds", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    public void testCreateProduct() throws Exception {
        // Given
        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(product1);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product created successfully"))
                .andExpect(jsonPath("$.data.productId").value(1))
                .andExpect(jsonPath("$.data.name").value("Táo Fuji Nhật Bản"));
    }

    @Test
    public void testCreateProductValidation() throws Exception {
        // Given
        CreateProductRequest invalidRequest = new CreateProductRequest();
        invalidRequest.setName(""); // Empty name should fail validation

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        // Given
        when(productService.updateProduct(anyLong(), any(UpdateProductRequest.class))).thenReturn(product1);

        // When & Then
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product updated successfully"))
                .andExpect(jsonPath("$.data.productId").value(1));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    public void testPermanentlyDeleteProduct() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/products/1/permanent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product permanently deleted successfully"));
    }

    @Test
    public void testCheckStockAvailability() throws Exception {
        // Given
        when(productService.checkStockAvailability(1L, 50)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/products/1/stock")
                        .param("quantity", "50"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testReduceStock() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/products/1/stock/reduce")
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Stock reduced successfully"));
    }

    @Test
    public void testAddStock() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/products/1/stock/add")
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Stock added successfully"));
    }

    @Test
    public void testGetLowStockProducts() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(product1);
        when(productService.getLowStockProducts(10)).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/admin/low-stock")
                        .param("threshold", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].productId").value(1));
    }

    @Test
    public void testGetTopProductsByStock() throws Exception {
        // Given
        List<ProductResponse> products = Arrays.asList(product2, product1);
        when(productService.getTopProductsByStock(10)).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/admin/top-stock")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].productId").value(2))
                .andExpect(jsonPath("$.data[1].productId").value(1));
    }
}
