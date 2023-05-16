package com.open.abyss.leaf

import com.open.abyss.Script
import com.open.abyss.helpers.House
import org.powbot.api.Condition
import org.powbot.api.Notifications
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class HouseTeleportRunes(script: Script) : Leaf<Script>(script, "House teleport with runes") {
    private val logger = java.util.logging.Logger.getLogger(this.javaClass.name)

    override fun execute() {
        if (Players.local().animation() != -1) {
            Condition.wait { Players.local().animation() == -1 }
        }

        if (Magic.Spell.TELEPORT_TO_HOUSE.canCast()) {
            if (!handleTeleportObstructed()) {
                return
            }
            if (Magic.Spell.TELEPORT_TO_HOUSE.cast()) {
                Condition.wait { House.inside() }
            }
        } else {
            Notifications.showNotification("Unable to cast home teleport, stopping")
            logger.info("Unable to cast house teleport stopping")
            ScriptManager.stop()
        }
    }

    private fun handleTeleportObstructed() : Boolean{
        val spellFiltersVisible = spellFiltersVisible()
        if (spellFiltersVisible) {
            return disableFilters()
        }
        val infoVisible = infoVisible()
        if (infoVisible) {
            return disableInfo()
        }
        return true
    }

    private fun disableInfo() : Boolean {
        val infoButton = Components.stream(218).text("Info").first()
        if (infoButton.click()){
            return Condition.wait { !infoVisible() }
        }
        return false
    }

    private fun infoVisible() : Boolean {
        return Widgets.component(218,191).visible()
    }

    private fun spellFiltersVisible() : Boolean {
        return Components.stream(218).text("Spell Filters").first().visible()
    }
    private fun disableFilters() : Boolean{
        val filtersButton = Components.stream(218).text("Filters").last()
        if (filtersButton.click()){
            return Condition.wait { !spellFiltersVisible() }
        }
        return false
    }
    private fun openChatComp(): Component {
        val w = Widgets.widget(162)
        val idx = (0 until 34).firstOrNull {
            val c = w.component(it)
            c.visible() && c.textureId() == 3053
        } ?: return Component.Nil

        return w.component(idx)
    }
}