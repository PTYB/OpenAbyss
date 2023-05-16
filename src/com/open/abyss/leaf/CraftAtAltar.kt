package com.open.abyss.leaf

import com.open.abyss.Script
import com.open.abyss.extensions.waitForExpGained
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Constants
import org.powbot.api.rt4.GameObject
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Objects
import org.powbot.api.script.tree.Leaf

class CraftAtAltar(script: Script) : Leaf<Script>(script, "Crafting runes") {

    override fun execute() {
        val altar = getAltar()

        if (!altar.inViewport() && Movement.step(altar)) {
            Condition.wait { altar.inViewport() }
        }

        if (altar.interact("Craft-rune") && Condition.waitForExpGained(Constants.SKILLS_RUNECRAFTING)) {
            // Wait a bit for it to allow interactions
            Condition.sleep(Random.nextInt(100, 200))
        }
    }

    private fun getAltar(): GameObject {
        return Objects.stream(15, GameObject.Type.INTERACTIVE).name("Altar")
            .action("Craft-rune").first()
    }
}