package com.petter.remembeer.helper

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Entity
data class Beer(
    @PrimaryKey val uid: UUID = UUID.randomUUID(),
    @ColumnInfo var type : String?,
    @ColumnInfo var name: String?,
    @ColumnInfo var note: String?,
    @ColumnInfo var rating: Int?,
    @ColumnInfo var image: String?,
    //var isScanned: Boolean
)

class BeerViewModel(application: Application) : AndroidViewModel(application) {

    val beerlistobs = AppDatabase.getDatabase(null).beerDao().getAllflow()
    fun addBeer(
        beerType: String,
        beerName: String,
        beerNote: String,
        beerRating: Int,
        beerImage: String
    ) {
        val beerdao = AppDatabase.getDatabase(null).beerDao()

        CoroutineScope(Dispatchers.IO).launch {

            val tempbeer = Beer(
                type = beerType,
                name = beerName,
                note = beerNote,
                rating = beerRating,
                image = beerImage
            )
            beerdao.insertAll(tempbeer)
        }
    }
    fun deleteBeer(deletebeer: Beer) {
        val beerdao = AppDatabase.getDatabase(null).beerDao()

        CoroutineScope(Dispatchers.IO).launch {
            beerdao.delete(deletebeer)
        }
    }


    suspend fun generateQRCodeBitmapForBeer(
        beerId: UUID,
        beerImageBitmap: Bitmap
    ): Bitmap? = withContext(Dispatchers.IO) {
        val beerdao = AppDatabase.getDatabase(null).beerDao()
        val beer = beerdao.getBeerById(beerId)
        if (beer != null) {
            try {
                val imageUrl = uploadBeerImageToFirebaseStorage(beer, beerImageBitmap)
                val beerWithImageUrl = beer.copy(image = imageUrl)
                val jsonBeerData = Gson().toJson(beerWithImageUrl)
                return@withContext generateQRCodeBitmap(jsonBeerData)
            } catch (e: Exception) {
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

    private suspend fun uploadBeerImageToFirebaseStorage(
        beer: Beer,
        beerImageBitmap: Bitmap
    ): String {
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
                            Log.e(
                                "BeerViewModel",
                                "Error getting download URL: ${exception.message}"
                            )
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



    /*
    fun handleScannedQRCode(scannedBeer: Beer?, callback: (Beer?, Uri?) -> Unit) {
        val imageUrl = scannedBeer?.image

        val storageRef = Firebase.storage.reference
        val imageRef = imageUrl?.let { storageRef.child(it) }

        imageRef?.getBytes(1 * 1024 * 1024)?.addOnSuccessListener { imageData ->
            // Save the downloaded image locally
            val localUri = saveImageLocally(imageData)
            // Pass the scanned beer object and local image Uri back to the caller
            callback(scannedBeer, localUri)
        }?.addOnFailureListener { exception ->
            // Handle failure to download image
            Log.e(TAG, "Error downloading image: ${exception.message}", exception)
            callback(scannedBeer, null) // Pass null Uri to indicate failure
        }
    }
}

private suspend fun downloadImage(url: String): ByteArray? {
    return try {
        val imageData = Firebase.storage.reference.child(url).getBytes(1 * 1024 * 1024).await()
        imageData
    } catch (e: Exception) {
        Log.e(TAG, "Error downloading image: ${e.message}", e)
        null
    }
}

private fun decodeScannedData(scannedData: String): Pair<UUID, String> {
    val jsonObject = JSONObject(scannedData)
    val beerId = UUID.fromString(jsonObject.getString("beerId"))
    val imageUrl = jsonObject.getString("imageUrl")
    return Pair(beerId, imageUrl)
}


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


     */
}

fun parseJsonFromString(jsonString: String): Beer? {
    return try {
        Gson().fromJson(jsonString, Beer::class.java)
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
        null
    }
}
