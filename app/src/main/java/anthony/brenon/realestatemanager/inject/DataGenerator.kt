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

object DataGenerator {

    fun generateAgentData(): List<Agent> {
        val agents = mutableListOf<Agent>()
        agents.add(Agent("Paul","paul@mail.com"))
        agents.add(Agent("Thierry","thierry@mail.com"))
        agents.add(Agent("Simon","simon@mail.com"))
        return agents
    }

    fun generateEstateData(context: Context): List<Estate> {
        Log.i("MYTAG", "generateEstateData is call")
        val estateList = mutableListOf<Estate>()
        estateList.add(
            Estate(
                0,
                "Maison",
                "156000",
                "147",
                "7",
                "Une MAISON D'HABITATION comprenant :\n" +
                        "Au rez-de-chaussée : entrée sur séjour avec cheminée, cuisine, salle à manger, " +
                        "A l'étage : palier, trois chambres, un bureau, salle de bains, wc, débarras, partie de grenier, " +
                        "Grenier au dessus, " +
                        "Dépendances, " +
                        "Garage.",
                "16 Rue Saint-Maurice",
                "Sèvremoine",
                "49230",
                "France",
                -1.1251437376514695,
                47.096144025682285,
                "ecole, transport, commerces, banque, piscine",
                "01/05/2023",
                "",
                "Paul",
                getPicturesEstate1(context),
                getPicture1(context),
                0
            )
        )
        estateList.add(
            Estate(
                0,
                "Maison",
                "126800",
                "67",
                "3",
                "Maison de bourg rénovée en 2020.\n" +
                        "De 67 m² habitables environ. " +
                        "Rdc: Salon avec pierres apparentes. " +
                        "Cuisine aménagée wc – lingerie. " +
                        "Cour extérieur sans vis à vis (17). " +
                        "Etage: Palier débarras 2 chambres. " +
                        "Avec placards salle d’eau – wc" +
                        "Travaux effectués: Ouvertures, Isolation," +
                        "Placo-cloisons, Plomberie, salle d’eau, cuisine," +
                        "Electricité, Chauffage, Ravalement. " +
                        "Raccordement au tout à l’égout conforme. " +
                        "MAISON LOUEE AVEC LOCATAIRES EN PLACE. " +
                        "Fin du bail le 6 Décembre 2024",
                "30 Rue du Collège",
                "Sèvremoine",
                "49230",
                "France",
                -1.1283940510749164,
                47.09494492378816,
                "ecole, transport, commerces",
                "02/05/2023",
                "",
                "Paul",
                getPicturesEstate2(context),
                getPicture2(context),
                0
            )
        )
        estateList.add(
            Estate(
                0,
                "Maison",
                "179600",
                "89",
                "5",
                "Agréable maison des Années 50 de 89 m² habitables;\n" +
                        "Rdc: hall d’entrée séjour avec cheminée et insert; " +
                        "Salon Cuisine aménagée 1 chambre salle d’eau (à refaire). " +
                        "Chaufferie (chauffage fuel) lingerie wc. Etage: palier 2 chambres avec placards débarras. " +
                        "Garage (1 voiture) cave grande terrasse bois. Jardin avec puits. Terrain de 750 m² environ.Travaux effectués: " +
                        "Isolation (2012) Porte d’entrée (2012).Bardages pignon (2013/2016) Aménagement étage (2014). " +
                        "Assainissement tout à l’égout (prévoir travaux de mise en conformité)",
                "6 Rue de la Gabelle",
                "Sèvremoine",
                "49230",
                "France",
                -1.1383424510761884,
                47.0834592079444,
                "transport, commerces",
                "25/04/2023",
                "",
                "Paul",
                getPicturesEstate3(context),
                getPicture3(context),
                0
            )
        )
        estateList.add(
            Estate(
                0,
                "Maison",
                "250140",
                "120",
                "6",
                "A 25 minutes de CHOLET et 15 minutes de CLISSON, dans son bourg, maison de 120m² habitables de plain-pied entourée de son terrain de 1820m² avec garage, " +
                        "local de 180m² et deux puits. La maison bien entretenue, avec une décoration intérieure entièrement refaite en 2013, " +
                        "se compose d'une entrée, une pièce de vie traversante donnant sur une terrasse abritée exposée Est, une cuisine ouverte entièrement aménagée et équipée, " +
                        "une arrière-cuisine également aménagée, une chaufferie, trois chambres, un bureau (ou 4ème chambre), dressing, salle d'eau et wc. " +
                        "Le local de 180m² possède un grand portail coulissant, un quai de chargement et une cave (réhabilitable). Un garage supplémentaire complète cet ensemble. " +
                        "Possibilité de divisions (terrains à bâtir) et terrain piscinable. " +
                        "Les informations sur les risques auxquels ce bien est exposé sont disponibles sur le site Géorisques : www.georisques.gouv.fr.",
                "0 La Verdrie",
                "Sèvremoine",
                "49230",
                "France",
                -1.1297809492269064,
                47.098375055349145,
                "commerces, banque, piscine",
                "20/04/2023",
                "",
                "Paul",
                getPicturesEstate4(context),
                getPicture4(context),
                0
            )
        )
        return estateList
    }

