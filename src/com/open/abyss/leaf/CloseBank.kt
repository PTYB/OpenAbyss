package com.open.abyss.leaf

import com.open.abyss.Script
import org.powbot.api.rt4.Bank
import org.powbot.api.script.tree.Leaf

class CloseBank(script: Script) : Leaf<Script>(script, "Close bank") {
    override fun execute() {
        Bank.close()
    }
}