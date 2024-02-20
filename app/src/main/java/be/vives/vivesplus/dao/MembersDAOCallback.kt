package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Member

interface MembersDAOCallback {
    fun dataLoaded(members: MutableList<Member>){}
    fun dataLoaded(member: Member){}
    fun putSucces(){}

    fun setError(error: Int){}



}