package com.muzz.datingappchat.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.muzz.datingappchat.R
import com.muzz.datingappchat.main.model.ChatUIState
import com.muzz.datingappchat.shared.entities.Message
import com.muzz.datingappchat.shared.mock.mockItems
import com.muzz.datingappchat.ui.theme.MuzzDatingAppChatTheme

/**
 * ChatScreen is stateless to make sure the parent handles all state.
 * Making it easier to preview when testing the UI
 */
@Composable
fun ChatScreen(
    contactName: String,
    messages: List<Message>,
    state: ChatUIState,
    onSend: (String) -> Unit,
    onToggleSender: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { ChatTopBar(contactName, onToggleSender) },
        bottomBar = {
            ChatInput(
                placeholder = "Type message as $contactName...",
                onSend = onSend,
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
            )
        },
    ) { padding ->
        when (val state = state) {
            is ChatUIState.Messages -> {
                MessageList(messages, Modifier.padding(padding))
            }

            ChatUIState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ChatUIState.Error -> {
                Text(state.error)
            }
        }
    }
}

@Composable
fun MessageList(items: List<Message>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    val focusManager = LocalFocusManager.current
    val isDragged by listState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(isDragged) {
        if (isDragged) focusManager.clearFocus()
    }

    val newestKey = remember(items) {
        derivedStateOf { items.firstOrNull()?.key }
    }

    /** When a new item gets added, the scroll is scrolled to the bottom**/
    LaunchedEffect(newestKey) {
        listState.animateScrollToItem(0)
    }

    /**
     * LazyColumn normally goes from top to bottom but with reverseLayout it goes from bottom to top.
     * The messages are fetched in descending order, so most recent are first and oldest are last
     */
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {

        items(items.indices.toList(), key = { items[it].key }) { index ->
            /**
             * We get the previous and next message to understand the gap between messages and know when to show a section header
             */
            MessageBubble(
                message = items[index],
                previousMessage = items.getOrNull(index + 1),
                nextMessage = items.getOrNull(index - 1),
                modifier = Modifier.animateItem(),
            )
        }
    }
}

@Composable
fun ChatTopBar(
    contactName: String,
    onToggleSender: () -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme

    Column(Modifier.statusBarsPadding()) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .padding(start = 16.dp, end = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

                Row(
                    modifier = Modifier.weight(1f).clickable { onToggleSender() },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Avatar(contactName)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = contactName,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
            Spacer(Modifier.weight(1f))
            Box {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "more options",
                        modifier = Modifier.rotate(90f),
                        tint = colors.onSurfaceVariant,
                    )
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(
                        text = {
                            Text("Change User")
                        },
                        onClick = {
                            menuOpen = false
                            onToggleSender()
                        },
                    )
                }
            }
        }
        HorizontalDivider(color = colors.outlineVariant)
    }
}

@Composable
private fun Avatar(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(32.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name.take(1),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

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
