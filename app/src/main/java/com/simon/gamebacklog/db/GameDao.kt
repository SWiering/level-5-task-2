package com.simon.gamebacklog.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.simon.gamebacklog.model.Game

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(game: Game)

    @Query("SELECT * FROM games ORDER BY release_date ASC")
    fun getGames(): LiveData<List<Game>>

    @Query("DELETE FROM games")
    suspend fun deleteAllGames()

    @Delete
    suspend fun deleteGame(game: Game)
}