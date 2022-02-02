package com.open.abyss.leaf

import com.open.abyss.Constants
import com.open.abyss.Script
import org.powbot.api.Condition
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf

class ConsumeSupplies(script: Script) : Leaf<Script>(script, "Consume supplies") {
    override fun execute() {
        val energyPotion = Inventory.stream().filter(Constants.energyPotionFilter).toList()
        val food = Inventory.stream().name(script.configuration.foodName).toList()
        if (food.isNotEmpty()) {
            food.forEach {
                it.interact("Eat") && Condition.wait { !it.valid() }
            }
        }

        if (energyPotion.isNotEmpty()) {
            energyPotion.forEach {
                val action = if(it.actions().contains("Drink")) "Drink" else "Eat"
                it.interact(action) && Condition.wait { !it.valid() }
            }
        }
    }
}