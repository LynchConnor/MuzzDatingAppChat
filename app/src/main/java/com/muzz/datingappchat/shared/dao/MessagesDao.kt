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
    fun addMessage(message: Message)

    /**
     * FETCHING MESSAGES IN A FLOW ALLOWS THE USE OF .ASLIVEDATA AND COLLECTASSTATE IN THE VIEW. AUTOMATICALLY UPDATING WITHOUT USE OF FETCH
     */
    @Query("SELECT * FROM Message")
    fun fetchMessages(): Flow<List<Message>>

    /**
     * FETCHING THE TOTAL COUNT OF MESSAGE IN ROOM
     */
    @Query("SELECT COUNT(*) FROM Message")
    suspend fun fetchMessagesCount(): Int

    @Insert
    fun insertAll(messages: List<Message>)


}