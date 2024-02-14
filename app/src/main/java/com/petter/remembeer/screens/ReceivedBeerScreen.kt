package com.petter.remembeer.screens

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.petter.remembeer.helper.Background
import com.petter.remembeer.helper.BeerViewModel
import com.petter.remembeer.helper.Header


@Composable
fun ReceivedBeerScreen( viewModel: BeerViewModel
) {
    val scannedResult = remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val barcode = result.data?.getStringExtra("SCAN_RESULT")
            barcode?.let {
                scannedResult.value = it
            }
        }
    }


    Background()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(text = "Received Beer")
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            scannedResult.value?.let { result ->
                Text(
                    text = "Scanned Result: $result",
                    textAlign = TextAlign.Center
                )
            }
        }
        ElevatedButton(
            onClick = {
                val intent = Intent(context, ScanActivity::class.java)
                launcher.launch(intent)
                      },
            modifier = Modifier
                .padding(36.dp)
                .size(width = 230.dp, height = 70.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text(
                text = "Scan QR Code",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Yellow
                )
            )
        }
    }
}

class ScanActivity : AppCompatActivity() {
    private lateinit var barcodeView: DecoratedBarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcodeView = DecoratedBarcodeView(this)
        setContentView(barcodeView)

        barcodeView.decodeContinuous(callback)
    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            result?.let {
                val intent = Intent()
                intent.putExtra("SCAN_RESULT", it.text)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}



/*@Composable
fun ReceivedBeerScreen(viewModel: BeerViewModel) {
    var scannedQRCode by remember { mutableStateOf<String?>(null) }

    Background()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(text = "Received Beer")
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScanBeerButton(viewModel, text = "Scan QR") {
            scannedQRCode = it
        }

        // Display the scanned QR code if available
        scannedQRCode?.let { qrCode ->
            Text(
                text = "Scanned QR Code: $qrCode",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun ScanBeerButton(
    viewModel: BeerViewModel,
    text: String,
    onScanResult: (String) -> Unit
) {
    var showScanner by remember { mutableStateOf(false) }

    // Function to handle scan result
    val onScanResultCallback: (String) -> Unit = { qrCode ->
        onScanResult(qrCode)
        showScanner = false
    }

    if (showScanner) {
        QRCodeScanner(
            onScanResult = onScanResultCallback,
            onScanCancelled = { showScanner = false }
        )
    }

    ElevatedButton(
        onClick = { showScanner = true },
        modifier = Modifier
            .padding(36.dp)
            .size(width = 230.dp, height = 70.dp),
        colors = ButtonDefaults.buttonColors(Color.Black)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Yellow
            )
        )
    }
}
*/


