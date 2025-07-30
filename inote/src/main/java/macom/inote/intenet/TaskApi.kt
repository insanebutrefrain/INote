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

    @GET("task/getAll/{user}")
    fun getAllTasks(@Path("user") user: String): Call<List<Task>>

    @PUT("task/update")
    fun updateTask(@Body task: Task): Call<Task>

    @DELETE("task/delete}")
    fun deleteTask(@Body task: Task): Call<Void>
}
