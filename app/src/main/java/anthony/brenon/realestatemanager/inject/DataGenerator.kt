package anthony.brenon.realestatemanager.inject

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture

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
                "",
                "01/05/2023",
                "",
                "Paul",
                BitmapFactory.decodeResource(context.resources, R.drawable.no_image),
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
                "",
                "02/05/2023",
                "",
                "Paul",
                BitmapFactory.decodeResource(context.resources, R.drawable.no_image),
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
                "",
                "25/04/2023",
                "",
                "Paul",
                BitmapFactory.decodeResource(context.resources, R.drawable.no_image),
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
                "",
                "20/04/2023",
                "",
                "Paul",
                BitmapFactory.decodeResource(context.resources, R.drawable.no_image),
                0
            )
        )
        return estateList
    }

    fun getPictureData1(): List<Picture> {
        val pictures = mutableListOf<Picture>()
        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val estateFiles = dcimDir.listFiles { file -> file.name.startsWith("estate1_") }
        estateFiles?.forEach { file ->
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                pictures.add(Picture(bitmap, 1))
            } else {
                Log.i("MY_TAG", "bitmap is null for file: ${file.absolutePath}")
            }
        }
        return pictures
    }

    fun getPictureData2(): List<Picture> {
        val pictures = mutableListOf<Picture>()
        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val estateFiles = dcimDir.listFiles { file -> file.name.startsWith("estate2_") }
        estateFiles?.forEach { file ->
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                pictures.add(Picture(bitmap, 2))
            } else {
                Log.i("MY_TAG", "bitmap is null for file: ${file.absolutePath}")
            }
        }
        return pictures
    }

    fun getPictureData3(): List<Picture> {
        val pictures = mutableListOf<Picture>()
        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val estateFiles = dcimDir.listFiles { file -> file.name.startsWith("estate3_") }
        estateFiles?.forEach { file ->
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                pictures.add(Picture(bitmap, 3))
            } else {
                Log.i("MY_TAG", "bitmap is null for file: ${file.absolutePath}")
            }
        }
        return pictures
    }

    fun getPictureData4(): List<Picture> {
        val pictures = mutableListOf<Picture>()
        val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val estateFiles = dcimDir.listFiles { file -> file.name.startsWith("estate4_") }
        estateFiles?.forEach { file ->
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                pictures.add(Picture( bitmap, 4))
            } else {
                Log.i("MY_TAG", "bitmap is null for file: ${file.absolutePath}")
            }
        }
        return pictures
    }
}