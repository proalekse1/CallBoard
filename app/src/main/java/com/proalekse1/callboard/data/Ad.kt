package com.proalekse1.callboard.data

data class Ad( //дата класс для хранения заполненных строк объявления
    val country: String? = null,
    val city: String? = null,
    val tel: String? = null,
    val index: String? = null,
    val withSent: String? = null,
    val category: String? = null,
    val price: String? = null,
    val description: String? = null
)
