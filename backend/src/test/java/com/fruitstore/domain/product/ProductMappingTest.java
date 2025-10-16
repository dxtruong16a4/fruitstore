package com.fruitstore.domain.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

/**
 * Simple test to verify Product entity functionality and business logic
 * This test doesn't require database connection
 */
public class ProductMappingTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100000.00"));
        product.setStockQuantity(50);
    }

    @Test
    public void testProductEntityCreation() {
        // Test default constructor
        Product product1 = new Product();
        assertNotNull(product1);

        // Test constructor with name, description, price
        Product product2 = new Product("Táo Fuji", "Táo ngon", new BigDecimal("150000.00"));
        assertNotNull(product2);
        assertEquals("Táo Fuji", product2.getName());
        assertEquals("Táo ngon", product2.getDescription());
        assertEquals(new BigDecimal("150000.00"), product2.getPrice());
        assertEquals(0, product2.getStockQuantity()); // Default value
        assertTrue(product2.getIsActive()); // Default value

        // Test constructor with all parameters
        Category category = new Category("Trái cây", "Các loại trái cây");
        Product product3 = new Product("Nho Mỹ", "Nho xanh", new BigDecimal("200000.00"), 60, category);
        assertNotNull(product3);
        assertEquals("Nho Mỹ", product3.getName());
        assertEquals("Nho xanh", product3.getDescription());
        assertEquals(new BigDecimal("200000.00"), product3.getPrice());
        assertEquals(60, product3.getStockQuantity());
        assertEquals(category, product3.getCategory());
        assertTrue(product3.getIsActive());
    }

    @Test
    public void testProductSettersAndGetters() {
        Product product = new Product();
        
        // Test setters and getters
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("50000.00"));
        product.setStockQuantity(25);
        product.setImageUrl("/test/image.jpg");
        product.setIsActive(false);
        
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(new BigDecimal("50000.00"), product.getPrice());
        assertEquals(25, product.getStockQuantity());
        assertEquals("/test/image.jpg", product.getImageUrl());
        assertFalse(product.getIsActive());
    }

    @Test
    public void testProductBusinessLogic() {
        // Test isInStock
        assertTrue(product.isInStock());
        product.setStockQuantity(0);
        assertFalse(product.isInStock());

        // Test hasSufficientStock
        product.setStockQuantity(50);
        assertTrue(product.hasSufficientStock(30));
        assertTrue(product.hasSufficientStock(50));
        assertFalse(product.hasSufficientStock(60));
        assertFalse(product.hasSufficientStock(null));
        assertFalse(product.hasSufficientStock(-5));

        // Test reduceStock
        product.setStockQuantity(100);
        product.reduceStock(20);
        assertEquals(80, product.getStockQuantity());

        // Test addStock
        product.addStock(10);
        assertEquals(90, product.getStockQuantity());
    }

    @Test
    public void testProductStockValidation() {
        // Test reduceStock with insufficient stock
        product.setStockQuantity(10);
        assertThrows(IllegalArgumentException.class, () -> product.reduceStock(20));
        assertEquals(10, product.getStockQuantity()); // Should remain unchanged

        // Test reduceStock with invalid quantity
        assertThrows(IllegalArgumentException.class, () -> product.reduceStock(null));
        assertThrows(IllegalArgumentException.class, () -> product.reduceStock(0));
        assertThrows(IllegalArgumentException.class, () -> product.reduceStock(-5));

        // Test addStock with invalid quantity
        assertThrows(IllegalArgumentException.class, () -> product.addStock(null));
        assertThrows(IllegalArgumentException.class, () -> product.addStock(-5));
    }

    @Test
    public void testProductEqualsAndHashCode() {
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Test Product");

        Product product2 = new Product();
        product2.setProductId(1L);
        product2.setName("Test Product");

        Product product3 = new Product();
        product3.setProductId(2L);
        product3.setName("Test Product");

        // Test equals
        assertEquals(product1, product2);
        assertNotEquals(product1, product3);

        // Test hashCode
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

    @Test
    public void testProductToString() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setProductId(1L);
        product.setPrice(new BigDecimal("100000.00"));
        product.setStockQuantity(50);

        String toString = product.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Test Product"));
        assertTrue(toString.contains("Test Description"));
        assertTrue(toString.contains("productId=1"));
        assertTrue(toString.contains("price=100000.00"));
        assertTrue(toString.contains("stockQuantity=50"));
    }

    @Test
    public void testProductWithCategory() {
        Category category = new Category("Trái cây", "Các loại trái cây");
        category.setCategoryId(1L);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100000.00"));
        product.setCategory(category);

        assertEquals(category, product.getCategory());
        assertEquals("Trái cây", product.getCategory().getName());

        // Test toString with category
        String toString = product.toString();
        assertTrue(toString.contains("category=Trái cây"));
    }
}
