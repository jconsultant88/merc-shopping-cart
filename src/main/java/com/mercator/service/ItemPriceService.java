package com.mercator.service;

import com.mercator.pojo.ItemName;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * Service responsible for providing prices of items.
 * Prices are stored in-memory and returned as {@link BigDecimal}.
 */
public class ItemPriceService {

    Map<ItemName, BigDecimal> itemPriceMap = Map.of(ItemName.APPLE, BigDecimal.valueOf(0.60),
            ItemName.ORANGE, BigDecimal.valueOf(0.25));

    /**
     * Returns the price of the specified item.
     *
     * @param itemName the item whose price is to be determined
     * @return the price of the item
     * @throws IllegalArgumentException if itemName supplied is null
     */
    public BigDecimal getPrice(ItemName itemName) {
        if (Objects.isNull(itemName)) {
            throw new IllegalArgumentException("Item supplied is null");
        }
        return itemPriceMap.get(itemName);
    }
}
