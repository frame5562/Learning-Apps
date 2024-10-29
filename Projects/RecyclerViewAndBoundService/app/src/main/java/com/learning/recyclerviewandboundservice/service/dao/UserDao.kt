package com.learning.recyclerviewandboundservice.service.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.learning.recyclerviewandboundservice.dto.UserDto

class UserDao(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    //DB선언부
    lateinit var db: SQLiteDatabase

    private val TABLE_NAME = "User"
    private val USER_ID = "_id"
    private val USER_NAME = "name"
    private val USER_AGE = "age"
    private val USER_GENDER = "gender"


    // 테이블 생성
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE if not exists $TABLE_NAME (
                    $USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $USER_NAME VARCHAR(50),
                    $USER_AGE INTEGER, 
                    $USER_GENDER VARCHAR(4)
                )
                """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) { //테이블 삭제 후 생성
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        this.db = db
    }

    // 물품 등록
    fun insert(userDto: UserDto): Long {
        Log.d("TAG", "insert: ${userDto.name}")
        val args = ContentValues()
        args.put(USER_NAME, userDto.name)
        args.put(USER_AGE, userDto.age)
        args.put(USER_GENDER, userDto.gender)
        return db.insert(TABLE_NAME, null, args)
    }

    // 특정 물품 조회 method
    fun select(stuffId: Int): UserDto {
        db.query(
            TABLE_NAME,
            arrayOf(USER_ID, USER_NAME, USER_AGE, USER_GENDER),
            "$USER_ID = ?",
            arrayOf(stuffId.toString()), null, null, null
        ).use {
            return if (it.moveToFirst())
                UserDto(it.getInt(0), it.getString(1), it.getInt(2), it.getString(3))
            else
                UserDto()
        }
    }

    // 물품 조회 method
    fun selectAll(): MutableList<UserDto> {
        val list: ArrayList<UserDto> = arrayListOf()
        db.rawQuery("SELECT $USER_ID, $USER_NAME, $USER_AGE, $USER_GENDER FROM $TABLE_NAME", null)
            .use {
                if (it.moveToFirst()) {
                    do {
                        list.add(
                            UserDto(
                                it.getInt(0),
                                it.getString(1),
                                it.getInt(2),
                                it.getString(3)
                            )
                        )
                    } while (it.moveToNext())
                }
            }
        return list
    }

    // 물품정보 변경
    fun update(userDto: UserDto): Int {
        val args = ContentValues()
        args.put(USER_NAME, userDto.name)
        args.put(USER_AGE, userDto.age)
        args.put(USER_GENDER, userDto.gender)
        return db.update(TABLE_NAME, args, "$USER_ID = ?", arrayOf(userDto.id.toString()))
    }

    // 물품 삭제 method
    fun delete(stuffId: Int): Int {
        return db.delete(TABLE_NAME, "$USER_ID = ?", arrayOf(stuffId.toString()))
    }

}