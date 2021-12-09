package com.luke.mychat

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.Polling
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import javax.inject.Singleton


@Singleton
class SocketManager() {

    val nameSpace = "http://52.79.155.156:3001/chat"

    init {
        invoke(nameSpace)
    }

    companion object {
        @Volatile
        private var instance: Socket? = null

        private var LOCK = Any()

        operator fun invoke(nameSpace: String) = instance ?: synchronized(
            LOCK
        ) {
            instance ?: createInstance(nameSpace).also { instance = it }
        }

        private fun createInstance(nameSpace: String) : Socket {
            val opts = IO.Options()
            opts.transports = arrayOf(WebSocket.NAME, Polling.NAME)
            val socket = IO.socket(nameSpace, opts)
            socket.connect()
            return socket
        }
    }

    fun connectSocket() {
        instance?.connect()
    }

    fun closeSocket() {
        instance?.off()
        instance?.disconnect()
        instance?.close()
    }

    fun setSocket(authListener: Emitter.Listener, chatListener: Emitter.Listener) {
        instance?.apply {
            on("auth", authListener)
            on("chat", chatListener)
        }
    }

    /**
     * 소통방에 조인!
     * 1. 소통방 내에서 공지/QNA 작성시
     * 2. 목록에서 공지/QNA 작성시
     * 3. 탭에서 QNA/작성시
     *
     * 1. 이때는 disconnect ㄴㄴ
     * 2. 이때도 disconnect ㄴㄴ
     * 3. 이때는 connect및 join해줘야 함!
     *
     * */

    fun joinLectureRoom(jsonObject: JSONObject) {
        if (instance?.connected() != true) {
            instance?.on(Socket.EVENT_CONNECT, Emitter.Listener {
                instance?.emit("join", jsonObject)
            })
            connectSocket()
        } else {
            instance?.emit("join", jsonObject)
        }
    }

    fun leaveLectureRoom(jsonObject: JSONObject) {
        instance?.emit("leave", jsonObject)
    }

    fun sendChat(jsonObject: JSONObject) {
        instance?.emit("chat", jsonObject)
    }

    fun sendRead(jsonObject: JSONObject) {
        instance?.emit("read", jsonObject)
    }
//
//    fun sendNotice(userIdx : Int, subjectIdx: Int, notice : Notice){
//        val messageSocket = JSONObject()
//        val user = JSONObject()
//        user.put("userIdx", userIdx)
//        user.put("nickname", notice.title)
//        messageSocket.put("user", user)
//        messageSocket.put("subjectIdx", subjectIdx)
//        messageSocket.put("message", notice.content)
//        messageSocket.put("messageType", 3)
//        messageSocket.put("idx", notice.noticeIdx)
//        sendChat(messageSocket)
//    }
//
//    fun sendQna(userIdx : Int, subjectIdx: Int, qna : QnaData){
//        val messageSocket = JSONObject()
//        val user = JSONObject()
//        user.put("userIdx", userIdx)
//        user.put("nickname", qna.title)
//        messageSocket.put("user", user)
//        messageSocket.put("subjectIdx", subjectIdx)
//        messageSocket.put("message", qna.content)
//        messageSocket.put("messageType", 4)
//        messageSocket.put("idx", qna.qnaIdx)
//        sendChat(messageSocket)
//    }


}