-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
#-keep class dev.trolle.af.wingman.ui.R.**
#-keepnames class dev.trolle.af.wingman.ui.R.**
#-keepclasseswithmembers class dev.trolle.af.wingman.ui.R.**

#-keepattributes InnerClasses dev.trolle.af.wingman.ui.R.**

-keep class **.ui.R
-keep class dev.trolle.af.wingman.ui.R$* {
    <fields>;
}