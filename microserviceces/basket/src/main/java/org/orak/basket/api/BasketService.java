package org.orak.basket.api;


public interface BasketService {

    BasketDto addProductToBasket(AddBasketDto basketDto);

    BasketDto getBasketByCustomerId(String customerId );

    void removeProductFromBasket(String basketItemId);



}
