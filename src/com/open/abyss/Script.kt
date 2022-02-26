package com.open.abyss

import com.google.common.eventbus.Subscribe
import com.open.abyss.branch.IsBankOpened
import com.open.abyss.helpers.PouchTracker
import com.open.abyss.extensions.count
import com.open.abyss.helpers.SystemMessageManager
import com.open.abyss.models.RuneType
import com.open.abyss.models.RunecraftingMethod
import org.powbot.api.Notifications
import org.powbot.api.event.GameActionEvent
import org.powbot.api.event.InventoryChangeEvent
import org.powbot.api.event.MessageEvent
import org.powbot.api.event.VarpbitChangedEvent
import org.powbot.api.rt4.Equipment
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
import java.util.logging.Logger

@ScriptManifest(
    name = "Open Abyss",
    description = "Crafts rune using the abyss.",
    version = "1.0.7",
    category = ScriptCategory.Runecrafting,
    author = "PTY, Okazaki",
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
            allowedValues = arrayOf("Air", "Cosmic", "Death", "Earth", "Fire", "Law", "Nature")
        ),
        ScriptConfiguration(
            name = "Method",
            description = "Method you wish to runecraft with.",
            optionType = OptionType.STRING,
            defaultValue = "Chronicle",
            allowedValues = arrayOf("Chronicle", "House", "HouseTablets", "RingOfDueling")
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
        ScriptConfiguration(
            name = "Phoenix necklace",
            description = "Use Phoenix necklace?",
            optionType = OptionType.BOOLEAN,
        ),
    ]
)
class Script : TreeScript() {
    private val logger = Logger.getLogger(this.javaClass.name)

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
            PouchTracker.supportedPouches.forEach { it.status = true }
        }
        PouchTracker.updatePouchesToTrack()
    }

    /**
     *  This method extracts the configuration from the GUI which is presented via the class annotations.
     */
    private fun extractConfiguration() {
        val runeType = RuneType.valueOf(getOption("Rune"))
        val method = RunecraftingMethod.valueOf(getOption("Method"))
        val food = getOption<String>("Food")
        val restoreEnergy = getOption<Boolean>("Restore energy")
        val essenceType = getOption<String>("Essence")
        val usePNeck = getOption<Boolean>("Phoenix necklace")

        if (food.isNullOrEmpty()) {
            Notifications.showNotification("Please enter food to eat.")
            ScriptManager.stop()
            return
        }

        configuration = Configuration(essenceType, runeType.altarArea, runeType, method, restoreEnergy, food, usePNeck)
        checkObstacleMethods()
    }

    /**
     *  Checks the possible equipment to see what abyss methods are possible
     */
    private fun checkObstacleMethods() {
        val equippedWeapon = Equipment.itemAt(Equipment.Slot.MAIN_HAND)

        if (equippedWeapon.name().contains(" axe")) {
            logger.info("Woodcutting enabled")
            configuration.useAxe = true
        } else if (equippedWeapon.name().contains(" pickaxe")) {
            logger.info("Mining enabled")
            configuration.usePickaxe = true
        }
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
        SystemMessageManager.messageRecieved(messageEvent)
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
    ScriptUploader().uploadAndStart("Open Abyss", "", "127.0.0.1:5645", true, false)
}