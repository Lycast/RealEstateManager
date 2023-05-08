package anthony.brenon.realestatemanager.utils

import android.content.ContentValues
import android.graphics.Bitmap
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import java.io.ByteArrayOutputStream

object ContentValuesUtil {

    fun toContentValues(agent: Agent): ContentValues {
        val values = ContentValues()
        values.put("id", agent.id)
        values.put("agent_name", agent.nameAgent)
        values.put("agent_email", agent.emailAgent)
        return values
    }

    fun getContentValues(estate: Estate): ContentValues {
        val values = ContentValues()
        values.put("id", estate.id)
        values.put("type", estate.type)
        values.put("price", estate.price)
        values.put("surface", estate.surface)
        values.put("roomsNumber", estate.roomsNumber)
        values.put("description", estate.description)
        values.put("addressStreet", estate.addressStreet)
        values.put("addressCity", estate.addressCity)
        values.put("addressCode", estate.addressCode)
        values.put("addressCountry", estate.addressCountry)
        values.put("lng", estate.lng)
        values.put("lat", estate.lat)
        values.put("interestingPoint", estate.interestingPoint)
        values.put("saleDate", estate.saleDate)
        values.put("soldDate", estate.soldDate)
        values.put("agentInChargeName", estate.agentInChargeName)
        values.put("numberOfPicture", estate.numberOfPicture)
        values.put("picture", getByteArray(estate.picture))

        if (!estate.pictures.isEmpty()) {
            values.put("pictures", estate.pictures.joinToString(","))
        }

        return values
    }

    fun getContentValues(agent: Agent): ContentValues {
        val values = ContentValues()
        values.put("id", agent.id)
        values.put("agent_name", agent.nameAgent)
        values.put("agent_email", agent.emailAgent)
        return values
    }

    private fun getByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun toAgent(values: ContentValues?): Agent? {
        if (values == null) {
            return null
        }
        val id = values.getAsLong("id") ?: return null
        val nameAgent = values.getAsString("agent_name") ?: return null
        val emailAgent = values.getAsString("agent_email") ?: return null
        val agent = Agent(nameAgent, emailAgent)
        agent.id = id.toInt()
        return agent
    }
}