package anthony.brenon.realestatemanager.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.repository.EstateRepository

class MainViewModel (private val estateRepository: EstateRepository) : ViewModel() {

    var estateSelected = MutableLiveData<Estate>()

    fun selectThisEstate(estate: Estate) { estateSelected.value = estate }
}