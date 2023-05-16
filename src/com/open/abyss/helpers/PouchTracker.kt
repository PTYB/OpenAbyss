package com.open.abyss.helpers

import com.open.abyss.Constants.ITEM_COLOSSAL_POUCH
import com.open.abyss.Constants.ITEM_DAEYALT_ESSENCE
import com.open.abyss.Constants.ITEM_GIANT_POUCH
import com.open.abyss.Constants.ITEM_LARGE_POUCH
import com.open.abyss.Constants.ITEM_MEDIUM_POUCH
import com.open.abyss.Constants.ITEM_SMALL_POUCH
import com.open.abyss.Constants.ITEM_PURE_ESSENCE
import com.open.abyss.extensions.count
import org.powbot.api.event.*
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import java.util.logging.Logger
import kotlin.streams.toList

object PouchTracker {
    private val logger = Logger.getLogger(this.javaClass.name)

    private var lastPouch = ""
    var supportedPouches = listOf(
        Pouch(ITEM_SMALL_POUCH, 0, false),
        Pouch(ITEM_MEDIUM_POUCH, 1, false),
        Pouch(ITEM_LARGE_POUCH, 2, false),
        Pouch(ITEM_GIANT_POUCH, 3, false),
        Pouch(ITEM_COLOSSAL_POUCH, 4, false),
    )

    var pouchesToTrack: List<Pouch> = listOf()

    fun updatePouchesToTrack() {
        val pouchNamesToTrap =
            Inventory.stream()
                .name(ITEM_SMALL_POUCH, ITEM_MEDIUM_POUCH, ITEM_LARGE_POUCH, ITEM_GIANT_POUCH, ITEM_COLOSSAL_POUCH)
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
        supportedPouches[3].status = varpbitValue ushr 3 and 0x1 == 1
        supportedPouches[4].status = varpbitValue ushr 4 and 0x1 == 1
        lastPouch = ""
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

        if (lastPouch.isEmpty()) {
            logger.info("Ignore")
            return
        }

        logger.info("Change")
        if (pouchesToTrack.size == 1 && pouchesToTrack.contains(supportedPouches[4])) {
            val essCount = Inventory.count(inventoryChangeEvent.itemName!!)
            if (essCount == 0 || Bank.opened()) {
                // Only care if it has essence in inventory and not full
                return
            }
            supportedPouches[4].status = !(Inventory.emptySlotCount() > 0 && essCount > 0)
            logger.info("Updated status = ${supportedPouches[4].status}")
        } else {
             when (inventoryChangeEvent.quantityChange) {
                3 -> supportedPouches[0].status = false
                6 -> supportedPouches[1].status = false
                7 -> supportedPouches[2].status = false// 7 = decayed
                12 -> when (lastPouch) {
                    ITEM_SMALL_POUCH, ITEM_LARGE_POUCH -> {
                        supportedPouches[0].status = false
                        supportedPouches[2].status = false
                    }
                    ITEM_GIANT_POUCH -> supportedPouches[3].status = false
                }
                9 -> when (lastPouch) {
                    ITEM_SMALL_POUCH, ITEM_MEDIUM_POUCH -> {
                        supportedPouches[0].status = false
                        supportedPouches[1].status = false
                    }
                    ITEM_GIANT_POUCH -> supportedPouches[3].status = false
                    ITEM_LARGE_POUCH -> supportedPouches[2].status = false
                }
                13 -> {
                    supportedPouches[1].status = false
                    supportedPouches[2].status = false
                }
                15 -> {
                    // Join both of em
                    supportedPouches[0].status = false
                    supportedPouches[3].status = false
                }
            }
        }
    }

    fun getPouchesToWithdraw(): Array<String> {
        return pouchesToTrack.filter { it.status }.map { it.itemName }.toTypedArray()
    }

    fun gameActionEvent(gameActionEvent: GameActionEvent) {
        when (gameActionEvent.name) {
            ITEM_SMALL_POUCH, ITEM_MEDIUM_POUCH, ITEM_LARGE_POUCH, ITEM_GIANT_POUCH, ITEM_COLOSSAL_POUCH -> {
                lastPouch = gameActionEvent.name
            }
        }
    }

    fun messageEvent(messageEvent: MessageEvent) {
        if (messageEvent.message.contains("There is no essence in this pouch.", true)) {
            when (lastPouch) {
                ITEM_SMALL_POUCH -> supportedPouches[0].status = false
                ITEM_MEDIUM_POUCH -> supportedPouches[1].status = false
                ITEM_LARGE_POUCH -> supportedPouches[2].status = false
                ITEM_GIANT_POUCH -> supportedPouches[3].status = false
                ITEM_COLOSSAL_POUCH -> supportedPouches[4].status = false
            }
        }
    }
}

data class Pouch(val itemName: String, val shift: Int, var status: Boolean)