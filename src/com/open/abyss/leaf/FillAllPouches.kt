package com.open.abyss.leaf

import com.open.abyss.Script
import com.open.abyss.extensions.count
import com.open.abyss.helpers.PouchTracker
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Item
import org.powbot.api.script.tree.Leaf

class FillAllPouches(script: Script) : Leaf<Script>(script, "Fill pouches") {

    override fun execute() {

        PouchTracker.supportedPouches.forEach {
            if (it.status) {
                return@forEach
            }
            val pouch = Inventory.stream().name(it.itemName).first()
            if (!pouch.actions().contains("Fill")) {
                it.status = true
            } else {
                fillPouch(pouch)
            }
        }

        Condition.sleep(Random.nextInt(600, 900))
    }

    private fun fillPouch(item: Item): Boolean {
        return Inventory.count(script.configuration.essenceName) > 0 && item.interact("Fill")
    }
}