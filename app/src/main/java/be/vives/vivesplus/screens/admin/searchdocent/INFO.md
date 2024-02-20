# searchDocent
Dit is het gedeelte over het opzoeken van een docent.


## SearchDocentFragment
###### be.vives.vivesplus.screens.searchdocent.SearchDocentFragment

Dit is de fragment die alles bevat over het zoeken van docenten.

## SearchDocentDetailFragment
#### be.vives.vivesplus.screens.searchdocent.DetailFragment

Dit is de fragment die de informatie van de docent op het scherm zal tonen

## DocentAdapter
#### be.vives.vivesplus.screens.searchdocent.DocentAdapter

De DocentAdapter zorgt ervoor dat de data van de docenten in de recyclerview komen als je zoekt naar een collega.

##### Super klassen en interfaces
| Klasse / interface  | Omschrijving |
|:------------|:-------------|
| Fragment | Zie android docs |
| MembersDAOCallback | Interface om data terug te koppelen van de dao naar het fragmen |

##### Variabelen
| Variabele   | type   | Omschrijving |
|:------------|:-------|:-------------|
| binding | FragmentSearchDocentBinding, FragmentSearchDocentDetailBinding  | Binding verantwoordelijk voor het manipuleren van de view |
| binding.telImage | AndroidHive | Een foto waar je op kan klikken voor te bellen. |
| binding.mailImage | AndroidHive | Een foto waar je op kan klikken voor een mail te sturen |
| binding.docentDescription | TextView | Tekst voor de richtingen waar een docent les geeft |
| binding.docentEmail | TextView | tekst voor de email |
| binding.docentTel | TextView |  tekst voor de telefoon  |
| binding.docentName | TextView  | tekst voor naam van docentennaam |
| binding.studiegebiedspinner | Spinner | Spinner om een studiegebied te selecteren  |
| binding.textDocent | EditTextView | Tekstgebied voor een docent in te geven |
| binding.docentenList | RecyclerView | Lijst met alle docenten.  |
| prefManager | PreferenceManager | referentie naar de PreferenceManger util klasse |

