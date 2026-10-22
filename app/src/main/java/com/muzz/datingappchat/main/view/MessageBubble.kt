package com.muzz.datingappchat.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.muzz.datingappchat.main.utils.MessageSeparation
import com.muzz.datingappchat.main.model.ChatUIState
import com.muzz.datingappchat.main.model.Contact
import com.muzz.datingappchat.shared.entities.Message
import com.muzz.datingappchat.shared.mock.mockItems
import com.muzz.datingappchat.ui.theme.MuzzDatingAppChatTheme

@Composable
fun MessageBubble(
    message: Message,
    previousMessage: Message?,
    nextMessage: Message?,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val (container, content) = if (message.isMine) {
        colors.primary to colors.onPrimary
    } else {
        colors.surfaceVariant to colors.onSurface
    }

    val separation by remember(message, nextMessage) {
        derivedStateOf { MessageSeparation.messageSeparation(message, nextMessage).dp }
    }

    Column(modifier) {
        if (MessageSeparation.showsSectionHeader(message, previousMessage)) {
            SectionHeader(message.messageSent)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = if (message.isMine) 64.dp else 0.dp,
                    end = if (message.isMine) 0.dp else 64.dp,
                    bottom = separation
                ),
            contentAlignment = if (message.isMine) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
            Text(
                text = message.message,
                modifier = Modifier
                    .background(container, RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                color = content,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ChatScreenPreview() {
    MuzzDatingAppChatTheme {
        ChatScreen(
            contactName = Contact.Muzz.name,
            state = ChatUIState.Messages,
            messages = mockItems,
            onSend = {},
            onToggleSender = {},
        )
    }
}
