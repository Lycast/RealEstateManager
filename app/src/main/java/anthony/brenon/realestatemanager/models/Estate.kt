package anthony.brenon.realestatemanager.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import anthony.brenon.realestatemanager.database.Converters
import anthony.brenon.realestatemanager.utils.Utils
import java.text.DecimalFormat

@Entity(tableName = "estate_table")
@TypeConverters(Converters::class)
data class Estate(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var type: String = "",
    var price: String = "",
    var surface: String = "",
    var roomsNumber: String = "",
    var description: String = "",
    var addressStreet: String = "",
    var addressCity: String = "",
    var addressCode: String = "",
    var addressCountry: String = "",
    var lng: Double = 0.00,
    var lat: Double = 0.00,
    var interestingPoint: String = "",
    var saleDate: String = "",
    var soldDate: String = "",
    var agentInChargeName: String = "",
    var pictures: List<String> = emptyList(),
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var picture: Bitmap,
    var numberOfPicture: Int = 0
    ) {

    fun isSold() = soldDate.isNotEmpty()

    fun getPriceFormat(monetary: Boolean) : String {
        val dec = DecimalFormat("#,###")
        return if (monetary) "\u20AC ${dec.format(price.toInt())}"
        else "$ ${dec.format(Utils.convertDollarToEuro(price.toInt()))}"
    }

    fun getAddressFormat() : String {
        return "$addressStreet\n$addressCode $addressCity\n$addressCountry"
    }

    fun getAddressFormatFilter() : String {
        return "$addressStreet $addressCode $addressCity $addressCountry"
    }

    override fun toString(): String {
        return "\nProperty(id=$id, type='$type', price='$price', surface='$surface', roomsNumber='$roomsNumber', " +
                "addressStreet='$addressStreet', addressCity='$addressCity', addressCode='$addressCode', addressCountry='$addressCountry', " +
                "lng=$lng, lat=$lat, interestingPoint='$interestingPoint', saleDate='$saleDate', soldDate='$soldDate', " +
                "agentInChargeName='$agentInChargeName', numberOfPicture=$numberOfPicture)"
    }
}