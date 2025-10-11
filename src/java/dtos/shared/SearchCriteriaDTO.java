package dtos.shared;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for search and filter parameters
 * Used for handling search criteria across all endpoints
 */
public class SearchCriteriaDTO {
    private String query;
    private String searchType;
    private List<String> categories;
    private List<String> subcategories;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy;
    private String sortOrder;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<String> statuses;
    private List<String> tags;
    private Map<String, Object> customFilters;
    private boolean includeInactive;
    private boolean exactMatch;
    private int limit;
    private int offset;

    // Constructors
    public SearchCriteriaDTO() {}

    public SearchCriteriaDTO(String query) {
        this.query = query;
    }

    public SearchCriteriaDTO(String query, String sortBy, String sortOrder) {
        this.query = query;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public String getSearchType() { return searchType; }
    public void setSearchType(String searchType) { this.searchType = searchType; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public List<String> getSubcategories() { return subcategories; }
    public void setSubcategories(List<String> subcategories) { this.subcategories = subcategories; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public List<String> getStatuses() { return statuses; }
    public void setStatuses(List<String> statuses) { this.statuses = statuses; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Map<String, Object> getCustomFilters() { return customFilters; }
    public void setCustomFilters(Map<String, Object> customFilters) { this.customFilters = customFilters; }

    public boolean isIncludeInactive() { return includeInactive; }
    public void setIncludeInactive(boolean includeInactive) { this.includeInactive = includeInactive; }

    public boolean isExactMatch() { return exactMatch; }
    public void setExactMatch(boolean exactMatch) { this.exactMatch = exactMatch; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }

    // Business methods
    public boolean hasQuery() {
        return query != null && !query.trim().isEmpty();
    }

    public String getTrimmedQuery() {
        return query != null ? query.trim() : null;
    }

    public boolean hasCategories() {
        return categories != null && !categories.isEmpty();
    }

    public boolean hasSubcategories() {
        return subcategories != null && !subcategories.isEmpty();
    }

    public boolean hasPriceRange() {
        return minPrice != null || maxPrice != null;
    }

    public boolean hasDateRange() {
        return startDate != null || endDate != null;
    }

    public boolean hasDateTimeRange() {
        return startDateTime != null || endDateTime != null;
    }

    public boolean hasStatuses() {
        return statuses != null && !statuses.isEmpty();
    }

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }

    public boolean hasCustomFilters() {
        return customFilters != null && !customFilters.isEmpty();
    }

    public boolean hasSorting() {
        return sortBy != null && !sortBy.trim().isEmpty();
    }

    public boolean hasPagination() {
        return limit > 0 || offset > 0;
    }

    public String getSortByOrDefault() {
        return sortBy != null ? sortBy : "id";
    }

    public String getSortOrderOrDefault() {
        return sortOrder != null ? sortOrder : "asc";
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(sortOrder);
    }

    public boolean isDescending() {
        return "desc".equalsIgnoreCase(sortOrder);
    }

    public String getFormattedSortOrder() {
        return isAscending() ? "Ascending" : "Descending";
    }

    public String getSortIcon() {
        return isAscending() ? "↑" : "↓";
    }

    public String getFormattedStartDate() {
        return startDate != null ? startDate.toString() : "Not set";
    }

    public String getFormattedEndDate() {
        return endDate != null ? endDate.toString() : "Not set";
    }

    public String getFormattedStartDateTime() {
        return startDateTime != null ? startDateTime.toString() : "Not set";
    }

    public String getFormattedEndDateTime() {
        return endDateTime != null ? endDateTime.toString() : "Not set";
    }

    public String getDateRangeDisplay() {
        if (hasDateRange()) {
            if (startDate != null && endDate != null) {
                return startDate.toString() + " to " + endDate.toString();
            } else if (startDate != null) {
                return "From " + startDate.toString();
            } else {
                return "Until " + endDate.toString();
            }
        }
        return "No date range";
    }

    public String getPriceRangeDisplay() {
        if (hasPriceRange()) {
            if (minPrice != null && maxPrice != null) {
                return String.format("%.2f - %.2f", minPrice, maxPrice);
            } else if (minPrice != null) {
                return String.format("From %.2f", minPrice);
            } else {
                return String.format("Up to %.2f", maxPrice);
            }
        }
        return "No price range";
    }

    public String getCategoriesDisplay() {
        if (hasCategories()) {
            return String.join(", ", categories);
        }
        return "All categories";
    }

    public String getSubcategoriesDisplay() {
        if (hasSubcategories()) {
            return String.join(", ", subcategories);
        }
        return "All subcategories";
    }

    public String getStatusesDisplay() {
        if (hasStatuses()) {
            return String.join(", ", statuses);
        }
        return "All statuses";
    }

    public String getTagsDisplay() {
        if (hasTags()) {
            return String.join(", ", tags);
        }
        return "No tags";
    }

    public boolean isSearchActive() {
        return hasQuery() || hasCategories() || hasSubcategories() || 
               hasPriceRange() || hasDateRange() || hasStatuses() || 
               hasTags() || hasCustomFilters();
    }

    public String getSearchSummary() {
        StringBuilder summary = new StringBuilder();
        
        if (hasQuery()) {
            summary.append("Query: ").append(getTrimmedQuery()).append("; ");
        }
        
        if (hasCategories()) {
            summary.append("Categories: ").append(getCategoriesDisplay()).append("; ");
        }
        
        if (hasPriceRange()) {
            summary.append("Price: ").append(getPriceRangeDisplay()).append("; ");
        }
        
        if (hasDateRange()) {
            summary.append("Date: ").append(getDateRangeDisplay()).append("; ");
        }
        
        if (hasSorting()) {
            summary.append("Sort: ").append(sortBy).append(" ").append(getSortIcon()).append("; ");
        }
        
        return summary.toString();
    }

    public String getFilterCount() {
        int count = 0;
        if (hasQuery()) count++;
        if (hasCategories()) count++;
        if (hasSubcategories()) count++;
        if (hasPriceRange()) count++;
        if (hasDateRange()) count++;
        if (hasStatuses()) count++;
        if (hasTags()) count++;
        if (hasCustomFilters()) count++;
        
        return count + " filter" + (count == 1 ? "" : "s");
    }

    public String getSearchTypeOrDefault() {
        return searchType != null ? searchType : "general";
    }

    public boolean isProductSearch() {
        return "product".equalsIgnoreCase(searchType);
    }

    public boolean isOrderSearch() {
        return "order".equalsIgnoreCase(searchType);
    }

    public boolean isUserSearch() {
        return "user".equalsIgnoreCase(searchType);
    }

    public boolean isGeneralSearch() {
        return "general".equalsIgnoreCase(searchType) || searchType == null;
    }

    public String getSearchTypeIcon() {
        switch (getSearchTypeOrDefault().toLowerCase()) {
            case "product": return "📦";
            case "order": return "📋";
            case "user": return "👤";
            case "promotion": return "🎉";
            case "combo": return "🎁";
            default: return "🔍";
        }
    }

    public void addCustomFilter(String key, Object value) {
        if (customFilters == null) {
            customFilters = new java.util.HashMap<>();
        }
        customFilters.put(key, value);
    }

    public Object getCustomFilter(String key) {
        return customFilters != null ? customFilters.get(key) : null;
    }

    public boolean hasCustomFilter(String key) {
        return customFilters != null && customFilters.containsKey(key);
    }

    public void removeCustomFilter(String key) {
        if (customFilters != null) {
            customFilters.remove(key);
        }
    }

    public void clearFilters() {
        query = null;
        categories = null;
        subcategories = null;
        minPrice = null;
        maxPrice = null;
        startDate = null;
        endDate = null;
        startDateTime = null;
        endDateTime = null;
        statuses = null;
        tags = null;
        customFilters = null;
        includeInactive = false;
        exactMatch = false;
    }

    public boolean isValidPriceRange() {
        return minPrice == null || maxPrice == null || minPrice <= maxPrice;
    }

    public boolean isValidDateRange() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }

    public boolean isValidDateTimeRange() {
        return startDateTime == null || endDateTime == null || !startDateTime.isAfter(endDateTime);
    }

    public String getValidationErrors() {
        StringBuilder errors = new StringBuilder();
        
        if (!isValidPriceRange()) {
            errors.append("Invalid price range. ");
        }
        
        if (!isValidDateRange()) {
            errors.append("Invalid date range. ");
        }
        
        if (!isValidDateTimeRange()) {
            errors.append("Invalid datetime range. ");
        }
        
        return errors.toString().trim();
    }

    public boolean isValid() {
        return isValidPriceRange() && isValidDateRange() && isValidDateTimeRange();
    }

    public String getSearchDisplayName() {
        if (hasQuery()) {
            return "\"" + getTrimmedQuery() + "\"";
        }
        return "Search Results";
    }
}
