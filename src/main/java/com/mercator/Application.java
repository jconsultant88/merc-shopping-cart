package com.mercator;

import com.mercator.components.ApplePromotionRule;
import com.mercator.components.OrangePromotionRule;
import com.mercator.service.CartService;
import com.mercator.service.DiscountService;
import com.mercator.service.InputAdapterService;
import com.mercator.service.ItemPriceService;

import java.util.List;

public class Application {
    public static void main(String[] args) {

        var itemNamesStr = List.of("apple", "APPLE", " orange", "guava", "-", "apple");

        var discountService = new DiscountService(new ItemPriceService(), List.of(new ApplePromotionRule(), new OrangePromotionRule()));

        var inputAdapterService = new InputAdapterService(new CartService(discountService));
        inputAdapterService.calculatePrice(itemNamesStr);
    }
}
