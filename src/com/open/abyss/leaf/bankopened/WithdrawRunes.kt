package com.open.abyss.leaf.bankopened

import com.open.abyss.Constants
import com.open.abyss.Script
import com.open.abyss.extensions.count
import org.powbot.api.Notifications
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager
import java.util.logging.Logger

class WithdrawRunes(script: Script) : Leaf<Script>(script, "Withdrawing runes") {

    private val logger = Logger.getLogger(this.javaClass.name)

    override fun execute() {

        if (Inventory.count(Constants.ITEM_RUNE_AIR) == 0) {
            withdrawItem(Constants.ITEM_RUNE_AIR, 10)
        }

        if (Inventory.count(Constants.ITEM_RUNE_LAW) == 0) {
            withdrawItem(Constants.ITEM_RUNE_LAW, 10)
        }

        if (Inventory.count(Constants.ITEM_RUNE_EARTH) == 0) {
            withdrawItem(Constants.ITEM_RUNE_EARTH, 10)
        }
    }

    private fun withdrawItem(itemName: String, itemCount: Int): Boolean {
        return if (Bank.stream().name(itemName).first().stack >= itemCount) {
            Bank.withdraw(itemName, itemCount)
        } else {
            Notifications.showNotification("Does not have $itemCount $itemName stopping script.")
            logger.info("Does not have $itemCount $itemName stopping script.")
            ScriptManager.stop()
            false
        }
    }
}