package com.open.abyss.extensions

import org.powbot.api.Notifications
import org.powbot.api.rt4.Bank
import org.powbot.mobile.script.ScriptManager
import java.util.logging.Logger

private val logger = Logger.getLogger("BankExtensions")

fun Bank.mustWithdrawItem(itemID: IntArray, itemCount: Int): Boolean {
    val itemToWithdraw = stream().id(*itemID).first()
    return if (itemToWithdraw.stack >= itemCount) {
        withdraw(itemToWithdraw, itemCount)
    } else {
        Notifications.showNotification("Does not have $itemCount $itemID stopping script.")
        logger.info("Does not have $itemCount $itemID stopping script.")
        ScriptManager.stop()
        false
    }
}

fun Bank.mustWithdrawItem(itemID: Int, itemCount: Int): Boolean {
    return if (stream().id(itemID).first().stack >= itemCount) {
        withdraw(itemID, itemCount)
    } else {
        Notifications.showNotification("Does not have $itemCount $itemID stopping script.")
        logger.info("Does not have $itemCount $itemID stopping script.")
        ScriptManager.stop()
        false
    }
}

fun Bank.mustWithdrawItem(itemName: String, itemCount: Int): Boolean {
    return if (stream().name(itemName).first().stack >= itemCount) {
        withdraw(itemName, itemCount)
    } else {
        Notifications.showNotification("Does not have $itemCount $itemName stopping script.")
        logger.info("Does not have $itemCount $itemName stopping script.")
        ScriptManager.stop()
        false
    }
}
