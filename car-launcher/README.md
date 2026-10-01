# Seyir — Araç Launcher

Android araç head-unit'leri için bir ana ekran (launcher). Büyük, sürüş sırasında
tek bakışta vurulabilecek dokunmatik kutucuklar, büyük bir saat/tarih, gece dostu
koyu bir tema ve cihazdaki tüm uygulamaları gösteren bir uygulama çekmecesi.

Native Android projesidir (Kotlin + Android Views), Android Studio'da açılır.

## Ana ekran

Sekiz kutucuk, cihazda hangi uygulama varsayılansa ona açılır:

| Kutucuk | Ne açar |
|---|---|
| **Navigasyon** | Varsayılan harita/navigasyon (`geo:` → Maps, Waze, Yandex, Sygic, TomTom…) |
| **Müzik** | Varsayılan müzik uygulaması (`CATEGORY_APP_MUSIC`) + Spotify/YT Music yedeği |
| **Telefon** | Çevirici (`ACTION_DIAL`) |
| **Radyo** | Head-unit FM radyo uygulaması (MTK/QCOM/Spreadtrum paketleri denenir) |
| **Bluetooth** | Bluetooth ayarları |
| **Kamera** | Kamera / geri görüş (`STILL_IMAGE_CAMERA`) |
| **Uygulamalar** | Uygulama çekmecesi |
| **Ayarlar** | Sistem ayarları |

Her kutucuk sırayla birkaç adayı dener; uygun uygulama yoksa çökmek yerine tek
satırlık bir uyarı gösterir.

## Özellikler

- **Sürüş dostu**: büyük hedefler, yüksek kontrast, 24 saat biçiminde saat, Türkçe
  gün/ay adları (cihaz diline göre).
- **Yatay kilitli** ve **immersive** (sistem çubukları gizli) — head-unit için.
- **Uygulama çekmecesi**: kurulu tüm uygulamalar, ada göre sıralı; uzun basınca
  uygulamanın sistem bilgi ekranı açılır. Sütun sayısı ekran genişliğine göre ayarlanır.
- **Varsayılan yap**: başka launcher aktifken üstte "Varsayılan yap" düğmesi çıkar ve
  sistemin ana ekran seçim ekranına götürür.
- **İzin istemez**: `QUERY_ALL_PACKAGES` yok; uygulama listesi için Android 11+ uyumlu
  `<queries>` bloğu kullanılır.

## Derleme

Android Studio (Koala / 2024.1+) ile `car-launcher/` klasörünü aç ve çalıştır, ya da:

```sh
cd car-launcher
./gradlew assembleDebug        # APK: app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug         # bağlı cihaza/head-unit'e kurar
```

- `compileSdk / targetSdk 35`, `minSdk 26` (Android 8.0+)
- Kotlin 2.0, AGP 8.7, Gradle 8.9 (wrapper dahil)
- İlk derlemede Gradle dağıtımı ve bağımlılıklar indirilir (internet gerekir).

## Varsayılan launcher yapma

Head-unit'te: **Ayarlar → Uygulamalar → Varsayılan uygulamalar → Ana ekran
uygulaması → Seyir**. Uygulama içindeki "Varsayılan yap" düğmesi de doğrudan bu
ekrana götürür.

## Özelleştirme

- **Kutucuklar / hedef uygulamalar**: `app/src/main/java/.../Launch.kt` içindeki paket
  listelerini düzenle.
- **Renkler / tema**: `res/values/colors.xml`.
- **İsim / etiketler**: `res/values/strings.xml` (uygulama adı `Seyir`).
- **Düzen**: `res/layout/activity_main.xml` (kutucuk sayısı/yerleşimi).

Paket adı `com.specialprojects.seyir`.
