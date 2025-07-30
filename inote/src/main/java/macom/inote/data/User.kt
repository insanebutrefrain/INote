package macom.inote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var id: String,
    var password: String,
    var name: String,
    val registerTime: Long,
    var signs: String? = null
)