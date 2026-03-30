# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# play-services-code-scanner crashes with NPE when proguard is enabled.
# Can be removed once they provide a consumer proguard file themselfes.
# https://github.com/googlesamples/mlkit/issues/1018
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.internal.mlkit_code_scanner.** { *; }

# Error during compilation:
# Missing class com.google.android.gms.common.annotation.NoNullnessRewrite (referenced from: void com.google.android.play.core.ktx.ReviewManagerKtxKt
# https://issuetracker.google.com/issues/374691245
-dontwarn com.google.android.gms.common.annotation.**