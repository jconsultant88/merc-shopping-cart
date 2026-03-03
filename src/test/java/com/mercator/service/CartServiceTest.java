package com.mercator.service;

import com.mercator.pojo.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private CartService cartService;

    private static final BigDecimal APPLE_PRICE = new BigDecimal("0.60");
    private static final BigDecimal ORANGE_PRICE = new BigDecimal("0.25");
    private static final BigDecimal BANANA_PRICE = new BigDecimal("0.40");

    @Test
    @DisplayName("Should return empty order when cart is empty list")
    void shouldReturnEmptyOrderWhenCartIsEmptyList() {
        Order order = cartService.checkout(List.of());

        //verifyNoInteractions(itemPriceService);
    }

}