package com.open.abyss

import org.powbot.api.Area
import org.powbot.api.Filter
import org.powbot.api.Tile
import org.powbot.api.rt4.Item

object Constants {

    const val NAME_ZAMORAK_MAGE = "Mage of Zamorak"
    const val NAME_REPAIR_MAGE = "Dark mage"

    const val ITEM_PURE_ESSENCE = "Pure essence"
    const val ITEM_SMALL_POUCH = "Small pouch"
    const val ITEM_MEDIUM_POUCH = "Medium pouch"
    const val ITEM_LARGE_POUCH = "Large pouch"
    const val ITEM_RUNE_AIR = "Air rune"
    const val ITEM_RUNE_EARTH = "Earth rune"
    const val ITEM_RUNE_LAW = "Law rune"

    val ID_DECAYED_POUCHES = arrayOf(5511, 5513).toIntArray()

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

    val energyPotionNames = arrayOf(
        "Energy Potion(4)", "Energy Potion(3)", "Energy Potion(2)",
        "Energy Potion(1)", "Strange fruit"
    )

    val energyPotionFilter = nameContainsFilter("energy potion", "Strange fruit")

    // TODO Move to extension
    private fun nameContainsFilter(vararg names: String): Filter<Item> {
        return object : Filter<Item> {
            override fun accept(t: Item?): Boolean {
                if (t == null || t == Item.Nil) {
                    return false
                }

                return names.any {
                    t.name().contains(it, true)
                }
            }
        }
    }
}