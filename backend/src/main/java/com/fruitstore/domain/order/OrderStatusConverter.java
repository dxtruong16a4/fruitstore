package com.fruitstore.domain.order;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Attribute Converter for OrderStatus enum
 * Handles case-insensitive conversion between database values and enum
 */
@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return orderStatus.name(); // Store as uppercase in database
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        try {
            // First try to convert using the enum name (uppercase)
            return OrderStatus.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            // If that fails, try using the fromValue method (case insensitive)
            return OrderStatus.fromValue(dbData);
        }
    }
}
