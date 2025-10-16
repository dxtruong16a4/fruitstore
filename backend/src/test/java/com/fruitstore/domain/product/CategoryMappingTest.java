package com.fruitstore.domain.product;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple test to verify Category entity mapping and basic functionality
 * This test doesn't require database connection
 */
public class CategoryMappingTest {

    @Test
    public void testCategoryEntityCreation() {
        // Test default constructor
        Category category = new Category();
        assertNotNull(category);
        
        // Test constructor with name and description
        Category category2 = new Category("Trái cây tươi", "Các loại trái cây tươi ngon");
        assertNotNull(category2);
        assertEquals("Trái cây tươi", category2.getName());
        assertEquals("Các loại trái cây tươi ngon", category2.getDescription());
        assertTrue(category2.getIsActive()); // Default should be true
        
        // Test constructor with all parameters
        Category category3 = new Category("Trái cây nhập khẩu", "Trái cây nhập khẩu từ các nước", "/images/categories/imported.jpg");
        assertNotNull(category3);
        assertEquals("Trái cây nhập khẩu", category3.getName());
        assertEquals("Trái cây nhập khẩu từ các nước", category3.getDescription());
        assertEquals("/images/categories/imported.jpg", category3.getImageUrl());
        assertTrue(category3.getIsActive());
    }

    @Test
    public void testCategorySettersAndGetters() {
        Category category = new Category();
        
        // Test setters and getters
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setImageUrl("/test/image.jpg");
        category.setIsActive(false);
        
        assertEquals("Test Category", category.getName());
        assertEquals("Test Description", category.getDescription());
        assertEquals("/test/image.jpg", category.getImageUrl());
        assertFalse(category.getIsActive());
    }

    @Test
    public void testCategoryEqualsAndHashCode() {
        Category category1 = new Category();
        category1.setCategoryId(1L);
        category1.setName("Test Category");
        
        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setName("Test Category");
        
        Category category3 = new Category();
        category3.setCategoryId(2L);
        category3.setName("Test Category");
        
        // Test equals
        assertEquals(category1, category2);
        assertNotEquals(category1, category3);
        
        // Test hashCode
        assertEquals(category1.hashCode(), category2.hashCode());
        assertNotEquals(category1.hashCode(), category3.hashCode());
    }

    @Test
    public void testCategoryToString() {
        Category category = new Category("Test Category", "Test Description");
        category.setCategoryId(1L);
        
        String toString = category.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Test Category"));
        assertTrue(toString.contains("Test Description"));
        assertTrue(toString.contains("categoryId=1"));
    }
}