    private fun getPicturesEstate1(context: Context): List<String> {
        val images = mutableListOf<String>()
        val imagesByte = mutableListOf<ByteArray>()
        val resources = context.resources
        val imageIds = arrayOf(R.drawable.estate1_img01, R.drawable.estate1_img02, R.drawable.estate1_img03, R.drawable.estate1_img04,
            R.drawable.estate1_img05, R.drawable.estate1_img06)
        imageIds.forEach { imageId ->
            val bitmap = BitmapFactory.decodeResource(resources, imageId)
            if (bitmap != null) {
                imagesByte.add(bitmap.toByteArray())
            } else {
                Log.i("MY_TAG", "bitmap is null for image: $imageId")
            }
        }
        images.addAll(imagesByte.toBase64List())
        return images
    }

    private fun getPicture1(context: Context): Bitmap {
        val resources = context.resources
        return BitmapFactory.decodeResource(resources, R.drawable.estate1_img01)
    }

    private fun getPicturesEstate2(context: Context): List<String> {
        val images = mutableListOf<String>()
        val imagesByte = mutableListOf<ByteArray>()
        val resources = context.resources
        val imageIds = arrayOf(R.drawable.estate2_img01, R.drawable.estate2_img03, R.drawable.estate2_img04, R.drawable.estate2_img05
            , R.drawable.estate2_img06, R.drawable.estate2_img07, R.drawable.estate2_img08, R.drawable.estate2_img09)
        imageIds.forEach { imageId ->
            val bitmap = BitmapFactory.decodeResource(resources, imageId)
            if (bitmap != null) {
                imagesByte.add(bitmap.toByteArray())
            } else {
                Log.i("MY_TAG", "bitmap is null for image: $imageId")
            }
        }
        images.addAll(imagesByte.toBase64List())
        return images
    }

    private fun getPicture2(context: Context): Bitmap {
        val resources = context.resources
        return BitmapFactory.decodeResource(resources, R.drawable.estate2_img01)
    }

    private fun getPicturesEstate3(context: Context): List<String> {
        val images = mutableListOf<String>()
        val imagesByte = mutableListOf<ByteArray>()
        val resources = context.resources
        val imageIds = arrayOf(R.drawable.estate3_img01, R.drawable.estate3_img03, R.drawable.estate3_img04, R.drawable.estate3_img05
            , R.drawable.estate3_img06, R.drawable.estate3_img07, R.drawable.estate3_img08, R.drawable.estate3_img09,
            R.drawable.estate3_img10)
        imageIds.forEach { imageId ->
            val bitmap = BitmapFactory.decodeResource(resources, imageId)
            if (bitmap != null) {
                imagesByte.add(bitmap.toByteArray())
            } else {
                Log.i("MY_TAG", "bitmap is null for image: $imageId")
            }
        }
        images.addAll(imagesByte.toBase64List())
        return images
    }

    private fun getPicture3(context: Context): Bitmap {
        val resources = context.resources
        return BitmapFactory.decodeResource(resources, R.drawable.estate3_img01)
    }

    private fun getPicturesEstate4(context: Context): List<String> {
        val images = mutableListOf<String>()
        val imagesByte = mutableListOf<ByteArray>()
        val resources = context.resources
        val imageIds = arrayOf(R.drawable.estate4_img01, R.drawable.estate4_img03, R.drawable.estate4_img04, R.drawable.estate4_img05
            , R.drawable.estate4_img06, R.drawable.estate4_img07, R.drawable.estate4_img08, R.drawable.estate4_img09,
            R.drawable.estate4_img10, R.drawable.estate4_img11)
        imageIds.forEach { imageId ->
            val bitmap = BitmapFactory.decodeResource(resources, imageId)
            if (bitmap != null) {
                imagesByte.add(bitmap.toByteArray())
            } else {
                Log.i("MY_TAG", "bitmap is null for image: $imageId")
            }
        }
        images.addAll(imagesByte.toBase64List())
        return images
    }

    private fun getPicture4(context: Context): Bitmap {
        val resources = context.resources
        return BitmapFactory.decodeResource(resources, R.drawable.estate4_img01)
    }
}