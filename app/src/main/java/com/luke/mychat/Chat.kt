package com.luke.mychat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class Chat : AppCompatActivity() {

    val mSocketManager= SocketManager()
    var username: String
    var roomNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }

    init {


        val intent = intent
        username = intent.getStringExtra("username").toString()
        roomNumber = intent.getStringExtra("roomNumber").toString()

        mSocketManager.joinLectureRoom()
    }
}