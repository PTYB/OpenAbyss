package com.open.abyss.leaf

import com.open.abyss.Constants.AREA_INNER_ABYSS
import com.open.abyss.Script
import com.open.abyss.helpers.MessageListener
import com.open.abyss.helpers.SystemMessageManager
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.GameObject
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Objects
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.walking.local.LocalPathFinder
import org.powbot.api.rt4.walking.local.Utils.getWalkableNeighbor
import org.powbot.api.script.tree.Leaf
import java.util.logging.Logger

class EnterAbyssInner(script: Script) : Leaf<Script>(script, "Enter abyss inner") {

    private val logger = Logger.getLogger(this.javaClass.name)
    private val gameObjectList: Array<String>
    // Theres are lots of tendrils in the abyss
    private val actionList: Array<String>
    private val messagesToListen = arrayOf(
        "but fail to distract them enough",
        "fail to break-up the rock.",
        "not agile enough to get through the gap",
        "fail to cut through the tendrils."
    )

    init {
        val gameObjects = mutableListOf("Gap", "Eyes")
        val actions = mutableListOf("Distract", "Squeeze-through",)

        if (script.configuration.usePickaxe) {
            gameObjects.add("Rock")
            actions.add("Mine")
        }
        if (script.configuration.useAxe) {
            gameObjects.add("Tendrils")
            actions.add("Chop")
        }

        gameObjectList = gameObjects.toTypedArray()
        actionList = actions.toTypedArray()
    }

    override fun execute() {
        val nearestGameObject = Objects.stream()
            .type(GameObject.Type.INTERACTIVE)
            .name(*gameObjectList)
            .action(*actionList)
            .nearest()
            .first()

        if (!nearestGameObject.inViewport(true)) {
            val path = LocalPathFinder.findPath(nearestGameObject.getWalkableNeighbor(false))
            path.traverseUntilReached(3.0) { nearestGameObject.tile.distance() < 4 && nearestGameObject.inViewport(true) }
            Condition.sleep(Random.nextInt(70, 200))
        }

        if (nearestGameObject.inViewport(true)) {
            val interaction = getInteraction(nearestGameObject.name)
            val message = MessageListener(1, messagesToListen)
            SystemMessageManager.addMessageToListen(message)

            if (nearestGameObject.interact(interaction)) {
                if (Condition.wait { message.count == 0 || Players.local().animation() != -1 }) {
                    Condition.wait({ message.count == 0 || AREA_INNER_ABYSS.contains(Players.local()) }, 500, 16)
                }
            } else {
                Movement.step(nearestGameObject)
            }
        } else {
            Movement.step(nearestGameObject)
        }
    }

    private fun getInteraction(name: String): String {
        return when (name) {
            "Gap" -> "Squeeze-through"
            "Rock" -> "Mine"
            "Eyes" -> "Distract"
            "Tendrils" -> "Chop"
            else -> {
                logger.info("Invalid name $name")
                ""
            }
        }
    }
}