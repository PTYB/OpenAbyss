package com.open.abyss.leaf

import com.open.abyss.Script
import org.powbot.api.Condition
import org.powbot.api.rt4.Equipment
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf

class RingOfDuelingTeleport (script: Script) : Leaf<Script>(script, "Teleporting with RoD") {
    override fun execute() {
        val item = Equipment.itemAt(Equipment.Slot.RING)
        if (Game.tab(Game.Tab.EQUIPMENT) && item.interact("Ferox Enclave")) {
                if (Condition.wait { Players.local().animation() != -1 }) {
                    Condition.wait { !script.configuration.alterArea.contains(Players.local()) }
                }
        }
    }
}