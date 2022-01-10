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


-keepclassmembers class ** {
@org.greenrobot.eventbus.Subscribe <methods>;
}

-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends     org.greenrobot.eventbus.util.ThrowableFailureEvent {
<init>(java.lang.Throwable);
}

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature, *Annotation*, EnclosingMethod
-keep class !android.support.v7.internal.view.menu.**,android.support.** {*;}

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Application classes that will be serialized/deserialized over Gson
-keep public class com.yoursource.yoursource.app.data.models.** { *; }

# removing warnings
-dontwarn org.apache.**
-dontwarn android.net.**
-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn javax.sound.**
-dontwarn oauth.signpost.**
-dontwarn java.nio.file.**
-dontwarn org.mozilla.javascript.**
-dontwarn org.codehaus.mojo.**
-dontwarn javax.servlet.**
# picassa library
-dontwarn com.squareup.okhttp.**
-dontwarn okio.

# google
-dontwarn com.google.android.gms.*
-dontwarn com.google.android.libraries.places.internal.*
-dontwarn com.google.j2objc.annotations.*

#rush orm
-keep public class * implements co.uk.rushorm.core.Rush { *; }

# keep source file info
-renamesourcefileattribute SourceFile
-keepattributes SourceFile, LineNumberTable
-printmapping mapping.txt

# explicitly preserve all serialization members. The serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# logs
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
# Avi loader
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }
