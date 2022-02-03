package com.open.abyss.leaf

import com.open.abyss.Script
import com.open.abyss.helpers.House
import org.powbot.api.Condition
import org.powbot.api.Notifications
import org.powbot.api.rt4.Magic
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class HouseTeleportRunes(script: Script) : Leaf<Script>(script, "House teleport with runes") {
    private val logger = java.util.logging.Logger.getLogger(this.javaClass.name)

    override fun execute() {
        if (Players.local().animation() != -1) {
            Condition.wait { Players.local().animation() == -1 }
        }

        if (Magic.Spell.TELEPORT_TO_HOUSE.canCast()) {
            if (Magic.Spell.TELEPORT_TO_HOUSE.cast("Cast")) {
                Condition.wait { House.inside() }
            }
        } else {
            Notifications.showNotification("Unable to cast home teleport, stopping")
            logger.info("Unable to cast house teleport stopping")
            ScriptManager.stop()
        }
    }
}