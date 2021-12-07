package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var adviceTextView : TextView
    private  lateinit var adviceButton : Button

    var getAdvice = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adviceTextView = findViewById(R.id.adviceTV)
        adviceButton = findViewById(R.id.advicebutton)
        adviceButton.setOnClickListener {

            requestApi()

        }

    }

    private fun requestApi()
    {

        CoroutineScope(Dispatchers.IO).launch {

            val data = async {

                fetchRandomAdvice()

            }.await()

            if (data.isNotEmpty())
            {

                updateAdviceText(data)
            }

        }

    }

    private fun fetchRandomAdvice():String{

        var response=""
        try {
            response =URL(getAdvice).readText(Charsets.UTF_8)

        }catch (e:Exception)
        {
            println("Error $e")

        }
        return response

    }

    private suspend fun updateAdviceText(data:String)
    {
        withContext(Dispatchers.Main)
        {

            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")

            adviceTextView.text = advice

        }

    }



}