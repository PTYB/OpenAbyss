package com.open.abyss.branch

import com.open.abyss.Constants
import com.open.abyss.Constants.ITEM_HOUSE_TELEPORT
import com.open.abyss.Script
import com.open.abyss.helpers.PouchTracker
import com.open.abyss.extensions.count
import com.open.abyss.helpers.SupplyHelper
import com.open.abyss.leaf.*
import com.open.abyss.leaf.bankopened.*
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class HasFoodToEat(script: Script) : Branch<Script>(script, "Has food to eat?") {
    override val successComponent: TreeComponent<Script> = EatFood(script)
    override val failedComponent: TreeComponent<Script> = ShouldWithdrawFood(script)

    override fun validate(): Boolean {
        return Inventory.count(script.configuration.foodName) > 0
    }
}

class ShouldWithdrawFood(script: Script) : Branch<Script>(script, "Should withdraw food?") {
    override val successComponent: TreeComponent<Script> = WithdrawFood(script)
    override val failedComponent: TreeComponent<Script> = HasEnergyRestorationItems(script)

    private val healthRequired = lazy {
        (Combat.maxHealth() * (script.configuration.healthRestorePercent / 100.0)).toInt()
    }

    override fun validate(): Boolean {
        return Combat.health() < healthRequired.value
    }
}

class HasEnergyRestorationItems(script: Script) : Branch<Script>(script, "Has energy restore items?") {
    override val successComponent: TreeComponent<Script> = ConsumeEnergyItems(script)
    override val failedComponent: TreeComponent<Script> = ShouldWithdrawEnergyRestorationItems(script)

    override fun validate(): Boolean {
        return script.configuration.useEnergyRestore && Inventory.count(*SupplyHelper.energyPotionNames()) > 0
    }
}

class ShouldWithdrawEnergyRestorationItems(script: Script) : Branch<Script>(script, "Should withdraw energy restoration") {
    override val successComponent: TreeComponent<Script> = WithdrawEnergyItems(script)
    override val failedComponent: TreeComponent<Script> = ShouldWithdrawRunes(script)

    override fun validate(): Boolean {
        return script.configuration.useEnergyRestore && Movement.energyLevel() < script.configuration.energyRestore
    }
}

class ShouldWithdrawRunes(script: Script) : Branch<Script>(script, "Withdraw runes") {
    override val successComponent: TreeComponent<Script> = WithdrawRunes(script)
    override val failedComponent: TreeComponent<Script> = ShouldWithdrawTablet(script)

    override fun validate(): Boolean {
        if (script.configuration.teleport != RunecraftingMethod.House) {
            return false
        }
        return Inventory.count(com.open.abyss.Constants.ITEM_RUNE_LAW) == 0 || Inventory.count(com.open.abyss.Constants.ITEM_RUNE_AIR) == 0 ||
                Inventory.count(com.open.abyss.Constants.ITEM_RUNE_EARTH) == 0
    }
}

class ShouldWithdrawTablet(script: Script) : Branch<Script>(script, "Withdraw tablets") {
    override val successComponent: TreeComponent<Script> = WithdrawTablet(script)
    override val failedComponent: TreeComponent<Script> = ShouldWithdrawEquipRing(script)

    override fun validate(): Boolean {
        if (script.configuration.teleport != RunecraftingMethod.HouseTablets) {
            return false
        }
        return Inventory.count(ITEM_HOUSE_TELEPORT) == 0
    }
}

class ShouldWithdrawEquipRing(script: Script) : Branch<Script>(script, "Withdraw & equip Ring of dueling?") {
    override val successComponent: TreeComponent<Script> = WithdrawEquipRing(script)
    override val failedComponent: TreeComponent<Script> = NeedsToFillPouches(script)

    override fun validate(): Boolean {
        if (script.configuration.teleport != RunecraftingMethod.RingOfDueling) {
            return false
        }
        return Equipment.stream().id(*Constants.ID_RING_OF_DUELING).first().name().isEmpty()
    }
}

class NeedsToFillPouches(script: Script) : Branch<Script>(script, "Needs to fill pouches") {
    override val successComponent: TreeComponent<Script> = ShouldFillPouches(script)
    override val failedComponent: TreeComponent<Script> = ShouldCloseBank(script)

    override fun validate(): Boolean {
        // Force it to update pouches since bank is open
        PouchTracker.varpbitChanged(Varpbits.varpbit(261))

        return PouchTracker.hasPouchToFill()
    }
}

class ShouldFillPouches(script: Script) : Branch<Script>(script, "Fill all pouches") {
    override val successComponent: TreeComponent<Script> = FillAllPouches(script)
    override val failedComponent: TreeComponent<Script> = WithdrawEssence(script)

    override fun validate(): Boolean {
        return Inventory.count(script.configuration.essenceName) > 0
    }
}

class ShouldCloseBank(script: Script) : Branch<Script>(script, "Fill all pouches") {
    override val successComponent: TreeComponent<Script> = CloseBank(script)
    override val failedComponent: TreeComponent<Script> = WithdrawEssence(script)

    override fun validate(): Boolean {
        return Inventory.isFull()
    }
}
