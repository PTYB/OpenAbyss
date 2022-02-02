package com.open.abyss.models

import org.powbot.api.Tile
import org.powbot.api.rt4.GameObject
import org.powbot.api.rt4.Objects

enum class Orientation(val x: Int, val y: Int) {
    East(1, 0),
    South(0, -1),
    West(-1, 0),
    North(0, 1),
}


fun GameObject.deriveOrientation(): Tile {
    val orientation = Orientation.values()[orientation() - 4]
    return tile.derive(orientation.x, orientation.y)
}