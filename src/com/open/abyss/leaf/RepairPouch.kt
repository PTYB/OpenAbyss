package com.open.abyss.leaf

import com.open.abyss.Constants.ID_DECAYED_POUCHES
import com.open.abyss.Constants.NAME_REPAIR_MAGE
import com.open.abyss.Script
import com.open.abyss.extensions.count
import com.open.abyss.extensions.nearestNpc
import org.powbot.api.Condition
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Npcs
import org.powbot.api.script.tree.Leaf

class RepairPouch(script: Script) : Leaf<Script>(script, "Repairing pouch") {
    override fun execute() {
        val npc = Npcs.nearestNpc(NAME_REPAIR_MAGE)

        if (!npc.inViewport()) {
            Movement.builder(npc.tile())
                .setWalkUntil { npc.inViewport() }
                .setRunMin(8)
                .setRunMax(20)
                .move()
        }

        if (npc.inViewport() && npc.interact("Repairs")) {
            Condition.wait({ Inventory.count(*ID_DECAYED_POUCHES) == 0 }, 1000, 12)
        }
    }
}