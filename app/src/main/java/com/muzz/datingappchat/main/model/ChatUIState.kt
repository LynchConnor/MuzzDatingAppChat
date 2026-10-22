package com.muzz.datingappchat.main.model

sealed interface ChatUIState {
    object Loading: ChatUIState
    object Messages: ChatUIState

    data class Error(val error: String): ChatUIState
}