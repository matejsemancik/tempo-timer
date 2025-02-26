package dev.matsem.bpm.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.matsem.bpm.data.database.model.User
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UserDao {

    @Upsert
    suspend fun upsert(user: User)

    @Query("DELETE FROM user")
    suspend fun delete()

    @Query("SELECT * FROM user LIMIT 1")
    fun observe(): Flow<User?>

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun get(): User?
}