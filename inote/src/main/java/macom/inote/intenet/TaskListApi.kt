package macom.inote.intenet

import macom.inote.data.TaskList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskListApi {

    @GET("taskList/getAll")
    fun getAllTaskLists(): Call<List<TaskList>>

    @POST("taskList/add")
    fun createTaskList(@Body taskList: TaskList): Call<TaskList>

    @PUT("taskList/update/{id}")
    fun updateTaskList(@Path("id") id: Long, @Body taskList: TaskList): Call<TaskList>

    @DELETE("taskList/delete/{id}")
    fun deleteTaskList(@Path("id") id: Long): Call<Void>
}
