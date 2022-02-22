package com.open.abyss.models

import com.open.abyss.Constants
import org.powbot.api.Tile

enum class RunecraftingMethod(val bankTile: Tile, val ditchTile: Tile) {
    Chronicle(Constants.BANK_VARROCK_WEST_SOUTH_SIDE, Constants.TILE_VARROCK_DITCH),
    House(Constants.BANK_EDGEVILLE, Constants.TILE_EDGEVILLE_DITCH),
    HouseTablets(Constants.BANK_EDGEVILLE, Constants.TILE_EDGEVILLE_DITCH),
    RingOfDueling(Constants.TILE_FEROX_BANK,Constants.TILE_FEROX_BARRIER)
}