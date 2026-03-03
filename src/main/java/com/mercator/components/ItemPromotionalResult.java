package com.mercator.components;

import com.mercator.pojo.ItemName;

import java.math.BigDecimal;

public record ItemPromotionalResult(
        ItemName itemName,
        int originalQuantity,
        int chargeableQuantity,
        BigDecimal unitPrice,
        BigDecimal price,
        BigDecimal discount
) {
}