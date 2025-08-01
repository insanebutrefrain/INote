package macom.inote.intenet

import macom.inote.data.Todo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApi {

    @POST("todo/add")
    suspend fun createTodo(@Body todo: Todo): Todo

    @DELETE("todo/delete")
    suspend fun deleteTodo(@Body todo: Todo): Void

    @PUT("todo/update")
    suspend fun updateTodo(@Body todo: Todo): Todo

    @GET("todo/getAll/{user}")
    suspend fun getAllTodos(@Path("user") user: String): List<Todo>
}
