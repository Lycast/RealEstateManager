package anthony.brenon.realestatemanager.inject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.utils.DataConverters.toBase64List
import anthony.brenon.realestatemanager.utils.DataConverters.toByteArray
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random
import kotlin.math.PI
import kotlin.math.cos

object DataGenerator {

    private val random = Random()

    private const val centerLatitude = 47.06081759974417
    private const val centerLongitude = -0.882213362492633
    private const val radiusInKm = 18.0
    private val estateTypes = listOf("House", "Apartment", "Villa", "Condo", "Cottage")
    private val estateImageIds = arrayOf(
        arrayOf(
            R.drawable.estate_profil_01, R.drawable.estate1_img01, R.drawable.estate1_img02,
            R.drawable.estate1_img03, R.drawable.estate1_img04, R.drawable.estate1_img05,
            R.drawable.estate1_img06),
        arrayOf(
            R.drawable.estate_profil_02, R.drawable.estate1_img01, R.drawable.estate1_img02),
        arrayOf(
            R.drawable.estate_profil_03, R.drawable.estate1_img01, R.drawable.estate1_img02,
            R.drawable.estate1_img03),
        arrayOf(
            R.drawable.estate_profil_04, R.drawable.estate1_img01),
        arrayOf(
            R.drawable.estate_profil_05, R.drawable.estate4_img02, R.drawable.estate4_img03,
            R.drawable.estate4_img04, R.drawable.estate4_img05, R.drawable.estate4_img06,
            R.drawable.estate4_img07, R.drawable.estate4_img08),
        arrayOf(
            R.drawable.estate_profil_06, R.drawable.estate4_img02, R.drawable.estate4_img03),
        arrayOf(
            R.drawable.estate_profil_07, R.drawable.estate4_img02),
        arrayOf(
            R.drawable.estate_profil_08, R.drawable.estate4_img02, R.drawable.estate4_img03,
            R.drawable.estate4_img04)
    )

    fun generateAgentData(): List<Agent> {
        val agents = ArrayList<Agent>()
        agents.add(Agent("Paul", "paul@mail.com"))
        agents.add(Agent("Thierry", "thierry@mail.com"))
        agents.add(Agent("Simon", "simon@mail.com"))
        return agents
    }

    fun generateEstateData(context: Context): List<Estate> {
        val estateList = ArrayList<Estate>()
        for (i in 1..3) {
            val estate = Estate(
                0,
                getRandomEstateType(),
                getRandomPrice(),
                getRandomArea(),
                getRandomNumberOfRooms(),
                getRandomDescription(),
                getRandomAddress(),
                "Cholet",
                "49600",
                "France",
                getRandomLongitude(),
                getRandomLatitude(),
                getRandomAmenities(),
                getRandomDate(),
                getRandomEndDate(),
                getRandomAgentName(),
                getRandomPictures(context),
                getRandomPicture(context),
                0
            )
            estateList.add(estate)
        }
        return estateList
    }

    private fun getRandomEstateType(): String { return estateTypes.random() }

    private fun getRandomPrice(): String {
        val price = random.nextInt(2500000) + 50000
        return price.toString()
    }

    private fun getRandomArea(): String {
        val area = random.nextInt(300) + 50
        return area.toString()
    }

    private fun getRandomNumberOfRooms(): String {
        val rooms = random.nextInt(12) + 1
        return rooms.toString()
    }

    private fun getRandomDescription(): String {
        val descriptions = listOf(
            "Spacious and modern apartment with open floor plan and high ceilings",
            "Cozy and charming cottage with a fireplace and a private garden",
            "Luxurious and elegant penthouse offering panoramic views of the city skyline",
            "Immaculate waterfront villa with panoramic ocean views, situated on a private sandy beach. " +
                    "This architectural masterpiece boasts an open-concept design with floor-to-ceiling windows, showcasing the picturesque coastline from every room. " +
                    "Indulge in the luxury of a gourmet chef's kitchen, a private home spa with a sauna and steam room, " +
                    "a home gym overlooking the ocean, and an outdoor entertainment area with a pool, cabana, and built-in BBQ grill.",
            "Historic mansion with timeless elegance, meticulously restored to its original grandeur. This exceptional residence features exquisite craftsmanship, " +
                    "including intricate woodwork, hand-painted ceilings, and stained glass windows. Enjoy the opulence of multiple living rooms, " +
                    "a formal dining room with a crystal chandelier, a library with a fireplace, " +
                    "a private wine cellar, and a sprawling landscaped garden with a fountain, rose garden, and a pergola-covered outdoor dining area.",
            "Contemporary urban penthouse offering the pinnacle of luxury living. With sleek, modern design and premium finishes, " +
                    "this extraordinary residence showcases an open floor plan with high ceilings and expansive floor-to-ceiling windows, " +
                    "allowing for abundant natural light and sweeping city skyline views. The gourmet kitchen is equipped with top-of-the-line appliances and a central island, " +
                    "while the master suite boasts a spa-like bathroom with a soaking tub, a walk-in rain shower, and a private terrace with a hot tub.",
            "Rustic yet luxurious log cabin retreat nestled in a secluded forest setting, offering a stone fireplace, exposed timber beams, a gourmet kitchen with a farmhouse sink, " +
                    "a wrap-around porch with a hot tub, and direct access to hiking trails and a private fishing lake.",
            "Elegant colonial-style estate located in an exclusive gated community, featuring a sweeping curved staircase, a wood-paneled library, a wine cellar, " +
                    "a resort-style pool with a cabana, and a private guesthouse with its own kitchen and living area."
        )
        return descriptions.random()
    }

