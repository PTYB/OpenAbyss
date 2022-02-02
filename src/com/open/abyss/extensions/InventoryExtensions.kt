package com.open.abyss.extensions

import org.powbot.api.rt4.Inventory

fun Inventory.count(vararg name: String): Int {
    return stream().name(*name).count(true).toInt()
}

fun Inventory.count(vararg ids: Int): Int {
    return stream().id(*ids).count(true).toInt()
}
