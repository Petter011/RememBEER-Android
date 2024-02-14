package com.petter.remembeer.helper

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


data class Beer(
    var id: UUID = UUID.randomUUID(),
    var type: String = "",
    var name: String = "",
    var note: String = "",
    var rating: Int = 0,
    var image: String?
)

class BeerViewModel(application: Application) : AndroidViewModel(application) {

    private val beersFile = File(application.filesDir, "beers.json")

    private val _beers = MutableStateFlow<List<Beer>>(emptyList())
    val beers: StateFlow<List<Beer>> = _beers

    init {
        loadBeers()
    }

    fun addBeer(beer: Beer, imageUri: Uri?) {
        val beerWithId = beer.copy(id = UUID.randomUUID())
        val imagePath = imageUri?.let { saveImage(it) }
        _beers.value = _beers.value + beerWithId.copy(image = imagePath)
        saveBeers()
    }

    private fun saveImage(imageUri: Uri): String {
        val context = getApplication<Application>()
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val directory = File(context.filesDir, "images")
        directory.mkdirs()
        val destinationFile = File(directory, "beer_${UUID.randomUUID()}.jpg")
        inputStream?.use { input ->
            FileOutputStream(destinationFile).use { output ->
                input.copyTo(output)
            }
        }
        return destinationFile.absolutePath
    }

    private fun loadBeers() {
        if (beersFile.exists()) {
            val jsonString = beersFile.readText()
            val beersList = Gson().fromJson(jsonString, Array<Beer>::class.java).toList()
            _beers.value = beersList
        }
    }

    private fun saveBeers() {
        val jsonString = Gson().toJson(_beers.value)
        beersFile.writeText(jsonString)
    }

    fun getBeerById(beerId: UUID): Beer? {
        return beers.value.find { it.id == beerId }
    }

    suspend fun generateQRCodeForBeer(beerId: UUID): File? = withContext(Dispatchers.IO) {
        val beer = getBeerById(beerId)
        if (beer != null) {
            try {
                val qrCodeFile = File(getApplication<Application>().cacheDir, "beer_qr_code.png")
                val bitmap = generateQRCodeBitmap(beer)
                Log.d("QR_CODE_CONTENT", "QR Code Content: ${beer.name}, ${beer.type}, ${beer.note}, ${beer.rating}")

                saveBitmapToFile(bitmap, qrCodeFile)
                return@withContext qrCodeFile
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
        return@withContext null
    }

    private fun generateQRCodeBitmap(beer: Beer): Bitmap {
        val text = "Name: ${beer.name}\nType: ${beer.type}\nNote: ${beer.note}\nRating: ${beer.rating}"
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix: BitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500)
        return createBitmapFromBitMatrix(bitMatrix)
    }

    private fun createBitmapFromBitMatrix(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (matrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
    }
}
