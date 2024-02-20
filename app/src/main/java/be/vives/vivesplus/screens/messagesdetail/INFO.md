# MessageDetail
Dit is het gedeelte over het inladen van een detail van een bericht.


## MessagesDetailFragment
###### be.vives.vivesplus.screens.messagesdetail.MessagesDetailFragment

Fragment dat een detail van een bericht toont op een overzichtelijke manier.

### Args plugin
Ik heb de arguments en directions plugin gebruikt om een object mee te geven van fragment naar fragment.
Je kan in de navigation file een object meegeven en dan ophalen in de fragment dat je hem wilt.
De code hiervoor,
val args =  MessagesDetailFragmentArgs.fromBundle(this.requireArguments())
val message = args.message

### OPGELET!
Viewdate van een bericht is wanneer het moet getoond worden en Startdate is de datum om te controleren voor de teller.

##### Variabelen
| Variabele   | type   | Omschrijving |
|:------------|:-------|:-------------|
| binding | FragmentMessagesDetailBinding | Binding verantwoordelijk voor het manipuleren van de view |
| binding.detailCategoryIcon| AndroidHive | Hierin komt het icoon van de categorie |
| binding.eventLocation | TextView | tekst voor de locatie van het evenement |
| binding.eventTime | TextView | Spinner voor het filteren van berichten |
| binding.layoutSchedule | layour | Layout voor de  evenementenbox |
| binding.btnMoreText | TextView | Button die een link bevat naar een website|
| binding.detailCreatietijd | TextView | tekst voor de tekst |
| binding.detailCategory | TextView  | tekst voor categorie |
| binding.detailVerstuurder | TextView | tekst voor de verzender |
| binding.detailOnderwerp | TextView | tekst voor het onderwerp |
| binding.detailBeschrijving | TextView | tekst voor het bericht zelf |

