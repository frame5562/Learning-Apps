package com.learning.recyclerviewandboundservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import com.learning.recyclerviewandboundservice.databinding.ActivityEditBinding
import com.learning.recyclerviewandboundservice.databinding.ActivityMainBinding
import com.learning.recyclerviewandboundservice.dto.UserDto
import com.learning.recyclerviewandboundservice.service.BoundService

class EditActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEditBinding.inflate(layoutInflater) }

    var itemID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        boundService()

        itemID = intent.getIntExtra("itemID", -1) // 값이 없을 때 기본값으로 0 설정

        initEvent()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (isBound){
            unbindService(connection)
            isBound = false
        }
    }

    private fun initData() {
        // Intent 로 넘어온 데이터 index 로 상태를 판단
        val user = myService.select(itemID)

        if(itemID == -1){
            binding.btnDelete.visibility = View.GONE
        }else{
            // 데이터 검색
            val name = user.name
            val age = user.age.toString()
            val gender = user.gender

            // 데이터 셋팅
            binding.editName.setText(name)
            binding.editAge.setText(age)
            binding.editGender.setText(gender)
        }


    }

    private fun initEvent() {
        // 저장 버튼
        binding.btnSave.setOnClickListener {
            val name = binding.editName.text.toString()
            val age = binding.editAge.text.toString().toInt()
            val gender = binding.editGender.text.toString()

            if(name.length <= 0){
                Toast.makeText(this, "모든 값이 필요합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(itemID != -1){
                // Update
                myService.update(UserDto(itemID, name, age, gender))
                finish()
            }else{
                // Insert
                Toast.makeText(this, "저장", Toast.LENGTH_SHORT).show()
                myService.insert(UserDto(name, age, gender))
                finish()
            }
        }

        // 삭제 버튼
        binding.btnDelete.setOnClickListener {
            myService.delete(itemID)
            finish()
        }

        // 취소
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }


    /************ Bound Service ************/
    private lateinit var myService: BoundService
    private var isBound = false
    private fun boundService() {
        Intent(this, BoundService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BoundService.MyBinder
            myService = binder.getService()
            isBound = true



            initData()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }
}