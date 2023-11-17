package com.example.tcpsocket

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tcpsocket.databinding.ActivityWaiterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket

class WaiterActivity : AppCompatActivity() {
    var receivedData: String = ""
    private lateinit var binding: ActivityWaiterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaiterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        startServer()
        val items = createOrderItems()
        binding.btnSendData.setOnClickListener {
            GlobalScope.launch {
                    sendMessage(items.toString())

            }
        }


    }
    private fun startServer() {
        GlobalScope.launch(Dispatchers.IO) {
            val serverSocket = ServerSocket(54321)
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
                receivedData = inputReader.readLine()

                runOnUiThread {
                    val receivedText = "Received message: $receivedData"
                    binding.tvServerResponse.text = receivedText

                }

                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun receiveFromCasher(message: String) {
//        runOnUiThread {
//            val receivedText = "Received message from Casher: $message"
//            binding.tvServerResponse.text = receivedText
//        }
//    }

    private suspend fun sendMessage(message: String) {
        val serverAddress = "192.168.0.217" // Replace with your server IP address
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
    private fun createOrderItems(): JSONArray {
        val pizza = JSONObject()
        pizza.put("item", "Pizza")
        pizza.put("price", 12)

        val burger = JSONObject()
        burger.put("item", "Burger")
        burger.put("price", 45)

        val items = JSONArray()
        items.put(pizza)
        items.put(burger)

        return items
    }



}