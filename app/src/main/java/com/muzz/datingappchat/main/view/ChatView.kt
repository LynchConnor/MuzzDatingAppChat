package com.muzz.datingappchat.main.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.muzz.datingappchat.main.viewModel.ChatViewModel
import com.muzz.datingappchat.main.model.Contact
import com.muzz.datingappchat.shared.database.MuzzDatingDatabase

@Composable
fun ChatView() {

    val context = LocalContext.current

    val messagesDao = MuzzDatingDatabase.getDatabase(context).messagesDao()
    val messages by messagesDao.fetchMessages().collectAsStateWithLifecycle(listOf())

    val viewModel = viewModel<ChatViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatViewModel(messagesDao) as T
            }
        }
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val contact by viewModel.contact.collectAsStateWithLifecycle()

    val sendingAsContact by remember(contact) {
        derivedStateOf { contact == Contact.Connor }
    }

    ChatScreen(
        contactName = contact.name,
        messages = messages,
        state = state,
        sendingAsContact = sendingAsContact,
        onSend = { viewModel.sendMessage(it) },
        onToggleSender = { viewModel.toggleContact() }
    )

}