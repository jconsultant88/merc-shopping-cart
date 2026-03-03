package com.mercator.pojo;

import java.math.BigDecimal;

public record Item(ItemName itemName, int quantity, BigDecimal unitPrice) {
}
