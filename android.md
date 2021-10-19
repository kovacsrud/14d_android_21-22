# Android programozás Kotlin nyelven

## Az Android projekt felépítése

Egy Android projekt számos fájlból áll. Egy új projekt készítése esetén egy MainActivity.kt fájl fogad minket, ez a programnak a kód része.

Nézzük a projekt struktúrát:

**Manifests/AndroidManifest.xml**: Ez a fájl tartalmazza az appunk fontosabb beállításait

**Java/com.raz.elso/MainActivity.kt**: Ez a fájl az activity kódja. Ide kerülnek a további kódok, pl. osztályok stb.

**Res mappa**: A Res mappa tartalmazza azokat az elemeket, amelyek az app megjelenésével kapcsolatosak.

**Res/layout**: Az activity kinézetét leíró XML fájlok találhatóak itt.

**Res/drawable**: Ide tesszük azokat a grafikus elemeket, amelyeket az activityben felhasználunk

**Res/values:**: Itt találhatóak azok a fájlok, amelyek tárolják az appban megjelenő szövegeket, értékeket, pl. méretek, színek stb. Az Android nagyon szorgalmazza, hogy ne legyen ún. hard-coded, azaz a layoutba bedrótozott szöveg az appban.

**Res/values/themes**: Az alkalmazásban használt témák találhatóak itt.

## Navigáció megvalósítása Android programban

A legtöbb Android program nem pusztán egy képernyőből áll. Amikor több képernyőt használunk, akkor szükség van a képernyők közötti navigáció megvalósítására. 
Első lépésként a gradle fájlokhoz hozzá kell adni a szükséges függőségeket.
Gradle Scripts-en belül meg kell nyitni a **build.gradle(Project...)** fájlt, és abba a következőket beírni a dependencies részhez:
```Kotlin
dependencies {
        ...
        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5"
        
    }
```
Ezt követően meg kell nyitni a **build.gradle(Module..)** fájlt és abba a következőket megadni a **plugins** részen belül:
A **...** azokat a részeket jelenti amelyek eleve benne vannak, tehát nem kell három pontot beírni!!!!!

```Kotlin
plugins {
    ...
    id 'kotlin-kapt'
    id 'androidx.navigation.safeargs' 
}
```

A függőségek részhez a következőket kell hozzáadni:

```Kotlin
dependencies {
        ...
        implementation "androidx.navigation:navigation-fragment-ktx:2.3.5"
        implementation "androidx.navigation:navigation-ui-ktx:2.3.5" 
}
```
A **Sync** megnyomása után készen állunk a navigáció megvalósítására.

Alakítsuk ki a fragmentek megfelelő layoutjait (gombok, textviewk stb)

Hozzuk létre a navigációt megvalósító resource fájlt, a **Res(jobb gomb)->New->Android Resource File** paranccsal.
**Resource type**-nak válasszuk a **Navigation**-t. Adjunk neki valami értelmes nevet (pl. navigation).

A mainActivity layout fájljába tegyünk egy fragment elemet:
Amit a működő navigációhoz meg kell adni:
 - id
 - name
 - app:defaultNavHost
 - app:navGraph

```Kotlin
  <fragment
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost='true'
        app:navGraph='@navigation/navigation'

  />
```
