package com.open.abyss.branch

import com.open.abyss.Constants
import com.open.abyss.Script
import com.open.abyss.helpers.PouchTracker
import com.open.abyss.extensions.count
import com.open.abyss.helpers.House
import com.open.abyss.leaf.*
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class ShouldRunecraft(script: Script) : Branch<Script>(script, "Checking essense") {
    override val successComponent: TreeComponent<Script> = InAltarWithEssense(script)
    override val failedComponent: TreeComponent<Script> = InAltar(script)

    override fun validate(): Boolean {
        return PouchTracker.hasPouchToEmpty() || Inventory.count(script.configuration.essenceName) > 0
    }
}

class InAltar(script: Script) : Branch<Script>(script, "In altar") {
    override val successComponent: TreeComponent<Script> = ShouldChronicleTeleport(script)
    override val failedComponent: TreeComponent<Script> = InHouse(script)

    override fun validate(): Boolean {
        return script.configuration.alterArea.contains(Players.local())
    }
}

class InHouse(script: Script) : Branch<Script>(script, "In house") {
    override val successComponent: TreeComponent<Script> = UseGlory(script)
    override val failedComponent: TreeComponent<Script> = OpenBank(script)

    override fun validate(): Boolean {
        return House.inside()
    }
}

class ShouldChronicleTeleport(script: Script) : Branch<Script>(script, "Which teleport") {
    override val successComponent: TreeComponent<Script> = ChronicleTeleport(script)
    override val failedComponent: TreeComponent<Script> = ShouldRuneTeleport(script)

    override fun validate(): Boolean {
        return script.configuration.teleport == RunecraftingMethod.Chronicle
    }
}

class ShouldRuneTeleport(script: Script) : Branch<Script>(script, "House teleport runes") {
    override val successComponent: TreeComponent<Script> = HouseTeleportRunes(script)
    override val failedComponent: TreeComponent<Script> = HouseTeleportTablet(script)

    override fun validate(): Boolean {
        return script.configuration.teleport == RunecraftingMethod.House
    }
}
