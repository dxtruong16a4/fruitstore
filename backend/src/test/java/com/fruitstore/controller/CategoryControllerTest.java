package com.fruitstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitstore.dto.request.category.CreateCategoryRequest;
import com.fruitstore.dto.request.category.UpdateCategoryRequest;
import com.fruitstore.dto.response.category.CategoryResponse;
import com.fruitstore.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for CategoryController
 */
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private CategoryResponse category1;
    private CategoryResponse category2;
    private CreateCategoryRequest createRequest;
    private UpdateCategoryRequest updateRequest;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();

        category1 = new CategoryResponse();
        category1.setCategoryId(1L);
        category1.setName("Trái cây tươi");
        category1.setDescription("Các loại trái cây tươi ngon");
        category1.setImageUrl("/images/categories/fresh-fruits.jpg");
        category1.setIsActive(true);
        category1.setCreatedAt(LocalDateTime.now());

        category2 = new CategoryResponse();
        category2.setCategoryId(2L);
        category2.setName("Trái cây nhập khẩu");
        category2.setDescription("Trái cây nhập khẩu từ các nước");
        category2.setImageUrl("/images/categories/imported.jpg");
        category2.setIsActive(false);
        category2.setCreatedAt(LocalDateTime.now());

        createRequest = new CreateCategoryRequest();
        createRequest.setName("Trái cây sấy");
        createRequest.setDescription("Các loại trái cây sấy khô dinh dưỡng");
        createRequest.setImageUrl("/images/categories/dried.jpg");

        updateRequest = new UpdateCategoryRequest();
        updateRequest.setName("Trái cây tươi cập nhật");
        updateRequest.setDescription("Mô tả cập nhật");
        updateRequest.setImageUrl("/images/categories/updated.jpg");
        updateRequest.setIsActive(true);
    }

    @Test
    public void testGetAllCategories() throws Exception {
        // Given
        List<CategoryResponse> categories = Arrays.asList(category1, category2);
        when(categoryService.getAllCategories()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].categoryId").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Trái cây tươi"))
                .andExpect(jsonPath("$.data[1].categoryId").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Trái cây nhập khẩu"));
    }

    @Test
    public void testGetActiveCategories() throws Exception {
        // Given
        List<CategoryResponse> activeCategories = Arrays.asList(category1);
        when(categoryService.getActiveCategories()).thenReturn(activeCategories);

        // When & Then
        mockMvc.perform(get("/api/categories/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].categoryId").value(1))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    public void testGetCategoryById() throws Exception {
        // Given
        when(categoryService.getCategoryById(1L)).thenReturn(category1);

        // When & Then
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.categoryId").value(1))
                .andExpect(jsonPath("$.data.name").value("Trái cây tươi"))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    public void testGetActiveCategoryById() throws Exception {
        // Given
        when(categoryService.getActiveCategoryById(1L)).thenReturn(category1);

        // When & Then
        mockMvc.perform(get("/api/categories/1/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.categoryId").value(1))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    public void testCreateCategory() throws Exception {
        // Given
        when(categoryService.createCategory(any(CreateCategoryRequest.class))).thenReturn(category1);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category created successfully"))
                .andExpect(jsonPath("$.data.categoryId").value(1))
                .andExpect(jsonPath("$.data.name").value("Trái cây tươi"));
    }

    @Test
    public void testCreateCategoryValidation() throws Exception {
        // Given
        CreateCategoryRequest invalidRequest = new CreateCategoryRequest();
        invalidRequest.setName(""); // Empty name should fail validation

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateCategory() throws Exception {
        // Given
        when(categoryService.updateCategory(anyLong(), any(UpdateCategoryRequest.class))).thenReturn(category1);

        // When & Then
        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category updated successfully"))
                .andExpect(jsonPath("$.data.categoryId").value(1));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category deleted successfully"));
    }

    @Test
    public void testPermanentlyDeleteCategory() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/categories/1/permanent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category permanently deleted successfully"));
    }

    @Test
    public void testActivateCategory() throws Exception {
        // Given
        when(categoryService.activateCategory(1L)).thenReturn(category1);

        // When & Then
        mockMvc.perform(put("/api/categories/1/activate"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category activated successfully"))
                .andExpect(jsonPath("$.data.categoryId").value(1));
    }

    @Test
    public void testDeactivateCategory() throws Exception {
        // Given
        when(categoryService.deactivateCategory(1L)).thenReturn(category1);

        // When & Then
        mockMvc.perform(put("/api/categories/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category deactivated successfully"))
                .andExpect(jsonPath("$.data.categoryId").value(1));
    }

    @Test
    public void testSearchCategoriesByName() throws Exception {
        // Given
        List<CategoryResponse> categories = Arrays.asList(category1);
        when(categoryService.searchCategoriesByName("tươi")).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/categories/search")
                        .param("name", "tươi"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Trái cây tươi"));
    }

    @Test
    public void testSearchActiveCategoriesByName() throws Exception {
        // Given
        List<CategoryResponse> categories = Arrays.asList(category1);
        when(categoryService.searchActiveCategoriesByName("tươi")).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/categories/search/active")
                        .param("name", "tươi"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    public void testGetCategoriesWithProductCounts() throws Exception {
        // Given
        category1.setProductCount(10L);
        List<CategoryResponse> categories = Arrays.asList(category1);
        when(categoryService.getCategoriesWithProductCounts()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/categories/with-counts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].productCount").value(10));
    }
}
