package anthony.brenon.realestatemanager.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

object DataConverters {

    fun convertStringToListOfWord(text: String) : List<String> {
        val cleanedText = text.lowercase().replace(Regex("[^\\w\\s]"), " ")
        val delimiter = " " // Change this to the delimiter used in your input
        return cleanedText.split(delimiter).map { it.trim() }.filter { it.isNotEmpty() }
    }

    suspend fun List<ByteArray>.toBitmapList(): List<Bitmap> = withContext(Dispatchers.IO) {
        map { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    suspend fun List<Bitmap>.bitmapsToByteArrayList(): List<ByteArray> = withContext(Dispatchers.IO) {
        map { bitmap ->
            ByteArrayOutputStream().use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.toByteArray()
            }
        }
    }

    fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, stream)
        return compressImage(stream.toByteArray())
    }

    fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }

    fun List<ByteArray>.toBase64List(): List<String> =
        map { Base64.encodeToString(it, Base64.DEFAULT) }

    fun List<String>.stringsToByteArrayList(): List<ByteArray> =
        map { Base64.decode(it, Base64.DEFAULT) }

    private fun compressImage(imageCompressed: ByteArray): ByteArray {
        var compressImage = imageCompressed
        while (compressImage.size > 200000) {
            val bitmap = BitmapFactory.decodeByteArray(compressImage, 0, compressImage.size)
            val resized = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * 0.80).toInt(),
                (bitmap.height * 0.80).toInt(), true
            )
            val stream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
            compressImage = stream.toByteArray()
        }
        return compressImage
    }
}