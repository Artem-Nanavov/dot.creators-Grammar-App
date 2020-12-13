package com.example.grammarapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ThirdActivity : AppCompatActivity() {
    private  var grammarTest : TextView? = null
    private  var answerVerbs : TextView? = null
    private var grammarApi: GrammarApi? = null
    private var getAnswer : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.third_activity)

        grammarTest = findViewById(R.id.testString)
        answerVerbs = findViewById(R.id.answerString)
        getAnswer = findViewById(R.id.getAnswer)
        val gson = GsonBuilder().serializeNulls().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        grammarApi = retrofit.create(GrammarApi::class.java)
        getQuestions()

        getAnswer!!.setOnClickListener {
            getAnswers()
        }
    }

    private fun getQuestions() {
        val call = grammarApi!!.getTest()
        call.enqueue(object : Callback<List<Test>> {
            override fun onResponse(call: Call<List<Test>>, response: Response<List<Test>>) {
                if (!response.isSuccessful) {
                    grammarTest!!.text = "Code: " + response.code()
                    return
                }
                val texts = response.body()!!
                var count = 1
                var downMark : Int
                for (text in texts) {
                    var questions = ""
                    downMark = text.readyVerb!!.length
                    questions += "${count++}): ${text.perhaps!!.replace(text.readyVerb!!, 
                            "${"_".repeat(downMark)}" + "(${text.rawVerb!!})")}"
                    downMark = 0
                    grammarTest!!.append(questions + "\n")
                }
            }

            override fun onFailure(call: Call<List<Test>>, t: Throwable) {
                grammarTest!!.text = t.toString()
            }
        })
    }
    private fun getAnswers() {
        val call = grammarApi!!.getTest()
        call.enqueue(object : Callback<List<Test>> {
            override fun onResponse(call: Call<List<Test>>, response: Response<List<Test>>) {
                if (!response.isSuccessful) {
                    //grammarTest!!.text = "Code: " + response.code()
                    return
                }
                val texts = response.body()!!
                var count = 1
                for (text in texts) {
                    var answers = ""
                    answers += "${count++}): ${text.readyVerb!!}"
                    answerVerbs!!.append(answers + "\n")
                }
            }

            override fun onFailure(call: Call<List<Test>>, t: Throwable) {
                //grammarTest!!.text = t.toString()
            }
        })
    }
}