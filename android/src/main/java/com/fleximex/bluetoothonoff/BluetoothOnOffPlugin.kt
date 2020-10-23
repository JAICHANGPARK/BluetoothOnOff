package com.fleximex.bluetoothonoff

import android.bluetooth.BluetoothAdapter
import android.os.Build
import androidx.annotation.NonNull
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.engine.plugins.FlutterPlugin

/** BluetoothOnOffPlugin  */
class BluetoothOnOffPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "BluetoothOnOff")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        var intResult = -1
        var callType = "on"

        if (call.method == "turnOnBluetooth") {
            callType = "on"
            intResult = turnOnBluetooth()
        } else if (call.method == "turnOffBluetooth") {
            callType = "off"
            intResult = turnOffBluetooth()
        }
        when (intResult) {
            0 -> result.error("FAILED", "Could not turn $callType Bluetooth.", intResult)
            1 -> result.success(intResult)
            2 -> result.error("ALERT", "Bluetooth was already $callType.", intResult)
            3 -> result.error("ERROR", "Android version lower than 4.4 (KitKat) is unsupported.", intResult)
            -1 -> result.notImplemented()
            else -> result.notImplemented()
        }
    }

    private fun turnOnBluetooth(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mBtAdapter = BluetoothAdapter.getDefaultAdapter()
            if (!mBtAdapter.isEnabled) {
                if (mBtAdapter.enable()) {
                    return 1
                }
            } else {
                return 2
            }
        } else {
            return 3
        }
        return 0
    }

    private fun turnOffBluetooth(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mBtAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBtAdapter.isEnabled) {
                if (mBtAdapter.disable()) {
                    return 1
                }
            } else {
                return 2
            }
        } else {
            return 3
        }
        return 0
    }


}