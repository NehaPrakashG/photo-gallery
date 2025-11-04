package com.example.photo_gallery.data.model

import com.google.gson.annotations.SerializedName

data class FlickrResponse(
    val photos: FlickrPhotos? = null,
    val stat: String = "fail"
)

data class FlickrPhotos(
    val page: Int = 1,
    val pages: Int = 1,

    @SerializedName("perpage")
    val perPage: Int = 0,

    val total: Int = 0,
    val photo: List<FlickrPhoto> = emptyList()
)

data class FlickrPhoto(
    val id: String = "",
    val owner: String = "",
    val secret: String = "",
    val server: String = "",
    val farm: Int = 0,
    val title: String = "",

    @SerializedName("ispublic")
    val isPublic: Int = 0,

    @SerializedName("isfriend")
    val isFriend: Int = 0,

    @SerializedName("isfamily")
    val isFamily: Int = 0
)