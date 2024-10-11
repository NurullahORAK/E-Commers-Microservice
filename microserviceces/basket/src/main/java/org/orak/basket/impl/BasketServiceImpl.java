package org.orak.basket.impl;

import org.orak.auth.customer.api.CustomerDto;
import org.orak.basket.api.AddBasketDto;
import org.orak.basket.api.BasketDto;
import org.orak.basket.api.BasketService;
import org.orak.basket.api.ProductDto;
import org.orak.basket.impl.basketitem.BasketItem;
import org.orak.basket.impl.basketitem.BasketItemServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final BasketRepository repository;
    private final BasketItemServiceImpl basketItemService;


    public final int BASKET_STATUS_NONE = 0;
    public final int BASKET_STATUS_SALED = 1;

    public Optional<CustomerDto> getCustomerById(String customerId) {
        RestTemplate restTemplate = new RestTemplate();
        CustomerDto customer = restTemplate.getForObject("http://localhost:8080/customers" + customerId, CustomerDto.class);
        return Optional.of(customer);
    }


    @Override
    public BasketDto addProductToBasket(AddBasketDto basketDto) {
        CustomerDto customerDto = getCustomerById(String.valueOf(basketDto.getCustomerId())).orElseThrow(() -> new RuntimeException("Category not found"));
        Basket basket = repository.findBasketByCustomerIdAndStatusEquals(customerDto.getCustomerId(), BASKET_STATUS_NONE);
        if (basket != null) {
            // Basket Var Senaryosu
            return sepetVarUrunEkle(basket, basketDto);
        } else {
            // Basket yok senaryosu
            return createNewBasket(basketDto, customerDto);
        }
    }


    @Override
    public BasketDto getBasketByCustomerId(String customerId) {
        Basket basket = repository.findBasketByCustomerIdAndStatusEquals(Integer.parseInt(customerId), BASKET_STATUS_NONE);
        return toDto(basket);
    }

    @Override
    public void removeProductFromBasket(String basketItemId) {
        basketItemService.delete(Integer.parseInt(basketItemId));
    }

    private BasketDto createNewBasket(AddBasketDto basketDto, CustomerDto customer) {
        List<BasketItem> basketItemList = new ArrayList<>();
        Basket basket = new Basket();
        basket.setCustomerId(customer.getCustomerId());
        basket.setStatus(BASKET_STATUS_NONE);
        basket = repository.save(basket);
        ProductDto product = getProductById(basketDto.getProductId()).orElseThrow(() -> new RuntimeException("product not found"));
        BasketItem newBasketItem = createBasketItem(product, basket, basketDto);
        basketItemList.add(newBasketItem);
        basket.setTotalAmount(calculateBasketAmount(basket.getBasketId()));

        basket.setBasketItemList(basketItemList);
        repository.save(basket);
        return toDto(basket);
    }

    private BasketDto sepetVarUrunEkle(Basket basket, AddBasketDto basketDto) {
        List<BasketItem> basketItemList = basket.getBasketItemList();
        BasketItem basketItem = basketItemService.findBasketItemByBasketIdAndProductId(basket.getBasketId(), basketDto.getProductId());

        if (basketItem == null) {
            System.out.println("Eklenen ürün sepette yok");
            ProductDto product = getProductById(basketDto.getProductId()).orElseThrow(() -> new RuntimeException("product not found"));
            BasketItem newBasketItem = createBasketItem(product, basket, basketDto);
            basketItemList.add(newBasketItem);
            ;
        } else {

            ProductDto product = getProductById(basketDto.getProductId()).orElseThrow(() -> new RuntimeException("product not found"));
            basketItem.setProductId(product.getProductId());
            basketItem.setCount(basketItem.getCount() + basketDto.getCount());
            basketItem.setBasketItemAmount(basketItem.getCount() * product.getPrice());
            basketItem.setBasket(basket);

        }

        basket.setTotalAmount(calculateBasketAmount(basket.getBasketId()));
        basket.setBasketItemList(basketItemList);
        repository.save(basket);

        return toDto(basket);

    }

    private BasketItem createBasketItem(ProductDto product, Basket basket, AddBasketDto basketDto) {
        BasketItem newBasketItem = new BasketItem();
        newBasketItem.setProductId(product.getProductId());
        newBasketItem.setCount(basketDto.getCount());
        newBasketItem.setBasketItemAmount(newBasketItem.getCount() * product.getPrice());
        newBasketItem.setBasket(basket);
        return newBasketItem;
    }


    private Optional<ProductDto> getProductById(int productId) {
        RestTemplate restTemplate = new RestTemplate();
        ProductDto product = restTemplate.getForObject("http://localhost:8081/stock" + productId, ProductDto.class);
        return Optional.of(product);
    }

    // Bu metod sepette daha önceden ürün varsa çalışır
    private double calculateBasketAmount(int basketId) {
        Basket basket = repository.findBasketByBasketId(basketId);
        double totalAmount = 0.0;
        for (BasketItem basketItem : basket.getBasketItemList()) {
            totalAmount += basketItem.getBasketItemAmount();
        }
        return totalAmount;
    }

    public BasketDto toDto(Basket basket) {
        return BasketDto.builder()
                .basketId(basket.getBasketId())
                .totalAmount(basket.getTotalAmount())
                .customerId(basket.getCustomerId())
                .status(basket.getStatus())
                .basketItemList(basket.getBasketItemList().stream().map(basketItem -> basketItemService.toDto(basketItem)).collect(Collectors.toList()))
                .build();
    }

    public Basket toEntity(BasketDto basketDto) {
        Basket basket = new Basket();
        basket.setTotalAmount(basketDto.getTotalAmount());
        basket.setStatus(basketDto.getStatus());
        basket.setCustomerId(getCustomerById(String.valueOf(basketDto.getCustomerId())).orElseThrow(() -> new RuntimeException("Customer not found")).getCustomerId());
        return basket;
    }

}
