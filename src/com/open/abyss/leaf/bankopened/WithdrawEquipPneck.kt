package com.open.abyss.leaf.bankopened

import com.open.abyss.Constants
import com.open.abyss.Constants.ID_RING_OF_DUELING
import com.open.abyss.Constants.ITEM_PHOENIX_NECKLACE
import com.open.abyss.Script
import com.open.abyss.extensions.mustWithdrawItem
import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Equipment
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf

class WithdrawEquipPneck(script: Script) : Leaf<Script>(script, "Withdrawing Phoenix necklace") {

    override fun execute() {
        if (Inventory.stream().name(ITEM_PHOENIX_NECKLACE).isEmpty()) {
            Bank.mustWithdrawItem(ITEM_PHOENIX_NECKLACE,1)
        } else if (Equipment.itemAt(Equipment.Slot.NECK).name() != ITEM_PHOENIX_NECKLACE){
            val necklace = Inventory.stream().name(ITEM_PHOENIX_NECKLACE).first()
            if (necklace.interact("Wear")) {
                Condition.wait { Equipment.itemAt(Equipment.Slot.NECK).name() == ITEM_PHOENIX_NECKLACE }
            }
        }
    }
}