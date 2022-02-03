package com.open.abyss.leaf

import com.open.abyss.Script
import com.open.abyss.extensions.nearestGameObject
import org.powbot.api.Condition
import org.powbot.api.Tile
import org.powbot.api.rt4.GameObject
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Objects
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf

class CrossDitch(script: Script) : Leaf<Script>(script, "Crossing ditch") {

    private val ditchTile: Tile get() = script.configuration.teleport.ditchTile

    override fun execute() {

        val ditch = Objects.stream(ditchTile, 6, GameObject.Type.INTERACTIVE).name("Wilderness ditch")
            .shuffled().firstOrNull() ?: GameObject.Nil

        if (ditch == GameObject.Nil) {
            Movement.builder(ditchTile)
                .setWalkUntil { ditchTile.distance() < 3 }
                .setRunMin(50)
                .setRunMax(70)
                .move()
        } else {
            walkToDitch(ditch)
        }
    }

    private fun walkToDitch(ditch: GameObject) {
        if (!ditch.inViewport()) {
            val targetTile = Tile(ditch.tile.x, 3520)
            Movement.builder(targetTile)
                .setWalkUntil { targetTile.distance() < 3 }
                .setRunMin(50)
                .setRunMax(70)
                .move()
        }

        if (ditch.inViewport() && ditch.interact("Cross")) {
            Condition.wait({ Players.local().y() >= 3523 }, 1000, 10)
        }
    }

}