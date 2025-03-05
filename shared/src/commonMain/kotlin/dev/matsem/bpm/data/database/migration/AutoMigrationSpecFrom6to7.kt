package dev.matsem.bpm.data.database.migration

import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import dev.matsem.bpm.data.persistence.ApplicationPersistence
import dev.matsem.bpm.tooling.Constants
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * A class that handles the automatic migration from version 6 to version 7 of the database schema.
 */
class AutoMigrationSpecFrom6to7 : AutoMigrationSpec, KoinComponent {

    private val applicationPersistence: ApplicationPersistence by inject()
    private val coroutineScope = MainScope()

    /**
     * Called after the migration process is complete.
     * Updates the 'browse_url' field in the 'jira_issue' table based on the 'key' field.
     *
     * @param connection The SQLite connection to the database.
     */
    override fun onPostMigrate(connection: SQLiteConnection) {
        coroutineScope.launch {
            val jiraDomain = applicationPersistence.getCredentials()?.jiraDomain ?: run {
                println("User is not signed in, no migration of jira_issue is needed")
                return@launch
            }

            val issueBrowseUrlPrefix = "${Constants.JiraApiUrl(jiraDomain)}browse/"
            connection.execSQL(
                "UPDATE jira_issue SET browse_url = '$issueBrowseUrlPrefix' || key"
            )
        }
    }
}
