package anthony.brenon.realestatemanager.models


/**
 * Created by Lycast on 28/07/2022.
 */
class Estate (
    var type: String = "type",
    var price: Int = 0,
    var surface: Int = 0,
    var roomsNumber: Int = 0,
    var description: String = "",
    var photo: String = "",
    var address: String = "",
    var interestingPoint: String = "",
    var isSale: Boolean = false,
    var onSaleDate: String = "date on sale",
    var dateOfSale: String = "date of sale",
    var agentInChargeName: String = "agent in charge") {
}