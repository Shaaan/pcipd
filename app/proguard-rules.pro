# Add project specific ProGuard rules here.

# Keep annotations & signature attributes required by Firebase / Kotlin Reflection
-keepattributes Signature, *Annotation*, InnerClasses, EnclosingMethod

# Keep @Keep annotated classes
-keep @androidx.annotation.Keep class * { *; }
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Keep Firebase Realtime Database Deserializer
-keep class com.google.firebase.database.DataSnapshot { *; }

# Keep all app data models used for Firebase deserialization
-keep class in.shaaan.pcipharmd.models.** { *; }
-keepclassmembers class in.shaaan.pcipharmd.models.** {
    public <init>();
    public <fields>;
    public <methods>;
}

# Keep YearData inside SyllabusRepository
-keep class in.shaaan.pcipharmd.repository.SyllabusRepository$YearData { *; }
-keepclassmembers class in.shaaan.pcipharmd.repository.SyllabusRepository$YearData {
    public <init>();
    public <fields>;
    public <methods>;
}

# Keep AdMob Native Ad Custom Views
-keep class com.google.android.gms.ads.nativead.NativeAdView { *; }
-keep class com.google.android.gms.ads.nativead.MediaView { *; }

-dontnote com.google.**
