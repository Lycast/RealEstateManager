package anthony.brenon.realestatemanager.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository
import anthony.brenon.realestatemanager.utils.NavigationStates

class MainViewModel (private val estateRepository: EstateRepository, private val agentRepository: AgentRepository) : ViewModel() {

    //val allAgent: LiveData<List<Agent>> = agentRepository.allAgent.asLiveData()
    var estateSelected = MutableLiveData<Estate>()
    var detailsNavigationStates = MutableLiveData<NavigationStates>()

    fun selectThisEstate(estate: Estate) { estateSelected.value = estate }
    fun selectDetailsStates(navigationStates: NavigationStates) { detailsNavigationStates.value = navigationStates }
}