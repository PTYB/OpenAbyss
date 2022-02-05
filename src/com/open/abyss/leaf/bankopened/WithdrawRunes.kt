package com.open.abyss.leaf.bankopened

import com.open.abyss.Constants
import com.open.abyss.Script
import com.open.abyss.extensions.count
import com.open.abyss.extensions.mustWithdrawItem
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf

class WithdrawRunes(script: Script) : Leaf<Script>(script, "Withdrawing runes") {

    override fun execute() {
        if (Inventory.count(Constants.ITEM_RUNE_AIR) == 0) {
            Bank.mustWithdrawItem(Constants.ITEM_RUNE_AIR, 10)
        }

        if (Inventory.count(Constants.ITEM_RUNE_LAW) == 0) {
            Bank.mustWithdrawItem(Constants.ITEM_RUNE_LAW, 10)
        }

        if (Inventory.count(Constants.ITEM_RUNE_EARTH) == 0) {
            Bank.mustWithdrawItem(Constants.ITEM_RUNE_EARTH, 10)
        }
    }
}