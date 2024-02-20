package com.joshuamaddelein.hackathon2024.util

import com.joshuamaddelein.hackathon2024.data.model.Les
import com.joshuamaddelein.hackathon2024.data.model.User
import java.time.LocalDate
import java.time.LocalTime

object MockUser {
    private val les1 = Les("Practice Enterprise","H3.08", LocalDate.now().plusDays(0), LocalTime.of(8,45),LocalTime.of(17,45))
    private val les2 = Les("Advanced Networks","H4.17", LocalDate.now().plusDays(1), LocalTime.of(8,45),LocalTime.of(10,45))
    private val les3 = Les("Network Automation 1","H2.11", LocalDate.now().plusDays(1), LocalTime.of(11,0),LocalTime.of(13,0))
    private val les4 = Les("Practice Enterprise","H60.04", LocalDate.now().plusDays(1), LocalTime.of(14,0),LocalTime.of(17,15))
    private val les5 = Les("Cybersecurity","H60.07", LocalDate.now().plusDays(2), LocalTime.of(14,0),LocalTime.of(16,15))
    private val lessen = listOf(les1, les2, les3, les4, les5);
    private val user = User(lessen,"Joshua","Maddelein","r0879578");
    fun getUser() : User {
        return user;
    }
    fun getLessen(): List<Les>
    {
        return lessen
    }
}
