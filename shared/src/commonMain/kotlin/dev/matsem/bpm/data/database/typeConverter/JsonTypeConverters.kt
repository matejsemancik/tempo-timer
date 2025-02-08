package dev.matsem.bpm.data.database.typeConverter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json

internal class JsonTypeConverters {

    // When implemented using @ProvidedTypeConverter according to documentation, the runtime error is thrown,
    // hence the local dependency on Json.
    // Ref: https://stackoverflow.com/questions/77383255/android-room-manually-provided-typeconverter-with-providedtypeconverter-throws
    private val json = Json {
        encodeDefaults = true
        isLenient = true
        prettyPrint = false
        ignoreUnknownKeys = true
        explicitNulls = true
        coerceInputValues = true
    }

    @TypeConverter
    fun instantToJson(instant: Instant): String =
        json.encodeToString(Instant.serializer(), instant)

    @TypeConverter
    fun jsonToInstant(string: String): Instant =
        json.decodeFromString(Instant.serializer(), string)
}