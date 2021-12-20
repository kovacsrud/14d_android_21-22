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

### Adat küldése egy adott fragmentnek

Gyakran van arra szükség, hogy adatot, vagy adatokat juttassunk át egy fragment részére. Azért hogy ez érték -és típusbiztosan történjen, az Android a safeargs plugint alkalmazza.

 - Első lépésben meg kell nyitni a navigációt, azaz a navigation.xml-t
 - Ki kell választani azt a fragmentet, amelyiknek adatot akarunk átadni. (Ez most a BFragment)
 - Keressük meg az Attributes-nél az Arguments részt.
 - Nyomjuk meg a + ,a name mezőbe írjuk be, hogy sendAdat. 
 - A típusnak String-et válasszunk.

A BFragment kódjának az **OnViewCreated** metódusába tegyük a következő kódot:
```Kotlin
  var args=BFragmentArgs.fromBundle(requireArguments()) 
```
A Textview szövegét így tudjuk megváltoztatni:
```Kotlin
szoveg.text=args.sendData 
```

Ne feledjük, hogy az AFragmentnél a SetOnClickListenerben, most már át kell adnunk szöveget paraméterként!

```kotlin
button.setOnClickListener {
            navController.navigate(AFragmentDirections.actionAFragmentToBFragment("Végre péntek!"))
        }
```

## Navigation Drawer készítése

Az első lépés egy újabb menü készítése, amit majd a Navigation Drawer használni fog. A menü neve legyen **drawermenu**
Egy menüpontot vegyünk fel, ami a CFragment-re navigál, a Title legyen C elem.

Ezt követően módosítani kell a MainActivity layoutját.
A felépítés a következő lesz:
A kezdő tag <layout> lesz. Ebbe kerül az **androidx.drawerlayout.widget.DrawerLayout**, ez fogja magába foglalni a
ConstraintLayout-ot, valamint a ConstraintLayout után egy NavigationView taget.
Így fog kinézni:
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

   <androidx.drawerlayout.widget.DrawerLayout
       android:id="@+id/drawerLayout"
       android:layout_width="match_parent"
       android:layout_height="match_parent">

     <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

            <fragment
              android:id="@+id/nav_host_fragment"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:name="androidx.navigation.fragment.NavHostFragment"
              app:defaultNavHost='true'
              app:navGraph='@navigation/navigation'


            />

      </androidx.constraintlayout.widget.ConstraintLayout>

       <com.google.android.material.navigation.NavigationView
           android:id="@+id/navView"
           android:layout_width="wrap_content"
           android:layout_height="match_parent"
           android:layout_gravity="start"
           app:menu="@menu/drawermenu"
           />


   </androidx.drawerlayout.widget.DrawerLayout>

    </layout>        
```
        
A MainActivity-be a következő kódok kellenek:
        
Az OnCreate fölé a következő deklarációk:
```kotlin
 private lateinit var drawerLayout: DrawerLayout
 private lateinit var appBarConfiguration: AppBarConfiguration
```        
        
Az OnCreate-be:
```kotlin
val navController=this.findNavController(R.id.nav_host_fragment)
drawerLayout=findViewById(R.id.drawerLayout)
val navigationView:NavigationView=findViewById(R.id.navView)
NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        
appBarConfiguration= AppBarConfiguration(navController.graph,drawerLayout)
NavigationUI.setupWithNavController(navigationView, navController)        
        
``` 
Az OnSupportNavigateUP() is módosul, navController.navigateUp helyett NavigationUI.navigateUp lesz:
```kotlin
   val navController=this.findNavController(R.id.nav_host_fragment)
   //return navController.navigateUp()
   return NavigationUI.navigateUp(navController,drawerLayout)        
