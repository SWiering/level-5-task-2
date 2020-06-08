package com.simon.gamebacklog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.simon.gamebacklog.db.GameRepository
import com.simon.gamebacklog.model.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val mainScope = CoroutineScope(Dispatchers.IO)
    private val gameRepository = GameRepository(application.applicationContext)

    val games: LiveData<List<Game>> = gameRepository.getGames()

    fun insertGame(game: Game) {
        mainScope.launch {
            gameRepository.insertGame(game)
        }
    }

    fun deleteGame(game: Game) {
        mainScope.launch {
            gameRepository.deleteGame(game)
        }
    }

    fun deleteAllGames() {
        mainScope.launch {
            gameRepository.deleteAllGames()
        }
    }
}