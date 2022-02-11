package com.open.abyss.extensions

import org.powbot.api.Condition
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Item
import org.powbot.api.rt4.Skills

fun Condition.waitForExpGained(skill: Int, time: Int = 4500): Boolean {
    val originalXp = Skills.experience(skill)
    return wait ({
        val newExp = Skills.experience(skill)
        newExp > originalXp
    }, 10, time/10)
}