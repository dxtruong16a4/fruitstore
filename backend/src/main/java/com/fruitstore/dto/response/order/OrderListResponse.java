package com.fruitstore.dto.response.order;

import com.fruitstore.dto.response.common.ApiResponse;

import java.util.List;

/**
 * DTO for order list response
 */
public class OrderListResponse extends ApiResponse<List<OrderSummaryResponse>> {

    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Boolean isFirst;
    private Boolean isLast;

    // Constructors
    public OrderListResponse() {
        super();
    }

    public OrderListResponse(boolean success, String message, List<OrderSummaryResponse> data) {
        super(success, message, data);
    }

    public OrderListResponse(boolean success, String message, List<OrderSummaryResponse> data, 
                           Long totalElements, Integer totalPages, Integer currentPage, 
                           Integer pageSize, Boolean hasNext, Boolean hasPrevious, 
                           Boolean isFirst, Boolean isLast) {
        super(success, message, data);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }

    // Static factory methods
    public static OrderListResponse success(List<OrderSummaryResponse> orders) {
        return new OrderListResponse(true, "Orders retrieved successfully", orders);
    }

    public static OrderListResponse success(List<OrderSummaryResponse> orders, Long totalElements, 
                                          Integer totalPages, Integer currentPage, Integer pageSize, 
                                          Boolean hasNext, Boolean hasPrevious, Boolean isFirst, Boolean isLast) {
        return new OrderListResponse(true, "Orders retrieved successfully", orders, totalElements, 
                                   totalPages, currentPage, pageSize, hasNext, hasPrevious, isFirst, isLast);
    }

    @SuppressWarnings("unchecked")
    public static OrderListResponse error(String message) {
        return new OrderListResponse(false, message, null);
    }

    // Getters and Setters
    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public Boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Boolean isFirst) {
        this.isFirst = isFirst;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
    }

    @Override
    public String toString() {
        return "OrderListResponse{" +
                "totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious +
                ", isFirst=" + isFirst +
                ", isLast=" + isLast +
                ", success=" + isSuccess() +
                ", message='" + getMessage() + '\'' +
                ", data=" + getData() +
                '}';
    }
}
