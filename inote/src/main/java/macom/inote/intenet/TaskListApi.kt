package macom.inote.intenet

import macom.inote.data.TaskList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface TaskListApi {

    @GET("taskList/getAll/{user}")
    fun getAllTaskLists(user: String): Call<List<TaskList>>

    @POST("taskList/add")
    fun createTaskList(@Body taskList: TaskList): Call<TaskList>

    @PUT("taskList/update")
    fun updateTaskList(@Body taskList: TaskList): Call<TaskList>

    @DELETE("taskList/delete")
    fun deleteTaskList(@Body taskList: TaskList): Call<Void>
}
