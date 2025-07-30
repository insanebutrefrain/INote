package macom.inote.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import macom.inote.data.Note
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.data.Todo
import macom.inote.data.User


@Database(
    entities = [Note::class, Todo::class, TaskList::class, Task::class, User::class],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun todoDao(): TodoDao
    abstract fun taskListDao(): TaskListDao
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "app_database"
                ).fallbackToDestructiveMigration().build() // 从第一个版本的迁移运行丢失数据
                INSTANCE = instance
                instance
            }
        }
    }
}
