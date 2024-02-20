# Message
Dit is het gedeelte over het inladen van de berichten.


## MessagesFragment
###### be.vives.vivesplus.screens.messages.MessagesFragment

Fragment dat de lijst van berichten in een recyclerview toont.

#### FontAwesome Library.
De iconen die u ziet zijn iconen die worden ingeladen door de backend aan de hand van font awesome.
Om een icoon op te halen op de correcte manier refereer ik naar lijn 28 en 30 van MessagesAdapter.

#### Custom Adapter
Dit is een adapater dat ik heb geschreven om een spinner te tonen met icoon en categorie. Dit wordt niet gebruik, omdat er nog wat problemen
waren bij het inladen van alles anagezien het twee verschillende soorten types was en ik vond geen manier om alle berichten terug te tonen.

#### MessagesAdapter
Dit is de adapter die alle data houdt van de berichten.
Deze code controleert of het bericht later is dan vandaag of niet.
if(difference == 0)
{
    if(data[position].viewDate.minute == 0)
    {
    holder.time.text = data[position].viewDate.dayOfMonth.toString().plus(" ").plus(data[position].viewDate.month.name.take(3).toLowerCase(Locale.ROOT)).plus(".").plus(" ").plus(data[position].viewDate.hour.toString()).plus(":").plus(data[position].viewDate.minute).plus("0").toLowerCase(Locale.ROOT)
    } else {
    holder.time.text = data[position].viewDate.dayOfMonth.toString().plus(" ").plus(data[position].viewDate.month.name.take(3).toLowerCase(Locale.ROOT)).plus(".").plus(" ").plus(data[position].viewDate.hour.toString()).plus(":").plus(data[position].viewDate.minute)
    }
    } else{
        holder.time.text = data[position].viewDate.dayOfMonth.toString().plus(" ").plus(data[position].viewDate.month.name.take(3).toLowerCase(Locale.ROOT)).plus(".")
    }


##### Super klassen en interfaces
| Klasse / interface  | Omschrijving |
|:------------|:-------------|
| Fragment | Zie android docs |
| MessagesDAOCallback | Interface om data terug te koppelen van de dao naar het fragmen |

##### Variabelen
| Variabele   | type   | Omschrijving |
|:------------|:-------|:-------------|
| binding | FragmentMessagesBinding | Binding verantwoordelijk voor het manipuleren van de view |
| binding.geenberichten | TextVIew | Tekst als er geen berichten zijn. |
| binding.rvMessages | TextView | recyclerview die lijst van berichten toont |
| binding.messagesSpinner | Spinner | Spinner voor het filteren van berichten |
| prefManager | PreferenceManager | referentie naar de PreferenceManger util klasse |

