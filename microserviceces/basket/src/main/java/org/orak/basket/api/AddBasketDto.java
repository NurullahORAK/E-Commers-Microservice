package org.orak.basket.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddBasketDto {
    private final int productId;
    private final int customerId;
    private final int count;
}
