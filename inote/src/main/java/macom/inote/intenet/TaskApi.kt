package macom.inote.intenet

import macom.inote.data.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {

    @POST("task/add")
    fun createTask(@Body task: Task): Call<Task>

    @GET("task/getAll")
    fun getAllTasks(): Call<List<Task>>

    @PUT("task/update/{id}")
    fun updateTask(@Path("id") id: Long, @Body task: Task): Call<Task>

    @DELETE("task/delete/{id}")
    fun deleteTask(@Path("id") id: Long): Call<Void>
}
