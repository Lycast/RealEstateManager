package anthony.brenon.realestatemanager.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by Lycast on 28/07/2022.
 */
@Entity(tableName = "estate_table")
class Estate (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "estate_id")
    val id: Int,

    @ColumnInfo(name = "estate_type")
    var type: String,

    @ColumnInfo(name = "estate_price")
    var price: Int,

    @ColumnInfo(name = "estate_surface")
    var surface: Int,

    @ColumnInfo(name = "estate_room_number")
    var roomsNumber: Int,

    @ColumnInfo(name = "estate_description")
    var description: String,

//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB, name = "estate_photos")
//    var photos: List<Int>,

    @ColumnInfo(name = "estate_address")
    var address: String,

    @ColumnInfo(name = "estate_interesting_point")
    var interestingPoint: String,

    @ColumnInfo(name = "estate_is_sale")
    var isSale: Boolean,

    @ColumnInfo(name = "estate_sale_date")
    var onSaleDate: String,

    @ColumnInfo(name = "estate_date_sale")
    var dateOfSale: String,

    @ColumnInfo(name = "estate_agent_charge")
    var agentInChargeName: String
)