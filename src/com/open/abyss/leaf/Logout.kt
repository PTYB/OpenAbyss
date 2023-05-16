package com.open.abyss.leaf

import com.open.abyss.Script
import org.powbot.api.Preferences
import org.powbot.api.rt4.Game
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class Logout(script: Script) : Leaf<Script>(script, "Logout") {
    override fun execute() {
        if (!Game.loggedIn()) {
            ScriptManager.stop()
        }  else {
            Preferences.setAutoLoginEnabled(false)
            Game.logout()
        }
    }
}