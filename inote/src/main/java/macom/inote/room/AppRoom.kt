package macom.inote.room

import android.app.Application
import androidx.room.Room

class AppRoom : Application() {
    lateinit var database: AppDatabase
    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).fallbackToDestructiveMigration().build()
    }
}