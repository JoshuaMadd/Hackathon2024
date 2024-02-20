# Admin Absences
Het **admin absences** gedeelte van deze app beheerd alles wat te maken heet met  
het ophalen, tonen, maken, verwijderen en wijzigen van afwezigheden van docenten.

## AdminAbsence
###### be.vives.vivesplus.model.AdminAbsence

Data klasse die alle info bijhoud voor een docent afwezigheid.

##### Variabelen
| Variabele   | type   | Omschrijving |
|:------------|:-------|:-------------|
| id | Int | Id van het object |
| start | LocalDateTime | Start datum en tijd van een afwezigheid |
| end | LocalDateTime | Eind datum en tijd van een afwezigheid |
| remark | String? | Extra info betreft reden van de afwezigheid voor administratieve doeleinden |

## AdminAbsencesFragment
###### be.vives.vivesplus.screens.admin.absences.AdminAbsencesFragment

Fragment verantwoordelijke voor het afbeelden van docenten afwezigheden.

##### Super klassen en interfaces
| Klasse / interface  | Omschrijving |
|:------------|:-------------|
| Fragment | Zie android docs |
| AdminAbsencesDAOCallback | Interface die gebruikt wordt om data terug te koppelen uit **AdminAbsencesDAO** naar het fragment |

##### Variabelen
| Variabele   | type   | Omschrijving |
|:------------|:-------|:-------------|
| binding | FragmentAdminAbsencesBinding | Binding verantwoordelijk voor het manipuleren van de view |
| binding.root | View | View die dit fragment representeert |
| binding.refresh | SwipeRefreshLayout | Refresh layout |
| binding.btnNew | Button | Button die naar het **AdminNewAbsencesFragment** navigeert |
| binding.absencesList | RecycerView | Recyclerview voor het afbeelden van alle afwezigheden |
| binding.indicator | ProgressBar | Loading icon |
| binding.nofound | TextView | Textview met noting found message |

##### fun onCreateView

In de **onCreateView** wordt een refreshListener geplaatst op **binding.refresh** die
de docent afwezigheden opnieuw zal gaan ophalen adhv **AdminAbsencesDAO**

Als volgende wordt een clickListener op **binding.btnNew** geplaatst die zal navigeren naar het **AdminNewAbsencesFragment** zonder
parameters mee te geven.

```kotlin
binding.refresh.setOnRefreshListener { AdminAbsencesDAO(context!!, this).get() }
binding.btnNew.setOnClickListener { view -> view.findNavController().navigate(R.id.action_adminAbsencesFragment_to_adminNewAbsencesFragment) }
```

Als volgende wordt een layoutManager gemaakt voor de **binding.absencesList** en krijgt deze een adapter toegewezen van type
**AdminAbsencesAdapter**, deze krijgt voorlopig een lege **ArrayList** mee als data.

```kotlin
binding.absencesList.layoutManager = LinearLayoutManager(context)
binding.absencesList.adapter = AdminAbsencesAdapter(ArrayList(), context!!, this)
```

Als laatste wordt een instantie van **AdminAbsencesDAO** aangemaakt en wordt deze zijn **get** methode uitgevoerd om alle
afwezigheden van de ingelogde docent op te halen.

##### fun dataLoaded

Callback methode geïmplementeerd van **AdminAbsencesDAOCallback** die getriggerd wordt
zodra data uit de API ingeladen is. Als eerste wordt **binding.refresh.isRefreshing** op false gezet
in het gevald dat de SwipeRefreshLayout zou aan het refreshen zijn.

Als volgende wordt het loading icon van het scherm verwijderd en wordt er gecontroleerd of de **ArrayList** weldegelijk
resultaten bevat. Zo niet (size == 0) wordt het **binding.nofound** TextView zichtbaar gemaakt.

Als laatste worden een nieuw **AdminAbsencesAdapter** object gemaakt met als data de nieuwe lijst met
**AdminAbsences** objecten.

