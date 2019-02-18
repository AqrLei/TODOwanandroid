-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-dontskipnonpubliclibraryclasses #不去忽略非公共的库类
-dontoptimize        #优化  不优化输入的类文件
##-dontpreverify    #不进行预校验,可加快混淆速度。 Android 不需要
-ignorewarning   #忽略警告
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

##记录生成的日志数据,gradle build时在本项目根目录输出##

-dump proguard/class_files.txt  #apk 包内所有 class 的内部结构
-printseeds proguard/seeds.txt  #未混淆的类和成员
-printusage proguard/unused.txt #列出从 apk 中删除的代码
-printmapping proguard/mapping.txt #混淆前后的映射
##记录生成的日志数据，gradle build时在本项目根目录输出##

-keepattributes *Annotation*  #保护代码中的Annotation不被混淆
-keepattributes Signature #避免混淆泛型，JSON实体映射时很重要
-keepattributes SourceFile,LineNumberTable #抛出异常时保留代码行号

#kotlin代码混淆
-dontwarn kotlin.**
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}


-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Fragment      # 保持哪些类不被混淆
-keep public class * extends androidx.fragment.app.Fragment    # 保持哪些类不被混淆
-keep public class * extends android.support.v4.app.Fragment    # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆

#-keep public class com.google.vending.licensing.ILicensingService #接入谷歌原生服务时有用
#-keep public class com.android.vending.licensing.ILicensingService

-dontwarn android.support.**  #如果引用了v4或者v7包

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

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

-keepclassmembers class * { # Keep native methods
    native <methods>;
}

#aqrlei
-dontwarn com.aqrlei.open.**
-keepnames class com.aqrlei.open.** { *;}

-keep class com.aqrlei.app.open.todowanandroid.net.** { *;}
-keep class com.aqrlei.app.open.todowanandroid.binding.** { *;}

#androidx
-dontwarn androidx.**
-keepnames class androidx.** { *; }

-keepnames class android.arch.** { *; }

#retrofit2
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn com.google.gson.**
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *;}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}