package com.open.abyss.branch

import com.open.abyss.Constants
import com.open.abyss.Constants.ID_DECAYED_POUCHES
import com.open.abyss.Constants.ITEM_RUNE_AIR
import com.open.abyss.Constants.ITEM_RUNE_EARTH
import com.open.abyss.Constants.ITEM_RUNE_LAW
import com.open.abyss.Constants.ITEM_PURE_ESSENCE
import com.open.abyss.Constants.energyPotionNames
import com.open.abyss.Script
import com.open.abyss.extensions.count
import com.open.abyss.com.open.abyss.helpers.PouchTracker
import com.open.abyss.helpers.House
import com.open.abyss.leaf.*
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class ShouldBank(script: Script) : Branch<Script>(script, "Bank open") {
    override val successComponent: TreeComponent<Script> = NeedsEnergyRecover(script) // TODO Banking
    override val failedComponent: TreeComponent<Script> = ShouldRunecraft(script)

    override fun validate(): Boolean {
        return Bank.opened()
    }
}

// TODO Banking
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
        return Inventory.count(ITEM_PURE_ESSENCE) > 0
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

        return script.configuration.useEnergyRestore && Inventory.count(*energyPotionNames) > 0
    }
}

class NeedsToWithdrawRunes(script: Script) : Branch<Script>(script, "Withdraw runes") {
    override val successComponent: TreeComponent<Script> = WithdrawRunes(script)
    override val failedComponent: TreeComponent<Script> = NeedsToFillPouches(script)

    override fun validate(): Boolean {
        if (script.configuration.teleport != RunecraftingMethod.House) {
            return false
        }
        return Inventory.count(ITEM_RUNE_LAW) == 0 || Inventory.count(ITEM_RUNE_AIR) == 0 ||
                Inventory.count(ITEM_RUNE_EARTH) == 0
    }
}

class ShouldRunecraft(script: Script) : Branch<Script>(script, "Checking essense") {
    override val successComponent: TreeComponent<Script> = InAltarWithEssense(script)
    override val failedComponent: TreeComponent<Script> = InAltar(script)

    override fun validate(): Boolean {
        return PouchTracker.hasPouchToEmpty() || Inventory.count(ITEM_PURE_ESSENCE) > 0
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

class InAltarWithEssense(script: Script) : Branch<Script>(script, "In altar with essense") {
    override val successComponent: TreeComponent<Script> = CraftRunes(script)
    override val failedComponent: TreeComponent<Script> = InAbyss(script)

    override fun validate(): Boolean {
        return script.configuration.alterArea.contains(Players.local())
    }
}

// TODO Check if fully stocked?

class CraftRunes(script: Script) : Branch<Script>(script, "Has essense in inventory") {
    override val successComponent: TreeComponent<Script> = CraftAtAltar(script)
    override val failedComponent: TreeComponent<Script> = EmptyPouches(script)

    override fun validate(): Boolean {
        return Inventory.count(ITEM_PURE_ESSENCE) > 0
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
    override val successComponent: TreeComponent<Script> = NeedsToRepairPouch(script)
    override val failedComponent: TreeComponent<Script> = EnterAbyssInner(script)

    override fun validate(): Boolean {
        return Constants.AREA_INNER_ABYSS.contains(Players.local())
    }
}

class NeedsToRepairPouch(script: Script) : Branch<Script>(script, "Needs to repair pouch") {
    override val successComponent: TreeComponent<Script> = RepairPouch(script)
    override val failedComponent: TreeComponent<Script> = EnterRift(script)

    override fun validate(): Boolean {
        return Inventory.count(*ID_DECAYED_POUCHES) > 0
    }
}