package com.open.abyss.branch

import com.open.abyss.Script
import com.open.abyss.com.open.abyss.helpers.PouchTracker
import com.open.abyss.extensions.count
import com.open.abyss.leaf.*
import com.open.abyss.leaf.bankopened.*
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent
import java.util.logging.Logger

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

    override fun validate(): Boolean {
        return Combat.healthPercent() < script.configuration.healthRestorePercent
    }
}

class HasEnergyRestorationItems(script: Script) : Branch<Script>(script, "Withdraw supplies") {
    override val successComponent: TreeComponent<Script> = ConsumeEnergyItems(script)
    override val failedComponent: TreeComponent<Script> = ShouldWithdrawEnergyRestorationItems(script)

    override fun validate(): Boolean {
        return script.configuration.useEnergyRestore && Inventory.count(*com.open.abyss.Constants.energyPotionNames) > 0
    }
}

class ShouldWithdrawEnergyRestorationItems(script: Script) : Branch<Script>(script, "Withdraw supplies") {
    override val successComponent: TreeComponent<Script> = WithdrawEnergyItems(script)
    override val failedComponent: TreeComponent<Script> = ShouldWithdrawRunes(script)

    override fun validate(): Boolean {
        return script.configuration.useEnergyRestore && Movement.energyLevel() < script.configuration.energyRestore
    }
}

class ShouldWithdrawRunes(script: Script) : Branch<Script>(script, "Withdraw runes") {
    override val successComponent: TreeComponent<Script> = WithdrawRunes(script)
    override val failedComponent: TreeComponent<Script> = NeedsToFillPouches(script)

    override fun validate(): Boolean {
        if (script.configuration.teleport != RunecraftingMethod.House) {
            return false
        }
        return Inventory.count(com.open.abyss.Constants.ITEM_RUNE_LAW) == 0 || Inventory.count(com.open.abyss.Constants.ITEM_RUNE_AIR) == 0 ||
                Inventory.count(com.open.abyss.Constants.ITEM_RUNE_EARTH) == 0
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
        return Inventory.count(com.open.abyss.Constants.ITEM_PURE_ESSENCE) > 0
    }
}

class ShouldCloseBank(script: Script) : Branch<Script>(script, "Fill all pouches") {
    override val successComponent: TreeComponent<Script> = CloseBank(script)
    override val failedComponent: TreeComponent<Script> = WithdrawEssence(script)

    override fun validate(): Boolean {
        return Inventory.isFull()
    }
}