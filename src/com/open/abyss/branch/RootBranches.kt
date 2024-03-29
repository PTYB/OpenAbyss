package com.open.abyss.branch

import com.open.abyss.Script
import com.open.abyss.leaf.Logout
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

/**
 *  The root node which is executed by the script
 */
class IsBankOpened(script: Script) : Branch<Script>(script, "Bank open") {
    override val successComponent: TreeComponent<Script> = HasFoodToEat(script)
    override val failedComponent: TreeComponent<Script> = ShouldLogout(script)

    override fun validate(): Boolean {
        return Bank.opened()
    }
}
