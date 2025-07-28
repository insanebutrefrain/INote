package macom.inote.intenet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.137.1:8080/inote/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val noteApi: NoteApi by lazy {
        retrofit.create(NoteApi::class.java)
    }
    val taskApi: TaskApi by lazy {
        retrofit.create(TaskApi::class.java)
    }
    val todoApi: TodoApi by lazy {
        retrofit.create(TodoApi::class.java)
    }
    val taskListApi: TaskListApi by lazy {
        retrofit.create(TaskListApi::class.java)
    }
}
