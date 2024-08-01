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

-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn javax.lang.model.SourceVersion
-dontwarn javax.lang.model.element.AnnotationMirror
-dontwarn javax.lang.model.element.AnnotationValue
-dontwarn javax.lang.model.element.AnnotationValueVisitor
-dontwarn javax.lang.model.element.Element
-dontwarn javax.lang.model.element.ElementKind
-dontwarn javax.lang.model.element.ElementVisitor
-dontwarn javax.lang.model.element.ExecutableElement
-dontwarn javax.lang.model.element.Modifier
-dontwarn javax.lang.model.element.Name
-dontwarn javax.lang.model.element.NestingKind
-dontwarn javax.lang.model.element.PackageElement
-dontwarn javax.lang.model.element.QualifiedNameable
-dontwarn javax.lang.model.element.TypeElement
-dontwarn javax.lang.model.element.TypeParameterElement
-dontwarn javax.lang.model.element.VariableElement
-dontwarn javax.lang.model.type.ArrayType
-dontwarn javax.lang.model.type.DeclaredType
-dontwarn javax.lang.model.type.ErrorType
-dontwarn javax.lang.model.type.ExecutableType
-dontwarn javax.lang.model.type.NoType
-dontwarn javax.lang.model.type.NullType
-dontwarn javax.lang.model.type.PrimitiveType
-dontwarn javax.lang.model.type.TypeKind
-dontwarn javax.lang.model.type.TypeMirror
-dontwarn javax.lang.model.type.TypeVariable
-dontwarn javax.lang.model.type.TypeVisitor
-dontwarn javax.lang.model.type.WildcardType
-dontwarn javax.lang.model.util.AbstractTypeVisitor6
-dontwarn javax.lang.model.util.ElementFilter
-dontwarn javax.lang.model.util.Elements
-dontwarn javax.lang.model.util.SimpleAnnotationValueVisitor7
-dontwarn javax.lang.model.util.SimpleElementVisitor6
-dontwarn javax.lang.model.util.SimpleTypeVisitor6
-dontwarn javax.lang.model.util.SimpleTypeVisitor7
-dontwarn javax.lang.model.util.Types
-dontwarn javax.tools.Diagnostic$Kind
-dontwarn javax.tools.FileObject
-dontwarn javax.tools.JavaFileManager$Location
-dontwarn javax.tools.SimpleJavaFileObject
-dontwarn javax.tools.StandardLocation
-dontwarn kotlin.Experimental
# -keep,allowobfuscation,allowshrinking interface retrofit2.Call
# -keep,allowobfuscation,allowshrinking class retrofit2.Response

# -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Add project specific ProGuard rules here.
# You can control the behavior of r8 with various options.
# For more details, see https://www.guardsquare.com/en/products/proguard/manual/usage

# Keep the entry point classes.
-keep class * {
    public static void main(java.lang.String[]);
}

# Keep Retrofit interfaces.
-if interface * { @retrofit2.http.* public *** *(...); }
 -keep,allowoptimization,allowshrinking,allowobfuscation class <3>
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
# Keep Moshi classes.
-keep class com.squareup.moshi.** { *; }

# Keep ViewModel classes.
-keep class androidx.lifecycle.ViewModel

# Keep Parcelable classes.
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep the classes for your model and data layer.
-keep class your.package.model.** { *; }
-keep class your.package.data.** { *; }

# Keep Lottie animation classes.
-keep class com.airbnb.lottie.** { *; }

# Keep Gson classes.
-keep class com.google.gson.** { *; }

# Keep kotlinx serialization classes.
-keep class kotlinx.serialization.** { *; }

# Keep Shimmer classes.
-keep class com.facebook.shimmer.** { *; }

# Keep Yandex Map classes.
-keep class ru.yandex.** { *; }
