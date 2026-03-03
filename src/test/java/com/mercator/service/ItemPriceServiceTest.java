package com.mercator.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.mercator.pojo.ItemName.APPLE;
import static com.mercator.pojo.ItemName.ORANGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemPriceServiceTest {

    private final ItemPriceService priceService = new ItemPriceService();

    @Test
    void testGetPrice_ValidItem() {
        assertEquals(BigDecimal.valueOf(0.60), priceService.getPrice(APPLE));
        assertEquals(BigDecimal.valueOf(0.25), priceService.getPrice(ORANGE));
    }

    @Test
    void testGetPrice_NullItem() {
        assertThrows(IllegalArgumentException.class, () -> priceService.getPrice(null));
    }
}