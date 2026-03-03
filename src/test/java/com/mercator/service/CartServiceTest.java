package com.mercator.service;

import com.mercator.pojo.Item;
import com.mercator.pojo.ItemName;
import com.mercator.pojo.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ItemPriceService itemPriceService;

    @InjectMocks
    private CartService cartService;

    @Test
    void testCheckoutEmptyCart() {
        Order order = cartService.checkout(List.of());
        assertEquals(BigDecimal.ZERO, order.totalPrice());
        assertTrue(order.quantities().isEmpty());
        verifyNoInteractions(itemPriceService);
    }

    @Test
    void testCheckoutNullCart() {
        Order order = cartService.checkout(null);
        assertEquals(BigDecimal.ZERO, order.totalPrice());
        assertTrue(order.quantities().isEmpty());
        verifyNoInteractions(itemPriceService);
    }

    @Test
    void testCheckoutSingleItem() {
        when(itemPriceService.getPrice(ItemName.APPLE)).thenReturn(new BigDecimal("0.60"));
        
        Order order = cartService.checkout(List.of(ItemName.APPLE));
        
        assertEquals(new BigDecimal("0.60"), order.totalPrice());
        assertEquals(1, order.quantities().size());
        
        Item item = order.quantities().get(0);
        assertEquals(ItemName.APPLE, item.itemName());
        assertEquals(1, item.quantity());
        assertEquals(new BigDecimal("0.60"), item.unitPrice());
        
        verify(itemPriceService, times(1)).getPrice(ItemName.APPLE);
    }

    @Test
    void testCheckoutMultipleSameItems() {
        when(itemPriceService.getPrice(ItemName.APPLE)).thenReturn(new BigDecimal("0.60"));
        
        Order order = cartService.checkout(List.of(ItemName.APPLE, ItemName.APPLE));
        
        assertEquals(new BigDecimal("1.20"), order.totalPrice());
        assertEquals(1, order.quantities().size());
        
        Item item = order.quantities().get(0);
        assertEquals(ItemName.APPLE, item.itemName());
        assertEquals(2, item.quantity());
        assertEquals(new BigDecimal("0.60"), item.unitPrice());
        
        verify(itemPriceService, times(1)).getPrice(ItemName.APPLE);
    }

    @Test
    void testCheckoutMultipleDifferentItems() {
        when(itemPriceService.getPrice(ItemName.APPLE)).thenReturn(new BigDecimal("0.60"));
        when(itemPriceService.getPrice(ItemName.ORANGE)).thenReturn(new BigDecimal("0.25"));
        
        Order order = cartService.checkout(List.of(ItemName.APPLE, ItemName.ORANGE, ItemName.APPLE));
        
        assertEquals(new BigDecimal("1.45"), order.totalPrice());
        assertEquals(2, order.quantities().size());
        
        assertTrue(order.quantities().stream().anyMatch(item -> 
            item.itemName().equals(ItemName.APPLE) && item.quantity() == 2));
        assertTrue(order.quantities().stream().anyMatch(item -> 
            item.itemName().equals(ItemName.ORANGE) && item.quantity() == 1));
        
        verify(itemPriceService, times(1)).getPrice(ItemName.APPLE);
        verify(itemPriceService, times(1)).getPrice(ItemName.ORANGE);
    }

    @Test
    void testCheckoutPriceRounding() {
        when(itemPriceService.getPrice(ItemName.APPLE)).thenReturn(new BigDecimal("0.333"));
        
        Order order = cartService.checkout(List.of(ItemName.APPLE, ItemName.APPLE, ItemName.APPLE));
        
        assertEquals(new BigDecimal("0.99"), order.totalPrice());
        
        verify(itemPriceService, times(1)).getPrice(ItemName.APPLE);
    }
}