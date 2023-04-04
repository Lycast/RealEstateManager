package anthony.brenon.realestatemanager.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture
import anthony.brenon.realestatemanager.database.dao.AgentDAO
import anthony.brenon.realestatemanager.database.dao.EstateDAO
import anthony.brenon.realestatemanager.database.dao.PictureDAO
import anthony.brenon.realestatemanager.utils.PictureConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Agent::class, Estate::class, Picture::class], version = 1, exportSchema = false)
@TypeConverters(PictureConverter::class)
abstract class REMRoomDatabase : RoomDatabase() {

    abstract fun agentDao() : AgentDAO
    abstract fun estateDao() : EstateDAO
    abstract fun pictureDao() : PictureDAO

    private class REMDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateAgentDatabase(database.agentDao())
                    populateEstateDatabase(database.estateDao())
                }
            }
        }

        suspend fun populateAgentDatabase(agentDAO: AgentDAO) {
            // Delete all content here.
            agentDAO.deleteAll()
            // Add sample agent.
            val agent = Agent("Paul", "paul.practice@gmail.com")
            agentDAO.insert(agent)
        }

        suspend fun populateEstateDatabase(estateDAO: EstateDAO) {
            // Delete all content here.
            estateDAO.deleteAll()
            // Add sample estate.
            val estate = Estate("Castle", "48 879 000",1450,48,"This one estate is a castle !!","Paris","Close to the Tour Eiffel",
            false,"12 july 2022",null,"paul")
            estateDAO.insert(estate)
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: REMRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ) : REMRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    REMRoomDatabase::class.java,
                    "database_rem")
                    .addCallback(REMDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}