package com.example.tcpsocket

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.DisplayMetrics
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.emh.thermalprinter.EscPosPrinter
import com.emh.thermalprinter.connection.tcp.TcpConnection
import com.emh.thermalprinter.exceptions.EscPosConnectionException
import com.emh.thermalprinter.textparser.PrinterTextParserImg
import com.example.tcpsocket.databinding.ActivityCasherBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket


class CasherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCasherBinding
    var receivedData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCasherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        startServer()
        binding.btnResponse.setOnClickListener {
            sendToWaiter("I received the data")
        }

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
                 receivedData = inputReader.readLine()

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



    fun parseJsonString(jsonString: String): List<Pair<String, Double>> {
        val jsonArray = JSONArray(jsonString)
        val itemsList = mutableListOf<Pair<String, Double>>()

        for (i in 0 until jsonArray.length()) {
            val itemObject = jsonArray.getJSONObject(i)
            val itemName = itemObject.getString("item")
            val itemPrice = itemObject.getDouble("price")

            itemsList.add(Pair(itemName, itemPrice))
        }

        return itemsList

    }

    private fun sendToWaiter(message: String) {
        GlobalScope.launch {
            val waiterAddress = "192.168.0.213" // Replace with your waiter's IP address
            val waiterPort = 54321

            try {
                val socket = Socket(waiterAddress, waiterPort)
                val outputStream: OutputStream = socket.getOutputStream()
                outputStream.write(message.toByteArray())
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




private fun printOne(orderNumber: String, items: List<Pair<String, Double>>, customerInfo: String) {
    GlobalScope.launch {
        try {
            val printer = EscPosPrinter(TcpConnection("192.168.0.10", 9100), 203, 65f, 42)

            val itemsText = items.joinToString("\n") { "[L] ${it.first} [R] ${it.second}" }

            val printText = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(
                printer,
                applicationContext.resources.getDrawableForDensity(
                    R.drawable.ic_launcher_background,
                    DisplayMetrics.DENSITY_MEDIUM
                )
            ) + "</img>\n" +
                    "[L]\n" +
                    "[L] <u><font size='big'>ORDER N°$orderNumber</font></u>\n[L]\n" +
                    "[L] _________________________________________\n" +
                    "[L] Description [R] Amount\n[L]\n" +
                    itemsText +
                    "\n[L] _________________________________________\n" +
                    "[L] TOTAL [R] ${items.sumByDouble { it.second }} BD\n" +
                    "[L]\n" +
                    "[L] _________________________________________\n" +
                    "[L]\n" +
                    "[C]<font size='tall'>Customer Info</font>\n" +
                    customerInfo +
                    "\n[L]\n" +
                    "[L]\n[L]\n[L]\n"

            printer.printFormattedTextAndCut(printText)
            printer.disconnectPrinter()
        } catch (e: EscPosConnectionException) {
            e.printStackTrace()
        }
    }
}}


