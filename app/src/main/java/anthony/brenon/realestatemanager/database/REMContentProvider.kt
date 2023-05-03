package anthony.brenon.realestatemanager.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import anthony.brenon.realestatemanager.database.dao.AgentDAO
import anthony.brenon.realestatemanager.database.dao.EstateDAO

class REMContentProvider : ContentProvider() {
    // Define constants for the URIs used in the content provider
    companion object {
        const val AUTHORITY = "com.example.remprovider"
        const val AGENT_PATH = "agent"
        const val ESTATE_PATH = "estate"
        const val PICTURE_PATH = "picture"
        val AGENT_CONTENT_URI = Uri.parse("content://$AUTHORITY/$AGENT_PATH")
        val ESTATE_CONTENT_URI = Uri.parse("content://$AUTHORITY/$ESTATE_PATH")
        val PICTURE_CONTENT_URI = Uri.parse("content://$AUTHORITY/$PICTURE_PATH")

        // Define constants for the different types of URIs used in the content provider
        private const val AGENT = 1
        private const val AGENT_ID = 2
        private const val ESTATE = 3
        private const val ESTATE_ID = 4
        private const val PICTURE = 5
        private const val PICTURE_ID = 6

        // Define the MIME types for the different types of data that the provider can return
        private const val AGENT_MIME_TYPE_DIR = "vnd.android.cursor.dir/vnd.example.agent"
        private const val AGENT_MIME_TYPE_ITEM = "vnd.android.cursor.item/vnd.example.agent"
        private const val ESTATE_MIME_TYPE_DIR = "vnd.android.cursor.dir/vnd.example.estate"
        private const val ESTATE_MIME_TYPE_ITEM = "vnd.android.cursor.item/vnd.example.estate"
        private const val PICTURE_MIME_TYPE_DIR = "vnd.android.cursor.dir/vnd.example.picture"
        private const val PICTURE_MIME_TYPE_ITEM = "vnd.android.cursor.item/vnd.example.picture"

        // Define the URI matcher used by the content provider
        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            URI_MATCHER.addURI(AUTHORITY, AGENT_PATH, AGENT)
            URI_MATCHER.addURI(AUTHORITY, "$AGENT_PATH/#", AGENT_ID)
            URI_MATCHER.addURI(AUTHORITY, ESTATE_PATH, ESTATE)
            URI_MATCHER.addURI(AUTHORITY, "$ESTATE_PATH/#", ESTATE_ID)
            URI_MATCHER.addURI(AUTHORITY, PICTURE_PATH, PICTURE)
            URI_MATCHER.addURI(AUTHORITY, "$PICTURE_PATH/#", PICTURE_ID)
        }
    }

    // Define the database helper and DAOs used by the provider
    private lateinit var database: REMRoomDatabase
    private lateinit var agentDao: AgentDAO
    private lateinit var estateDao: EstateDAO

    // Initialize the provider
    override fun onCreate(): Boolean {
//        database = REMRoomDatabase.getDatabase(context!!)
//        agentDao = database.agentDao()
//        estateDao = database.estateDao()
//        pictureDao = database.pictureDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}