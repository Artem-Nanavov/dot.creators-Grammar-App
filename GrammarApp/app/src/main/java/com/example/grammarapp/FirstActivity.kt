package com.example.grammarapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException

class FirstActivity : AppCompatActivity() {
    private var postButton : Button? = null
    private var editTextUrl : EditText? = null
    private var grammarApi: GrammarApi? = null
    private var responseIsTrue = false
    var errorText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity)

        val gson = GsonBuilder().serializeNulls().create()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.14:5000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()


        errorText = findViewById(R.id.errorText)
        postButton = findViewById(R.id.button_submit)
        editTextUrl = findViewById(R.id.editUrl)

        postButton!!.setOnClickListener{
                getTextFromWeb(editTextUrl!!.text.toString())
                if (responseIsTrue) {
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
                }
        }
        grammarApi = retrofit.create(GrammarApi::class.java)
    }

    private fun getTextFromWeb(url: String)  {
        Thread(Runnable {
            var exFlag = false
            val str = StringBuilder()
                try {
                    var temp = ""
                    val doc: Document = Jsoup.connect(url).get()
                    val title: String = doc.title()
                    val elements: Elements = doc.select("div")
                    str.append(title).append("\n")
                    for (element in elements) {
                        if (!temp.contains(element.text())) {
                            if (element.text().split(' ').size > 50) {
                                if (element.text() != "") {
                                    temp = element.text()
                                    str.append("\n").append(element.text())
                                }
                            }
                        }
                    }
                    val htmlContainer = PostText(str.toString())
                    val call: Call<PostText> = grammarApi!!.createHtml(htmlContainer)
                    call!!.enqueue(object : Callback<PostText> {
                        override fun onFailure(call: Call<PostText>, t: Throwable) {
                            if (exFlag)
                                errorText!!.text = "Failed to connect to server"
                            responseIsTrue = false
                        }

                        override fun onResponse(call: Call<PostText>, response: Response<PostText>) {
                            if (!response.isSuccessful) {
                                responseIsTrue = false
                            }
                            responseIsTrue = true
                        }

                    })
                } catch (e: IllegalArgumentException) {
                    responseIsTrue = false
                    exFlag = true
                    errorText!!.text = "Введите другой URL"
                }catch (ex : HttpStatusException){
                    responseIsTrue = false
                    exFlag = true
                    errorText!!.text = "Cdff"
                }
        }).start()
    }
}