package com.mercator.components;

import com.mercator.pojo.Item;

/**
 * Promotion rule applied per item quantity.
 */
public interface PromotionRule {

    boolean appliesTo(Item item);

    ItemPromotionalResult apply(Item item);
}