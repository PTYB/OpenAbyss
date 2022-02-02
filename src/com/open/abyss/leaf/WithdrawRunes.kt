package com.open.abyss.leaf

import com.open.abyss.Constants.ITEM_RUNE_AIR
import com.open.abyss.Constants.ITEM_RUNE_EARTH
import com.open.abyss.Constants.ITEM_RUNE_LAW
import com.open.abyss.Script
import com.open.abyss.extensions.count
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf

class WithdrawRunes(script: Script) : Leaf<Script>(script, "Withdrawing runes") {
    override fun execute() {
        if (Inventory.count(ITEM_RUNE_AIR) == 0) {
            Bank.withdraw(ITEM_RUNE_AIR, 10)
        }
        if (Inventory.count(ITEM_RUNE_LAW) == 0) {
            Bank.withdraw(ITEM_RUNE_LAW, 10)
        }
        if (Inventory.count(ITEM_RUNE_EARTH) == 0) {
            Bank.withdraw(ITEM_RUNE_EARTH, 10)
        }
    }
}