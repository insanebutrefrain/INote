package macom.inote.intenet

import macom.inote.data.Todo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface TodoApi {

    @POST("todo/add")
    fun createTodo(@Body todo: Todo): Call<Todo>

    @DELETE("todo/delete")
    fun deleteTodo(@Body todo: Todo): Call<Void>

    @PUT("todo/update")
    fun updateTodo(@Body todo: Todo): Call<Todo>

    @GET("todo/getAll/{user}")
    fun getAllTodos(user: String): Call<List<Todo>>
}
