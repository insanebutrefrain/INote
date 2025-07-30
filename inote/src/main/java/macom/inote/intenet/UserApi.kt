package macom.inote.intenet

import macom.inote.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @POST(value = "")
    suspend fun addUser(): Call<User>

    @DELETE(value = "/{id}")
    suspend fun deleteUser(@Path("id") id: String): Call<Void>

    @PUT(value = "/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Call<User>

    @GET(value = "/{id}")
    suspend fun findUser(@Path("id") id: String): Call<User>

    @GET(value = "/{id}/{psw}")
    suspend fun findUser(@Path("id") id: String, @Path("psw") psw: String): Call<User>
}