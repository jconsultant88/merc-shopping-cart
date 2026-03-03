package com.mercator.components;

import com.mercator.pojo.Item;
import com.mercator.pojo.ItemName;

import java.math.BigDecimal;

public class ApplePromotionRule implements PromotionRule {

    @Override
    public boolean appliesTo(Item item) {
        return item.itemName() == ItemName.APPLE;
    }

    @Override
    public ItemPromotionalResult apply(Item item) {

        var quantity = item.quantity();
        var unitPrice = item.unitPrice();
        int chargeable = (quantity / 2) + (quantity % 2);

        BigDecimal discounted = unitPrice.multiply(BigDecimal.valueOf(chargeable));
        BigDecimal original = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discountedApplied = original.subtract(discounted);

        return new ItemPromotionalResult(
                item.itemName(),
                quantity,
                chargeable,
                unitPrice,
                discounted,
                discountedApplied
        );
    }
}