package anthony.brenon.realestatemanager.ui

import androidx.lifecycle.*
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.launch

class MainViewModel(private val agentRepository : AgentRepository, private val estateRepository : EstateRepository) : ViewModel() {

    var estateSelected = MutableLiveData<Estate>()
    var agentSelected = MutableLiveData<Agent>()

    fun selectThisEstate(estate: Estate) { estateSelected.value = estate }
    fun selectThisAgent(agent: Agent) { agentSelected.value = agent }


    // ROOM
    // - Repository is completely separated from the UI through the ViewModel.
    val allAgents: LiveData<List<Agent>> = agentRepository.allAgents.asLiveData()
    val allEstates: LiveData<List<Estate>> = estateRepository.allEstates.asLiveData()

    fun getPicturesByEstate(estateId: Int) : LiveData<List<Picture>> { return estateRepository.getPicturesByEstate(estateId).asLiveData() }

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insertAgent(agent: Agent) = viewModelScope.launch {
        agentRepository.insertAgent(agent)
    }

    fun insertEstate(estate: Estate) = viewModelScope.launch {
        estateRepository.insertEstate(estate)
    }
//    fun deleteEstate(estate: Estate) = viewModelScope.launch {
//        estateRepository.deleteEstate(estate)
//    }
//
    fun insertPicture(picture: Picture) = viewModelScope.launch {
        estateRepository.insertPicture(picture)
    }
//    fun deletePicture(picture: Picture) = viewModelScope.launch {
//        estateRepository.deletePicture(picture)
//    }
}