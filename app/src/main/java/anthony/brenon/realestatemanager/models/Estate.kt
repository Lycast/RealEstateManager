package anthony.brenon.realestatemanager.models


import android.os.Parcelable
import anthony.brenon.realestatemanager.R
import kotlinx.parcelize.Parcelize


/**
 * Created by Lycast on 28/07/2022.
 */
@Parcelize
data class Estate (
    val id: Int = 0,
    var type: String = "type",
    var price: Int = 0,
    var surface: Int = 0,
    var roomsNumber: Int = 0,
    var description: String = "description",
    var photos: List<Int> = listOf(R.drawable.img_estate_test_1, R.drawable.img_estate_test_1, R.drawable.img_estate_test_1),
    var address: String = "address",
    var interestingPoint: String = "interestingPoint",
    var isSale: Boolean = false,
    var onSaleDate: String = "date on sale",
    var dateOfSale: String = "date of sale",
    var agentInChargeName: String = "agent in charge") : Parcelable