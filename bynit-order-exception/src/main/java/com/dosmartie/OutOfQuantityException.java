package com.dosmartie;

public class OutOfQuantityException extends RuntimeException{
    public OutOfQuantityException() {
        super("The product in your cart is out of stock.");
    }
}
