package com.mercator.service;

import com.mercator.pojo.Item;
import com.mercator.pojo.ItemName;
import com.mercator.pojo.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for managing shopping cart operations.
 * Handles item addition and total price calculation.
 */
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final ItemPriceService itemPriceService;

    public CartService(ItemPriceService itemPriceService) {
        this.itemPriceService = itemPriceService;
    }

    /**
     * Calculates total price of cart items.
     *
     * @param itemNames cart items
     * @return total price rounded to 2 decimals
     */
    public Order checkout(List<ItemName> itemNames) {

        if (itemNames == null || itemNames.isEmpty()) {
            log.warn("Cart is empty, total price = 0");
            return new Order(List.of(), BigDecimal.ZERO);
        }

        var items = toItems(itemNames);
        var total = items.stream()
                .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal rounded = total.setScale(2, RoundingMode.FLOOR);

        log.debug("Cart total = {}", rounded);

        return new Order(items, rounded);
    }

    private List<Item> toItems(List<ItemName> itemNames) {
        var itemQuantityMap = toItemQuantityMap(itemNames);
        return itemQuantityMap.entrySet().stream()
                .map(entry -> {
                    var unitPrice = itemPriceService.getPrice(entry.getKey());
                    return new Item(entry.getKey(), entry.getValue(), unitPrice);
                })
                .toList();
    }

    private Map<ItemName, Integer> toItemQuantityMap(List<ItemName> itemNames) {
        Map<ItemName, Integer> itemQuantityMap = new EnumMap<>(ItemName.class);

        if (itemNames == null) {
            return itemQuantityMap;
        }

        for (ItemName itemName : itemNames) {
            if (itemName != null) {
                itemQuantityMap.merge(itemName, 1, Integer::sum);
            }
        }

        return itemQuantityMap;
    }
}