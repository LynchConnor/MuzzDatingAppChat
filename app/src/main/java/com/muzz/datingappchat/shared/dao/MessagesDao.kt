package com.muzz.datingappchat.shared.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import com.muzz.datingappchat.shared.entities.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {
    @Insert
    suspend fun addMessage(message: Message)

    /**
     * Fetching messages as a Flow means the list updates automatically whenever the table changes, without fetching again.
     */
    @Query("SELECT * FROM Message ORDER BY messageSent")
    fun fetchMessages(): Flow<List<Message>>

    /**
     * Fetches the total number of messages in Room.
     */
    @Query("SELECT COUNT(*) FROM Message")
    suspend fun fetchMessagesCount(): Int

    @Insert
    suspend fun insertAll(messages: List<Message>)

}