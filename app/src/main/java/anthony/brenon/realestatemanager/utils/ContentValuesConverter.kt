package anthony.brenon.realestatemanager.utils

import android.content.ContentValues
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.utils.DataConverters.toBitmap

object ContentValuesConverter {

    fun fromContentValues(values: ContentValues): Estate {
        return Estate(
            id = values.getAsLong("id"),
            type = values.getAsString("type"),
            price = values.getAsString("price"),
            surface = values.getAsString("surface"),
            roomsNumber = values.getAsString("roomsNumber"),
            description = values.getAsString("description"),
            addressStreet = values.getAsString("addressStreet"),
            addressCity = values.getAsString("addressCity"),
            addressCode = values.getAsString("addressCode"),
            addressCountry = values.getAsString("addressCountry"),
            lng = values.getAsDouble("lng"),
            lat = values.getAsDouble("lat"),
            interestingPoint = values.getAsString("interestingPoint"),
            saleDate = values.getAsString("saleDate"),
            soldDate = values.getAsString("soldDate"),
            agentInChargeName = values.getAsString("agentInChargeName"),
            numberOfPicture = values.getAsInteger("numberOfPicture"),
            picture = (values.getAsByteArray("picture")).toBitmap(),
            pictures = values.getAsString("pictures")?.split(",")?.toMutableList() ?: mutableListOf()
        )
    }
}