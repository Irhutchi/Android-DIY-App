package ie.wit.doityourself.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DIYModel(var id: Long = 0,
                    var title: String = "",
                    var description: String = "") : Parcelable
