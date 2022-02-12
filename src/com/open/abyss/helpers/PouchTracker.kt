package com.open.abyss.helpers

import com.open.abyss.Constants.ITEM_DAEYALT_ESSENCE
import com.open.abyss.Constants.ITEM_LARGE_POUCH
import com.open.abyss.Constants.ITEM_MEDIUM_POUCH
import com.open.abyss.Constants.ITEM_SMALL_POUCH
import com.open.abyss.Constants.ITEM_PURE_ESSENCE
import org.powbot.api.event.*
import org.powbot.api.rt4.Inventory
import kotlin.streams.toList

object PouchTracker {

    private var lastPouch = ""
    var supportedPouches = listOf(
        Pouch(ITEM_SMALL_POUCH, 0, false),
        Pouch(ITEM_MEDIUM_POUCH, 1, false),
        Pouch(ITEM_LARGE_POUCH, 2, false),
    )

    var pouchesToTrack: List<Pouch> = listOf()

    fun updatePouchesToTrack() {
        val pouchNamesToTrap = Inventory.stream().name(ITEM_SMALL_POUCH, ITEM_MEDIUM_POUCH, ITEM_LARGE_POUCH)
            .map { it.name() }
            .toList()
        pouchesToTrack = supportedPouches.stream().filter { pouchNamesToTrap.contains(it.itemName) }.toList()
    }

    fun hasPouchToEmpty(): Boolean {
        return pouchesToTrack.any { it.status }
    }

    fun varpbitChanged(varpbitValue: Int) {
        // Emptying sets them all to 0 for some reason
        if (varpbitValue == 0) {
            return
        }

        supportedPouches[0].status = varpbitValue and 0x1 == 1
        supportedPouches[1].status = varpbitValue ushr 1 and 0x1 == 1
        supportedPouches[2].status = varpbitValue ushr 2 and 0x1 == 1
    }

    fun hasPouchToFill(): Boolean {
        return pouchesToTrack.any { !it.status }
    }

    fun inventoryChangedEvent(inventoryChangeEvent: InventoryChangeEvent) {
        if (inventoryChangeEvent.itemName != ITEM_PURE_ESSENCE &&
            inventoryChangeEvent.itemName != ITEM_DAEYALT_ESSENCE
        ) {
            return
        }

        when (inventoryChangeEvent.quantityChange) {
            3 -> supportedPouches[0].status = false
            6 -> supportedPouches[1].status = false
            7, 9 -> supportedPouches[2].status = false // 7 = decayed
        }
    }

    fun getPouchesToWithdraw(): Array<String> {
        return pouchesToTrack.filter { it.status }.map { it.itemName }.toTypedArray()
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
                ITEM_SMALL_POUCH -> supportedPouches[0].status = false
                ITEM_MEDIUM_POUCH -> supportedPouches[1].status = false
                ITEM_LARGE_POUCH -> supportedPouches[2].status = false
            }
        }
    }
}

data class Pouch(val itemName: String, val shift: Int, var status: Boolean)