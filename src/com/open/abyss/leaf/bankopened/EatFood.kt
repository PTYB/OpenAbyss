package com.open.abyss.leaf.bankopened

import com.open.abyss.Script
import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Combat
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf

class EatFood(script: Script) : Leaf<Script>(script, "Eating food") {
    override fun execute() {
        val food = Inventory.stream().name(script.configuration.foodName).toList()

        if (Combat.healthPercent() >= 100){
            Bank.deposit(script.configuration.foodName, 0)
        } else {
            food.forEach {
                val action = if (it.actions().contains("Drink")) "Drink" else "Eat"
                if (it.interact(action)) {
                    Condition.wait { !it.valid() && Players.local().animation() == -1 }
                }
                if (Combat.healthPercent() >= 100) {
                    return@forEach
                }
            }
        }

    }
}