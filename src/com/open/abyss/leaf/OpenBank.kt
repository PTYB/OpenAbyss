package com.open.abyss.leaf

import com.open.abyss.Script
import org.powbot.api.Tile
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.walking.FailureReason
import org.powbot.api.rt4.walking.local.Utils.onMap
import org.powbot.api.script.tree.Leaf
import java.util.logging.Logger

class OpenBank(script: Script) : Leaf<Script>(script, "Opening bank") {
    private val logger: Logger = Logger.getLogger(this.javaClass.simpleName)
    private val bankTile: Tile get() = script.configuration.teleport.bankTile

    override fun execute() {
        if (!bankTile.onMap()) {
            val result = Movement.builder(bankTile)
                .setRunMin(10)
                .setRunMax(20)
                .setWalkUntil { bankTile.distance() < 7 && bankTile.matrix().inViewport() }
                .move()
            if (!result.success) {
                logger.info("Failed ${result.failureReason}")
            }
            if (result.failureReason == FailureReason.Unknown) {
                Movement.step(bankTile)
            }
        }

        if (bankTile.onMap()) {
            val nearestBank = Bank.nearest()
            if (!nearestBank.tile().matrix().inViewport()) {
                Movement.builder(nearestBank)
                    .setRunMin(10)
                    .setRunMax(20)
                    .setWalkUntil { bankTile.distance() < 7 && nearestBank.tile().matrix().inViewport() }
                    .move()
            } else {
                Bank.open()
            }
        }
    }

}