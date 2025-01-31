# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.example.calorieapp.** { *; }
# Google Play Services
-keep class com.google.android.gms.** { *; }
-keep interface com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Firebase Authentication
-keep public class com.google.firebase.** { *; }
-keep class com.google.android.gms.internal.** { *; }
-keepclasseswithmembers class com.google.firebase.FirebaseException
-keepattributes Signature
-keepattributes *Annotation*

# Google Auth Library
-keep class com.google.api.client.** { *; }
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

# Prevent obfuscation of critical classes
-keepnames class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    *;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *

-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}