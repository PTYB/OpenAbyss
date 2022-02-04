package com.open.abyss.helpers

import com.open.abyss.Constants.ITEM_DAEYALT_ESSENCE
import com.open.abyss.Constants.ITEM_LARGE_POUCH
import com.open.abyss.Constants.ITEM_MEDIUM_POUCH
import com.open.abyss.Constants.ITEM_SMALL_POUCH
import com.open.abyss.Constants.ITEM_PURE_ESSENCE
import org.powbot.api.event.*

object PouchTracker {

    private var lastPouch = ""

    var smallPouchFull = false
    var mediumPouchFull = false
    var largePouchFull = false

    fun hasPouchToEmpty(): Boolean {
        return smallPouchFull || mediumPouchFull || largePouchFull
    }

    fun varpbitChanged(varpbitValue: Int) {
        // Emptying sets them all to 0 for some reason
        if (varpbitValue == 0) {
            return
        }

        smallPouchFull = varpbitValue and 0x1 == 1
        mediumPouchFull = varpbitValue ushr 1 and 0x1 == 1
        largePouchFull = varpbitValue ushr 2 and 0x1 == 1
    }

    // TODO Add pouches to track on startup
    fun hasPouchToFill(): Boolean {
        return !smallPouchFull || !mediumPouchFull || !largePouchFull
    }

    fun inventoryChangedEvent(inventoryChangeEvent: InventoryChangeEvent) {
        if (inventoryChangeEvent.itemName != ITEM_PURE_ESSENCE &&
                inventoryChangeEvent.itemName != ITEM_DAEYALT_ESSENCE) {
            return
        }

        when (inventoryChangeEvent.quantityChange) {
            3 -> smallPouchFull = false
            6 -> mediumPouchFull = false
            7,9 -> largePouchFull = false // 7 = decayed
        }
    }

    fun getPouchesToWithdraw(): Array<String> {
        val list = mutableListOf<String>()
        if (smallPouchFull) {
            list.add(ITEM_SMALL_POUCH)
        }
        if (mediumPouchFull) {
            list.add(ITEM_MEDIUM_POUCH)
        }
        if (largePouchFull) {
            list.add(ITEM_LARGE_POUCH)
        }
        return list.toTypedArray()
    }

    fun gameActionEvent(gameActionEvent: GameActionEvent) {
        when (gameActionEvent.name) {
            ITEM_SMALL_POUCH, ITEM_MEDIUM_POUCH, ITEM_LARGE_POUCH -> {
                lastPouch = gameActionEvent.name
            }
        }
    }

    fun messageEvent(messageEvent: MessageEvent) {
        if (messageEvent.message.contains("There are no essences in this pouch.", true)) {
            when (lastPouch) {
                ITEM_SMALL_POUCH -> smallPouchFull = false
                ITEM_MEDIUM_POUCH -> mediumPouchFull = false
                ITEM_LARGE_POUCH -> largePouchFull = false
            }
        }
    }
}