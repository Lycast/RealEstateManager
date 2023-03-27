package anthony.brenon.realestatemanager.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.dao.AgentDAO
import anthony.brenon.realestatemanager.models.dao.EstateDAO
import anthony.brenon.realestatemanager.utils.PictureConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Agent::class, Estate::class], version = 1, exportSchema = false)
@TypeConverters(PictureConverter::class)
abstract class REMRoomDatabase : RoomDatabase() {

    abstract fun agentDao() : AgentDAO
    abstract fun estateDao() : EstateDAO

    private class AgentDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {

                // agent injection
                scope.launch {
                    val agentDAO = it.agentDao()

                    // Delete all content here.
                    agentDAO.deleteAll()
                    // Add example account 1.
                    var agent = Agent("Paul","paul@paul.com")
                    agentDAO.insert(agent)
                    // Add example account 2.
                    agent = Agent("Maurice","maurice@maurice.com")
                    agentDAO.insert(agent)
                    // Add example account 3.
                    agent = Agent("Jack","jack@jack.com")
                    agentDAO.insert(agent)
                }

//                // estate injection
//                scope.launch {
//                    val estateDAO = it.estateDao()
//                    val resourcesInit = Resources.getSystem()
//                    val imageBitmap = BitmapFactory.decodeResource(resourcesInit, R.drawable.img_estate_test_2)
//
//                    // load bitmap image
//                    val pictureConverter = PictureConverter()
//
//                    // Delete all content here.
//                    estateDAO.deleteAll()
//                    // Add example estate 1.
//                    //var image = pictureConverter.compressImage(pictureConverter.fromBitmap(pictures.pictures[0]))
//                    var estate = Estate("House",41480000, 750,8,"description too long"
//                        ,pictureConverter.fromBitmap(imageBitmap)
//                        ,"New York","idk",false,"sale date","date sale","maurice")
//                    estateDAO.insert(estate)
//                    // Add example estate 2.
//                    //image = pictureConverter.compressImage(pictureConverter.fromBitmap(pictures.pictures[1]))
//                    estate = Estate("Flat",17870000, 500,12,"description too long"
//                        ,pictureConverter.fromBitmap(imageBitmap)
//                        ,"Manhattan","idk",false,"sale date","date sale","paul")
//                    estateDAO.insert(estate)
//                }
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