```        
## Az Activity-k életciklusa
Egy Android program alapvetően a következő 4 állapot valamelyikében lehet:
 - Running - Az alkalmazás az előtérben fut
 - Paused - Az alkalmazás bár látható, de elvesztette a fókusz, szünetel
 - Stopped - Ha egy másik tevékenység teljes egészében letakarja, akkor leáll, de minden állapot és taginformációja megőrződik
 - Finished/killed - Szünetelő, vagy leállított Activity esetében az operációs rendszer dönthet úgy, hogy az Activity-t befejezi, vagy  egyszerűen kitörli. Ilyenkor áll be ez az állapot.
        
Láttuk hogy a programban deklarált változók nem élik túl az életciklus változásait pl. elfordítás. Ha az adatokat szeretnénk megőrizni, akkor ViewModel-t kell használni.
        
## ViewModel használata az alkalmazásban
Készítsünk egy egyszerű alkalmazást, amely viewmodelt használ. Az alkalmazás csak annyit fog tudni, hogy egy értéket tudunk majd növelni, vagy csökkenteni.
        
Először adjuk hozzá a szükséges függőségeket az alkalmazáshoz, valamint állítsuk be az adatkötés használatát. Mindkettőt a build.gradle(Module:..) fájlban.
Adatkötés használata:
```kotlin
 buildFeatures {
    dataBinding true
 }
```
Lifecycle extension
```kotlin
implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'        
```        
        
Hozzunk létre egy új osztályt **PracticeViewModel** néven!
```kotlin
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PracticeViewModel: ViewModel() {
    var adatErtek=MutableLiveData<Int>()

    init {
        adatErtek.value=0
    }

    fun Novel(){
        adatErtek.value=adatErtek.value?.plus(1)

    }

    fun Csokkent(){
        adatErtek.value=adatErtek.value?.minus(1)
    }
}
```        
A MainActivityben deklaráljuk az adatkötést, és a viewmodel-t
```kotlin
private lateinit var binding:ActivityMainBinding
private lateinit var viewModel: PracticeViewModel
```        
Ezt követően értéket is kell adni nekik.
A databinding a szokásos módon megy:
```kotlin
binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
```        
A viewmodel példányosítása nem a megszokott módon megy, ugyanis itt a példányt egy ViewModelProvider-től kell elkérni:
```kotlin
 viewModel=ViewModelProvider(this).get(PracticeViewModel::class.java)
```
Beállítjuk a bindingot:
```kotlin
 binding.viewmodel=viewModel
```
Valamint az ún. lifecycleownert
```kotlin
binding.setLifecycleOwner(this)
```
Az activity_main.xml-ben a következőket kell megtenni:
Ne feledjük a layout-ot data binding layout-ra konvertálni!
Be kell állítani, hogy milyen osztályt akarunk bindingolni:
```xml
 <data>
        <variable
            name="viewmodel"
            type="com.razgon.viewmodelpractice.PracticeViewModel" />

</data>
```
Figyelem, a type mindenkinél egyéni lehet!!!
Az értéket megjelenítő TextView text tulajdonságához kötjük be a viewmodel-ben lévő értéket:
```xml
android:text="@{viewmodel.adatErtek.toString()}"        
```
A gombokhoz pedig a viewmodelben definiált függvényeket.
```xml
android:onClick="@{()->viewmodel.Novel()}"
```
A másik gombhoz a másik függvényt, értelemszerűen.
        
        
## ViewModelFactory használata

Az egyszerű viewmodel nem ad lehetőséget arra, hogy kezdőértéket adjunk át neki. Ha erre van szükség, akkor ViewModelFactory-t kell használni.
Először is módosítsuk a ViewModel-t hogy tudjon fogadni bejövő paramétert. Az ertek.value most a bejövő paramétert kapja meg.
```kotlin
class PracticeViewModel(beErtek:Int):ViewModel() {

    var ertek=MutableLiveData<Int>()
    init {
        ertek.value=beErtek
    }

