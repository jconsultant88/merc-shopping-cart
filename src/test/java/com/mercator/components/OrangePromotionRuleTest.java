package com.mercator.components;

import com.mercator.pojo.Item;
import com.mercator.pojo.ItemName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class OrangePromotionRuleTest {

    private final BigDecimal bigDecimalZero = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final OrangePromotionRule rule = new OrangePromotionRule();

    @Test
    void testAppliesTo_ShouldReturnTrue_ForOrange() {
        var item = new Item(ItemName.ORANGE, 3, new BigDecimal("0.25"));
        assertTrue(rule.appliesTo(item));
    }

    @Test
    void testAppliesTo_ShouldReturnFalse_ForOtherItem() {
        var item = new Item(ItemName.APPLE, 3, new BigDecimal("0.60"));
        assertFalse(rule.appliesTo(item));
    }

    @Test
    void testApply_ShouldHandleZeroQuantity() {
        var item = new Item(ItemName.ORANGE, 0, new BigDecimal("0.25"));

        var result = rule.apply(item);

        assertEquals(0, result.chargeableQuantity());
        assertEquals(bigDecimalZero, result.price());
        assertEquals(bigDecimalZero, result.discount());
    }

    @Test
    void testApply_ShouldHandleSingleItem() {
        var item = new Item(ItemName.ORANGE, 1, new BigDecimal("0.25"));

        var result = rule.apply(item);

        assertEquals(1, result.chargeableQuantity());
        assertEquals(new BigDecimal("0.25"), result.price());
        assertEquals(bigDecimalZero, result.discount());
    }

    @Test
    void testApply_ShouldHandleTwoItems() {
        var item = new Item(ItemName.ORANGE, 2, new BigDecimal("0.25"));

        var result = rule.apply(item);

        assertEquals(2, result.chargeableQuantity());
        assertEquals(new BigDecimal("0.50"), result.price());
        assertEquals(bigDecimalZero, result.discount());
    }

    @Test
    void testApply_ShouldHandleThreeItems_Buy3Pay2() {
        var item = new Item(ItemName.ORANGE, 3, new BigDecimal("0.25"));

        var result = rule.apply(item);

        assertEquals(3, result.originalQuantity());
        assertEquals(2, result.chargeableQuantity());
        assertEquals(new BigDecimal("0.50"), result.price());
        assertEquals(new BigDecimal("0.25"), result.discount());
    }

    @Test
    void testApply_ShouldHandleFourItems() {
        var item = new Item(ItemName.ORANGE, 4, new BigDecimal("0.25"));

        var result = rule.apply(item);

        // 4 -> (4/3)*2 + 1 = 2 + 1 = 3
        assertEquals(3, result.chargeableQuantity());
        assertEquals(new BigDecimal("0.75"), result.price());
        assertEquals(new BigDecimal("0.25"), result.discount());
    }

    @Test
    void testApply_ShouldHandleSixItems() {
        var item = new Item(ItemName.ORANGE, 6, new BigDecimal("0.25"));

        var result = rule.apply(item);

        // 6 -> (6/3)*2 = 4
        assertEquals(4, result.chargeableQuantity());
        assertEquals(new BigDecimal("1.00"), result.price());
        assertEquals(new BigDecimal("0.50"), result.discount());
    }

    @Test
    void testApply_ShouldHandleLargeQuantity() {
        var item = new Item(ItemName.ORANGE, 9, new BigDecimal("0.25"));

        var result = rule.apply(item);

        // 9 -> (9/3)*2 = 6
        assertEquals(6, result.chargeableQuantity());
        assertEquals(new BigDecimal("1.50"), result.price());
        assertEquals(new BigDecimal("0.75"), result.discount());
    }
}