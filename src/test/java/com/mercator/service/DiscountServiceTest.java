package com.mercator.service;

import com.mercator.components.ApplePromotionRule;
import com.mercator.components.PromotionRule;
import com.mercator.pojo.ItemName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private ItemPriceService priceService;

    private DiscountService discountService;

    @BeforeEach
    void setup() {
        List<PromotionRule> rules = List.of(new ApplePromotionRule());
        discountService = new DiscountService(priceService, rules);
    }

    @Test
    void applyDiscounts_ShouldReturnZero_ForEmptyCart() {

        var order = discountService.applyDiscounts(List.of());

        assertEquals(BigDecimal.ZERO, order.originalTotalPrice());
        assertEquals(BigDecimal.ZERO, order.totalDiscount());
        assertEquals(BigDecimal.ZERO, order.totalPrice());
    }

    @Test
    void applyDiscounts_ShouldApplyAppleBogo() {

        when(priceService.getPrice(ItemName.APPLE))
                .thenReturn(new BigDecimal("0.60"));

        var order = discountService.applyDiscounts(
                List.of(ItemName.APPLE, ItemName.APPLE)
        );

        assertEquals(new BigDecimal("1.20"), order.originalTotalPrice());
        assertEquals(new BigDecimal("0.60"), order.totalDiscount());
        assertEquals(new BigDecimal("0.60"), order.totalPrice());
    }

    @Test
    void applyDiscounts_ShouldHandleOddAppleQuantity() {

        when(priceService.getPrice(ItemName.APPLE))
                .thenReturn(new BigDecimal("0.60"));

        var order = discountService.applyDiscounts(
                List.of(ItemName.APPLE, ItemName.APPLE, ItemName.APPLE)
        );

        assertEquals(new BigDecimal("1.80"), order.originalTotalPrice());
        assertEquals(new BigDecimal("0.60"), order.totalDiscount());
        assertEquals(new BigDecimal("1.20"), order.totalPrice());
    }

    @Test
    void applyDiscounts_ShouldNotApplyRule_ForOtherItems() {

        when(priceService.getPrice(ItemName.ORANGE))
                .thenReturn(new BigDecimal("0.25"));

        var order = discountService.applyDiscounts(
                List.of(ItemName.ORANGE, ItemName.ORANGE)
        );

        assertEquals(new BigDecimal("0.50"), order.originalTotalPrice());
        assertEquals(BigDecimal.ZERO, order.totalDiscount());
        assertEquals(new BigDecimal("0.50"), order.totalPrice());
    }

    @Test
    void applyDiscounts_ShouldHandleMixedItems() {

        when(priceService.getPrice(ItemName.APPLE))
                .thenReturn(new BigDecimal("0.60"));
        when(priceService.getPrice(ItemName.ORANGE))
                .thenReturn(new BigDecimal("0.25"));

        var order = discountService.applyDiscounts(
                List.of(
                        ItemName.APPLE,
                        ItemName.APPLE,
                        ItemName.ORANGE
                )
        );

        assertEquals(new BigDecimal("1.45"), order.originalTotalPrice());
        assertEquals(new BigDecimal("0.60"), order.totalDiscount());
        assertEquals(new BigDecimal("0.85"), order.totalPrice());
    }
}