package com.open.abyss.leaf.bankopened

import com.open.abyss.Constants
import com.open.abyss.Constants.ITEM_HOUSE_TELEPORT
import com.open.abyss.Script
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.Notifications
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Item
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager
import java.util.logging.Logger

class WithdrawEssence(script: Script) : Leaf<Script>(script, "Withdrawing essence") {

    private val logger = Logger.getLogger(this.javaClass.name)
    var itemsToKeep: Array<String> = arrayOf()

    init {
        val items = mutableListOf(
            script.configuration.essenceName,
            Constants.ITEM_SMALL_POUCH,
            Constants.ITEM_MEDIUM_POUCH,
            Constants.ITEM_LARGE_POUCH
        )
        if (script.configuration.teleport == RunecraftingMethod.House) {
            items.addAll(arrayOf(Constants.ITEM_RUNE_AIR, Constants.ITEM_RUNE_EARTH, Constants.ITEM_RUNE_LAW))
        } else if (script.configuration.teleport == RunecraftingMethod.HouseTablets) {
            items.add(ITEM_HOUSE_TELEPORT)
        }

        itemsToKeep = items.toTypedArray()
    }

    override fun execute() {
        if (Bank.depositAllExcept(*itemsToKeep)) {
            val bankEssenceCount = Bank.stream().name(script.configuration.essenceName).first()
            if (bankEssenceCount == Item.Nil || bankEssenceCount.stackSize() == 0) {
                Notifications.showNotification("Out of ${script.configuration.essenceName} stopping")
                logger.info("Out of ${script.configuration.essenceName} stopping")
                ScriptManager.stop()
                return
            }
            Bank.withdraw(script.configuration.essenceName, 0)
        }
    }
}