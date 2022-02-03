package com.open.abyss.leaf

import com.open.abyss.Constants.AREA_INNER_ABYSS
import com.open.abyss.Script
import com.open.abyss.extensions.nearestGameObject
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Objects
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.walking.local.LocalPathFinder
import org.powbot.api.rt4.walking.local.Utils.getWalkableNeighbor
import org.powbot.api.script.tree.Leaf

class EnterAbyssInner(script: Script) : Leaf<Script>(script, "Enter abyss inner") {

    private val gameObjectList = arrayOf("Gap", "Rock", "Eyes")
    private var failed = false

    override fun execute() {
        val nearestGameObject = Objects.nearestGameObject(*gameObjectList)

        if (!nearestGameObject.inViewport(true)) {
            val path = LocalPathFinder.findPath(nearestGameObject.getWalkableNeighbor(false))
            path.traverseUntilReached(3.0) { nearestGameObject.tile.distance() < 4 && nearestGameObject.inViewport(true) }
            Condition.sleep(Random.nextInt(70, 200))
        }

        if (nearestGameObject.inViewport(true)) {
            val interaction = getInteraction(nearestGameObject.name)
            if (nearestGameObject.interact(interaction)) {
                if (Condition.wait { failed || Players.local().animation() != -1 } && !failed) {
                    Condition.wait({ failed || AREA_INNER_ABYSS.contains(Players.local()) }, 1000, 8)
                }
            } else {
                // If it fails to click walk closer to avoid NPCS
                Movement.step(nearestGameObject)
            }
        }

        // TODO Create manager which listens for certain events
        failed = false
    }

    private fun getInteraction(name: String): String {
        return when (name) {
            "Gap" -> "Squeeze-through"
            "Rock" -> "Mine"
            "Eyes" -> "Distract"
            else -> ""
        }
    }
}