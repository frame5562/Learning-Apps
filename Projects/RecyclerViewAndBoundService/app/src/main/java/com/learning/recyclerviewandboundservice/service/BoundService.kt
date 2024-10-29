package com.learning.recyclerviewandboundservice.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.learning.recyclerviewandboundservice.dto.UserDto
import com.learning.recyclerviewandboundservice.service.dao.UserDao

class BoundService : Service() {

    private val myBinder = MyBinder()
    private lateinit var userDAO: UserDao

    override fun onCreate() {
        super.onCreate()
        userDAO = UserDao(this, "user_info.db", null, 1)
        userDAO.writableDatabase
    }

    inner class MyBinder : Binder() {
        // Service 객체를 return
        fun getService(): BoundService = this@BoundService
    }

    // 물품 추가
    fun insert(userDto: UserDto): Long {
        return userDAO.insert(userDto)
    }

    fun select(stuffId: Int): UserDto {
        return userDAO.select(stuffId)
    }

    fun selectAll(): MutableList<UserDto> {
        return userDAO.selectAll()
    }

    fun update(userDto: UserDto): Int {
        return userDAO.update(userDto)
    }

    fun delete(userId: Int): Int {
        return userDAO.delete(userId)
    }

    // Bound Service에서 서비스 연결시 호출
    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        userDAO.close()
        super.onDestroy()
    }
}