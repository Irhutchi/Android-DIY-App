package ie.wit.doityourself.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DIYModel(var id: Long = 0,
                    var title: String = "",
                    var description: String = "",
                    var rating: String = "",
                    var image: Uri = Uri.EMPTY) : Parcelable
