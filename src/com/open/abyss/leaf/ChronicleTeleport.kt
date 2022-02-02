package com.open.abyss.leaf

import com.open.abyss.Script
import org.powbot.api.Condition
import org.powbot.api.rt4.Equipment
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf

class ChronicleTeleport (script: Script) : Leaf<Script>(script, "Entering abyss") {

    override fun execute() {
        val item = Equipment.itemAt(Equipment.Slot.OFF_HAND)

        if (item.interact("Teleport")) {
            if (Condition.wait { Players.local().animation() != -1 }) {
                Condition.wait { !script.configuration.alterArea.contains(Players.local()) }
            }
        }
    }
}