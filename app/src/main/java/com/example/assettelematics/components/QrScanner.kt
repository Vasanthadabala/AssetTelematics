package com.example.assettelematics.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun BarcodeScanner(
    onScanResult: (String) -> Unit,
    onPermissionDenied: () -> Unit,
    isScanning: MutableState<Boolean>
) {
    val context = LocalContext.current

    val barCodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        isScanning.value = false
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
        } else {
            onScanResult(result.contents)
        }
    }

    fun showCamera() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scan a QR Code")
            setCameraId(0) // Use the back camera
            setBeepEnabled(false)
            setOrientationLocked(true)
        }

        barCodeLauncher.launch(options)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCamera()
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(isScanning.value) {
        if (isScanning.value) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showCamera()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as androidx.activity.ComponentActivity,
                    Manifest.permission.CAMERA
                )
            ) {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                onPermissionDenied()
                isScanning.value = false
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}