```kotlin
override fun dataLoaded(absences: ArrayList<AdminAbsence>) {
        binding.refresh.isRefreshing = false

        binding.indicator.visibility = View.GONE
        if(absences.size == 0)
            binding.nofound.visibility = View.VISIBLE

        binding.absencesList.adapter = AdminAbsencesAdapter(absences, context!!, this)
}
```

##### fun edit

Functie die ook zal navigeren naar het **AdminNewAbsencesFragment** met het verschil dat deze een **Bundle** met een
id meekrijgt die zal gebruikt worden wanneer er een docenten afwezigheid moet ge-edit worden.

```kotlin
fun edit(id: Int) {
        var bundle = bundleOf("id" to id)
        binding.root.findNavController().navigate(R.id.action_adminAbsencesFragment_to_adminNewAbsencesFragment, bundle)
}
```
## AdminAbsencesAdapter
###### be.vives.vivesplus.screens.admin.absences.AdminAbsencesAdapter

Adapter klasse voor het afbeelden van data in de **absencesList** RecyclerView.

##### Super klassen en interfaces
| Klasse / interface  | Omschrijving |
|:------------|:-------------|
| RecyclerView.Adapter | Zie android docs |

##### Variabelen
| Variabele   | type   | Omschrijving |
|:------------|:-------|:-------------|
| data | ArrayList<AdminAbsence> | Lijst van **AdminAbsence** objecten |
| context | Context | Huidge context van de applicatie |
| fragment | AdminAbsencesFragment | Referentie naar het fragment |

##### fun onCreateViewHolder

Inflatie van een **admin_absences_row** holder layout.

##### fun onBindViewHolder

In deze methode word een holder layout aangepaste met de correcte data adhv een corresponderend **AdminAbsence** object  
uit de data ArrayList.

De start en eind datum worden geformateerd aan de hand van de **DateTimeHelper** util klasse. Ook worden er
clickListeners op de delete en edit button geplaats.

```kotlin
override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.van.text = "van: ${DateTimeHelper().formatDateTimeToFullString(data[position].start)}"
        holder.tot.text = "tot:  ${DateTimeHelper().formatDateTimeToFullString(data[position].end)}"

        holder.delete.setOnClickListener { delete(data[position].id, position) }
        holder.edit.setOnClickListener{ edit(data[position].id, position) }
}
```

##### fun getItemCount

Geeft het aantal item in de **data** ArrayList terug.

##### class ViewHolder

Inner klasse die de benodigde TextView's en dergelijke uit de holderview haalt zodat
deze gemanipuleerd kunnen worden.

##### fun delete

Deze functie krijgt de positie van het geselecteerd item in de lijst mee. Als eerste word gecontroleerd of
de start datum van de afwezigheid voor de huidige datum en tijd ligt, in de geval word het verwijderen geblokkeerd
en krijgt de gebruiker een melding in de vorm van een Toast message met de melding dat hij de afwezigheid niet kan verwijderen.

In het geval dat dit niet zo is komt een confirmatie **AlertDialog** die vraagt of de gebruiker
deze afwezigheid wel zeker willen verwijderen. Zo ja wordt de data verwijderd uit **data** lijst
en wordt een **delete** call gemaakt via de dao

```kotlin
AdminAbsencesDAO(context, this).delete(data[position].id)
data.removeAt(position)
notifyDataSetChanged()            
```

##### fun edit

De functie edit ontvangt 2 parameters, het **id** van de te editten afwezigheid en de einddatum van deze afwezigheid (**end**). Wanneer
de einddatum al voorbij is zal een Toast message verschijnen die de docent erop wijst dat de afwezigheid niet meer kan gewijzigd worden.
Kan deze wel nog gewijzigd worden dan zal de **edit** functie van **fragment** aangeroepen worden en word het id aan deze meegegeven.

## AdminNewAbsencesFragment
###### be.vives.vivesplus.screens.admin.absences.AdminNewAbsencesFragment