    private fun getRandomAddress(): String {
        val addresses = listOf(
            "Rue de la Paix",
            "Avenue Anatole France",
            "Place de la Concorde",
            "Boulevard Carnot",
            "Rue du Faubourg Saint-Honoré",
            "Rue de la Liberté",
            "Avenue des Champs-Élysées",
            "Boulevard Saint-Germain",
            "Place de l'Étoile",
            "Rue de Rivoli",
            "Avenue de la République",
            "Boulevard Haussmann",
            "Place de la Bastille",
            "Rue du Faubourg Saint-Antoine",
            "Avenue Montaigne")
        return addresses.random()
    }

    private fun getRandomLongitude(): Double {
        val maxLongitudeOffset = radiusInKm / (111.320 * cos(centerLatitude * PI / 180.0))
        return centerLongitude + random.nextDouble() * 2 * maxLongitudeOffset - maxLongitudeOffset
    }

    private fun getRandomLatitude(): Double {
        val maxLatitudeOffset = radiusInKm / 110.574
        return centerLatitude + random.nextDouble() * 2 * maxLatitudeOffset - maxLatitudeOffset
    }

    private fun getRandomAmenities(): String {
        val amenities = listOf("Swimming pool", "Garden", "Garage", "Gym", "Fireplace")
        val numAmenities = random.nextInt(amenities.size) + 1
        return amenities.shuffled().take(numAmenities).joinToString(", ")
    }

    private fun getRandomDate(): String {
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -12)
        val endDate = Calendar.getInstance()

        val randomDate = startDate.timeInMillis + (Math.random() * (endDate.timeInMillis - startDate.timeInMillis)).toLong()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date(randomDate))
    }

    private fun getRandomEndDate(): String {
        val random = Random()
        return if (random.nextInt(10) > 7) {
            val startDate = Calendar.getInstance()
            startDate.add(Calendar.MONTH, 1)
            val endDate = Calendar.getInstance()

            val randomDate = startDate.timeInMillis + (Math.random() * (endDate.timeInMillis - startDate.timeInMillis)).toLong()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            dateFormat.format(Date(randomDate))
        } else { "" }
    }

    private fun getRandomAgentName(): String {
        val randomIndex = (Math.random() * generateAgentData().size).toInt()
        return generateAgentData()[randomIndex].nameAgent
    }

    private fun getRandomPictures(context: Context): List<String> {
        val randomIndex = (Math.random() * estateImageIds.size).toInt()
        val imageIds = estateImageIds[randomIndex]
        return getPictures(context, imageIds)
    }

    private fun getPictures(context: Context, imageIds: Array<Int>): List<String> {
        val images = mutableListOf<String>()
        val imagesByte = mutableListOf<ByteArray>()
        val resources = context.resources
        imageIds.forEach { imageId ->
            val bitmap = BitmapFactory.decodeResource(resources, imageId)
            if (bitmap != null) {
                imagesByte.add(bitmap.toByteArray())
            } else {
                Log.i("TAG", "bitmap is null for image: $imageId")
            }
        }
        images.addAll(imagesByte.toBase64List())
        return images
    }

    private fun getRandomPicture(context: Context): Bitmap {
        val randomIndex = (Math.random() * estateImageIds.size).toInt()
        val imageIds = estateImageIds[randomIndex]
        return getPicture(context, imageIds[0])
    }

    private fun getPicture(context: Context, imageId: Int): Bitmap {
        val resources = context.resources
        return BitmapFactory.decodeResource(resources, imageId)
    }
}