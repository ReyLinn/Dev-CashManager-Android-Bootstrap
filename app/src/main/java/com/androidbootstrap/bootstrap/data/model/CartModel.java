package com.androidbootstrap.bootstrap.data.model;

import java.util.ArrayList;

public class CartModel {
    private ArrayList<ProductModel> Cart;

    public ArrayList<ProductModel> getCart() {
        return Cart;
    }

    public void setCart(ArrayList<ProductModel> cart) {
        Cart = cart;
    }

    public void addProduct(ProductModel Product) {
        Cart.add(Product);
    }
}
