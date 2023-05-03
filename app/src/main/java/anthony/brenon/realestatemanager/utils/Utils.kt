package anthony.brenon.realestatemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.wifi.WifiManager
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * 0.812).roundToInt()
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    val todayDate: String
        @SuppressLint("SimpleDateFormat")
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }

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
        return stream.toByteArray()
    }

    fun List<ByteArray>.toBase64List(): List<String> =
        map { Base64.encodeToString(it, Base64.DEFAULT) }

    fun List<String>.stringsToByteArrayList(): List<ByteArray> =
        map { Base64.decode(it, Base64.DEFAULT) }
}