package com.muzz.datingappchat.shared.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.muzz.datingappchat.main.model.Contact
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Message(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var senderId: String,
    val message: String,
    val messageSent: LocalDateTime = LocalDateTime.now()
) {
    var isMine: Boolean
        get() = senderId == Contact.Connor.id
        set(value) {
            senderId = if (value) {
                Contact.Connor.id
            } else {
                Contact.Muzz.id
            }
        }

    val key: String
        get() = "message:$id"
}