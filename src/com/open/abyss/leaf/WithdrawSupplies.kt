package com.open.abyss.leaf

import com.open.abyss.Constants.energyPotionFilter
import com.open.abyss.Script
import org.powbot.api.Filter
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Combat
import org.powbot.api.rt4.Item
import org.powbot.api.rt4.Movement
import org.powbot.api.script.tree.Leaf

class WithdrawSupplies(script: Script) : Leaf<Script>(script, "Withdrawing supplies") {

    override fun execute() {
        val needsToWithdrawFood = Combat.healthPercent() < script.configuration.healthRestorePercent
        val needsToWithdrawEnergy = script.configuration.useEnergyRestore &&
                Movement.energyLevel() < script.configuration.energyRestore

        if (needsToWithdrawFood) {
            // TODO Calculate if it needs more than one
            Bank.withdraw(script.configuration.foodName, 1)
        }

        if (needsToWithdrawEnergy) {
            // TODO Calculate if it needs more than one
            Bank.withdraw(energyPotionFilter, 1)
        }
    }
}