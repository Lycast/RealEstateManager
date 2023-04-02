package anthony.brenon.realestatemanager.repository

import androidx.annotation.WorkerThread
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.database.dao.AgentDAO
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AgentRepository(private val agentDAO: AgentDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allAgents: Flow<List<Agent>> = agentDAO.getAllAgents()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAgent(agent: Agent) {
        agentDAO.insert(agent)
    }
}