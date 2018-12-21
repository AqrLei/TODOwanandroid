# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Admin\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-dontskipnonpubliclibraryclasses #不去忽略非公共的库类
-dontoptimize        #优化  不优化输入的类文件
-dontpreverify    #预校验
-ignorewarning   #忽略警告
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

##记录生成的日志数据,gradle build时在本项目根目录输出##

-dump proguard/class_files.txt  #apk 包内所有 class 的内部结构
-printseeds proguard/seeds.txt  #未混淆的类和成员
-printusage proguard/unused.txt #列出从 apk 中删除的代码
-printmapping proguard/mapping.txt #混淆前后的映射
##记录生成的日志数据，gradle build时在本项目根目录输出##

#kotlin代码混淆
-dontwarn kotlin.**
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}


-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Fragment      # 保持哪些类不被混淆
-keep public class * extends android.support.v4.app.Fragment    # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

-dontwarn android.support.**  #如果引用了v4或者v7包

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class **.R$* {  #不混淆资源类
    public static <fields>;
}
-keep public class * extends android.view.View { # 保持自定义控件类不被混淆
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepnames class * implements java.io.Serializable #保持 Serializable 不被混淆

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

-keepattributes *Annotation* #保护注解


-keepclassmembers class * { # Keep native methods
    native <methods>;
}

#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-dontwarn com.google.gson.**
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *;}


