package com.learning.bmi_calculator_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val heightEditText: EditText = findViewById(R.id.editHeight)
        val weightEditText: EditText = findViewById(R.id.editWeight)
        val resultButton: Button = findViewById<Button>(R.id.btnOk)

        resultButton.setOnClickListener {

            if(heightEditText.text.isEmpty() || weightEditText.text.isEmpty()){
                Toast.makeText(this, "모든 값을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val height: Int = heightEditText.text.toString().toInt()
            val weight: Int = weightEditText.text.toString().toInt()

            startActivity(Intent(this, ResultActivity::class.java).apply {
                putExtra("height", height)
                putExtra("weight", weight)
            })
        }


    }
}