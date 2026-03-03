package com.mercator.service;

import com.mercator.pojo.ItemName;
import com.mercator.pojo.Order;

import java.util.List;

/**
 * Service responsible for managing shopping cart operations.
 * Handles item addition and total price calculation.
 */
public class CartService {

    private final DiscountService discountService;

    public CartService(DiscountService discountService) {
        this.discountService = discountService;
    }

    public Order checkout(List<ItemName> itemNames) {
        return discountService.applyDiscounts(itemNames);
    }
}