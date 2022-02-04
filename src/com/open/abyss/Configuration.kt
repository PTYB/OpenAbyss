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
    val foodName: String
) {

    // TODO Make this shit not ugly
    var failedObstacle = false

    val healthRestorePercent = 83
    val energyRestore = 70

}