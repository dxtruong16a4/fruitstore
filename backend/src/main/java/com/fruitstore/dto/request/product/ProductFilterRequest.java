package com.fruitstore.dto.request.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

/**
 * DTO for product filtering request
 */
public class ProductFilterRequest {

    private Long categoryId;

    @DecimalMin(value = "0.0", message = "Minimum price must be 0 or greater")
    private BigDecimal minPrice;

    @DecimalMin(value = "0.0", message = "Maximum price must be 0 or greater")
    private BigDecimal maxPrice;

    private String keyword;

    @Min(value = 0, message = "Page number must be 0 or greater")
    private Integer page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    private Integer size = 20;

    private String sortBy = "name";
    private String sortDirection = "asc";

    // Constructors
    public ProductFilterRequest() {
    }

    public ProductFilterRequest(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String keyword) {
        this.categoryId = categoryId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.keyword = keyword;
    }

    public ProductFilterRequest(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String keyword, 
                               Integer page, Integer size) {
        this.categoryId = categoryId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.keyword = keyword;
        this.page = page;
        this.size = size;
    }

    public ProductFilterRequest(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String keyword, 
                               Integer page, Integer size, String sortBy, String sortDirection) {
        this.categoryId = categoryId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.keyword = keyword;
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }

    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    @Override
    public String toString() {
        return "ProductFilterRequest{" +
                "categoryId=" + categoryId +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", keyword='" + keyword + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                '}';
    }
}
