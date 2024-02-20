package be.vives.vivesplus.screens.admin.absences

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.AdminAbsencesDAO
import be.vives.vivesplus.dao.AdminAbsencesDAOCallback
import be.vives.vivesplus.databinding.FragmentAdminNewAbsencesBinding
import be.vives.vivesplus.model.AdminAbsence
import be.vives.vivesplus.util.DateTimeHelper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class AdminNewAbsencesFragment : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener,
    AdminAbsencesDAOCallback {

    lateinit var binding: FragmentAdminNewAbsencesBinding

    var id: Int? = null

    var startDate: LocalDate = LocalDate.now()
    var endDate: LocalDate = LocalDate.now()
    var startTime: LocalTime = LocalTime.of(8, 0)
    private var endTime: LocalTime = LocalTime.of(18, 0)
    private var isStart: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_new_absences, container, false)

        id = arguments?.getInt("id")
        if(id != null) {
            binding.indicator.visibility = View.VISIBLE
            binding.content.visibility = View.GONE
            binding.btnRegister.visibility = View.GONE
            AdminAbsencesDAO(requireContext(), this).getById(id!!)
        }


        binding.startTime.setOnClickListener {
            isStart = true
            openTimeDialog()
        }
        binding.endTime.setOnClickListener {
            isStart = false
            openTimeDialog()
        }
        binding.startDate.setOnClickListener {
            isStart = true
            openDateDialog()
        }
        binding.endDate.setOnClickListener {
            isStart = false
            openDateDialog()
        }

        formatAllDates()

        binding.btnRegister.setOnClickListener {view: View ->
            if(id == null) {
                AdminAbsencesDAO(requireContext(), this).post(
                    DateTimeHelper().formatDateAndTimeToString(startDate, startTime),
                    DateTimeHelper().formatDateAndTimeToString(endDate, endTime),
                    binding.remark.text.toString())
            } else {
                AdminAbsencesDAO(requireContext(), this).put(
                    DateTimeHelper().formatDateAndTimeToString(startDate, startTime),
                    DateTimeHelper().formatDateAndTimeToString(endDate, endTime),
                    binding.remark.text.toString(),
                    id!!)
            }
        }

        return binding.root
    }

    private fun formatAllDates() {
        binding.startDate.text = DateTimeHelper().formatDateToFullString(startDate)
        binding.endDate.text = DateTimeHelper().formatDateToFullString(endDate)
        binding.startTime.text = DateTimeHelper().formatTimeToString(startTime)
        binding.endTime.text = DateTimeHelper().formatTimeToString(endTime)
    }

    private fun openTimeDialog() {
        if(isStart)
            TimePickerDialog(activity, this, startTime.hour , startTime.minute, DateFormat.is24HourFormat(activity)).show()
        else
            TimePickerDialog(activity, this, endTime.hour , endTime.minute, DateFormat.is24HourFormat(activity)).show()
    }

    private fun openDateDialog() {
        val picker = if(isStart)
            DatePickerDialog(requireContext(), this, startDate.year, startDate.monthValue - 1, startDate.dayOfMonth)
        else
            DatePickerDialog(requireContext(), this, endDate.year, endDate.monthValue - 1, endDate.dayOfMonth)

        if(isStart)
            picker.datePicker.minDate = System.currentTimeMillis()
        else
            picker.datePicker.minDate = LocalDateTime.of(startDate.year, startDate.month, startDate.dayOfMonth, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        picker.show()
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(isStart) {
            startTime = LocalTime.of(hourOfDay, minute)
            binding.startTime.text = DateTimeHelper().formatTimeToString(startTime)
        } else {
            endTime = LocalTime.of(hourOfDay, minute)
            binding.endTime.text = DateTimeHelper().formatTimeToString(endTime)
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        if(isStart) {
            startDate = LocalDate.of(year, month + 1, dayOfMonth)
            binding.startDate.text = DateTimeHelper().formatDateToFullString(startDate)
            if(startDate.isAfter(endDate)) {
                endDate = LocalDate.of(startDate.year, startDate.monthValue, startDate.dayOfMonth)
                binding.endDate.text = DateTimeHelper().formatDateToFullString(endDate)
            }
        } else {
            endDate = LocalDate.of(year, month + 1, dayOfMonth)
            binding.endDate.text = DateTimeHelper().formatDateToFullString(endDate)
        }
    }

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

    override fun postOrPutSuccesful() {
        requireActivity().onBackPressed()
    }
}