    fun Novel(){
        ertek.value=ertek.value?.plus(1)
    }
    fun Csokkent(){
        ertek.value=ertek.value?.minus(1)
    }

}
```        
Hozzuk létre a ViewModelFactory-t legyen a neve PracticeViewModelFactory
```kotlin
class PracticeViewModelFactory(private val ertek:Int):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
            return PracticeViewModel(ertek) as T
        } else {
            throw IllegalArgumentException("Ismeretlen ViewModel!")
        }
    }

}
```
A fejlécben deklaráljuk a bejövő értéket, valamilyen néven, ez most ertek. Ez az osztály egy interfészt valósít meg gyakorlatilag. Ha a modelClass megegyezik a megadott ViewModel-el, akkor létrehozza, egyébként kivételt dob.

A mainactivity-ben a következők változások lesznek szükségesek:
 - először is deklarálni kell factory osztályt
```kotlin
private lateinit var viewModelFactory: PracticeViewModelFactory
```
Ezt követően létrehozni:
```kotlin
viewModelFactory=PracticeViewModelFactory(10)
```
A viewmodel létrehozása annyiban módosul, hogy a this mellett a factory-t is át kell adni.
```kotlin
viewModel=ViewModelProvider(this,viewModelFactory).get(PracticeViewModel::class.java)
```
        
## Listakezelés a programban, RecyclerView használata.

A legtöbb mobil app használ listát. Android esetén kizárólag a RecyclerView használatát javasolják, mert ez a komponens nagy számú listaelem esetén is jó teljesítményt nyújt(nem akad meg a görgetés stb.)
Cserébe az implementálása nem annyira egyszerű.
Az app navigációt is fog alkalmazni, ezért az ehhez szükséges függőségeket is telepíteni kell, mint korábban.
        
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
        implementation "androidx.recyclerview:recyclerview:1.2.1"
}
```       
Adatkötés használata:
```kotlin
 buildFeatures {
    dataBinding true
 }
```
A **Sync** megnyomása után készen állunk a navigáció megvalósítására.

Az activity_main.xml be tegyük be a következőt:

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
## Nézetek
Az apphoz három layoutot kell készíteni. Minden fragmentnek meglesz a saját layoutja, valamit a listaelemnek is kell egy layoutot készíteni.
        
## PersonAdapter készítése
A RecyclerView használatához szükség van egy adapter osztály készítésére. Az adapter osztálynak meg kell valósítania egy ún. ViewHolder-t, illetve az ehhez tartozó metódusokat.
        
Hozzunk létre egy új osztályt PersonAdapter néven.
Az osztály kezdő kódja:
```kotlin
class PersonAdapter(private val context:Context,val data:List<Person>) {
        class PersonViewHolder(){
    
        }
}
```
Származtassuk le a megfelelő osztályokból:
```kotlin
class PersonAdapter(private val context:Context,val data:List<Person>):RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    class PersonViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        
    }


}
```
A PersonViewHolder-ben azonosítjuk, deklaráljuk azokat az elemeket, amit a lista megjelenít
```kotlin
 class PersonViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            var vezeteknev:TextView=itemView.findViewById(R.id.list_vezeteknev)
            var keresztnev:TextView=itemView.findViewById(R.id.list_keresztnev)
        
    }
```
Valósítsuk meg az osztály által igényelt metódusokat.
```kotlin
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        return PersonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val aktData:Person=data[position]
        holder.vezeteknev?.text=aktData.vezeteknev
        holder.keresztnev?.text=aktData.keresztnev
    }

    override fun getItemCount(): Int {
       return data.size
    }
```
## A ListFragmentben deklaráljuk a következőket:
```kotlin
private lateinit var binding:FragmentListBinding
private lateinit var layoutManager:LinearLayoutManager
private  lateinit var adapter: PersonAdapter
```
Az OnCreateView így fog kinézni
```kotlin
  override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var adatok= listOf(
            Person("Kiss","Elek",1999),
            Person("Kiss","Endre",1998),
            Person("Kiss","Ubul",1997),
            Person("Kiss","Elek",1999),
            Person("Kiss","Endre",1998),
            Person("Kiss","Ubul",1997),
            Person("Kiss","Elek",1999),
            Person("Kiss","Endre",1998),
            Person("Kiss","Ubul",1997),
            Person("Kiss","Elek",1999),
            Person("Kiss","Endre",1998),
            Person("Kiss","Ubul",1997)

        )

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_list,container,false)
        adapter= PersonAdapter(requireContext(),adatok)
        layoutManager= LinearLayoutManager(requireContext())
        binding.lista.layoutManager=layoutManager
        binding.lista.adapter=adapter


        return binding.root
        
    }
```
## A lista elemeinek kattinthatóvá tétele

