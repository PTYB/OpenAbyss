package com.open.abyss.leaf

import com.open.abyss.Script
import com.open.abyss.extensions.nearestGameObject
import org.powbot.api.Condition
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Objects
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf

class EnterRift(script: Script) : Leaf<Script>(script, "Enter rift") {
    override fun execute() {
        val rift = Objects.nearestGameObject("${script.configuration.runeType} rift")

        if (!rift.inViewport()) {
            Movement.builder(rift.tile())
                .setWalkUntil { rift.inViewport() }
                .setRunMin(8)
                .setRunMax(20)
                .move()
        }

        if (rift.inViewport() && rift.interact("Exit-through")) {
            Condition.wait({ script.configuration.alterArea.contains(Players.local()) }, 1000, 10)
        }
    }
}