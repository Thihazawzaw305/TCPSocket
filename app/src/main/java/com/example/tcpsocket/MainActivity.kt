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
        setUpListener()

    }


    private fun setUpListener() {

        binding.btnCasher.setOnClickListener {
            startActivity(Intent(this, CasherActivity::class.java))
        }

        binding.btnWaiter.setOnClickListener {
            startActivity(Intent(this, WaiterActivity::class.java))
        }

    }


}

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
//        reader.clos e()
//        writer.close()
//        connection.close()
//the
// thehd
