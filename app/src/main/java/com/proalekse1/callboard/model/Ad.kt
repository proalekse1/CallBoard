package com.proalekse1.callboard.model

data class Ad( //дата класс для хранения заполненных строк объявления
    val country: String? = null,
    val city: String? = null,
    val tel: String? = null,
    val index: String? = null,
    val withSent: String? = null,
    val category: String? = null,
    val title: String? = null,
    val price: String? = null,
    val description: String? = null,
    val key: String? = null, //уникальный ключ каждого объявления
    val uid: String? = null //идентификатор пользователя
)
