package com.example.basiccoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val RESULT_1="Result #1"
    private val RESULT_2="Result #2"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button: Button =findViewById(R.id.btn_clickme)
        button.setOnClickListener{
            //IO ->network,
            //local db,Main -> main threads interacting ui,
            //Default->heavy works
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

     private fun setNewText(input:String){
         var text: TextView =findViewById(R.id.tv_text)
         val newText=text.text.toString()+"\n$input"
         text.text=newText
    }
    private suspend fun setTextOnMainThread(input: String){
        //switch context of the coroutines
        withContext(Main){
            setNewText(input)
        }
    }
    private suspend fun fakeApiRequest(){

        val result1 = getResult1FromApi()
        println("debug: $result1")
        setTextOnMainThread(result1)
        val result2=getResult2FromApi(result1)
        setTextOnMainThread(result2)
    }
// suspend -> fun async
    private suspend fun getResult1FromApi():String{
        logThread("getResultFromApi")
        delay(1000)
        return RESULT_1
    }
    private suspend fun getResult2FromApi(result1:String):String{
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName:String){
        println("debug: $methodName:  ${Thread.currentThread().name}")
    }
}