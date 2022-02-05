package com.open.abyss.leaf

import com.open.abyss.Constants.ITEM_HOUSE_TELEPORT
import com.open.abyss.Script
import com.open.abyss.helpers.House
import org.powbot.api.Condition
import org.powbot.api.Notifications
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Item
import org.powbot.api.rt4.Magic
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class HouseTeleportTablet(script: Script) : Leaf<Script>(script, "House teleport with tablet") {
    private val logger = java.util.logging.Logger.getLogger(this.javaClass.name)

    override fun execute() {
        if (Players.local().animation() != -1) {
            Condition.wait { Players.local().animation() == -1 }
        }

        val tablet = Inventory.stream().name(ITEM_HOUSE_TELEPORT).first()

        if (tablet != Item.Nil) {
            if (tablet.interact("Inside")) {
                Condition.wait { House.inside() }
            }
        } else {
            Notifications.showNotification("Unable to home teleport via tablet")
            logger.info("Unable to home teleport via tablet stopping")
            ScriptManager.stop()
        }
    }
}