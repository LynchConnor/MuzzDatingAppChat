package com.muzz.datingappchat.ui.chat

import com.muzz.datingappchat.main.model.Contact
import com.muzz.datingappchat.main.utils.MessageSeparation
import com.muzz.datingappchat.shared.entities.Message
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

/**
 * These tests are to confirm the logic for the separation logic between messages works as intended.
 * Claude helped me build these tests
 */
class MessageSeparationTest {

    private val t0 = LocalDateTime.of(2026, 10, 22, 12, 0)

    private fun msg(from: Contact, secondsAfterStart: Long) =
        Message(senderId = from.id, message = "", messageSent = t0.plusSeconds(secondsAfterStart))

    /** The gap below each message, oldest first — exactly what MessageList renders. */
    private fun gapsOnScreen(messages: List<Message>) =
        messages.mapIndexed { i, message -> MessageSeparation.messageSeparation(message, messages.getOrNull(i + 1)) }

    /** Whether each message has a header above it, oldest first. */
    private fun headersOnScreen(messages: List<Message>) =
        messages.mapIndexed { i, message -> MessageSeparation.showsSectionHeader(message, messages.getOrNull(i - 1)) }

    // --- Separation -----------------------------------------------------------------------

    @Test
    fun `same sender 15s apart is small`() {
        val first = msg(Contact.Connor, 0)
        val second = msg(Contact.Connor, 15)
        assertEquals(4, MessageSeparation.messageSeparation(first, second))
    }

    @Test
    fun `the other user's messages group too`() {
        val first = msg(Contact.Muzz, 0)
        val second = msg(Contact.Muzz, 5)
        assertEquals(4, MessageSeparation.messageSeparation(first, second))
    }

    @Test
    fun `exactly 19s apart is small`() {
        val first = msg(Contact.Connor, 0)
        val second = msg(Contact.Connor, 19)
        assertEquals(4, MessageSeparation.messageSeparation(first, second))
    }

    @Test
    fun `exactly 20s apart is large`() {
        val first = msg(Contact.Connor, 0)
        val second = msg(Contact.Connor, 20)
        assertEquals(12, MessageSeparation.messageSeparation(first, second))
    }

    @Test
    fun `exactly 21s apart is large`() {
        val first = msg(Contact.Connor, 0)
        val second = msg(Contact.Connor, 21)
        assertEquals(12, MessageSeparation.messageSeparation(first, second))
    }

    @Test
    fun `different senders are never small`() {
        val first = msg(Contact.Connor, 0)
        val second = msg(Contact.Muzz, 5)
        assertEquals(12, MessageSeparation.messageSeparation(first, second))
    }

    @Test
    fun `whole conversation renders the expected gaps`() {
        val conversation = listOf(
            msg(Contact.Connor, 0), // Next is Connor 5s later, small gap [true]
            msg(Contact.Connor, 5), // Next is 60s later, large gap [false]
            msg(Contact.Connor, 65), // Next is a different user, large gap [false]
            msg(Contact.Muzz, 70), // Next is Muzz 8s later, small gap [true]
            msg(Contact.Muzz, 78), // Newest message, nothing below it [false]
        )
        val expectedSmall = listOf(true, false, false, true, false)

        assertEquals(expectedSmall, gapsOnScreen(conversation).map { it == 4 })
    }

    // --- Section headers ------------------------------------------------------------------

    @Test
    fun `first message always has a header`() {
        assertTrue(MessageSeparation.showsSectionHeader(msg(Contact.Connor, 0), null))
    }

    @Test
    fun `exactly an hour later has no header`() {
        val first = msg(Contact.Connor, 0)
        val second = msg(Contact.Muzz, 60 * 60)
        assertFalse(MessageSeparation.showsSectionHeader(second, first))
    }

    @Test
    fun `an hour and a second later has a header`() {
        val first = msg(Contact.Connor, 0)
        val second = msg(Contact.Muzz, 60 * 60 + 1)
        assertTrue(MessageSeparation.showsSectionHeader(second, first))
    }

    @Test
    fun `header gap is measured from the previous message, not the last header`() {
        val conversation = listOf(
            msg(Contact.Connor, 0), // First message [true]
            msg(Contact.Connor, 50 * 60), // 50 minutes after the previous [false]
            msg(Contact.Connor, 100 * 60), // 50 minutes after the previous, 100 after the header [false]
            msg(Contact.Muzz, 200 * 60), // 100 minutes after the previous [true]
        )
        val expectedHeaders = listOf(true, false, false, true)

        assertEquals(expectedHeaders, headersOnScreen(conversation))
    }
}
