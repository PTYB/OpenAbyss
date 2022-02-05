package com.open.abyss.leaf.bankopened

import com.open.abyss.Constants
import com.open.abyss.Script
import com.open.abyss.extensions.count
import com.open.abyss.extensions.mustWithdrawItem
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf

class WithdrawTablet(script: Script) : Leaf<Script>(script, "Withdrawing tablets") {

    override fun execute() {
        if (Inventory.count(Constants.ITEM_HOUSE_TELEPORT) == 0) {
            Bank.mustWithdrawItem(Constants.ITEM_HOUSE_TELEPORT, 10)
        }
    }
}