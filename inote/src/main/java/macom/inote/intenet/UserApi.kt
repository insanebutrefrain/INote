package macom.inote.intenet

import macom.inote.data.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @POST(value = "user/add")
    suspend fun addUser(@Body user: User): User?

    @DELETE(value = "user/delete")
    suspend fun deleteUser(@Body user: User): Void

    @PUT(value = "user/update")
    suspend fun updateUser(@Body user: User): User?

    @GET(value = "user/get/{id}")
    suspend fun findUser(@Path("id") id: String): User?

    @GET(value = "user/get/{id}/{psw}")
    suspend fun findUser(@Path("id") id: String, @Path("psw") psw: String): User?

    @GET(value = "user/getAll")
    suspend fun getAllUser(): List<User>
}