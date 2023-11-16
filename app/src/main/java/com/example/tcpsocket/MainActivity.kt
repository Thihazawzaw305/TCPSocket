package com.example.tcpsocket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tcpsocket.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.net.Socket
import java.util.Scanner

class MainActivity : AppCompatActivity() {
    private var active: Boolean = false
    private var data: String = ""
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        val adress = "192.168.100.28"
//        val port = 5000
//        binding.btnConnect.setOnClickListener {
//            if (binding.btnConnect.text == "Connect") {
//                binding.btnConnect.text = "Disconnect"
//                active = true
//                CoroutineScope(IO).launch {
//                    client(adress, port)
//                }
//            } else {
//                active = false
//                binding.btnConnect.text == "Connect"
//            }
//
//        }
//    }
//
//    private suspend fun client(adress: String, port: Int) {
//        val connection = Socket(adress, port)
//        val writer: OutputStream = connection.getOutputStream()
//        writer.write(1)
//        val reader = Scanner(connection.getInputStream())
//        while (active) {
//
//            var input = ""
//            input = reader.nextLine()
//            if (data.length < 300) {
//                data += "\n$input"
//            } else data = input
//            binding.appCompatTextView.text = data
//        }
//        reader.close()
//        writer.close()
//        connection.close()
//    }
        binding.btnConnect.setOnClickListener {
            GlobalScope.launch {
                sendMessage("Thiha ZAW ZAW")
            }
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this,CasherActivity::class.java))
        }
    }

    private suspend fun sendMessage(message: String) {
        val serverAddress = "192.168.100.28" // Replace with your server IP address
        val serverPort = 12345

        try {
            val socket = Socket(serverAddress, serverPort)
            val outputStream: OutputStream = socket.getOutputStream()
            outputStream.write(message.toByteArray())
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}