package com.petter.remembeer.qrCode

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun QRCodeScanner(
    onScanResult: (String) -> Unit,
    onScanCancelled: () -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data: Intent? = result.data
            // Check if data is not null and contains the QR code
            if (data != null) {
                // Process the QR code data
                // For example, you can use ZXing library to decode the QR code
                // Here, we'll just pass an empty string as a placeholder
                onScanResult("")
            } else {
                // Scanning cancelled or failed
                onScanCancelled()
            }
        } else {
            // Scanning cancelled or failed
            onScanCancelled()
        }
    }

    Box {
        launcher.launch(Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE))
    }
}

/*@Composable
fun QRCodeScreen() {
    val scannedQRCode = remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (scannedQRCode.value == null) {
            QRCodeScanner(
                onScanResult = { qrCode ->
                    scannedQRCode.value = qrCode
                },
                onScanCancelled = {
                    // Handle cancellation or failure
                }
            )
        } else {
            // QR code scanned successfully, display the result
            DisplayScannedQRCode(scannedQRCode.value!!)
        }
    }
}

@Composable
fun DisplayScannedQRCode(qrCodeData: String) {
    // Display the scanned QR code data
}
*/
