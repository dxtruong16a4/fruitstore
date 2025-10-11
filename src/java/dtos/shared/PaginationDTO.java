package dtos.shared;

import java.util.List;

/**
 * DTO for pagination information
 * Used for handling paginated data across all endpoints
 */
public class PaginationDTO {
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private int nextPage;
    private int previousPage;
    private int startIndex;
    private int endIndex;
    private List<Integer> pageNumbers;

    // Constructors
    public PaginationDTO() {}

    public PaginationDTO(int currentPage, int pageSize, long totalItems) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        calculatePagination();
    }

    public PaginationDTO(int currentPage, int pageSize, long totalItems, int totalPages) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        calculatePagination();
    }

    // Getters and Setters
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { 
        this.currentPage = currentPage;
        calculatePagination();
    }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { 
        this.pageSize = pageSize;
        calculatePagination();
    }

    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { 
        this.totalItems = totalItems;
        calculatePagination();
    }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public boolean isHasNextPage() { return hasNextPage; }
    public void setHasNextPage(boolean hasNextPage) { this.hasNextPage = hasNextPage; }

    public boolean isHasPreviousPage() { return hasPreviousPage; }
    public void setHasPreviousPage(boolean hasPreviousPage) { this.hasPreviousPage = hasPreviousPage; }

    public int getNextPage() { return nextPage; }
    public void setNextPage(int nextPage) { this.nextPage = nextPage; }

    public int getPreviousPage() { return previousPage; }
    public void setPreviousPage(int previousPage) { this.previousPage = previousPage; }

    public int getStartIndex() { return startIndex; }
    public void setStartIndex(int startIndex) { this.startIndex = startIndex; }

    public int getEndIndex() { return endIndex; }
    public void setEndIndex(int endIndex) { this.endIndex = endIndex; }

    public List<Integer> getPageNumbers() { return pageNumbers; }
    public void setPageNumbers(List<Integer> pageNumbers) { this.pageNumbers = pageNumbers; }

    // Business methods
    private void calculatePagination() {
        // Calculate total pages
        if (pageSize > 0) {
            this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
        } else {
            this.totalPages = 1;
        }

        // Ensure current page is within bounds
        if (currentPage < 1) {
            this.currentPage = 1;
        } else if (currentPage > totalPages && totalPages > 0) {
            this.currentPage = totalPages;
        }

        // Calculate navigation
        this.hasNextPage = currentPage < totalPages;
        this.hasPreviousPage = currentPage > 1;
        this.nextPage = hasNextPage ? currentPage + 1 : currentPage;
        this.previousPage = hasPreviousPage ? currentPage - 1 : currentPage;

        // Calculate start and end indices
        this.startIndex = (currentPage - 1) * pageSize + 1;
        this.endIndex = Math.min(currentPage * pageSize, (int) totalItems);

        // Generate page numbers for navigation
        generatePageNumbers();
    }

    private void generatePageNumbers() {
        this.pageNumbers = new java.util.ArrayList<>();
        
        if (totalPages <= 7) {
            // Show all pages if 7 or fewer
            for (int i = 1; i <= totalPages; i++) {
                pageNumbers.add(i);
            }
        } else {
            // Show first page
            pageNumbers.add(1);
            
            if (currentPage > 4) {
                pageNumbers.add(-1); // Ellipsis
            }
            
            // Show pages around current page
            int start = Math.max(2, currentPage - 1);
            int end = Math.min(totalPages - 1, currentPage + 1);
            
            for (int i = start; i <= end; i++) {
                if (!pageNumbers.contains(i)) {
                    pageNumbers.add(i);
                }
            }
            
            if (currentPage < totalPages - 3) {
                pageNumbers.add(-1); // Ellipsis
            }
            
            // Show last page
            if (!pageNumbers.contains(totalPages)) {
                pageNumbers.add(totalPages);
            }
        }
    }

    public boolean isValidPage(int page) {
        return page >= 1 && page <= totalPages;
    }

    public boolean isFirstPage() {
        return currentPage == 1;
    }

    public boolean isLastPage() {
        return currentPage == totalPages;
    }

    public boolean isEmpty() {
        return totalItems == 0;
    }

    public boolean hasItems() {
        return totalItems > 0;
    }

    public String getPageInfo() {
        if (isEmpty()) {
            return "No items found";
        }
        
        return String.format("Page %d of %d (%d - %d of %d items)", 
                currentPage, totalPages, startIndex, endIndex, totalItems);
    }

    public String getPageSummary() {
        if (isEmpty()) {
            return "No items";
        }
        
        return String.format("%d - %d of %d", startIndex, endIndex, totalItems);
    }

    public String getPageNavigation() {
        StringBuilder nav = new StringBuilder();
        
        if (hasPreviousPage) {
            nav.append("← Previous ");
        }
        
        nav.append("Page ").append(currentPage).append(" of ").append(totalPages);
        
        if (hasNextPage) {
            nav.append(" Next →");
        }
        
        return nav.toString();
    }

    public String getItemsRange() {
        if (isEmpty()) {
            return "0 items";
        }
        
        if (totalItems == 1) {
            return "1 item";
        }
        
        return startIndex + " - " + endIndex + " of " + totalItems + " items";
    }

    public double getCompletionPercentage() {
        if (totalPages == 0) return 0.0;
        return (double) currentPage / totalPages * 100;
    }

    public String getFormattedCompletionPercentage() {
        return String.format("%.1f%%", getCompletionPercentage());
    }

    public int getItemsPerPage() {
        return pageSize;
    }

    public String getPageSizeDisplay() {
        return pageSize + " per page";
    }

    public boolean hasMultiplePages() {
        return totalPages > 1;
    }

    public String getPageNumbersDisplay() {
        if (pageNumbers == null || pageNumbers.isEmpty()) {
            return "1";
        }
        
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < pageNumbers.size(); i++) {
            if (i > 0) display.append(" ");
            
            int pageNum = pageNumbers.get(i);
            if (pageNum == -1) {
                display.append("...");
            } else {
                display.append(pageNum);
            }
        }
        
        return display.toString();
    }

    public List<Integer> getVisiblePageNumbers() {
        return pageNumbers.stream()
                .filter(pageNum -> pageNum != -1)
                .collect(java.util.stream.Collectors.toList());
    }

    public boolean isEllipsisVisible() {
        return pageNumbers != null && pageNumbers.contains(-1);
    }

    public String getPaginationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Showing ").append(getItemsRange());
        summary.append(" (Page ").append(currentPage).append(" of ").append(totalPages).append(")");
        return summary.toString();
    }

    public String getNavigationButtons() {
        StringBuilder buttons = new StringBuilder();
        
        if (hasPreviousPage) {
            buttons.append("◀ ");
        }
        
        buttons.append(currentPage);
        
        if (hasNextPage) {
            buttons.append(" ▶");
        }
        
        return buttons.toString();
    }

    public boolean isNearEnd() {
        return currentPage > totalPages * 0.8;
    }

    public boolean isNearStart() {
        return currentPage < totalPages * 0.2;
    }

    public int getRemainingPages() {
        return totalPages - currentPage;
    }

    public int getRemainingItems() {
        return (int) totalItems - endIndex;
    }

    public String getRemainingInfo() {
        if (isEmpty()) return "No items";
        return getRemainingItems() + " items remaining";
    }

    public boolean canGoToPage(int page) {
        return isValidPage(page) && page != currentPage;
    }

    public String getPageStatus() {
        if (isEmpty()) return "Empty";
        if (isFirstPage()) return "First Page";
        if (isLastPage()) return "Last Page";
        return "Middle Page";
    }

    public String getPageStatusColor() {
        if (isEmpty()) return "gray";
        if (isFirstPage()) return "blue";
        if (isLastPage()) return "green";
        return "orange";
    }
}
