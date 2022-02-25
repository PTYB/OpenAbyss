package com.open.abyss

import org.powbot.api.Area
import org.powbot.api.Filter
import org.powbot.api.Tile
import org.powbot.api.rt4.Item

object Constants {

    const val NAME_ZAMORAK_MAGE = "Mage of Zamorak"
    const val NAME_REPAIR_MAGE = "Dark mage"

    const val ITEM_DAEYALT_ESSENCE = "Daeyalt essence"
    const val ITEM_PURE_ESSENCE = "Pure essence"
    const val ITEM_SMALL_POUCH = "Small pouch"
    const val ITEM_MEDIUM_POUCH = "Medium pouch"
    const val ITEM_LARGE_POUCH = "Large pouch"
    const val ITEM_GIANT_POUCH = "Giant pouch"
    const val ITEM_RUNE_AIR = "Air rune"
    const val ITEM_RUNE_EARTH = "Earth rune"
    const val ITEM_RUNE_LAW = "Law rune"
    const val ITEM_HOUSE_TELEPORT = "Teleport to house"
    const val ITEM_RING_OF_DUELING = "Ring of dueling(8)"
    const val ITEM_PHOENIX_NECKLACE = "Phoenix necklace"

    val ID_DECAYED_POUCHES = arrayOf(5511, 5513).toIntArray()
    val ID_RING_OF_DUELING = arrayOf(2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566).toIntArray()

    val TILE_ZAMORAK_MAGE = Tile(3106, 3558, 0)

    val AREA_AIR_ALTAR = Area(Tile(2828, 4848), Tile(2858, 4817))
    val AREA_COSMIC_ALTAR = Area(Tile(2115, 4859), Tile(2171, 4809))
    val AREA_EARTH_ALTAR = Area(Tile(2626, 4859), Tile(2685, 4815))
    val AREA_FIRE_ALTAR = Area(Tile(2556, 4863), Tile(2618, 4807))
    val AREA_NATURE_ALTAR = Area(Tile(2387, 4854), Tile(2412, 4828))
    val AREA_LAW_ALTAR = Area(Tile(2441, 4856), Tile(2486, 4811))

    val BANK_VARROCK_WEST_SOUTH_SIDE = Tile(3185, 3436)
    val BANK_EDGEVILLE = Tile(3094, 3490)
    val TILE_VARROCK_DITCH = Tile(3137, 3520, 0)
    val TILE_EDGEVILLE_DITCH = Tile(3108, 3520, 0)

    val TILE_FEROX_BANK = Tile(3129,3636, 0)
    val TILE_FEROX_BARRIER = Tile(3135,3617, 0)
    val TILE_FEROX_RESTORE = Tile(3129, 3625, 0)
    var AREA_FEROX = Area(Tile(3123, 3640), Tile(3143, 3617))

    val AREA_ABYSS = Area(Tile(3008, 4863), Tile(3070, 4804))
    val AREA_INNER_ABYSS = Area(
        Tile(3037, 4848),
        Tile(3023, 4840),
        Tile(3026, 4818),
        Tile(3040, 4815),
        Tile(3055, 4822),
        Tile(3056, 4834),
        Tile(3052, 4845)
    )
}