package com.mercator.components;

import com.mercator.pojo.Item;
import com.mercator.pojo.ItemName;

import java.math.BigDecimal;

public class OrangePromotionRule implements PromotionRule {

    @Override
    public boolean appliesTo(Item item) {
        return item.itemName() == ItemName.ORANGE;
    }

    @Override
    public ItemPromotionalResult apply(Item item) {

        var quantity = item.quantity();
        var unitPrice = item.unitPrice();
        int chargeable = (quantity / 3) * 2 + (quantity % 3);

        BigDecimal original = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discounted = unitPrice.multiply(BigDecimal.valueOf(chargeable));

        return new ItemPromotionalResult(
                item.itemName(),
                quantity,
                chargeable,
                unitPrice,
                discounted,
                original.subtract(discounted)
        );
    }
}