A lista megjelenik, a következő feladat az elemeinek kattinthatóvá tétele.
A PersonAdapter osztály fejlécéhez adjuk hozzá a következőt: val OnItemClick:((Person,Int)->Unit)
```kotlin
class PersonAdapter(private val context: Context, val data:List<Person>,val OnItemClick:((Person,Int)->Unit)):RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(){
        ...
}
```        
Az OnBindViewHolder-t egészítsük ki:
```kotlin
holder?.itemView.setOnClickListener {
     OnItemClick(aktData,position)
}

```        
A Fragmentben az adapter példányosítása így módosul:
```kotlin
adapter=PersonAdapter(requireContext(),adatok){
    itemDto:Person,position:Int->
    Log.i("Klikkelés","Klikk ${itemDto.keresztnev}")

}
```        
## A navigálás megvalósítása
A navigálás megvalósításához két sor fog kelleni.
Először meg kell keresni a navcontrollert
```kotlin
val navController=this.findNavController()
```
A navigálás parancsa:
```kotlin
navController.navigate(ListFragmentDirections.actionListFragmentToDetailFragment())
```
Egyben:
```kotlin
val navController=this.findNavController()

adapter= PersonAdapter(requireContext(),adatok){
        itemDto:Person,position:Int->
        Log.i("Click","${itemDto.keresztnev}")
        navController.navigate(ListFragmentDirections.actionListFragmentToDetailFragment())
   }
```
A következő lépés, hogy betöltsük az osztályok szerializációját végző plugint. A build.gradle(Modules..) fájlt nyissuk meg, a pluginekhez adjuk hozzá a következőt:
```kotlin
plugins {
    ...
    id 'kotlin-parcelize'
}

```
Alakítsuk szerializálhatóvá a Person osztályt:
```kotlin

@Parcelize
data class Person(
    var vezeteknev:String,
    var keresztnev:String,
    var szuletesiev:Int
) :Parcelable{}

```
Meg kell nyitni a navigációt.
 - Válasszuk ki a detailFragmentet
 - Jobb oldalon keressük meg a következőt: **Arguments**
 - Kattintsunk a **+**-ra
 - A Type-nál gördítsük le a menüt, válasszuk a Custom Parcelable-t, itt meg fogjuk találni az osztályt
 - adjunk nevet neki (aktPerson)

Nyissuk meg a DetailFragment-et!

Adjuk hozzá a binding deklarációját:
```kotlin
private lateinit var binding: FragmentDetailBinding        
```
Az OnCreateView metódusba pedig:
```kotlin

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)
        val aktPerson=DetailFragmentArgs.fromBundle(requireArguments()).aktPerson
        binding.person=aktPerson

        return binding.root
```
Az egész osztály egyben:
```kotlin

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)
        val aktPerson=DetailFragmentArgs.fromBundle(requireArguments()).aktPerson
        binding.person=aktPerson

        return binding.root

    }

}
```
Navigáció a listára:
Nyissuk meg a MainActiviy-t
Az OnCreate-be jön a következő két sor:
```kotlin
  val navController=this.findNavController(R.id.nav_host_fragment)
  NavigationUI.setupActionBarWithNavController(this,navController) 
```
Végezetül felül kell írni az OnSupportNavigateUp metódust:
```kotlin
 override fun onSupportNavigateUp(): Boolean {
        val navController=this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    } 
```
# Rest API hívása Android app-ból

