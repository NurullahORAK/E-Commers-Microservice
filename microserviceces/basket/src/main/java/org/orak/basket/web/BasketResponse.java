package org.orak.basket.web;


import org.orak.basket.api.basketitem.BasketItemDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BasketResponse {
    private final int basketId;
    private final double totalAmount;
    private final int status;
    private final int customerId;
    private final List<BasketItemDto> basketItemList;
}
