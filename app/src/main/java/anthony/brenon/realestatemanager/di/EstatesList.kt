package anthony.brenon.realestatemanager.di

import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.models.Estate

/**
 * Created by Lycast on 29/07/2022.
 */
class EstatesList() {

    fun getEstatesList(): List<Estate> {
        return listOf(
            Estate(
                1202,
                "Maison",
                145210,
                95,
                4,
                "Traditionnelle et moderne à la fois, cette maison en L est idéal pour accueillir toute la famille ! " +
                        "Avec ses 3 chambres, son bureau, sa cuisine ouverte et son garage, cette maison de 95m² se personnalise sur-mesure. " +
                        "Inclus dans le prix maison + terrain : Grandes baies vitrées, volets roulants électriques, chauffage économique avec plancher-chauffant, meuble vasque, " +
                        "mitigeurs HANSGROHE, faïence jusqu'à 30x90, carrelage jusqu'à 60x60, garanties et assurances obligatoires incluses (voir détails en agence). Terrain sélectionné " +
                        "et vu pour vous sous réserve de disponibilité et au prix indiqué par notre partenaire foncier.\n" +
                        "Hors raccordements, hors branchements. Hors frais de notaire.",
                listOf(
                    R.drawable.img_estate_test_1,
                    R.drawable.img_estate_test_2,
                    R.drawable.img_estate_test_3
                ),
                "CHOLET (49)",
                "",
                false,
                "28/07/2022",
                "",
                "Maurice Baudoin"
            ),

            Estate(
                150,
                "Villa",
                188210,
                95,
                4,
                "Traditionnelle et moderne à la fois, cette maison en L est idéal pour accueillir toute la famille ! " +
                        "Avec ses 3 chambres, son bureau, sa cuisine ouverte et son garage, cette maison de 95m² se personnalise sur-mesure. " +
                        "Inclus dans le prix maison + terrain : Grandes baies vitrées, volets roulants électriques, chauffage économique avec plancher-chauffant, meuble vasque, " +
                        "mitigeurs HANSGROHE, faïence jusqu'à 30x90, carrelage jusqu'à 60x60, garanties et assurances obligatoires incluses (voir détails en agence). Terrain sélectionné " +
                        "et vu pour vous sous réserve de disponibilité et au prix indiqué par notre partenaire foncier.\n" +
                        "Hors raccordements, hors branchements. Hors frais de notaire.",
                listOf(
                    R.drawable.img_estate_test_2,
                    R.drawable.img_estate_test_1,
                    R.drawable.img_estate_test_3
                ),
                "LA RENAUDIERE (49)",
                "",
                false,
                "28/07/2022",
                "",
                "Maurice Baudoin"
            ),

            Estate(
                56841,
                "Appartement",
                1245000,
                95,
                4,
                "Traditionnelle et moderne à la fois, cette maison en L est idéal pour accueillir toute la famille ! " +
                        "Avec ses 3 chambres, son bureau, sa cuisine ouverte et son garage, cette maison de 95m² se personnalise sur-mesure. " +
                        "Inclus dans le prix maison + terrain : Grandes baies vitrées, volets roulants électriques, chauffage économique avec plancher-chauffant, meuble vasque, " +
                        "mitigeurs HANSGROHE, faïence jusqu'à 30x90, carrelage jusqu'à 60x60, garanties et assurances obligatoires incluses (voir détails en agence). Terrain sélectionné " +
                        "et vu pour vous sous réserve de disponibilité et au prix indiqué par notre partenaire foncier.\n" +
                        "Hors raccordements, hors branchements. Hors frais de notaire.",
                listOf(
                    R.drawable.img_estate_test_3,
                    R.drawable.img_estate_test_2,
                    R.drawable.img_estate_test_1
                ),
                "VALLET (44)",
                "",
                false,
                "28/07/2022",
                "",
                "Maurice Baudoin"
            )
        )
    }
}