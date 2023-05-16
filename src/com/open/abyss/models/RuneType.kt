package com.open.abyss.models

import com.open.abyss.Constants.AREA_AIR_ALTAR
import com.open.abyss.Constants.AREA_BLOOD
import com.open.abyss.Constants.AREA_CHAOS_ALTAR
import com.open.abyss.Constants.AREA_COSMIC_ALTAR
import com.open.abyss.Constants.AREA_DEATH_ALTAR
import com.open.abyss.Constants.AREA_EARTH_ALTAR
import com.open.abyss.Constants.AREA_FIRE_ALTAR
import com.open.abyss.Constants.AREA_LAW_ALTAR
import com.open.abyss.Constants.AREA_NATURE_ALTAR
import org.powbot.api.Area

enum class RuneType(val runeId: Int, val altarArea: Area) {

    Air(556, AREA_AIR_ALTAR),
    Cosmic(564, AREA_COSMIC_ALTAR),
    Death(560, AREA_DEATH_ALTAR),
    Earth(557, AREA_EARTH_ALTAR),
    Fire(554, AREA_FIRE_ALTAR),
    Law(563, AREA_LAW_ALTAR),
    Nature(561, AREA_NATURE_ALTAR),
    Chaos(562, AREA_CHAOS_ALTAR),
    Blood(565, AREA_BLOOD)

}