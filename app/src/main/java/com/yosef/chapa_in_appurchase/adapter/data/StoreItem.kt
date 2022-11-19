package com.yosef.chapa_in_appurchase.adapter.data

import com.yosef.chapainapppurchase.payment_type.ItemPayment
import kotlin.reflect.KFunction1

data class StoreItem(
    val title: String, val data: ItemPayment, val onSuccess: KFunction1<String, Unit>
)