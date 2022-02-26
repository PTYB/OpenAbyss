package com.open.abyss

import com.open.abyss.models.RuneType
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.Area

data class Configuration(
    val essenceName: String,
    val alterArea: Area,
    val runeType: RuneType,
    val teleport: RunecraftingMethod,
    val useEnergyRestore: Boolean,
    val foodName: String,
    val usePNeck: Boolean
) {
    val healthRestorePercent = 83
    val energyRestore = 70


    var useAxe = false
    var usePickaxe = false

}