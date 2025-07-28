package macom.inote.intenet

import macom.inote.data.Todo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApi {

    @GET("todo/getAll")
    fun getAllTodos(): Call<List<Todo>>

    @POST("todo/add")
    fun createTodo(@Body todo: Todo): Call<Todo>

    @PUT("todo/update/{id}")
    fun updateTodo(@Path("id") id: Long, @Body todo: Todo): Call<Todo>

    @DELETE("todo/delete/{id}")
    fun deleteTodo(@Path("id") id: Long): Call<Void>
}
