package com.open.abyss.leaf.bankopened

import com.open.abyss.Script
import com.open.abyss.extensions.count
import com.open.abyss.helpers.PouchTracker
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf

class WithdrawPouches(script: Script) : Leaf<Script>(script, "Withdrawing pouches") {
    override fun execute() {
        PouchTracker.pouchesToTrack.forEach {
            if (Inventory.count(it.itemName) == 0) {
                Bank.withdraw(it.itemName, 1)
            }
        }
    }
}