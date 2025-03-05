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

class AutoMigrationSpecFrom6to7 : AutoMigrationSpec, KoinComponent {

    private val applicationPersistence: ApplicationPersistence by inject()
    private val coroutineScope = MainScope()

    override fun onPostMigrate(connection: SQLiteConnection) {
        coroutineScope.launch {
            val jiraDomain = applicationPersistence.getCredentials()?.jiraDomain ?: run {
                println("User is not signed in, no migration of jira_issue is needed")
                return@launch
            }

            val issueBrowseUrlPrefix = "${Constants.JiraApiUrl(jiraDomain)}browse/"

            // For each entry in table 'jira_issue', take a 'key' field and set the field 'browse_url' to the string "https://example.com/browse/$key"
            connection.execSQL(
                "UPDATE jira_issue SET browse_url = '$issueBrowseUrlPrefix' || key"
            )
        }
    }
}
