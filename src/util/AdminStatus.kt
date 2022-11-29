package com.example.util

sealed class AdminStatus{
    class Admin(data:Any? = null): AdminStatus()
    class User(data:Any? = null): AdminStatus()
}
