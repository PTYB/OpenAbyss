package com.open.abyss.leaf

import com.open.abyss.Constants
import com.open.abyss.Constants.TILE_FEROX_BARRIER
import com.open.abyss.Script
import com.open.abyss.extensions.nearestGameObject
import org.powbot.api.Condition
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.local.LocalPathFinder
import org.powbot.api.script.tree.Leaf

class LeaveFerox(script: Script) : Leaf<Script>(script, "Leave ferox, walk to mage") {
    override fun execute() {
        val barrier = Objects.stream(TILE_FEROX_BARRIER, GameObject.Type.INTERACTIVE).first()
        if (barrier.inViewport() && barrier.interact("Pass-Through")) {
            Condition.wait { !Constants.AREA_FEROX.contains(Players.local()) }
        } else { Movement.step(TILE_FEROX_BARRIER) }
    }
}