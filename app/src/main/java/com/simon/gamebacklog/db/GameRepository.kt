package com.simon.gamebacklog.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.simon.gamebacklog.model.Game

class GameRepository (context: Context) {

    private val gameDao: GameDao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    suspend fun insertGame(game: Game){
        return gameDao.insertGame(game)
    }

    fun getGames(): LiveData<List<Game>>{
        return gameDao.getGames()
    }

    suspend fun deleteGame(game: Game){
        return gameDao.deleteGame(game)
    }

    suspend fun deleteAllGames(){
        return gameDao.deleteAllGames()
    }
}