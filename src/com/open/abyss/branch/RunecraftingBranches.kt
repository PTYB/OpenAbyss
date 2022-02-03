package com.open.abyss.branch

import com.open.abyss.Constants
import com.open.abyss.Script
import com.open.abyss.extensions.count
import com.open.abyss.leaf.*
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class InAltarWithEssense(script: Script) : Branch<Script>(script, "In altar with essense") {
    override val successComponent: TreeComponent<Script> = ShouldCraftRunes(script)
    override val failedComponent: TreeComponent<Script> = InAbyss(script)

    override fun validate(): Boolean {
        return script.configuration.alterArea.contains(Players.local())
    }
}

class ShouldCraftRunes(script: Script) : Branch<Script>(script, "Has essense in inventory") {
    override val successComponent: TreeComponent<Script> = CraftAtAltar(script)
    override val failedComponent: TreeComponent<Script> = EmptyPouches(script)

    override fun validate(): Boolean {
        return Inventory.count(script.configuration.essenceName) > 0
    }
}

class InAbyss(script: Script) : Branch<Script>(script, "In abyss") {
    override val successComponent: TreeComponent<Script> = InInnerAbyssRing(script)
    override val failedComponent: TreeComponent<Script> = PassedDitch(script)

    override fun validate(): Boolean {
        return Constants.AREA_ABYSS.contains(Players.local())
    }
}

class PassedDitch(script: Script) : Branch<Script>(script, "Past ditch") {
    override val successComponent: TreeComponent<Script> = EnterAbyss(script)
    override val failedComponent: TreeComponent<Script> = CrossDitch(script)

    override fun validate(): Boolean {
        return Players.local().y() >= 3523
    }
}

class InInnerAbyssRing(script: Script) : Branch<Script>(script, "In abyss") {
    override val successComponent: TreeComponent<Script> = ShouldRepairPouch(script)
    override val failedComponent: TreeComponent<Script> = EnterAbyssInner(script)

    override fun validate(): Boolean {
        return Constants.AREA_INNER_ABYSS.contains(Players.local())
    }
}

class ShouldRepairPouch(script: Script) : Branch<Script>(script, "Needs to repair pouch") {
    override val successComponent: TreeComponent<Script> = RepairPouch(script)
    override val failedComponent: TreeComponent<Script> = EnterRift(script)

    override fun validate(): Boolean {
        return Inventory.count(*Constants.ID_DECAYED_POUCHES) > 0
    }
}