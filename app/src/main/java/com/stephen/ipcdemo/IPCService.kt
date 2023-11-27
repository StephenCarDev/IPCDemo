package com.stephen.ipcdemo

import android.app.Service
import android.content.Intent
import com.stephen.serviceproxy.IDemoService

class IPCService : Service() {

    override fun onBind(intent: Intent) = iBinder

    override fun onCreate() {
        super.onCreate()
        infoLog("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

    private val iBinder = object : IDemoService.Stub() {
        override fun voidFunction() {
            infoLog("remote app call voidFunction")
        }

        override fun getANumber(): Int {
            infoLog("remote app call getANumber")
            return 1999;
        }
    }
}