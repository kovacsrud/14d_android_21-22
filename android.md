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
Nyissuk meg a navigation.xml-t. Itt már látható(ha minden jó lett csinálva), hogy megvan a navhostfragment.
Adjuk hozzá a navigációhoz az A, majd a B fragmentet. Az A fragment oldalán lévő kis kört meg kell fogni, és húzni a B-re, majd a b fölött elengedni. Ennek hatására legenerálódik automatikusan a két fragment közötti váltást megoldó metódus.

Nyissuk meg azt a fragmentet (egész pontosan a forráskódját), amelyikről navigálni akarunk, ez most az AFragment.
Ha a viewFindById-t akarjuk használni, akkor az nem lehet az OnCreateView metódusban megtenni, mert kivételt okoz fragment esetében, ezért ezeket a tevékenységeket az onViewCreated metódusban kell megtenni.
A CTRL-O lenyomásával lehet megnyitni a felülírható metódusok listáját.
Így nézzen ki ez a metódus (természetesen csak akkor jó, ha a Button id-je gomb!!!):
```Kotlin
 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button: Button =requireView().findViewById(R.id.gomb)
        val navController=this.findNavController()

        button.setOnClickListener {
            navController.navigate(AFragmentDirections.actionAFragmentToBFragment())
        }

    }
```

A visszafelé irányuló navigációhoz a MainActivity megnyitása szükséges.
Az OnCreate metódusba kell a következő két sor:
```Kotlin
val navController=this.findNavController(R.id.nav_host_fragment)
NavigationUI.setupActionBarWithNavController(this,navController)
```
Ezt követően felül kell írni az onSupportNavigateUp metódust, a metódusba a következő kód kerüljön:
```Kotlin
 val navController=this.findNavController(R.id.nav_host_fragment)
 return navController.navigateUp() 
```
A teljes metódus tehát így néz ki:
```Kotlin
  override fun onSupportNavigateUp(): Boolean {
        
        val navController=this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    } 
```

## Navigáció létrehozása menüvel

Első lépésben készíteni kell egy menüt. 
**Res->New->Android Resource File**-ra kattintsunk. A megnyíló ablakban a **Resource Type** legyen **menu**.
A name legyen:**opmenu**

Adjunk hozzá egy menuitem-et! A **Title** legyen **B elem**, az **id** pedig **BFragment**

A menü xml kódja most így néz ki:
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/BFragment"
        android:title="B elem" />
</menu>
```

### A szükséges kódok

Az AFragment fogja megvalósítani a menüt, ennek a kódját kell megnyitni.

Az OnCreateView metódushoz adjuk hozzá a következő sor:
```kotlin
setHasOptionsMenu(true)
```
Ezután két metódust kell felülírni, használjuk a CTRL-O kombinációt.
Az első az onCreateOptionsMenu, ehhez a következő sort kell hozzáadni:
```kotlin
        inflater?.inflate(R.menu.opmenu,menu)
```

Ez a parancs végzi a menü 'felfújását', azaz létrehozását.

```kotlin
 override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.opmenu,menu)
    }
```
A következő az OnOptionsItemSelected felülírása:

Ebbe a metódusba a következő sor kerül:
```kotlin
   return NavigationUI.onNavDestinationSelected(item,requireView().findNavController())
```
Ezt egy or (||) logikai kapcsolattal kötjük össze az ős osztály metódusának hívásával.

```kotlin
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,requireView().findNavController()) ||
        return super.onOptionsItemSelected(item)
    }
```
