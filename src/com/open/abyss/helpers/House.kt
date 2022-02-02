package com.open.abyss.helpers

import org.powbot.api.rt4.GameObject
import org.powbot.api.rt4.Objects

object House {
    fun inside() : Boolean{
        return Objects.stream(20, GameObject.Type.INTERACTIVE).action("Remove board advert")
            .first() != GameObject.Nil
    }
}