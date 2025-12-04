package com.linnea;

public interface PricePolicy {

    public int premiumDiscount(int price);

    public int studentDiscount(int price);
}