//package com.example.smartlinkconfig.screens
//
//import android.annotation.SuppressLint
//import android.app.Application
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.net.ConnectivityManager
//import android.net.NetworkCapabilities
//import android.net.wifi.WifiManager
//import android.util.Log
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.smartlinkconfig.data.ConfigurationUiState
//import com.example.smartlinkconfig.data.NetworkDevice
//import com.stealthcopter.networktools.SubnetDevices
//import com.stealthcopter.networktools.subnet.Device
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.async
//import kotlinx.coroutines.awaitAll
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import java.net.InetAddress
//
//@SuppressLint("MissingPermission")
//class ConfigurationViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val bluetoothManager =
//        application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
//    private val connectivityManager =
//        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    private val wifiManager =
//        application.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//    private val _uiState = MutableStateFlow(ConfigurationUiState())
//    val uiState = _uiState.asStateFlow()
//
//    fun startNetworkScan() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _uiState.update { it.copy(isScanningNetwork = true, networkDevices = emptyList()) }
//
//            SubnetDevices.fromLocalAddress().findDevices(object : SubnetDevices.OnSubnetDeviceFound {
//                override fun onDeviceFound(device: Device) {
//                    Log.d("NetworkScan", "Device found: ${device.ip}")
//                }
//
//                override fun onFinished(devices: ArrayList<Device>) {
//                    Log.d("NetworkScan", "Scan finished. Found ${devices.size} devices.")
//                    viewModelScope.launch(Dispatchers.IO) {
//                        val resolvedDevices = devices.map { device ->
//                            async {
//                                val ip = device.ip ?: "Unknown IP"
//                                val mac = device.mac ?: "Unknown MAC"
//                                val hostname = try {
//                                    InetAddress.getByName(ip).canonicalHostName
//                                } catch (e: Exception) {
//                                    ip
//                                }
//                                NetworkDevice(ipAddress = ip, macAddress = mac, hostname = hostname)
//                            }
//                        }.awaitAll()
//
//                        Log.d("NetworkScan", "Hostname resolution complete.")
//                        _uiState.update {
//                            it.copy(isScanningNetwork = false, networkDevices = resolvedDevices)
//                        }
//                    }
//                }
//            })
//        }
//    }
//
//    private val bluetoothStateReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            when (intent.action) {
//                BluetoothDevice.ACTION_FOUND -> {
//                    val device: BluetoothDevice? =
//                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//                    device?.let {
//                        if (it.name != null && !_uiState.value.scannedDevices.contains(it)) {
//                            _uiState.update { state ->
//                                state.copy(scannedDevices = state.scannedDevices + it)
//                            }
//                        }
//                    }
//                }
//                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
//                    _uiState.update { it.copy(isScanning = false) }
//                }
//            }
//        }
//    }
//
//    fun startScan() {
//        if (bluetoothAdapter == null) {
//            _uiState.update { it.copy(errorMessage = "Bluetooth is not supported.") }
//            return
//        }
//        if (!bluetoothAdapter.isEnabled) {
//            _uiState.update { it.copy(errorMessage = "Please enable Bluetooth.") }
//            return
//        }
//        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//        getApplication<Application>().registerReceiver(bluetoothStateReceiver, filter)
//        _uiState.update { it.copy(isScanning = true, scannedDevices = emptyList()) }
//        bluetoothAdapter.startDiscovery()
//    }
//
//    private fun getCurrentWifiSsid() {
//        val network = connectivityManager.activeNetwork
//        val capabilities = connectivityManager.getNetworkCapabilities(network)
//        if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
//            val wifiInfo = wifiManager.connectionInfo
//            val ssid = wifiInfo.ssid.removeSurrounding("\"")
//            _uiState.update { it.copy(ssid = ssid) }
//        } else {
//            _uiState.update { it.copy(ssid = "No WiFi Connected", useCurrentWifi = false) }
//        }
//    }
//
//    fun onUseCurrentWifiToggled(enabled: Boolean) {
//        _uiState.update { it.copy(useCurrentWifi = enabled) }
//        if (enabled) {
//            getCurrentWifiSsid()
//        } else {
//            _uiState.update { it.copy(ssid = "") }
//        }
//    }
//
//    fun onDeviceSelected(device: BluetoothDevice) {
//        _uiState.update { it.copy(selectedDevice = device) }
//    }
//
//    fun onSsidChanged(ssid: String) {
//        _uiState.update { it.copy(ssid = ssid) }
//    }
//
//    fun onPasswordChanged(password: String) {
//        _uiState.update { it.copy(password = password) }
//    }
//
//    fun onIpAddressChanged(ip: String) {
//        _uiState.update { it.copy(ipAddress = ip) }
//    }
//
//    fun sendConfiguration() {
//        _uiState.update { it.copy(showConfirmationDialog = true) }
//    }
//
//    fun dismissDialog() {
//        _uiState.update { it.copy(showConfirmationDialog = false) }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        try {
//            getApplication<Application>().unregisterReceiver(bluetoothStateReceiver)
//        } catch (e: IllegalArgumentException) {
//            Log.w("ViewModel", "Bluetooth receiver was not registered or already unregistered.")
//        }
//    }
//}



