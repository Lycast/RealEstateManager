package anthony.brenon.realestatemanager.models




import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by Lycast on 28/07/2022.
 */
@Entity(tableName = "estate_table")
data class Estate(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var type: String,
    var price: String,
    var surface: String,
    var roomsNumber: String,
    var description: String,
    var addressStreet: String,
    var addressCity: String,
    var addressCode: String,
    var addressCountry: String,
    var lng: Double,
    var lat: Double,
    var interestingPoint: String,
    var onSaleDate: String,
    var dateOfSale: String,
    var agentInChargeName: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var picture: Bitmap,
    var numberOfPicture: Int
    ) {
    fun isSold() = dateOfSale.isNotEmpty()
}