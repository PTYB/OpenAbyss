package com.open.abyss.leaf.bankopened

import com.open.abyss.Script
import com.open.abyss.helpers.SupplyHelper
import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf

class ConsumeEnergyItems(script: Script) : Leaf<Script>(script, "Restoring run energy") {
    override fun execute() {
        val energyPotion = Inventory.stream().filter { SupplyHelper.isEnergyPotion(it.name()) }.toList()

        if (Movement.energyLevel() == 100) {
            energyPotion.forEach {
                Bank.deposit(it.name(), Bank.Amount.ALL)
            }
        } else if (energyPotion.isNotEmpty()) {
            energyPotion.forEach {
                val action = if (it.actions().contains("Drink")) "Drink" else "Eat"
                it.interact(action) && Condition.wait { !it.valid() && Players.local().animation() == -1}

                if (Movement.energyLevel() == 100) {
                    return
                }
            }
        }
    }
}