A következő függőségekre lesz szükség:
```kotlin
dependencies {
    implementation "com.squareup.moshi:moshi:1.12.0"
    implementation "com.squareup.moshi:moshi-kotlin:1.12.0"        
    implementation "com.squareup.retrofit2:converter-moshi:2.9.0"
    implementation "com.squareup.retrofit2:converter-scalars:2.9.0"
    implementation "com.github.bumptech.glide:glide:4.12.0"
    implementation 'androidx.multidex:multidex:2.0.1'
}

A Moshi egy JSON library, a Retrofit pedig egy HTTP kliens. A Glide konyvtár segítségével pedig képeket tudunk letölteni, ha az api tartalmaz hivakozást ilyen jellegű adatra.
       
```
Adjuk hozzá az adatkötést is:
```kotlin
 buildFeatures {
    dataBinding true
 }
```
Nyissuk meg az AndroidManifest állományt, mert az app számára engedélyezni kell az internet hozzáférést.
Írjuk be az Application tag fölé:
```xml
  <uses-permission android:name="android.permission.INTERNET" />        
```
Először csinálunk egy Data Class-t, ez fogja majd tartalmazni az apitól lekért adatokat.
```kotlin
data class BlogAdat(
    val id:Int,
    val title:String,
    val body:String,
    val userId:Int
)
```
A következő egy Api service lesz (menthetjük ApiService néven)
```kotlin
private const val BASE_URL="https://jsonplaceholder.typicode.com/posts/"

private val moshi= Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit=Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
    BASE_URL).build()

interface ApiService {
    @GET("1")
    fun getData():Call<BlogAdat>
}

object BlogApi {
    val retrofitService:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
```
A MainActivity-be csinálunk binding-ot (a layout-ot ne felejtsük konvertálni!)
```kotlin
private lateinit var binding: ActivityMainBinding
        ...
 binding=DataBindingUtil.setContentView(this,R.layout.activity_main)        
```
Ezt követi az előbb létrehozott service meghívása:
```kotlin
 BlogApi.retrofitService.getData().enqueue(object :
            retrofit2.Callback<BlogAdat> {

            override fun onResponse(call: Call<BlogAdat>, response: Response<BlogAdat>) {
                val valasz=response.body()
                binding.blogadat=valasz
                }

            override fun onFailure(call: Call<BlogAdat>, t: Throwable) {
                val valasz="Error"
                Log.e("Api","Api hiba")
                }
            })
```
# Rest API hívás, listával
Az előző app csak egyetlen objektumot kért le, azonban sokkal jellemzőbb, hogy különböző hosszúságú listákat kér le az alkalmazás. Erre következik egy példa.
A függőségek: (a recyclerview-val egészül ki)
```kotlin
dependencies {
    implementation "com.squareup.moshi:moshi:1.12.0"
    implementation "com.squareup.moshi:moshi-kotlin:1.12.0"        
    implementation "com.squareup.retrofit2:converter-moshi:2.9.0"
    implementation "com.squareup.retrofit2:converter-scalars:2.9.0"
    implementation "com.github.bumptech.glide:glide:4.12.0"
    implementation 'androidx.multidex:multidex:2.0.1'
    implementation "androidx.recyclerview:recyclerview:1.2.1"    
}
Kell a data binding is:
```kotlin
 buildFeatures {
        dataBinding true
    } 
```
Nyissuk meg az AndroidManifest állományt, mert az app számára engedélyezni kell az internet hozzáférést.
Írjuk be az Application tag fölé:
```xml
  <uses-permission android:name="android.permission.INTERNET" />        
```
        
Az adat osztály:
```kotlin
data class BlogData(
    val userId:Int,
    val id:Int,
    val title:String,
    val body:String
)
```
A MainActivity layoutja egyszerű lesz, pusztán csak egy RecyclerView-t fog tartalmazni.
Kell készíteni, egy másik layout-ot, ez fogja majd megjeleníteni a lista egy elemét. Ez lesz a list_item.xml
A listaelemnél csak az id és a title fog megjelenni.

Készítsük el az adapter osztályt a recyclerview-hoz, ez lesz a BlogDataAdapter:
```kotlin
class BlogDataAdapter(private val context: Context,val data:List<BlogData>):RecyclerView.Adapter<BlogDataAdapter.BlogDataViewHolder>() {


    class BlogDataViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var id:TextView=itemView.findViewById(R.id.blog_id)
        var title:TextView=itemView.findViewById(R.id.blog_title)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogDataViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        return  BlogDataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BlogDataViewHolder, position: Int) {
        val aktData:BlogData=data[position]
        holder.id?.text=aktData.id.toString()
        holder.title?.text=aktData.title

    }

    override fun getItemCount(): Int {
        return data.size
    }


}
```
A következő az ApiService:
```kotlin
private const val BASE_URL="https://jsonplaceholder.typicode.com/"

private val moshi=Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit=Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("posts")
    fun getData(): Call<List<BlogData>>
}

object BlogApi {
    val retrofitService:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

```

```kotlin
        private const val BASE_URL="https://jsonplaceholder.typicode.com/"

private val moshi=Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit=Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("posts")
    fun getData(): Call<List<BlogData>>
}

object BlogApi {
    val retrofitService:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}