package com.example.smartlinkconfig.screens

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlinkconfig.data.ConfigurationUiState
import com.example.smartlinkconfig.data.NetworkDevice
import com.stealthcopter.networktools.SubnetDevices
import com.stealthcopter.networktools.subnet.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.InetAddress
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
class ConfigurationViewModel(application: Application) : AndroidViewModel(application) {

    private val bluetoothManager =
        application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val wifiManager =
        application.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val _uiState = MutableStateFlow(ConfigurationUiState())
    val uiState = _uiState.asStateFlow()

    fun startNetworkScan() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isScanningNetwork = true, networkDevices = emptyList()) }
            try {
                val devicesFound = findSubnetDevices()
                Log.d("NetworkScan", "Scan finished. Found ${devicesFound.size} devices. Resolving hostnames...")

                val resolvedDevices = devicesFound.map { device ->
                    async {
                        val ip = device.ip ?: "Unknown IP"
                        val mac = device.mac ?: "Unknown MAC"
                        val hostname = try {
                            InetAddress.getByName(ip).canonicalHostName
                        } catch (e: Exception) {
                            ip
                        }
                        NetworkDevice(ipAddress = ip, macAddress = mac, hostname = hostname)
                    }
                }.awaitAll()

                Log.d("NetworkScan", "Hostname resolution complete.")
                _uiState.update {
                    it.copy(isScanningNetwork = false, networkDevices = resolvedDevices)
                }
            } catch (e: Exception) {
                Log.e("NetworkScan", "Network scan failed", e)
                _uiState.update { it.copy(isScanningNetwork = false) }
            }
        }
    }


    private suspend fun findSubnetDevices(): ArrayList<Device> = suspendCancellableCoroutine { continuation ->
        SubnetDevices.fromLocalAddress().findDevices(object : SubnetDevices.OnSubnetDeviceFound {
            override fun onDeviceFound(device: Device) {
                // We can update the UI incrementally here if we want a live list
            }
            override fun onFinished(devices: ArrayList<Device>) {
                if (continuation.isActive) {
                    continuation.resume(devices)
                }
            }
        })
    }

    private val bluetoothStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        if (it.name != null && !_uiState.value.scannedDevices.contains(it)) {
                            _uiState.update { state ->
                                state.copy(scannedDevices = state.scannedDevices + it)
                            }
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    _uiState.update { it.copy(isScanning = false) }
                }
            }
        }
    }

    fun startScan() {
        if (bluetoothAdapter == null) {
            _uiState.update { it.copy(errorMessage = "Bluetooth is not supported.") }
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            _uiState.update { it.copy(errorMessage = "Please enable Bluetooth.") }
            return
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        getApplication<Application>().registerReceiver(bluetoothStateReceiver, filter)
        _uiState.update { it.copy(isScanning = true, scannedDevices = emptyList()) }
        bluetoothAdapter.startDiscovery()
    }

    private fun getCurrentWifiSsid() {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
            val wifiInfo = wifiManager.connectionInfo
            val ssid = wifiInfo.ssid.removeSurrounding("\"")
            _uiState.update { it.copy(ssid = ssid) }
        } else {
            _uiState.update { it.copy(ssid = "No WiFi Connected", useCurrentWifi = false) }
        }
    }

    fun onUseCurrentWifiToggled(enabled: Boolean) {
        _uiState.update { it.copy(useCurrentWifi = enabled) }
        if (enabled) {
            getCurrentWifiSsid()
        } else {
            _uiState.update { it.copy(ssid = "") }
        }
    }

    fun clearConfigurationInputs() {
        _uiState.update { currentState ->
            currentState.copy(
                ssid = "",
                password = "",
                ipAddress = "",
                selectedDevice = null
            )
        }
    }

    fun onDeviceSelected(device: BluetoothDevice) {
        _uiState.update { it.copy(selectedDevice = device) }
    }

    fun onSsidChanged(ssid: String) {
        _uiState.update { it.copy(ssid = ssid) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onIpAddressChanged(ip: String) {
        _uiState.update { it.copy(ipAddress = ip) }
    }

    fun sendConfiguration() {
        _uiState.update { it.copy(showConfirmationDialog = true) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showConfirmationDialog = false) }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            getApplication<Application>().unregisterReceiver(bluetoothStateReceiver)
        } catch (e: IllegalArgumentException) {
            Log.w("ViewModel", "Bluetooth receiver was not registered or already unregistered.")
        }
    }
}