package macom.inote.intenet

import macom.inote.data.TaskList
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskListApi {

    @POST("taskList/add")
    suspend  fun createTaskList(@Body taskList: TaskList): TaskList

    @PUT("taskList/update")
    suspend fun updateTaskList(@Body taskList: TaskList):TaskList

    @DELETE("taskList/delete")
    suspend fun deleteTaskList(@Body taskList: TaskList): Void

    @GET("taskList/getAll/{user}")
    suspend fun getAllTaskLists(@Path("user") user: String): List<TaskList>
}
