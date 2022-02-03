package com.open.abyss.leaf.bankopened

import com.open.abyss.Script
import com.open.abyss.helpers.SupplyHelper
import org.powbot.api.Notifications
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager
import java.util.logging.Logger

class WithdrawEnergyItems(script: Script) : Leaf<Script>(script, "Withdrawing supplies") {

    private val logger = Logger.getLogger(this.javaClass.name)

    override fun execute() {
        val item = Bank.stream().filter{SupplyHelper.isEnergyPotion(it.name()) }.minByOrNull { it.stack }

        if (item == Item.Nil || item == null) {
            Notifications.showNotification("Out of energy restore items stopping")
            logger.info("Out of energy restore items stopping")
            ScriptManager.stop()
            return
        }

        val itemsRequired = SupplyHelper.staminaItemsRequired(item.name(), script.configuration.energyRestore)
        logger.info("Energy restoring items required $itemsRequired")
        Bank.withdraw(item.name(), itemsRequired)
    }
}