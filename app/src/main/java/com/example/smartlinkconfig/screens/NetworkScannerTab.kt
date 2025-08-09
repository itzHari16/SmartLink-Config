package com.example.smartlinkconfig.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartlinkconfig.data.NetworkDevice

@Composable
fun NetworkScannerTab(viewModel: ConfigurationViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.startNetworkScan() },
            enabled = !uiState.isScanningNetwork,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Scan WiFi Network")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isScanningNetwork && uiState.networkDevices.isEmpty()) {
            ShimmerLoading()
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.networkDevices) { device ->
                    DeviceCard(device = device)
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: NetworkDevice) {
    val deviceIcon = when {
        device.hostname.contains("android", ignoreCase = true) -> Icons.Default.PhoneAndroid
        device.hostname.contains("iphone", ignoreCase = true) -> Icons.Default.PhoneIphone
        device.hostname.contains("windows", ignoreCase = true) -> Icons.Default.Laptop
        else -> Icons.Default.Router
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = deviceIcon,
                contentDescription = "Device Type",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = if (device.hostname != device.ipAddress) device.hostname else "Unknown Host",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = device.ipAddress, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = device.macAddress.uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ShimmerLoading() {
    val shimmerColors = listOf(
        Color.DarkGray.copy(alpha = 0.6f),
        Color.DarkGray.copy(alpha = 0.2f),
        Color.DarkGray.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "shimmer_translate"
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    Column {
        repeat(6) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(vertical = 4.dp)
                    .background(brush, shape = RoundedCornerShape(12.dp))
            )
        }
    }
}