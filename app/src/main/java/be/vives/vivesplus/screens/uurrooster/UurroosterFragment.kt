/**
 * Created by Cantaert Jordy
 * 10 feb 2020
 *
 * Last edited by Cantaert Jordy
 * 20 feb 2020
 */

package be.vives.vivesplus.screens.uurrooster

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentUurroosterBinding
import be.vives.vivesplus.model.VivesEvent
import be.vives.vivesplus.util.*
import com.alamkanak.weekview.jsr310.scrollToDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class UurroosterFragment : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var viewModel: UurroosterViewModel
    private lateinit var viewModelFactory: UurroosterViewModelFactory
    private lateinit var binding: FragmentUurroosterBinding
    private var dateStringArg : String? = null
    private var intialLoadFinished = false


    @SuppressLint("FragmentLiveDataObserve", "ClickableViewAccessibility", "InflateParams",
        "SetJavaScriptEnabled"
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_uurrooster, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
        viewModelFactory = UurroosterViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(UurroosterViewModel::class.java)
        binding.viewModel = viewModel

        UurroosterDecorator().setupWeekview(binding.weekView)
        binding.weekView.setTimeFormatter { hour ->
            UurroosterDecorator().interpretTime(hour)
        }

        val calenderAdapter = MyPagingEventAdapter(
            ClickOnEventListener {
                 navigateToDetailScreen(it)
            },
            LoadMoreHandler { firstDate, _ ->
                if (intialLoadFinished) {
                    viewModel.getVivesEvents(firstDate)
                }
            },
            RangeChangedListener { firstVisibleDate, lastVisibleDate ->
                goToDate(firstVisibleDate.year, firstVisibleDate.monthValue, firstVisibleDate.dayOfMonth)
            }
        )

        binding.weekView.adapter = calenderAdapter

        viewModel.errorLogOut.observe(this, androidx.lifecycle.Observer {
            if(it!!){
                LogoutHandler.showLogoutAlert(requireContext(), requireActivity())
           }
        })

        viewModel.errorForbidden.observe(this, androidx.lifecycle.Observer {
            if(it!!){
                binding.toestemmingClickable.visibility = View.VISIBLE
            }
        })

        viewModel.navigateToWebToestemming.observe(this, androidx.lifecycle.Observer {
            if(it){
                onClickToestemmingNavigateToWeb()
            }
        })

        binding.weekView.computeScroll()

        viewModel.vivesEvents.observe(this, androidx.lifecycle.Observer {
            val events = ArrayList<Event>()
            var index = 0L
            for (vivesEvent in it) {
                val localStartTime = DateTimeHelper().formatStringToDateTime(vivesEvent.startDateTimeString)
                val localEndTime = DateTimeHelper().formatStringToDateTime(vivesEvent.endDateTimeString)
                var titleString = ""
                if(vivesEvent.allDay){
                    titleString = vivesEvent.title
                } else {
                    titleString = makeTitleTimeString(localStartTime, localEndTime, vivesEvent.title)
                }
                val event = Event(index, titleString, localStartTime, localEndTime, vivesEvent.location
                    ,vivesEvent.getColor(requireContext()), vivesEvent.allDay, isCanceled = false
                )
                events.add(event)
                index++
            }
            calenderAdapter.submitList(events)
            intialLoadFinished = true
        })

        setupCorrectOrientationSettings()

        onWeekViewVisible()


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(
                UurroosterFragmentDirections.actionGlobalModernDashboardFragment()
            )
            isEnabled = true
        }


        return binding.root
    }

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.uurrooster_nav_menu, menu)
    }

     fun onWeekViewVisible() {

        dateStringArg = arguments?.getString("date")

       if (dateStringArg != null) {
           val year = dateStringArg!!.substring(0, 4).toInt()
            val month = dateStringArg!!.substring(5, 7).toInt()
            val day = dateStringArg!!.substring(8, 10).toInt()
            println("$year  $month $day")
            goToDate(year, month, day)
       }
     }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        viewModel.errorForbidden.observe(this, androidx.lifecycle.Observer {
            if(it!!){
                binding.toestemmingClickable.visibility = View.VISIBLE
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.calender -> {
                val today = LocalDate.now()
                val year = today.year
                val month = today.monthValue
                val day = today.dayOfMonth
                goToDate(year, month, day)
                requireActivity().invalidateOptionsMenu()
                return true
            }
            R.id.settingsFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
            }
            R.id.uurroosterFragment -> {
                popUpCalender(item)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val today = LocalDate.now()
        val year = today.year
        val month = today.monthValue
        val day = today.dayOfMonth

        menu.findItem(R.id.calender).title = "$day-$month-$year"

        return super.onPrepareOptionsMenu(menu)
    }

    private fun popUpCalender(item: MenuItem) {
        val today = LocalDate.now()
        val year = today.year
        val month = today.monthValue
        val day = today.dayOfMonth

        val dpd = DatePickerDialog(requireContext(), { view, mYear, mMonth, mDay ->
            var monthModified = mMonth + 1
            item.title = "$mDay-$monthModified-$mYear"
            goToDate(mYear, monthModified, mDay)
        }, year, month -1, day
        )
        dpd.show()
    }

    private fun goToDate(year: Int, month: Int, day: Int){
        var l = LocalDate.of(year, month, day)
        val ldt = l.atStartOfDay()
        val ldtPlus8Hour = ldt.plusHours(8)
        binding.weekView.post {
            println(ldtPlus8Hour)
            binding.weekView.scrollToDateTime(ldtPlus8Hour)
        }

        viewModel.getVivesEvents(LocalDate.now())
    }

    private fun setupCorrectOrientationSettings() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.weekView.numberOfVisibleDays = 5
            binding.weekView.hourHeight = 90
        }
    }

    private fun onClickToestemmingNavigateToWeb(){
        binding.weekView.visibility = View.GONE
        binding.toestemmingWebView.visibility = View.VISIBLE

        setUpWebViewToestemming()

        viewModel.navigateToWebToestemmingFinished()
    }


    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    private fun setUpWebViewToestemming(){
        binding.toestemmingWebView.settings.javaScriptEnabled = true
        binding.toestemmingWebView.webViewClient = object  : WebViewClient(){
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                println(description)
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView, req: WebResourceRequest, err: WebResourceError) {
                onReceivedError(view, err.errorCode, err.description.toString(), req.url.toString())
            }

            override fun onPageFinished(view: WebView, url: String) {
                if(url.contains("/mobile/timetable/unauthorized")){
                    view.isVisible = false
                    view.loadUrl("javascript:HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
                    navigateBackToUurroosterUnauthorized()
                } else if (url.contains("/mobile/timetable/authorized")){
                    view.isVisible = false
                    view.loadUrl("javascript:HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
                    navigateBackToUurroosterAuthorized()
                } else if (url.contains("/login/oauth2/code/kul/?code")){
                    view.isVisible = false
                    view.loadUrl("javascript:HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
                    navigateBackToUurroosterAuthorized()
                }
                super.onPageFinished(binding.toestemmingWebView, url)
            }
        }
        binding.toestemmingWebView.loadUrl(getString(R.string.vivesplus_base_link) + "/mobile/timetable/authorize/")
    }


    private fun navigateBackToUurroosterAuthorized(){
        binding.toestemmingClickable.visibility = View.GONE
        viewModel.getVivesEvents(LocalDate.now())
        binding.weekView.visibility = View.VISIBLE
    }


    private fun navigateBackToUurroosterUnauthorized(){
        binding.toestemmingClickable.visibility = View.VISIBLE
        binding.weekView.visibility = View.VISIBLE
    }

    private fun navigateToDetailScreen(event : Event){
        val checkedVivesEvent = checkIfWeekViewEventIsRight(event)
        if(checkedVivesEvent != null){
            this.findNavController().navigate(UurroosterFragmentDirections.actionUurroosterFragmentToUurroosterDetailFragment(
                checkedVivesEvent.id, checkedVivesEvent.type)
            )
        }
    }

    private fun makeTitleTimeString(startTime: LocalDateTime, endTime: LocalDateTime, title: String) : String{
        val timeString = "(" + UurroosterDecorator().interpretTimeHourAndMinutes(startTime) + " - " + UurroosterDecorator().interpretTimeHourAndMinutes(endTime) + ")"
        return "$title $timeString"

    }

    private fun checkIfWeekViewEventIsRight(weekViewEvent : Event) : VivesEvent? {
        val vivesEvent = viewModel.vivesEvents.value?.get(weekViewEvent.id.toInt())!!

        var titleIsRight = false
        if(vivesEvent.allDay){
            if(vivesEvent.title.equals(weekViewEvent.title)){
                titleIsRight = true
            }
        } else {
            val vivesEventNewCheckTitle = makeTitleTimeString(DateTimeHelper().formatStringToDateTime(vivesEvent.startDateTimeString),
                DateTimeHelper().formatStringToDateTime(vivesEvent.endDateTimeString), vivesEvent.title)
            if(vivesEventNewCheckTitle.equals(weekViewEvent.title)){
                titleIsRight = true
            }
        }

        if(titleIsRight &&
            DateTimeHelper().formatStringToDateTime(vivesEvent.startDateTimeString).equals(weekViewEvent.startTime) &&
            vivesEvent.location.equals(weekViewEvent.location)){

            return vivesEvent
        }
        return null
    }
}
