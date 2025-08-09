package com.example.smartlinkconfig.data

import android.bluetooth.BluetoothDevice

data class ConfigurationUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val selectedDevice: BluetoothDevice? = null,
    val ssid: String = "",
    val password: String = "",
    val ipAddress: String = "",
    val isScanning: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val errorMessage: String? = null,
    val useCurrentWifi: Boolean = false,
    val isScanningNetwork: Boolean = false,
    val networkDevices: List<NetworkDevice> = emptyList()
)