Fragment verantwoordelijke voor maken en wijzigen van docenten afwezigheden.

##### Super klassen en interfaces
| Klasse / interface  | Omschrijving |
|:------------|:-------------|
| Fragment | Zie android docs |
| AdminAbsencesDAOCallback | Interface die gebruikt wordt om data terug te koppelen uit **AdminAbsencesDAO** naar het fragment |
| TimePickerDialog.OnTimeSetListener | Interface die nodige functie voorziet die om data uit de TimePicker te verkrijgen |
| DatePickerDialog.OnDateSetListener | Interface die nodige functie voorziet die om data uit de DatePicker te verkrijgen |

##### Variabelen
| Variabele   | type   | Omschrijving |
|:------------|:-------|:-------------|
| binding | FragmentAdminAbsencesBinding | Binding verantwoordelijk voor het manipuleren van de view |
| binding.root | View | View die dit fragment representeert |
| id | Int? | Id dat aanwezig is in het geval we een bestaande afwezigheid editten, bij een nieuwe afwezigheid is dit null |
| startDate | LocalDate | Start datum van de afwezigheid |
| endDate | LocalDate | Eind datum van de afwezigheid |
| startTime | LocalTime | Start tijd van een afwezigheid |
| endTime | LocalTime | Eind tijd van een afwezigheid |
| isStart | Boolean | Boolean om te bepalen of start of eind moet aangepast worden door de huige Date- of TimePicker |
| binding.btnRegister | Button | Button die naar het de afwezigheid registreerd  |
| binding.remark | TextView | TextView die de remark bevat |
| binding.startTime | TextView | TextView die de start tijd afbeeld |
| binding.endTime | TextView | TextView die de eind tijd afbeeld |
| binding.startDate | TextView | TextView die de start datum afbeeld |
| binding.endDate | TextView | TextView die de eind datum afbeeld |

##### fun onCreateView

In de **onCreateView** wordt als eerste gecheckt of een id werd meegegeven in de **arguments**, word alles van het scherm gehaald en
het loading icon zichtbaar gemaakt en wordt de **getById** uitgevoerd van **AdminAbsencesDAO** om de te editen afwezigheid
op te halen.

Als volgende worden op de TextViews clickListeners gezet zodat wanneer op deze geklikt worde de **isStart** Boolean in te stellen
en vervolgens een Time- of DatePicker te openen.

Hierna wordt de functie **formatAllDates** opgeroepen.

Als laatste wordt een clickListener op **binding.btnRegister** geplaatst. Deze zal afhankelijk van of het een nieuwe afwezigheid of een
edit is (**id** null of niet) de data gaan posten of putten via de **AdminAbsencesDAO** en terugnavigeren naar het vorige scherm.

##### fun formatAllDates

Deze functie zal adhv de **DateTimeHelper** util klasse alle LocalDate's en LocalTime's gaan formatten naar strings en afbeelden op de view.

##### fun openTimeDialog

Opent een TimePicker met tijden afhankelijk of de eind of start tijd geselecteerd is.

##### fun openDateDialog

Opent de DatePicker met datum afhankelijk van de eind of start datum rekening gehouden met het feit dat de eind datum niet voor
de start datum kan zijn.

##### fun onTimeSet

Functie die getriggerd wordt wanneer de TimePicker gesloten word, afhankelijk van de **isStart** Boolean word de teruggegeven tijd
ingstil in **startTime** of **endTime**.

##### fun onDateSet

Functie die getriggerd wordt wanneer de DatePicker gesloten word, afhankeljk van **isStart** wordt de meegegeven datum ingsteld op
**startDate** of **endDate**, ook wordt rekening gehouden dat de einddatum aangepast wordt als de startdatum na de einddatum zou
verplaatst zijn.

##### fun dataLoaded

