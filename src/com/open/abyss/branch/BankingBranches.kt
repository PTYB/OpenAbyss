package com.open.abyss.branch

import com.open.abyss.Constants
import com.open.abyss.Script
import com.open.abyss.com.open.abyss.helpers.PouchTracker
import com.open.abyss.extensions.count
import com.open.abyss.helpers.House
import com.open.abyss.leaf.*
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class NeedsEnergyRecover(script: Script) : Branch<Script>(script, "Checking essense") {
    override val successComponent: TreeComponent<Script> = WithdrawRecoveryItems(script)
    override val failedComponent: TreeComponent<Script> = NeedsToWithdrawRunes(script)

    override fun validate(): Boolean {
        return Combat.healthPercent() < script.configuration.healthRestorePercent ||
                (script.configuration.useEnergyRestore && Movement.energyLevel() < script.configuration.energyRestore)
    }
}

class NeedsToFillPouches(script: Script) : Branch<Script>(script, "Needs to fill pouches") {
    override val successComponent: TreeComponent<Script> = FillPouches(script)
    override val failedComponent: TreeComponent<Script> = FinishBanking(script)

    override fun validate(): Boolean {
        // Force it to update pouches since bank is open
        PouchTracker.varpbitChanged(Varpbits.varpbit(261))

        return PouchTracker.hasPouchToFill()
    }
}

class FillPouches(script: Script) : Branch<Script>(script, "Fill all pouches") {
    override val successComponent: TreeComponent<Script> = FillAllPouches(script)
    override val failedComponent: TreeComponent<Script> = WithdrawEssence(script)

    override fun validate(): Boolean {
        return Inventory.count(Constants.ITEM_PURE_ESSENCE) > 0
    }
}

class FinishBanking(script: Script) : Branch<Script>(script, "Fill all pouches") {
    override val successComponent: TreeComponent<Script> = CloseBank(script)
    override val failedComponent: TreeComponent<Script> = WithdrawEssence(script)

    override fun validate(): Boolean {
        return Inventory.isFull()
    }
}

class WithdrawRecoveryItems(script: Script) : Branch<Script>(script, "Withdraw supplies") {
    override val successComponent: TreeComponent<Script> = ConsumeSupplies(script)
    override val failedComponent: TreeComponent<Script> = WithdrawSupplies(script)

    override fun validate(): Boolean {
        if (Inventory.count(script.configuration.foodName) > 0) {
            return true
        }

        return script.configuration.useEnergyRestore && Inventory.count(*Constants.energyPotionNames) > 0
    }
}

class NeedsToWithdrawRunes(script: Script) : Branch<Script>(script, "Withdraw runes") {
    override val successComponent: TreeComponent<Script> = WithdrawRunes(script)
    override val failedComponent: TreeComponent<Script> = NeedsToFillPouches(script)

    override fun validate(): Boolean {
        if (script.configuration.teleport != RunecraftingMethod.House) {
            return false
        }
        return Inventory.count(Constants.ITEM_RUNE_LAW) == 0 || Inventory.count(Constants.ITEM_RUNE_AIR) == 0 ||
                Inventory.count(Constants.ITEM_RUNE_EARTH) == 0
    }
}

class ShouldRunecraft(script: Script) : Branch<Script>(script, "Checking essense") {
    override val successComponent: TreeComponent<Script> = InAltarWithEssense(script)
    override val failedComponent: TreeComponent<Script> = InAltar(script)

    override fun validate(): Boolean {
        return PouchTracker.hasPouchToEmpty() || Inventory.count(Constants.ITEM_PURE_ESSENCE) > 0
    }
}

class InAltar(script: Script) : Branch<Script>(script, "In altar") {
    override val successComponent: TreeComponent<Script> = Teleport(script)
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

class Teleport(script: Script) : Branch<Script>(script, "Which teleport") {
    override val successComponent: TreeComponent<Script> = ChronicleTeleport(script)
    override val failedComponent: TreeComponent<Script> = HouseTeleport(script)

    override fun validate(): Boolean {
        return script.configuration.teleport == RunecraftingMethod.Chronicle
    }
}

class HouseTeleport(script: Script) : Branch<Script>(script, "House teleport runes") {
    override val successComponent: TreeComponent<Script> = HouseTeleportRunes(script)
    override val failedComponent: TreeComponent<Script> = HouseTeleportTablet(script)

    override fun validate(): Boolean {
        return script.configuration.teleport == RunecraftingMethod.House
    }
}
