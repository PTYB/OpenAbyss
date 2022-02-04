package com.open.abyss.leaf.bankopened

import com.open.abyss.Script
import com.open.abyss.helpers.SupplyHelper
import org.powbot.api.Notifications
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager
import java.util.logging.Logger

class WithdrawFood(script: Script) : Leaf<Script>(script, "Withdrawing supplies") {

    private val logger = Logger.getLogger(this.javaClass.name)

    private val foodName: String get() = script.configuration.foodName
    private val healthRequired = lazy {
        (Combat.maxHealth() * (script.configuration.healthRestorePercent / 100.0)).toInt()
    }

    override fun execute() {
        val foodRequired = SupplyHelper.foodRequired(foodName, healthRequired.value)
        if (foodRequired > 0) {
            withdrawItem(foodName, foodRequired)
        } else {
            Bank.deposit(foodName, foodRequired)
        }
    }

    private fun withdrawItem(itemName: String, itemCount: Int): Boolean {
        return if (Bank.stream().name(itemName).first().stack >= itemCount) {
            Bank.withdraw(foodName, itemCount)
        } else {
            Notifications.showNotification("Does not have $itemCount $itemName stopping script.")
            logger.info("Does not have $itemCount $itemName stopping script.")
            ScriptManager.stop()
            false
        }
    }
}