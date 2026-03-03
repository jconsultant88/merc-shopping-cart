package com.mercator.pojo;

import java.math.BigDecimal;
import java.util.List;

public record Order(List<Item> quantities,
                    BigDecimal totalPrice) {
}
