package com.open.abyss.leaf

import com.open.abyss.Constants.ITEM_LARGE_POUCH
import com.open.abyss.Constants.ITEM_MEDIUM_POUCH
import com.open.abyss.Constants.ITEM_SMALL_POUCH
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
        if (!PouchTracker.smallPouchFull) {
            val smallPouch = Inventory.stream().name(ITEM_SMALL_POUCH).first()
            if (!smallPouch.actions().contains("Fill")) {
                PouchTracker.smallPouchFull = true
            } else {
                fillPouch(smallPouch)
            }
        }
        if (!PouchTracker.mediumPouchFull) {
            val mediumPouch = Inventory.stream().name(ITEM_MEDIUM_POUCH).first()
            if (!mediumPouch.actions().contains("Fill")) {
                PouchTracker.mediumPouchFull = true
            } else {
                fillPouch(mediumPouch)
            }
        }

        if (!PouchTracker.largePouchFull) {
            val largePouch = Inventory.stream().name(ITEM_LARGE_POUCH).first()
            if (!largePouch.actions().contains("Fill")) {
                PouchTracker.largePouchFull = true
            } else {
                fillPouch(largePouch)
            }
        }

        Condition.sleep(Random.nextInt(600, 900))
    }

    private fun fillPouch(item: Item): Boolean {
        return Inventory.count(script.configuration.essenceName) > 0 && item.interact("Fill")
    }
}