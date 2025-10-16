package com.fruitstore.dto.response.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ApiResponse generic wrapper
 */
class ApiResponseTest {

    @Test
    void testDefaultConstructor() {
        // When
        ApiResponse<String> response = new ApiResponse<>();

        // Then
        assertNotNull(response);
    }

    @Test
    void testConstructorWithAllFields() {
        // When
        ApiResponse<String> response = new ApiResponse<>(true, "Success message", "data");

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Success message", response.getMessage());
        assertEquals("data", response.getData());
    }

    @Test
    void testSuccessWithData() {
        // When
        ApiResponse<String> response = ApiResponse.success("Test data");

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
        assertEquals("Test data", response.getData());
    }

    @Test
    void testSuccessWithMessageAndData() {
        // When
        ApiResponse<String> response = ApiResponse.success("Custom message", "Test data");

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Custom message", response.getMessage());
        assertEquals("Test data", response.getData());
    }

    @Test
    void testErrorWithMessage() {
        // When
        ApiResponse<String> response = ApiResponse.error("Error message");

        // Then
        assertFalse(response.isSuccess());
        assertEquals("Error message", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testErrorWithMessageAndData() {
        // When
        ApiResponse<String> response = ApiResponse.error("Error message", "Error data");

        // Then
        assertFalse(response.isSuccess());
        assertEquals("Error message", response.getMessage());
        assertEquals("Error data", response.getData());
    }

    @Test
    void testGettersAndSetters() {
        // Given
        ApiResponse<Integer> response = new ApiResponse<>();

        // When
        response.setSuccess(true);
        response.setMessage("Test message");
        response.setData(42);

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Test message", response.getMessage());
        assertEquals(42, response.getData());
    }

    @Test
    void testWithComplexDataType() {
        // Given
        class CustomData {
            String name;
            int value;
            
            CustomData(String name, int value) {
                this.name = name;
                this.value = value;
            }
        }
        
        CustomData data = new CustomData("Test", 123);

        // When
        ApiResponse<CustomData> response = ApiResponse.success(data);

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Test", response.getData().name);
        assertEquals(123, response.getData().value);
    }

    @Test
    void testToString() {
        // Given
        ApiResponse<String> response = new ApiResponse<>(true, "Success", "Test data");

        // When
        String toString = response.toString();

        // Then
        assertTrue(toString.contains("success=true"));
        assertTrue(toString.contains("Success"));
        assertTrue(toString.contains("Test data"));
    }

    @Test
    void testNullData() {
        // When
        ApiResponse<String> response = ApiResponse.success(null);

        // Then
        assertTrue(response.isSuccess());
        assertNull(response.getData());
    }
}

