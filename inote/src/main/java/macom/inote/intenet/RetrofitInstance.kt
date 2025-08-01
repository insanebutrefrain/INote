package macom.inote.intenet

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.137.1:8080/iNote/"

    // 创建 OkHttpClient 并设置超时时间
    val client = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)  // 连接超时时间
        .readTimeout(3, TimeUnit.SECONDS)     // 读取超时时间
        .writeTimeout(3, TimeUnit.SECONDS)    // 写入超时时间
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder().client(client)
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
    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}
