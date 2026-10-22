package com.muzz.datingappchat.main.view

import android.text.format.DateFormat
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.muzz.datingappchat.main.model.ChatUIState
import com.muzz.datingappchat.main.model.Contact
import com.muzz.datingappchat.shared.mock.mockItems
import com.muzz.datingappchat.ui.theme.MuzzDatingAppChatTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun SectionHeader(time: LocalDateTime, modifier: Modifier = Modifier) {
    val (day, clock) = remember(time) { formatSection(time) }

    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(day) }
            append(" $clock")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelSmall,
    )
}

internal fun formatSection(
    time: LocalDateTime,
    locale: Locale = Locale.getDefault(),
): Pair<String, String> {
    return time.format(DateTimeFormatter.ofPattern("EEEE", locale)) to
        time.format(DateTimeFormatter.ofPattern("HH:mm", locale))
}

@PreviewLightDark
@Composable
private fun ChatScreenPreview() {
    MuzzDatingAppChatTheme {
        ChatScreen(
            contactName = Contact.Muzz.name,
            state = ChatUIState.Messages,
            messages = mockItems,
            sendingAsContact = false,
            onSend = {},
            onToggleSender = {},
        )
    }
}

