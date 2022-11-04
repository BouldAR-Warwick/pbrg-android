package com.example.pbrg_android.repository

import com.example.pbrg_android.database.DatabaseManager
import com.example.pbrg_android.database.UsersTable
import com.example.pbrg_android.utility.LoginInfo
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.tencent.mmkv.MMKV
import org.ktorm.dsl.*
import java.lang.reflect.Type


import java.math.BigInteger
import java.security.MessageDigest



class LoginRepository {

    private val database = DatabaseManager().database

    fun userAuthorise(userName : String, password : String) {

        val query = database.from(UsersTable).select().where{ UsersTable.username eq userName }
        if (query.rowSet.next()) {
            if (md5(password) == query.rowSet[UsersTable.password]) {
                // information correct move to user page
                // cache the login info
                val kv = MMKV.defaultMMKV()
                //TODO: add keeplogin para
                //if keeplogin is chosen then expire in 7 days
                val expireTime = 7*86400*1000L + System.currentTimeMillis()
                val loginInfo = LoginInfo(query.rowSet[UsersTable.uid]!!)
                kv.encode("login_info", loginInfo.toMyJson())
            } else {
                // wrong password

            }
        } else {
            // user not exist

        }

    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun Any.toMyJson(): String? {
        return Gson().toJson(this)
    }

    inline fun <reified T> String.toMyObject(): List<T> {
        val listType: Type = `$Gson$Types`.newParameterizedTypeWithOwner(null, ArrayList::class.java, T::class.java)
        return if(!contains("[")){
            Gson().fromJson("[${this}]", listType)
        }else{
            Gson().fromJson(this, listType)
        }
    }
}