A MainActivity így fog kinézni:
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: BlogDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        layoutManager= LinearLayoutManager(this)

        BlogApi.retrofitService.getData().enqueue(object:
            retrofit2.Callback<List<BlogData>>{
            override fun onResponse(call: Call<List<BlogData>>,response: Response<List<BlogData>>) {
                val data=response.body()
                adapter= BlogDataAdapter(this@MainActivity,data!!)
                binding.lista.layoutManager=layoutManager
                binding.lista.adapter=adapter
            }

            override fun onFailure(call: Call<List<BlogData>>, t: Throwable) {
                Log.e("API error","Api hiba")
            }

        })


    }
}
```
## Api hívása navigációval, kép letöltéssel (https://randomuser.me)
        
Ebben az appban megint a random useradatokat szolgáltató API-t hívjuk meg. Kihívás, hogy a válaszban beágyazott objektumok is vannak, erre figyelemmel kell lenni a feldolgozás során. Figyelni kell arra is, hogy a szervernek adatokat küldünk, ami ugye a lekérdezni kívánt userek számát jelenti. Az api hívás eredménye egy lista lesz, a listaelemre bökve pedig a listaelem részletei lesznek láthatóak.
##Függőségek megadása
Először nyissuk meg a build.gradle(Project...) fájlt és a dependencies részhez adjuk hozzá a következő sort:
```kotlin
 dependencies {

        ***
classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5"
       
    }
```
Ezután nyissuk meg a build.gradle(Module....) fájlt! 
A plugins részhez adjuk hozzá a következőket:
```kotlin
        plugins {
    ***
    id 'kotlin-kapt'
    id 'androidx.navigation.safeargs'
    id 'kotlin-parcelize'
}
```
Adjuk hozzá az adatkötést:
        
```kotlin
 buildFeatures {
        dataBinding true
    }
```
Majd a következő függőségeket:
```kotlin
dependencies {

    ***
    implementation "androidx.navigation:navigation-fragment-ktx:2.3.5"
    implementation "androidx.navigation:navigation-ui-ktx:2.3.5"
    implementation "androidx.recyclerview:recyclerview:1.2.1"
    implementation "com.squareup.moshi:moshi:1.12.0"
    implementation "com.squareup.moshi:moshi-kotlin:1.12.0"
    implementation "com.squareup.retrofit2:converter-moshi:2.9.0"
    implementation "com.squareup.retrofit2:converter-scalars:2.9.0"
    implementation "com.github.bumptech.glide:glide:4.12.0"
    implementation 'androidx.multidex:multidex:2.0.1'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'

    ***
}
```
### Hozzunk létre két fragmentet, az egyik legyen **ListFragment**, a másik **DetailFragment** ! Töröljük ki a felesleges részeket!
A felesleges részek törlése után így néz ki a fragment kódja:
```kotlin
class ListFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       


        return inflater.inflate(R.layout.fragment_list, container, false)
    }


}
```
A másik fragmentnél is hasonlóan kell eljárni.
        
### Hozzuk létre a navigációt leíró fájlt, **navigation** néven!
Res->New->Android Resource File
Állítsuk a Resource Type-ot Navigation-ra, a name navigation legyen!
  
### Fragment megadása a MainActivity-ben
Nyissuk meg a MainActivity-t töröljük ki a benne lévő TextView-t!
Helyezzük el a következő elemet:
```kotlin
   <fragment
          android:id="@+id/nav_host_fragment"
          android:name="androidx.navigation.fragment.NavHostFragment"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          app:defaultNavHost='true'
          app:navGraph='@navigation/navigation'
          />
