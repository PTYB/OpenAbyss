package com.open.abyss.helpers

import org.powbot.api.rt4.Combat
import org.powbot.api.rt4.Movement
import kotlin.math.ceil

object SupplyCalculator {

    val foodList = mapOf(
        "Trout" to 7,
        "Lobster" to 12,
        "Bass" to 13
    )

    val staminaList = mapOf(
        "Energy potion(4)" to 40,
        "Energy potion(3)" to 30,
        "Energy potion(2)" to 20,
        "Energy potion(1)" to 10,
        "Strange fruit" to 30,
    )

    fun foodRequired(foodName: String, healthRequired: Int): Int {
        val foodHealing = foodList[foodName]!!
        return ceil((healthRequired - Combat.health()) / foodHealing.toDouble()).toInt()
    }

    fun staminaItemsRequired(itemName: String, energyRequired: Int): Int {
        val energyRestore = SupplyCalculator.staminaList[itemName]!!
        return ceil((energyRequired - Movement.energyLevel()) / energyRestore.toDouble()).toInt()
    }
}