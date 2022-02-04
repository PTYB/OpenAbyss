package com.open.abyss.leaf

import com.open.abyss.Script
import com.open.abyss.helpers.PouchTracker
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf

class EmptyPouches(script: Script) : Leaf<Script>(script, "Emptying pouches") {

    override fun execute() {
        var pouchesWithEss = Inventory.stream().name(*PouchTracker.getPouchesToWithdraw()).toList()

        if (!Inventory.opened()) {
            Inventory.open()
        }

        // When crafting you can't withdraw from pouch
        if (Players.local().animation() != -1) {
            Condition.wait { Players.local().animation() == -1 }
        }

        pouchesWithEss.forEach {
            if (it.interact("Empty")) {
                // TODO Add better trigger/time to open. Could do frames maybe?
                Condition.sleep(Random.nextInt(550, 650))
            }
        }
    }
}