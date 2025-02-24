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
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
   <methods>;
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable

# Obfuscate Kotlin Metadata to prevent easy reverse engineering
-keepclassmembers,allowobfuscation class * {
    @kotlin.Metadata *;
}
-keep class android.security.keystore.** { *; }
-keep class * implements java.lang.reflect.InvocationHandler { *; }
-keep class * extends java.lang.reflect.Method { *; }
# General obfuscation enhancements
-repackageclasses ''
-flattenpackagehierarchy

# Suppress warnings for Java Management classes
-dontwarn java.lang.management.**
-dontwarn javax.management.**

# Suppress warnings for BouncyCastle classes
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.bouncycastle.jsse.provider.**

# Suppress warnings for Conscrypt classes
-dontwarn org.conscrypt.**

# Suppress warnings for Joda-Time conversion classes
-dontwarn org.joda.convert.**

# Suppress warnings for OpenJSSE classes
-dontwarn org.openjsse.**
-keep class androidx.work.** { *; }
-keep class androidx.work.impl.** { *; }