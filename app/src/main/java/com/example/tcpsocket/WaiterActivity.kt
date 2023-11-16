package com.example.tcpsocket

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tcpsocket.databinding.ActivityWaiterBinding
import java.io.OutputStream
import java.net.Socket

class WaiterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWaiterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaiterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

       binding.btnCallCasher.setOnClickListener{
            startActivity(Intent(this,CasherActivity::class.java))
        }

        binding.btnSendData.setOnClickListener {
            val dataToSend = "Table 1 needs assistance."
            SendDataToServerTask().execute(dataToSend)
        }




    }

    private inner class SendDataToServerTask : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg params: String?): Void? {
            val serverAddress = "192.168.100.28" // Replace with your server IP address
            val serverPort = 12345

            try {
                val socket = Socket(serverAddress, serverPort)
                val outputStream: OutputStream = socket.getOutputStream()
                val message = params[0]?.toByteArray()
                outputStream.write(message)
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }

}