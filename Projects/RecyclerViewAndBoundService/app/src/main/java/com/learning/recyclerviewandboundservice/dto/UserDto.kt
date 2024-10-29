package com.learning.recyclerviewandboundservice.dto

import java.io.Serializable

data class UserDto (
    var name: String = ""
    , var age: Int = 0
    , var gender: String = ""
    ) : Serializable {
        var id = -1

        constructor(
            _id: Int
            , name: String
            , age: Int
            , gender: String) : this(name, age, gender) {
            this.id = _id
        }

        override fun toString(): String {
            return "이름 ${name}, 나이 ${age}, 성별 ${gender}"
        }
    }