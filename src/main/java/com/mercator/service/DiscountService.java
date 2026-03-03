package com.mercator.service;

import com.mercator.components.ItemPromotionalResult;
import com.mercator.components.PromotionRule;
import com.mercator.pojo.Item;
import com.mercator.pojo.ItemName;
import com.mercator.pojo.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class DiscountService {

    private static final Logger log = LoggerFactory.getLogger(DiscountService.class);

    private final ItemPriceService itemPriceService;
    private final List<PromotionRule> rules;

    public DiscountService(ItemPriceService itemPriceService,
                           List<PromotionRule> rules) {
        this.itemPriceService = itemPriceService;
        this.rules = rules;
    }

    public Order applyDiscounts(List<ItemName> itemNames) {

        if (itemNames == null || itemNames.isEmpty()) {
            log.warn("Cart is empty, total price = 0");
            return new Order(List.of(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        var items = toItems(itemNames);
        var originalTotalPrice = items.stream()
                .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var promotionalResults = items.stream()
                .map(item -> {
                    BigDecimal unitPrice = item.unitPrice();

                    var rule = rules.stream()
                            .filter(r -> r.appliesTo(item))
                            .findFirst();

                    if (rule.isPresent()) {
                        return rule.get().apply(item);
                    }

                    var itemPrice = item.unitPrice().multiply(BigDecimal.valueOf(item.quantity()));

                    return new ItemPromotionalResult(
                            item.itemName(),
                            item.quantity(),
                            item.quantity(),
                            unitPrice,
                            itemPrice,
                            BigDecimal.ZERO
                    );

                })
                .toList();

        var finalTotal = promotionalResults.stream()
                .map(ItemPromotionalResult::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var totalDiscount = promotionalResults.stream()
                .map(ItemPromotionalResult::discount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Order(promotionalResults, originalTotalPrice, totalDiscount, finalTotal);
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