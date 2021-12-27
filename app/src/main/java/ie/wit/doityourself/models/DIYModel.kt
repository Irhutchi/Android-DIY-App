package ie.wit.doityourself.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class DIYModel(
    var uid: String? = "",
    var title: String = "",
    var description: String = "",
    var rating: String = "",
    var profilepic: String = "",
    var email: String? = "")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "rating" to rating,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}
