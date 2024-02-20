/**
 * Created by Cantaert Jordy
 * 11 feb 2020
 *
 * Last edited by Cantaert Jordy
 * 11 feb 2020
 */

package be.vives.vivesplus.model

import java.time.LocalDateTime

data class Absence(
    var teacherName: String,
    var startDate: LocalDateTime,
    var endDate: LocalDateTime
)