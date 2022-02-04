package com.open.abyss

import com.google.common.eventbus.Subscribe
import com.open.abyss.branch.IsBankOpened
import com.open.abyss.helpers.PouchTracker
import com.open.abyss.extensions.count
import com.open.abyss.models.RuneType
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.Notifications
import org.powbot.api.event.GameActionEvent
import org.powbot.api.event.InventoryChangeEvent
import org.powbot.api.event.MessageEvent
import org.powbot.api.event.VarpbitChangedEvent
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.OptionType
import org.powbot.api.script.ScriptCategory
import org.powbot.api.script.ScriptConfiguration
import org.powbot.api.script.ScriptManifest
import org.powbot.api.script.paint.Paint
import org.powbot.api.script.paint.PaintBuilder
import org.powbot.api.script.tree.TreeComponent
import org.powbot.api.script.tree.TreeScript
import org.powbot.mobile.script.ScriptManager
import org.powbot.mobile.service.ScriptUploader
import java.util.*

@ScriptManifest(
    name = "Open Abyss",
    description = "Crafts rune using the abyss.",
    version = "1.0.3",
    category = ScriptCategory.Runecrafting,
    author = "PTY",
    markdownFileName = "OpenAbyss.md"
)
@ScriptConfiguration.List(
    [
        ScriptConfiguration(
            name = "Essence",
            description = "Type of essence you wish to use.",
            optionType = OptionType.STRING,
            defaultValue = "Pure essence",
            allowedValues = arrayOf("Pure essence", "Daeyalt essence")
        ),
        ScriptConfiguration(
            name = "Rune",
            description = "Type of rune you wish to craft.",
            optionType = OptionType.STRING,
            defaultValue = "Air",
            allowedValues = arrayOf("Air", "Cosmic", "Earth", "Fire", "Law", "Nature")
        ),
        ScriptConfiguration(
            name = "Method",
            description = "Method you wish to runecraft with.",
            optionType = OptionType.STRING,
            defaultValue = "Chronicle",
            allowedValues = arrayOf("Chronicle", "House") //TODO , "HouseTablets")
        ),
        ScriptConfiguration(
            name = "Food",
            description = "Food you wish to eat when low.",
            optionType = OptionType.STRING,
            allowedValues = arrayOf("Trout", "Salmon", "Tuna", "Lobster", "Bass", "Swordfish", "Monkfish")
        ),
        ScriptConfiguration(
            name = "Restore energy",
            description = "Use energy potions to restore.",
            optionType = OptionType.BOOLEAN,
        ),
    ]
)
class Script : TreeScript() {

    override val rootComponent: TreeComponent<*> by lazy {
        IsBankOpened(this)
    }

    lateinit var configuration: Configuration

    override fun onStart() {
        super.onStart()
        extractConfiguration()
        addPaint()

        // Varpbit doesnt update until its interacted locally, so assume full if it has essence
        if (Inventory.count(configuration.essenceName) > 0) {
            PouchTracker.largePouchFull = true
            PouchTracker.mediumPouchFull = true
            PouchTracker.smallPouchFull = true
        }
    }

    /**
     *  This method extracts the configuration from the GUI which is presented via the class annotations.
     */
    private fun extractConfiguration() {
        val runeType = RuneType.valueOf(getOption<String>("Rune")!!)
        val method = RunecraftingMethod.valueOf(getOption<String>("Method")!!)
        val food = getOption<String>("Food")!!
        val restoreEnergy = getOption<Boolean>("Restore energy")!!
        val essenceType = getOption<String>("Essence")!!

        if (food.isNullOrEmpty()) {
            Notifications.showNotification("Please enter food to eat.")
            ScriptManager.stop()
            return
        }

        configuration = Configuration(essenceType, runeType.altarArea, runeType, method, restoreEnergy, food)
    }

    private fun addPaint() {
        val p: Paint = PaintBuilder.newBuilder()
            .addString("Last leaf:") { lastLeaf.name }
            .addString("Method") { configuration.teleport.toString() }
            .trackSkill(Skill.Runecrafting)
            .trackInventoryItems(configuration.runeType.runeId)
            .y(45)
            .x(40)
            .build()
        addPaint(p)
    }

    /**
     *  Subscribes to the messages received from the game and updates the status accordingly
     *
     *  @param messageEvent The message received form the game.
     */
    @Subscribe
    open fun message(messageEvent: MessageEvent) {
        // Ensure its a game message not a player trying to mess it up
        if (messageEvent.sender.isNotEmpty()) {
            return
        }

        PouchTracker.messageEvent(messageEvent)

        // TODO Make this not in the script but somewhere else
        val m = messageEvent.message.lowercase(Locale.getDefault())
        if (m.contains("but fail to distract them enough") ||
            m.contains("fail to break-up the rock.") ||
            m.contains("not agile enough to get through the gap")
        ) {
            configuration.failedObstacle = true
        }
    }

    /**
     *  Subscribes to changes in the inventory to update the current status of our pouches
     *
     *  @param inventoryChangeEvent The event received from the game
     */
    @Subscribe
    fun inventoryChanged(inventoryChangeEvent: InventoryChangeEvent) {
        PouchTracker.inventoryChangedEvent(inventoryChangeEvent)
    }

    /**
     *  Subscribes to game actions which will update the current status of our pouches
     *
     *  @param gameActionEvent The event received in the game
     */
    @Subscribe
    fun gameActionEvent(gameActionEvent: GameActionEvent) {
        PouchTracker.gameActionEvent(gameActionEvent)
    }

    /**
     *  Subscribes to game actions which will update the current status of our pouches
     *
     *  @param varpbitChangedEvent The event received in the game
     */
    @Subscribe
    fun varpbitChanged(varpbitChangedEvent: VarpbitChangedEvent) {
        if (varpbitChangedEvent.index != 261) {
            return
        }
        PouchTracker.varpbitChanged(varpbitChangedEvent.newValue)
    }
}

fun main(args: Array<String>) {
    ScriptUploader().uploadAndStart("Open Abyss", "", "127.0.0.1:5567", true, false)
}