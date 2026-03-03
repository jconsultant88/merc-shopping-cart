package com.mercator.service;

import com.mercator.pojo.ItemName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Adapter service that takes various types of input (e.g., strings) and converts them to standard {@link ItemName} objects to calculate
 * prices via
 */
public class InputAdapterService {

    private static final Logger log = LoggerFactory.getLogger(InputAdapterService.class);

    private final CartService cartService;

    /**
     * Default constructor using real services.
     */
    public InputAdapterService(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Converts a list of strings to {@link ItemName} and calculates the total price.
     *
     * @param itemList list of strings representing item names
     * @return total price
     */
    public BigDecimal calculatePrice(List<String> itemList) {
        if (itemList == null || itemList.isEmpty()) {
            log.warn("Input list is empty");
            return BigDecimal.ZERO;
        }
        var items = itemList.stream()
                .map(String::trim)
                .map(String::toUpperCase)
                .map(this::safeParseItemName)
                .filter(Objects::nonNull)
                .toList();

        var order = cartService.checkout(items);

        log.info("Items: {} ", order.quantities());
        log.info("Total price: {} ", order.totalPrice());
        return order.totalPrice();
    }

    /**
     * Safely converts a string to {@link ItemName}.
     *
     * @param name input string
     * @return corresponding ItemName, or null if invalid
     */
    private ItemName safeParseItemName(String name) {
        try {
            return ItemName.valueOf(name);
        } catch (IllegalArgumentException e) {
            log.error("Invalid item '{}', ignoring", name);
            return null;
        }
    }

}
