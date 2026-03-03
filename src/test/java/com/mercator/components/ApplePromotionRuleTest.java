package com.mercator.components;

import com.mercator.pojo.Item;
import com.mercator.pojo.ItemName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ApplePromotionRuleTest {

    private final ApplePromotionRule rule = new ApplePromotionRule();

    @Test
    void testAppliesTo_ShouldReturnTrue_ForApple() {
        var item = new Item(ItemName.APPLE, 2, new BigDecimal("0.60"));
        assertTrue(rule.appliesTo(item));
    }

    @Test
    void testAppliesTo_ShouldReturnFalse_ForOtherItem() {
        var item = new Item(ItemName.ORANGE, 2, new BigDecimal("0.25"));
        assertFalse(rule.appliesTo(item));
    }

    @Test
    void testApplyTo_ShouldHandleEvenQuantity() {
        var item = new Item(ItemName.APPLE, 4, new BigDecimal("0.60"));

        var result = rule.apply(item);

        assertEquals(4, result.originalQuantity());
        assertEquals(2, result.chargeableQuantity());
        assertEquals(new BigDecimal("1.20"), result.price());
        assertEquals(new BigDecimal("1.20"), result.discount());
    }

    @Test
    void testApply_ShouldHandleOddQuantity() {
        var item = new Item(ItemName.APPLE, 3, new BigDecimal("0.60"));

        var result = rule.apply(item);

        assertEquals(3, result.originalQuantity());
        assertEquals(2, result.chargeableQuantity());
        assertEquals(new BigDecimal("1.20"), result.price());
        assertEquals(new BigDecimal("0.60"), result.discount());
    }

    @Test
    void testApply_ShouldHandleSingleItem() {
        var item = new Item(ItemName.APPLE, 1, new BigDecimal("0.60"));

        var result = rule.apply(item);

        assertEquals(1, result.chargeableQuantity());
        assertEquals(new BigDecimal("0.60"), result.price());
        assertEquals(new BigDecimal("0.00"), result.discount());
    }

    @Test
    void testApply_ShouldHandleZeroQuantity() {
        var item = new Item(ItemName.APPLE, 0, new BigDecimal("0.60"));

        var result = rule.apply(item);

        assertEquals(0, result.chargeableQuantity());
        assertEquals(new BigDecimal("0.00"), result.price());
        assertEquals(new BigDecimal("0.00"), result.discount());
    }
}