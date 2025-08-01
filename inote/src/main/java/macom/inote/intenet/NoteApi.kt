package macom.inote.intenet

import macom.inote.data.Note
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {

    @POST("note/add")
    suspend fun createNote(@Body note: Note): Note

    @PUT("note/update")
    suspend fun updateNote(@Body note: Note): Note

    @DELETE("note/delete")
    suspend fun deleteNote(@Body note: Note): Void

    @GET("note/getAll/{user}")
    suspend fun getAllNotes(@Path("user") user: String): List<Note>
}
