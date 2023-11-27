package com.stephen.serviceproxy

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

@SuppressLint("StaticFieldLeak")
object MyProxy {

    private lateinit var context: Context

    private lateinit var listener: ServiceConnectedListener

    private lateinit var mService: IDemoService

    fun init(context: Context, listener: ServiceConnectedListener) {
        infoLog("init bind")
        this.context = context
        this.listener = listener
        bindService(context)
    }

    private fun bindService(context: Context) {
        infoLog("bindService")
        val result = context.bindService(
            Intent().setComponent(
                ComponentName(
                    "com.stephen.ipcdemo",
                    "com.stephen.ipcdemo.IPCService"
                )
            ), mServiceConnection, Context.BIND_AUTO_CREATE
        )
        infoLog("bindService result: $result")
    }

    fun voidFunction() {
        try {
            mService.voidFunction()
        } catch (e: Exception) {
            e.message?.let { errorLog(it) }
        }
    }

    fun getANumber(): Int {
        var value = 0
        try {
            value = mService.aNumber
        } catch (e: Exception) {
            e.message?.let { errorLog(it) }
        }
        return value
    }

    /**
     * 服务连接回调.
     */
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            infoLog("onServiceConnected componentName：" + componentName.packageName)
            mService = IDemoService.Stub.asInterface(iBinder)
            listener.onServiceConnected()
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            infoLog("onServiceDisconnected ")
            listener.onServiceDisconnected()
        }

        override fun onNullBinding(name: ComponentName) {
            infoLog("onNullBinding ")
        }

        override fun onBindingDied(name: ComponentName) {
            infoLog("onBindingDied ")
        }
    }
}

interface ServiceConnectedListener {
    fun onServiceConnected()
    fun onServiceDisconnected()
}