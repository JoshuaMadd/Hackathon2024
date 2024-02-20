package be.vives.vivesplus.dao

import be.vives.vivesplus.model.StudyProposal

interface StudyProposalsDAOCallback {
    fun proposalDataLoaded(studyProposals: MutableList<StudyProposal>)
    fun dataLoaded(studyProposal: StudyProposal)
    fun putSucces()
}