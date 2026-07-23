package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.WithdrawRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface WithdrawDao {
    @Query("SELECT * FROM withdraw_requests ORDER BY dateTimestamp DESC")
    fun getAllRequests(): Flow<List<WithdrawRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: WithdrawRequest)

    @Update
    suspend fun updateRequest(request: WithdrawRequest)

    @Query("SELECT * FROM withdraw_requests WHERE id = :id")
    suspend fun getRequestById(id: String): WithdrawRequest?
}