Functie die getriggerd word van zodra de data uit de dao beschikbaar is (enkel in geval van edit). Deze functie krijgt een ArrayList met maar 1 object in. Als eerste
wordt de **binding.indicator** (loading icon) terug van het scherm gehaald en de content samen met e button terug
op het scherm geplaatst.

Als volgende worden de LocalDate's en LocalTimes's ingesteld adhv de teruggekregen data en worden de TextViews geformat adhv de
**formatAllDates** functie.

Als laaste wordt nog gecheckt of de **remark** beschikbaar en zo ja wordt deze in de TextView geplaatst.

```kotlin
override fun dataLoaded(absences: ArrayList<AdminAbsence>) {
        binding.indicator.visibility = View.GONE
        binding.btnRegister.visibility = View.VISIBLE
        binding.content.visibility = View.VISIBLE

        startDate = LocalDate.of(absences[0].start.year, absences[0].start.monthValue, absences[0].start.dayOfMonth)
        endDate = LocalDate.of(absences[0].end.year, absences[0].end.monthValue, absences[0].end.dayOfMonth)

        startTime = LocalTime.of(absences[0].start.hour, absences[0].start.minute)
        endTime = LocalTime.of(absences[0].end.hour, absences[0].end.minute)

        formatAllDates()

        if(absences[0].remark != null)
            binding.remark.setText(absences[0].remark)
}
```

## AdminNewAbsencesFragment
###### be.vives.vivesplus.dao.AdminAbsencesDAO

Fragment verantwoordelijke voor maken en wijzigen van docenten afwezigheden.

##### Super klassen en interfaces
| Klasse / interface  | Omschrijving |
|:------------|:-------------|
| WebServiceCallback | Interface die gebruikt wordt om data terug te koppelen uit **WebService** naar de dao |
| WebServicePutCallback | Interface die nodige functie voorziet die getriggerd word zodra een PUT succesvol is |

##### Variabelen
| Variabele   | type   | Omschrijving |
|:------------|:-------|:-------------|
| context | Context | Huidige context van de applicatie |
| callback | AdminAbsencesDAOCallback | Interface die gebruikt wordt om terug te koppelen naar het fragment |
| fileName | String | Naam van de file waar de json lokaal gecached word |
| fileEdit | String | Naam van de file waar een put response gecached word |
| url | String | Endpoint van de API |

##### fun get

Get alle afwezigheden van de ingelogde docent adhv **WebService**

##### fun getById

Get de afwezigheid van een docent adhv **id**

##### fun post

Maak (post) een nieuwe afwezigheid in de backend

##### fun put

Edit (put) een gewijzigde afwezigheid

##### fun delete

Delete een afwezigheid uit de backend adhv **id**

##### fun dataLoaded(jsonArray: JSONArray)

Deze methode wordt getriggerd zodra de lijst met afwezigheden van de **get** functie beschikbaar is.
Deze methode zal voor elk **JSONObject** in de **JSONArray** een **AdminAbsence** object maken en toevoegen aan
een **ArrayList**.

```kotlin
val obj = jsonArray.getJSONObject(i)
val start = DateTimeHelper().formatStringToLocalDateTime(obj.getString("startDate"))
val end = DateTimeHelper().formatStringToLocalDateTime(obj.getString("endDate"))
val id = obj.getInt("id")
var remark: String? = obj.getString("remark")
if(remark == "null")
     remark = null
list.add(AdminAbsence(id, start, end, remark))
```

##### fun dataLoaded(jsonObject: JSONObject)

Deze methode wordt getriggerd zodra de afwezigheid van de **getById** functie beschikbaar is. Deze zal net als
**dataLoaded(jsonArray: JSONArray)** een **AdminAbsence** object gaan maken adhv het teruggekregen **JSONObject**
met het verschil dat hier maar 1 object in de **ArrayList** zal zitten.

## AdminNewAbsencesFragmentCallback
###### be.vives.vivesplus.dao.AdminAbsencesDAOCallback

Interface voor terugkoppeling van data naar het fragment.

