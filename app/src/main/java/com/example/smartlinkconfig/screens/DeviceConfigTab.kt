package com.example.smartlinkconfig.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartlinkconfig.ui.theme.ElectricBlue
import kotlinx.coroutines.delay
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun DeviceConfigTab(viewModel: ConfigurationViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showSuccess by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                RadarScanner(
                    isScanning = uiState.isScanning,
                    onClick = { viewModel.startScan() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (uiState.scannedDevices.isNotEmpty()) {
                items(uiState.scannedDevices) { device ->
                    val isSelected = uiState.selectedDevice == device
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { viewModel.onDeviceSelected(device) },
                        shape = RoundedCornerShape(12.dp),
                        border = if (isSelected) BorderStroke(2.dp, ElectricBlue) else null
                    ) {
                        Text(
                            text = device.name ?: "Unknown Device",
                            modifier = Modifier.padding(16.dp),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = uiState.ssid,
                    onValueChange = { viewModel.onSsidChanged(it) },
                    label = { Text("WiFi SSID") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text("WiFi Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.ipAddress,
                    onValueChange = { newText ->
                        if (newText.all { it.isDigit() } && newText.length <= 12) {
                            viewModel.onIpAddressChanged(newText)
                        }
                    },
                    label = { Text("IP Address for Device") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = IpAddressVisualTransformation()
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        viewModel.sendConfiguration()
                        showSuccess = true
                        viewModel.clearConfigurationInputs()
                    },
                    enabled = uiState.selectedDevice != null && uiState.ssid.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Send Configuration")
                }
            }
        }

        AnimatedVisibility(
            visible = showSuccess,
            enter = fadeIn(),
            exit = fadeOut(animationSpec = tween(delayMillis = 1500))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false, onClick = {}),
                contentAlignment = Alignment.Center
            ) {
                var scale by remember { mutableStateOf(0.5f) }
                LaunchedEffect(Unit) {
                    animate(0.5f, 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) { value, _ ->
                        scale = value
                    }
                    delay(2000)
                    showSuccess = false
                }
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale)
                )
            }
        }
    }
}

@Composable
fun RadarScanner(isScanning: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(150.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isScanning) {
            val infiniteTransition = rememberInfiniteTransition(label = "radar")
            (1..3).forEach { i ->
                val duration = 1500
                val delay = i * (duration / 3)
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(duration, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "radar_alpha_$i"
                )
                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(duration, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "radar_scale_$i"
                )
                LaunchedEffect(Unit) {
                    delay(delay.toLong())
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scale)
                        .alpha(alpha)
                        .background(
                            color = ElectricBlue.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                )
            }
        }

        Button(
            onClick = onClick,
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
        ) {
            Icon(
                Icons.Default.BluetoothSearching,
                contentDescription = "Scan Bluetooth",
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }
    }
}




class IpAddressVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 12) text.text.substring(0..11) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 3 == 2 && i < 9) {
                out += "."
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 8) return offset + 2
                if (offset <= 11) return offset + 3
                return 15
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset - 1
                if (offset <= 11) return offset - 2
                if (offset <= 15) return offset - 3
                return 12
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}