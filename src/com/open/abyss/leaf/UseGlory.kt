package com.open.abyss.leaf

import com.open.abyss.Script
import com.open.abyss.extensions.nearestGameObject
import com.open.abyss.helpers.House
import com.open.abyss.models.deriveOrientation
import org.powbot.api.Condition
import org.powbot.api.rt4.Camera
import org.powbot.api.rt4.GameObject
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Objects
import org.powbot.api.script.tree.Leaf

class UseGlory(script: Script) : Leaf<Script>(script, "Using glory") {
    override fun execute() {
        val glory = Objects.nearestGameObject("Amulet of Glory")
        if (glory != GameObject.Nil) {
            interactWithHouseObject(glory)
        }
    }

    private fun interactWithHouseObject(houseObject: GameObject) {
        val tile = houseObject.deriveOrientation()

        if (!houseObject.inViewport()) {
            Camera.turnTo(houseObject)
        }

        if (!tile.reachable()) {
            Movement.builder(tile)
                .setWalkUntil{tile.reachable()}
                .move()
        }

        if (houseObject.interact("Edgeville")) {
            Condition.wait({ !House.inside() }, 500, 16)
        }
    }

}