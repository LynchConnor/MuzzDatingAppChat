package com.muzz.datingappchat.main.utils

import android.util.Log
import com.muzz.datingappchat.shared.entities.Message
import java.time.Duration

object MessageSeparation {

    val TAG = "MessageSeparation"

    /**
     * [messageSeparation] takes a [message] and the [nextMessage] sent after it to
     * understand the amount of separation below the message.
     * If both messages are owned by the same person and the next one is sent less than
     * 20 seconds afterwards, give it a smaller separation.
     */
    fun messageSeparation(message: Message, nextMessage: Message?): Int {
        val nextMessage = nextMessage ?: return 16
        val secondsUntilNext = Duration.between(message.messageSent, nextMessage.messageSent).seconds
        return if (message.senderId == nextMessage.senderId && secondsUntilNext < 20) 4 else 12
    }

    /**
     * [showsSectionHeader] is true when there is no [previousMessage], or it was sent
     * more than an hour before [message].
     */
    fun showsSectionHeader(message: Message, previousMessage: Message?): Boolean {
        val previousMessage = previousMessage ?: return true
        return Duration.between(previousMessage.messageSent, message.messageSent) > Duration.ofHours(1)
    }
}