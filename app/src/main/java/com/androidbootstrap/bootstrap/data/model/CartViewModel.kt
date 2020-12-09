package com.androidbootstrap.bootstrap.data.model

import androidx.lifecycle.ViewModel
import java.util.ArrayList

class CartViewModel  {
    private val cart = ArrayList<String>()

    fun add(product: String) {
        cart.add(product)
    }

    fun getCart(): ArrayList<String> {
        return cart
    }
}