package com.open.abyss.leaf

import com.open.abyss.Constants
import com.open.abyss.Constants.ITEM_RUNE_AIR
import com.open.abyss.Constants.ITEM_RUNE_EARTH
import com.open.abyss.Constants.ITEM_RUNE_LAW
import com.open.abyss.Constants.ITEM_PURE_ESSENCE
import com.open.abyss.Script
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.Notifications
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Item
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager
import java.util.logging.Logger

class WithdrawEssence(script: Script) : Leaf<Script>(script, "Consume supplies") {

    private val logger = Logger.getLogger(this.javaClass.name)
    var itemsToKeep: Array<String> = arrayOf()

    init {
        val items = mutableListOf(
            ITEM_PURE_ESSENCE,
            Constants.ITEM_SMALL_POUCH,
            Constants.ITEM_MEDIUM_POUCH,
            Constants.ITEM_LARGE_POUCH
        )
        if (script.configuration.teleport == RunecraftingMethod.House) {
            items.addAll(arrayOf(ITEM_RUNE_AIR, ITEM_RUNE_EARTH, ITEM_RUNE_LAW))
        }

        itemsToKeep = items.toTypedArray()
    }

    override fun execute() {
        if (Bank.depositAllExcept(*itemsToKeep)) {
            val bankEssenceCount = Bank.stream().name(ITEM_PURE_ESSENCE).first()
            if (bankEssenceCount == Item.Nil || bankEssenceCount.stackSize() == 0) {
                Notifications.showNotification("Out of essence stopping")
                logger.info("Out of essence stopping")
                ScriptManager.stop()
            }
            Bank.withdraw(ITEM_PURE_ESSENCE, 0)
        }
    }
}