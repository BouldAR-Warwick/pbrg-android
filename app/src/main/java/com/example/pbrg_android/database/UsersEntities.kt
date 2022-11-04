package com.example.pbrg_android.database

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.ktorm.entity.Entity



object UsersTable: Table<UsersEntity>("Users") {
    val uid = int("uid").primaryKey().bindTo { it.uid }
    val username = varchar("username").bindTo { it.username }
    val email = varchar("email").bindTo { it.email }
    val password = varchar("password").bindTo { it.password }
}

interface UsersEntity: Entity<UsersEntity> {

    companion object : Entity.Factory<UsersEntity>()

    val uid: Int
    val username: String
    val email: String
    val password: String

}