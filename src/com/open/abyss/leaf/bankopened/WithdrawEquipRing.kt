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
import java.util.logging.Logger

class WithdrawEquipRing(script: Script) : Leaf<Script>(script, "Withdrawing Ring of dueling") {

    private val logger = Logger.getLogger(this.javaClass.name)
    override fun execute() {
        Bank.mustWithdrawItem(Constants.ITEM_RING_OF_DUELING, 1)
        val ring = Inventory.stream().name(Constants.ITEM_RING_OF_DUELING).first()
        val ringinfo = Equipment.stream().id(*ID_RING_OF_DUELING).first().name().isEmpty()
        logger.info("ring slot: $ringinfo")
        ring.interact("Wear")
        Condition.wait { Equipment.stream().name("Ring of Dueling(8)").isNotEmpty()}
    }
}