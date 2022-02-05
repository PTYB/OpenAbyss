package com.open.abyss.leaf

import com.open.abyss.Script
import com.open.abyss.extensions.nearestGameObject
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.Tile
import org.powbot.api.rt4.GameObject
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Objects
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.walking.local.LocalPath
import org.powbot.api.rt4.walking.local.LocalPathFinder
import org.powbot.api.script.tree.Leaf
import java.util.logging.Logger

class CrossDitch(script: Script) : Leaf<Script>(script, "Crossing ditch") {

    private var logger: Logger = Logger.getLogger(this.javaClass.simpleName)
    private val ditchTile: Tile get() = script.configuration.teleport.ditchTile

    override fun execute() {

        val ditch = Objects.stream(ditchTile, 6, GameObject.Type.INTERACTIVE).name("Wilderness ditch")
            .shuffled().firstOrNull() ?: GameObject.Nil

        if (ditch == GameObject.Nil) {
            logger.info("Invalid GO walking to ${ditch.tile}")
            Movement.builder(ditchTile.derive(0, Random.nextInt(0, -1)))
                .setWalkUntil { ditchTile.distance() < 3 }
                .setRunMin(50)
                .setRunMax(70)
                .move()
        } else {
            walkToDitch(ditch)
        }
    }

    private fun walkToDitch(ditch: GameObject) {
        if (!ditch.inViewport() || ditch.tile.distance() >= 3) {
            logger.info("Walking to ditch viewport ${ditch.inViewport()},  dist ${ditch.tile.distance()} ${ditch.tile}")
            val targetTile = Tile(ditch.tile.x, 3520)
            LocalPathFinder.findPath(targetTile)
                .traverseUntilReached(3.0)
        }
        logger.info("Walking to ditch viewport ${ditch.inViewport()}, and attempting interacting")
        if (ditch.inViewport() && ditch.interact("Cross")) {
            Condition.wait({ Players.local().y() >= 3523 }, 1000, 10)
        }
    }

}