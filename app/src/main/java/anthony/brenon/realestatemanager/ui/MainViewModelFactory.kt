package anthony.brenon.realestatemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import anthony.brenon.realestatemanager.repository.EstateRepository

class MainViewModelFactory(private val repository: EstateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(repository) as T
        throw IllegalArgumentException("Class ViewModel not found")
    }
}