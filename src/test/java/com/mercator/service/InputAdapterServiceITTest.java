package com.mercator.service;

import com.mercator.components.ApplePromotionRule;
import com.mercator.components.OrangePromotionRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputAdapterServiceITTest {

    @InjectMocks
    private InputAdapterService adapterService;

    @BeforeEach
    void setup() {
        var discountService = new DiscountService(new ItemPriceService(), List.of(new ApplePromotionRule(), new OrangePromotionRule()));
        adapterService = new InputAdapterService(new CartService(discountService));
    }

    @Test
    void testCalculatePrice_ValidAndInvalidItems() {
        var input = List.of("apple", "oraneg", "guava", "APPLE", "orange");

        var total = adapterService.calculatePrice(input);

        assertEquals(new BigDecimal("0.85"), total);
    }

    @Test
    void testCalculatePrice_EmptyList() {
        var total = adapterService.calculatePrice(List.of());
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void testCalculatePrice_onNull() {
        var total = adapterService.calculatePrice(null);
        assertEquals(BigDecimal.ZERO, total);
    }

}