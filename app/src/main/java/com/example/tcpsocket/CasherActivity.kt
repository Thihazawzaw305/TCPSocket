package com.example.tcpsocket

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.tcpsocket.databinding.ActivityCasherBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket


class CasherActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCasherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCasherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        startServer()
    }

    private fun startServer() {
        GlobalScope.launch(Dispatchers.IO) {
            val serverSocket = ServerSocket(12345)
            while (true) {
                val socket = serverSocket.accept()
                handleClient(socket)
            }
        }
    }

    private suspend fun handleClient(socket: Socket) {
        withContext(Dispatchers.IO) {
            try {
                val inputReader = BufferedReader(InputStreamReader(socket.getInputStream()))
                val receivedData = inputReader.readLine()

                runOnUiThread {
                    val receivedText = "Received message: $receivedData"
                    binding.tvReceiveData.text = receivedText
                }

                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    }




