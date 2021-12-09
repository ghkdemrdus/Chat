package com.luke.mychat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<TextView>(R.id.btn_enter)
        val name = findViewById<EditText>(R.id.et_name)
        btn.setOnClickListener {
            val intent = Intent(this, Chat::class.java)
            intent.putExtra("username", name.toString())
            intent.putExtra("roomNumber", 100)
            startActivity(intent)
        }
    }
}