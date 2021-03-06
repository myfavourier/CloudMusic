package com.example.cloud_music.model.user;

import com.example.cloud_music.model.KBaseModel


data class User(val ecode: Int, val emsg: String, val data: UserContent) : KBaseModel()

data class UserContent(val userId: String, val photoUrl: String, val name: String, val tick: String, val mobile: String, val platform: String) : KBaseModel()