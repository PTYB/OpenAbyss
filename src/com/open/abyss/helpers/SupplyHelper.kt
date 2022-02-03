package com.open.abyss.helpers

import org.powbot.api.rt4.Combat
import org.powbot.api.rt4.Movement
import kotlin.math.ceil

object SupplyHelper {

    val foodList = mapOf(
        "Trout" to 7,
        "Salmon" to 9,
        "Tuna" to 10,
        "Lobster" to 12,
        "Bass" to 13,
        "Swordfish" to 14,
        "Monkfish" to 16,
    )

    val staminaList = mapOf(
        "Energy potion(4)" to 40,
        "Energy potion(3)" to 30,
        "Energy potion(2)" to 20,
        "Energy potion(1)" to 10,
        "Super energy(4)" to 80,
        "Super energy(3)" to 60,
        "Super energy(2)" to 40,
        "Super energy(1)" to 20,
        "Strange fruit" to 30,
    )

    fun foodRequired(foodName: String, healthRequired: Int): Int {
        val foodHealing = foodList[foodName]!!
        return ceil((healthRequired - Combat.health()) / foodHealing.toDouble()).toInt()
    }

    fun staminaItemsRequired(itemName: String, energyRequired: Int): Int {
        val energyRestore = SupplyHelper.staminaList[itemName]!!
        return ceil((energyRequired - Movement.energyLevel()) / energyRestore.toDouble()).toInt()
    }

    fun isEnergyPotion(itemName: String): Boolean {
        return staminaList[itemName] != null
    }

    fun energyPotionNames(): Array<String> {
        return staminaList.keys.toTypedArray()
    }

}