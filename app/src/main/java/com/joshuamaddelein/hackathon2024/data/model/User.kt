package com.joshuamaddelein.hackathon2024.data.model

data class User(
    var lessen: List<Les>,
    var voornaam: String,
    var achternaam: String,
    var rNummer: String
)
