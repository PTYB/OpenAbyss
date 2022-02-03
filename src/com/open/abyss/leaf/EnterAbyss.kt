package com.open.abyss.leaf

import com.open.abyss.Constants.AREA_ABYSS
import com.open.abyss.Constants.NAME_ZAMORAK_MAGE
import com.open.abyss.Constants.TILE_ZAMORAK_MAGE
import com.open.abyss.Script
import com.open.abyss.extensions.nearestNpc
import org.powbot.api.Condition
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Npc
import org.powbot.api.rt4.Npcs
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf

class EnterAbyss(script: Script) : Leaf<Script>(script, "Entering abyss") {

    override fun execute() {
        val mage = Npcs.nearestNpc(NAME_ZAMORAK_MAGE)
        if (mage.inViewport()) {
            if (mage.interact("Teleport")) {
                Condition.wait { AREA_ABYSS.contains(Players.local()) }
            }
        } else {
            val tile = if (mage != Npc.Nil) mage.tile() else TILE_ZAMORAK_MAGE
            Movement.step(tile, 3)
        }
    }

}