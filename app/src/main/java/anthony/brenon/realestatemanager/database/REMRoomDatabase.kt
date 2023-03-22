package anthony.brenon.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.dao.AgentDAO
import anthony.brenon.realestatemanager.models.dao.EstateDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Agent::class, Estate::class], version = 1, exportSchema = false)
abstract class REMRoomDatabase : RoomDatabase() {

    abstract fun agentDao() : AgentDAO
    abstract fun estateDao() : EstateDAO

    private class AgentDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {

                // Agent injection
                scope.launch {
                    val agentDAO = it.agentDao()

                    // Delete all content here.
                    agentDAO.deleteAll()
                    // Add example account 1.
                    var agent = Agent(0,"Paul","paul@paul.com")
                    agentDAO.insert(agent)
                    // Add example account 2.
                    agent = Agent(0,"Maurice","maurice@maurice.com")
                    agentDAO.insert(agent)
                    // Add example account 3.
                    agent = Agent(0,"Jack","jack@jack.com")
                    agentDAO.insert(agent)
                }

                // Estate injection
                scope.launch {
                    val estateDAO = it.estateDao()

                    // Delete all content here.
                    estateDAO.deleteAll()
                    // Add example account 1.
                    var estate = Estate(0,"House",41480000, 750,8,"description too long"
//                        , listOf(
//                        R.drawable.img_estate_test_1,
//                        R.drawable.img_estate_test_2,
//                        R.drawable.img_estate_test_3)
                        ,"New York","idk",false,"sale date","date sale","maurice")
                    estateDAO.insert(estate)
                    // Add example account 2.
                    estate = Estate(0,"Flat",17870000, 50,12,"description too long"
//                        , listOf(
//                        R.drawable.img_estate_test_1,
//                        R.drawable.img_estate_test_2,
//                        R.drawable.img_estate_test_3)
                        ,"Manhattan","idk",false,"sale date","date sale","paul")
                    estateDAO.insert(estate)
                    // Add example account 3.
                    //estate = Estate(0,"Jack","jack@jack.com")
                    //estateDAO.insert(estate)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: REMRoomDatabase? = null

        fun getAgentDatabase(
            context: Context,
            scope: CoroutineScope
        ) : REMRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    REMRoomDatabase::class.java,
                    "agent_database")
                    .addCallback(AgentDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}