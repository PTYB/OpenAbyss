package com.open.abyss.leaf.bankopened

import com.open.abyss.Constants
import com.open.abyss.Constants.ID_RING_OF_DUELING
import com.open.abyss.Script
import com.open.abyss.extensions.mustWithdrawItem
import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Equipment
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf

class WithdrawEquipRing(script: Script) : Leaf<Script>(script, "Withdrawing Ring of dueling") {

    override fun execute() {
        if (Inventory.stream().id(*ID_RING_OF_DUELING).isEmpty()) {
            Bank.mustWithdrawItem(ID_RING_OF_DUELING,1)
        } else if (Equipment.stream().id(*ID_RING_OF_DUELING).isEmpty()){
            val ring = Inventory.stream().id(*ID_RING_OF_DUELING).first()
            if (ring.interact("Wear")) {
                Condition.wait { Equipment.stream().id(*ID_RING_OF_DUELING).isNotEmpty() }
            }
        }
    }
}