```
Nyissuk meg az előbb létrehozott navigation.xml nevű resource fájlt! Adjuk hozzá először a listfragmentet, majd a detailfragmentet! A listfragment jobb oldalán fogjuk meg a kört és húzzuk a másikra. Ezzel létrehoztuk a navigációs action, navigáláskor ezt fogjuk majd hívni kódból.
        
### Layout-ok elkészítése
Három layout fog kelleni. A ListFragment layoutján csak a RecyclerView kap helyet. A DetailFragment layoutján a felhasználó képe, neve, telefonszámai, e.mail címe fog megjelenni. A harmadik layout a listaelem, ezen a felhasználó képe, valamit vezeték és keresztneve jelenik meg.
        
### Adat osztályok készítése
Mivel az api válaszában beágyazott objektumok vannak, ezért ezeket külön adatosztályokkal kell majd leírni.
A következő adatosztályok fognak kelleni:
A név tárolására:
```kotlin
@Parcelize
data class UserName(
    val title:String,
    val first:String,
    val last:String
):Parcelable{}
```
A felhasználó képeinek tárolására:
```kotlin
@Parcelize
data class UserPicture(
    val large:String,
    val medium:String,
    val thumbnail:String
):Parcelable{}
```
A felhasználó adatai:
```kotlin        
@Parcelize
data class UserData(
    val gender:String,
    val email:String,
    val name:UserName,
    val phone:String,
    val cell:String,
    val picture:UserPicture
):Parcelable {}
```
Végül a results:
```kotlin
data class UserResults(
    val results:List<UserData>
)
```
### Adapter osztály a listához:
A képnek csak az url-jét tartalmazza az adat, az url-t felhasználva a Glide segítségével töltjük le, majd helyezzük el az ImageView-ba a képet.
```kotlin
class UserDataAdapter(val context: Context,val data:UserResults,val onItemClick:((UserData,Int)->Unit)):RecyclerView.Adapter<UserDataAdapter.UserDataViewHolder>() {

    class UserDataViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val userImage:ImageView=itemView.findViewById(R.id.list_image)
        val userFirstname:TextView=itemView.findViewById(R.id.list_first)
        val userLastname:TextView=itemView.findViewById(R.id.list_last)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        return UserDataViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserDataViewHolder, position: Int) {
        val aktData:UserData=data.results[position]
        holder.userFirstname?.text=aktData.name.first
        holder.userLastname?.text=aktData.name.last
        holder.itemView.setOnClickListener { onItemClick(aktData,position) }
        Glide.with(context).load(aktData.picture.large).into(holder.userImage)
    }

    override fun getItemCount(): Int {
        return data.results.size
    }
}
```
### Apiservice megvalósítása:
Az előző példákhoz képest megjelenik egy Query annotation, itt lehet megadni a szerver felé, hogy hány usert akarunk lekérni.
```kotlin
private const val BASE_URL="https://randomuser.me/"

private val moshi=Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit=Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
    BASE_URL).build()

interface ApiService {
    @GET("api")
    fun getAdatok(@Query("results") results:Int): Call<UserResults>
}

object UserDataApi {
    val retrofitService:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
```
### ListFragment kódja
```kotlin
class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: UserDataAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_list,container,false)



        UserDataApi.retrofitService.getAdatok(25).enqueue(object:
            retrofit2.Callback<UserResults>{
            override fun onResponse(call: Call<UserResults>, response: Response<UserResults>) {
                val valasz=response.body()
                Log.i("Valasz","${response.body()?.results?.size}")
                layoutManager= LinearLayoutManager(requireContext())
                val navController=this@ListFragment.findNavController()
                adapter= UserDataAdapter(requireContext(),valasz!!)
                {
                    itemDto:UserData,position->
                    navController.navigate(ListFragmentDirections.actionListFragmentToDetailFragment(itemDto))
                }
                binding.lista.adapter=adapter
                binding.lista.layoutManager=layoutManager
            }

            override fun onFailure(call: Call<UserResults>, t: Throwable) {
                Log.e("Hiba","Api hiba:${t.message}")
            }

        }
        )

        return binding.root
       
    }


}
```
        
### DetailFragment megvalósítása
Mielőtt ebbe belekezdünk meg kell nyitni a navigációt, és hozzá kell adni a detailfragment-hez egy argumentumot. Ez az argumentum adja meg, hogy milyen osztályt kap a fragment, amelynek az adatait majd megjeleníti. Ez az UserData lesz. Figyelni kell arra, hogy ennek az osztálynak Parcelable annotációval kell rendelkeznie!
```kotlin
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)
        val aktUser=DetailFragmentArgs.fromBundle(requireArguments()).userdata
        Glide.with(requireContext()).load(aktUser.picture.large).circleCrop().into(binding.userPicture)
        binding.userdata=aktUser


        return binding.root
   
    }


}
```
