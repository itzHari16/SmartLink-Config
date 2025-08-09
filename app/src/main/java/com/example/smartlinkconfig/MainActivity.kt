package com.example.smartlinkconfig

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.smartlinkconfig.ui.theme.SmartLinkConfigTheme


class MainActivity : ComponentActivity() {
    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent  {
            SmartLinkConfigTheme {
                NavGraph()
            }
        }
    }
}
