package macom.inote.intenet

import macom.inote.data.Task
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {

    @POST("task/add")
    suspend fun createTask(@Body task: Task): Task

    @PUT("task/update")
    suspend fun updateTask(@Body task: Task): Task

    @DELETE("task/delete}")
    suspend fun deleteTask(@Body task: Task): Void

    @GET("task/getAll/{user}")
    suspend fun getAllTasks(@Path("user") user: String): List<Task>
}
