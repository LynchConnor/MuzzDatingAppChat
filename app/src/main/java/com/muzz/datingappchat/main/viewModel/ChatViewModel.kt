package com.muzz.datingappchat.main.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muzz.datingappchat.main.model.ChatUIState
import com.muzz.datingappchat.main.model.Contact
import com.muzz.datingappchat.shared.dao.MessagesDao
import com.muzz.datingappchat.shared.entities.Message
import com.muzz.datingappchat.shared.mock.mockItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

/**
 * Everything is managed within the viewModel, as part of MVVM
 */
class ChatViewModel(
    private val dao: MessagesDao
): ViewModel() {

    private val TAG = "ChatViewModel"

    val messages = dao.fetchMessages().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _contact = MutableStateFlow(Contact.Connor)
    val contact = _contact.asStateFlow()

    private val _uiState = MutableStateFlow<ChatUIState>(ChatUIState.Loading)
    val uiState = _uiState.asStateFlow()


    /**
     * [seedMessages] is called in the viewModel to ensure that compositional changes don't re-fetch the messages. Moved from LaunchedEffect(Unit) to init
     */
    init {
        viewModelScope.launch(Dispatchers.Default) {
            seedMessages()
        }
    }


    /**
     * Check if the messages are empty in [seedMessages], then insert all of the mock messages we have
     * Because Room can still throw an exception, like if there isn't enough space on the device, the Error UIState is useful
     * to catch these issues and show the user feedback instead of loading.
     * [seedMessages] is made private as we only fetch it in the viewModel
     */
    private suspend fun seedMessages() {
        try {
            if (dao.fetchMessagesCount() == 0) {
                dao.insertAll(mockItems)
            }
            _uiState.value = ChatUIState.Messages
        } catch (e: Exception) {
            _uiState.value = ChatUIState.Error(e.localizedMessage ?: "")
        }
    }

    /**
     * Toggle the character, changing from Connor -> Muzz, Muzz -> Connor
     */
    fun toggleContact() {
        _contact.value = _contact.value.toggle()
    }

    /**
     * [sendMessage] adds a message to Room
     * A try catch is used similarly because if there's not enough space on the device, it will throw.
     * If I had more time I would instruct the user to remove storage to continue using the app as intended
     */
    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                dao.addMessage(
                    Message(
                        UUID.randomUUID().toString(),
                        contact.value.id,
                        message,
                        LocalDateTime.now()
                    )
                )
            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage ?: "")
            }
        }
    }
}