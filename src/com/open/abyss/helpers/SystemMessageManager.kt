package com.open.abyss.helpers

import org.powbot.api.event.MessageEvent
import java.util.logging.Logger

object SystemMessageManager {
    private var logger: Logger = Logger.getLogger(this.javaClass.simpleName)
    private var listeners = mutableListOf<MessageListener>()

    fun addMessageToListen(messageListener: MessageListener) {
        listeners.add(messageListener)
    }

    fun messageRecieved(messageEvent: MessageEvent) {
        if (!messageEvent.sender.isNullOrEmpty() || listeners.isEmpty()) {
            return
        }
        logger.info("Message: " + messageEvent.message)

        listeners.forEach { l ->
            if (l.messages.any { messageEvent.message.contains(it) }) {
                l.count = l.count - 1
            }
        }
        val currentTime = System.currentTimeMillis()
        listeners.removeAll { it.count <= 0 || it.expireTime + it.time <= currentTime }
    }
}

class MessageListener(var count: Int, var messages: Array<String>, var expireTime: Int = 5000) {
    val time = System.currentTimeMillis()
}