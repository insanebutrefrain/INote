package macom.inote.intenet

import macom.inote.data.Note
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {

    @GET("note/getAll")
    fun getAllNotes(): Call<List<Note>>

    @POST("note/add")
    fun createNote(@Body note: Note): Call<Note>

    @PUT("note/update/{id}")
    fun updateNote(@Path("id") id: Long, @Body note: Note): Call<Note>

    @DELETE("note/delete/{id}")
    fun deleteNote(@Path("id") id: Long): Call<Void>
}
