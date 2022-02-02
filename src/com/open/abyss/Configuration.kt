package com.open.abyss

import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.Area


data class Configuration(
    val alterArea: Area,
    val runeType: String,
    val teleport: RunecraftingMethod,
    val useEnergyRestore: Boolean,
    val foodName: String
) {

    // TODO Make this shit not ugly
    var failedObstacle = false

    val healthRestorePercent = 70
    val energyRestore = 70

}