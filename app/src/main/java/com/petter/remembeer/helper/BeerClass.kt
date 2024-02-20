package com.petter.remembeer.helper

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Firebase
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.storage.storage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


data class Beer(
    var id: UUID = UUID.randomUUID(),
    var type: String,
    var name: String,
    var note: String,
    var rating: Int,
    var image: String?,
    var isScanned: Boolean
)

fun parseJsonFromString(jsonString: String): Beer? {
    return try {
        Gson().fromJson(jsonString, Beer::class.java)
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
        null
    }
}

class BeerViewModel(application: Application) : AndroidViewModel(application) {

    private val beersFile = File(application.filesDir, "beers.json")

    private val _beers = MutableStateFlow<List<Beer>>(emptyList())
    val beers: StateFlow<List<Beer>> = _beers

    init {
        loadBeers()
    }

    fun addBeer(beer: Beer, imageUri: Uri?, isScanned: Boolean) {
        val beerWithId = beer.copy(id = UUID.randomUUID(),isScanned = isScanned)
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

    suspend fun generateQRCodeBitmapForBeer(
        beerId: UUID,
        beerImageBitmap: Bitmap
    ): Bitmap? = withContext(Dispatchers.IO) {
        val beer = getBeerById(beerId)
        if (beer != null) {
            try {
                // Upload beer image to Firebase Storage and get download URL
                val imageUrl = uploadBeerImageToFirebaseStorage(beer, beerImageBitmap)

                // Modify beer object with image URL
                val beerWithImageUrl = beer.copy(image = imageUrl)

                // Convert the modified beer object to JSON
                val jsonBeerData = Gson().toJson(beerWithImageUrl)

                // Generate QR code bitmap using the modified JSON data
                return@withContext generateQRCodeBitmap(jsonBeerData)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
        return@withContext null
    }


    private fun generateQRCodeBitmap(jsonData: String): Bitmap {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix: BitMatrix =
            multiFormatWriter.encode(jsonData, BarcodeFormat.QR_CODE, 500, 500)
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

    private suspend fun uploadBeerImageToFirebaseStorage(beer: Beer, beerImageBitmap: Bitmap): String {
        return suspendCoroutine { continuation ->
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imageRef = storageRef.child("beer_images/${UUID.randomUUID()}.jpg")

            // Convert bitmap to byte array
            val byteArrayOutputStream = ByteArrayOutputStream()
            beerImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageData = byteArrayOutputStream.toByteArray()

            // Upload image data to Firebase Storage
            imageRef.putBytes(imageData)
                .addOnSuccessListener { taskSnapshot ->
                    // Retrieve the download URL
                    imageRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            // Return the download URL string
                            continuation.resume(uri.toString())
                        }
                        .addOnFailureListener { exception ->
                            // Handle failure to get download URL
                            Log.e("BeerViewModel", "Error getting download URL: ${exception.message}")
                            continuation.resumeWithException(exception)
                        }
                }
                .addOnFailureListener { exception ->
                    // Handle failure to upload image
                    Log.e("BeerViewModel", "Error uploading image: ${exception.message}")
                    continuation.resumeWithException(exception)
                }
        }
    }

    private fun decodeScannedData(scannedData: String): Pair<UUID, String> {
        val jsonObject = JSONObject(scannedData)
        val beerId = UUID.fromString(jsonObject.getString("beerId"))
        val imageUrl = jsonObject.getString("imageUrl")
        return Pair(beerId, imageUrl)
    }

    fun handleScannedQRCode(scannedBeer: Beer, callback: (ByteArray?) -> Unit) {
        val imageUrl = scannedBeer.image

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = imageUrl?.let { storageRef.child(it) }

        imageRef?.getBytes(1 * 1024 * 1024)?.addOnSuccessListener { imageData ->
            // Pass the downloaded image data back to the caller
            callback(imageData)
        }?.addOnFailureListener { exception ->
            // Handle failure to download image
            Log.e(TAG, "Error downloading image: ${exception.message}", exception)
            callback(null) // Pass null to indicate failure
        }
    }

}


/*
   suspend fun generateQRCodeBitmapForBeer(beerId: UUID): Bitmap? = withContext(Dispatchers.IO) {
        val beer = getBeerById(beerId)
        if (beer != null) {
            try {
                // Exclude the image field from the beer object before converting to JSON
                val beerWithoutImage = beer.copy(image = null)

                // Convert the modified beer object to JSON
                val jsonBeerData = Gson().toJson(beerWithoutImage)

                // Generate QR code bitmap using the modified JSON data
                return@withContext generateQRCodeBitmap(jsonBeerData)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
        return@withContext null
    }

 */
