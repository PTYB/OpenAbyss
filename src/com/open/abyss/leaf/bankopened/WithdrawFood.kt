package com.open.abyss.leaf.bankopened

import com.open.abyss.Script
import com.open.abyss.extensions.mustWithdrawItem
import com.open.abyss.helpers.SupplyHelper
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf

class WithdrawFood(script: Script) : Leaf<Script>(script, "Withdrawing food") {
    private val foodName: String get() = script.configuration.foodName
    private val healthRequired = lazy {
        (Combat.maxHealth() * (script.configuration.healthRestorePercent / 100.0)).toInt()
    }

    override fun execute() {
        val foodRequired = SupplyHelper.foodRequired(foodName, healthRequired.value)
        if (foodRequired > 0) {
            Bank.mustWithdrawItem(foodName, foodRequired)
        } else {
            Bank.deposit(foodName, foodRequired)
        }
    }
}