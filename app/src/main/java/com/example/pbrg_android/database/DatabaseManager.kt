package com.example.pbrg_android.database

import org.ktorm.database.Database

class DatabaseManager {
     val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/ktorm", //postgresql data base url
        driver = "postgresql.Driver", //postgresql driver
        user = "postgres", //username
        password = "fas200" //password
    )

}