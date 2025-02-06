package dev.matsem.bpm.data.database.dao

import androidx.room.*
import dev.matsem.bpm.data.model.database.User
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UserDao {

    @Upsert
    suspend fun upsert(user: User)

    @Query("DELETE FROM users")
    suspend fun delete()

    @Query("SELECT * FROM users LIMIT 1")
    fun observe(): Flow<User>
}