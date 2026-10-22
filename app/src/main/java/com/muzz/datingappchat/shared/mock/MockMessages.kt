package com.muzz.datingappchat.shared.mock

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.muzz.datingappchat.main.model.ChatUIState
import com.muzz.datingappchat.main.model.Contact
import com.muzz.datingappchat.shared.entities.Message
import com.muzz.datingappchat.main.view.ChatScreen
import com.muzz.datingappchat.ui.theme.MuzzDatingAppChatTheme
import java.time.LocalDate

private val start = LocalDate.now().minusDays(3).atTime(11, 59)

private fun at(hours: Long, minutes: Long = 0, seconds: Long = 0) =
    start.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds)

/**
 * Used Claude to generate the mock data to match the implementation outlined in the doc.
 * Created a basic time adjustment function in order to see separated messages and make
 * sure the UI matches the logic.
 */
val mockItems = listOf(
    // Section: first message. 2 days old, so the header shows a date.
    Message(senderId = Contact.Muzz.id, message = "Hey! Saw you're into hiking too", messageSent = start.minusDays(2)),

    // Section: 2 days since the previous message. Small gap below (next is mine, 19s later).
    Message(senderId = Contact.Connor.id, message = "Haha yes, love it!", messageSent = at(0)),
    // Normal gap below: the next message is exactly 20s later (not less than 20s).
    Message(senderId = Contact.Connor.id, message = "Are you free this weekend?", messageSent = at(0, 0, 19)),
    // Normal gap below: the next message is 6s later, but from a different sender.
    Message(senderId = Contact.Connor.id, message = "Thinking a walk then food", messageSent = at(0, 0, 39)),
    Message(senderId = Contact.Muzz.id, message = "Wowsa sounds fun", messageSent = at(0, 0, 45)),

    // No section: exactly 1h after the previous message (not more than 1h).
    Message(senderId = Contact.Muzz.id, message = "Yeh for sure that works. What time do you think?", messageSent = at(1, 0, 45)),
    // Section: 1h 1s after the previous message.
    Message(senderId = Contact.Connor.id, message = "Does 7pm work for you? I've got to go pick up my little brother first from a party", messageSent = at(2, 0, 46)),
    // No section: 50m gap. Normal gap below: same sender, but 50m apart.
    Message(senderId = Contact.Muzz.id, message = "Ok cool!", messageSent = at(2, 50, 46)),
    // No section: 1h 40m after the last header, but only 50m after the previous message.
    Message(senderId = Contact.Muzz.id, message = "See you then!", messageSent = at(3, 40, 46)),

    // Section: next day, so a new weekday.
    Message(senderId = Contact.Connor.id, message = "What are you up to today?", messageSent = at(24)),
    // Group of three with small gaps between them (5s, then 10s).
    Message(senderId = Contact.Muzz.id, message = "Nothing much", messageSent = at(24, 1)),
    Message(senderId = Contact.Muzz.id, message = "Actually just about to go shopping, got any recommendations for a good shoe shop? I'm a fashion disaster", messageSent = at(24, 1, 5)),
    // Normal gap below: same sender, but 25s later.
    Message(senderId = Contact.Muzz.id, message = "The last one went on for hours", messageSent = at(24, 1, 15)),
)

@PreviewLightDark
@Composable
private fun ChatScreenPreview() {
    MuzzDatingAppChatTheme {
        ChatScreen(
            contactName = "Sarah",
            state = ChatUIState.Messages,
            messages = mockItems.reversed(),
            onSend = {},
            onToggleSender = {},
        )
    }
}