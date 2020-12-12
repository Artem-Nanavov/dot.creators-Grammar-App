package com.example.grammarapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.koushikdutta.ion.Ion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var urlResult: TextView? = null
    private var postButton : Button? = null
    private var editTextUrl : EditText? = null
    private var grammarApi: GrammarApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        urlResult = findViewById(R.id.textView)
        postButton =findViewById(R.id.postButton)


        postButton!!.setOnClickListener{
            postHtml(editTextUrl!!.text.toString())
        }

        val gson = GsonBuilder().serializeNulls().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://127.0.0.1:5000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        grammarApi = retrofit.create(GrammarApi::class.java)
    }
    fun postHtml(url : String){
        var temp : String? = null
        Ion.with(applicationContext).load(url).asString()
                .setCallback { e, result ->
                    temp = result
                }
        val htmlContainer = PostHTML(temp!!)
        val call : Call<PostHTML> = grammarApi!!.createHtml(htmlContainer)

        call!!.enqueue(object : Callback<PostHTML> {
            override fun onFailure(call: Call<PostHTML>, t: Throwable) {
                urlResult!!.text = t.message
            }

            override fun onResponse(call: Call<PostHTML>, response: Response<PostHTML>) {
                if(!response.isSuccessful){
                    urlResult!!.text = "Code: " + response.code()
                    return
                }
            }

        })
    }
}