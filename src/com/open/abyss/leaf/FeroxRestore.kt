package com.open.abyss.leaf

import com.open.abyss.Constants
import com.open.abyss.Script
import org.powbot.api.Condition
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.local.LocalPathFinder
import org.powbot.api.script.tree.Leaf

class FeroxRestore(script: Script) : Leaf<Script>(script, "Use pool") {
    override fun execute() {
        val pool = Objects.stream().type(GameObject.Type.INTERACTIVE).name("Pool of Refreshment").nearest().first()
        if (pool != GameObject.Nil && pool.inViewport()) {
                if (pool.interact("Drink")){
                    Condition.wait { Prayer.prayerPoints() != 0 }
                }
        } else Movement.step(Constants.TILE_FEROX_RESTORE)
    }
}