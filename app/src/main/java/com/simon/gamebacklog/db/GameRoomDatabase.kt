package com.simon.gamebacklog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simon.gamebacklog.model.Game

@Database(entities=[Game::class], version=1, exportSchema=false)
@TypeConverters(Converters::class)
abstract class GameRoomDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object{
        private const val DATABASE_NAME = "GAME_DATABASE"

        @Volatile
        private var instance: GameRoomDatabase? = null

        fun getDatabase(context: Context): GameRoomDatabase? {
            if (instance == null) {
                synchronized(GameRoomDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            GameRoomDatabase::class.java, DATABASE_NAME
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    }
                }
            }
            return instance
        }
    }
}