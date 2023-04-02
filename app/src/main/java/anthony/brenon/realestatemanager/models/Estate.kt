package anthony.brenon.realestatemanager.models


import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by Lycast on 28/07/2022.
 */
@Entity(tableName = "estate_table")
data class Estate(

    var type: String,

    var price: Int,

    var surface: Int,

    var roomsNumber: Int,

    var description: String,

//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    var picture: Bitmap,

    var address: String,

    var interestingPoint: String,

    var isSale: Boolean,

    var onSaleDate: String,

    var dateOfSale: String,

    var agentInChargeName: String
    )
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}