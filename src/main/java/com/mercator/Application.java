package com.mercator;

import com.mercator.service.CartService;
import com.mercator.service.InputAdapterService;
import com.mercator.service.ItemPriceService;

import java.util.List;

public class Application {
    public static void main(String[] args) {

        var itemNamesStr = List.of("apple", "APPLE", " orange", "guava", "-", "apple");

        var inputAdapterService = new InputAdapterService(new CartService(new ItemPriceService()));
        inputAdapterService.calculatePrice(itemNamesStr);
    }
}
