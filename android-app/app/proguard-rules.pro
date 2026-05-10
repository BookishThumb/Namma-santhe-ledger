# Keep Room entities and DAOs
-keep class com.nammasanthe.ledger.data.db.** { *; }
-keepclassmembers class com.nammasanthe.ledger.data.db.** { *; }

# Keep enum names for TypeConverter
-keepclassmembers enum * { *; }

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}
