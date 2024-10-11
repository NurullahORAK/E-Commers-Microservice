package org.orak.basket.impl.basketitem;

import org.orak.basket.impl.Basket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class BasketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int basketItemId;

    private int productId;
    private int count;
    private double basketItemAmount;
    @ManyToOne
    @JoinColumn(name = "basketId")
    private Basket basket;


}
