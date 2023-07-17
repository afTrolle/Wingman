-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**


# Drawables uses reflection here. R id must stay same as drawable.
-keep class **.ui.R
-keep class dev.trolle.af.wingman.ui.R$* {
    <fields>;
}