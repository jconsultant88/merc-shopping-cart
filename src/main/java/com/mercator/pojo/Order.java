package com.mercator.pojo;

import com.mercator.components.ItemPromotionalResult;

import java.math.BigDecimal;
import java.util.List;

public record Order(
        List<ItemPromotionalResult> itemPromotionalResults,
        BigDecimal originalTotalPrice,
        BigDecimal totalDiscount,
        BigDecimal totalPrice
) {
}
