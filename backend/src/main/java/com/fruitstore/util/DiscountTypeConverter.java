package com.fruitstore.util;

import com.fruitstore.domain.discount.DiscountType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Custom converter for DiscountType enum to handle database mapping
 * Converts between database string values and enum constants
 */
@Converter(autoApply = true)
public class DiscountTypeConverter implements AttributeConverter<DiscountType, String> {

    @Override
    public String convertToDatabaseColumn(DiscountType discountType) {
        if (discountType == null) {
            return null;
        }
        return discountType.getValue();
    }

    @Override
    public DiscountType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return DiscountType.fromValue(dbData);
    }
}
