package com.learning.recyclerviewandboundservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.recyclerviewandboundservice.databinding.ActivityMainBinding
import com.learning.recyclerviewandboundservice.dto.UserDto
import com.learning.recyclerviewandboundservice.service.BoundService

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var customAdapter: CustomAdapter
    private lateinit var userList: MutableList<UserDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        boundService()
        initEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound){
            unbindService(connection)
            isBound = false
        }
    }



    private fun initEvent() {
        binding.btnLoadData.setOnClickListener {
            getServiceData()
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }
    }



    private fun getServiceData() {
        val list = myService.selectAll()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        customAdapter = CustomAdapter(this, list)
        customAdapter.setOnBoardClickListener(object : CustomAdapter.OnBoardClickListener {
            override fun onEditPage(id: Int) {
                startActivity(Intent(this@MainActivity, EditActivity::class.java).apply {
                    putExtra("itemID", id)
                })
            }

        })




        binding.recyclerView.adapter = customAdapter
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

            // 서비스가 연결된 후 어댑터 초기화
            // initAdapter()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }





}