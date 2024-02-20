package be.vives.vivesplus.screens.modernDashboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import be.vives.vivesplus.MainActivity
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.databinding.*
import be.vives.vivesplus.enum.DashboardType
import be.vives.vivesplus.enum.ProfileRole
import be.vives.vivesplus.enum.Severity
import be.vives.vivesplus.model.*
import be.vives.vivesplus.model.dashboardcard.*
import be.vives.vivesplus.screens.profilesettings.docent.studies.DocentEducationsViewModel
import be.vives.vivesplus.screens.profilesettings.docent.studies.DocentEducationsViewModelFactory
import be.vives.vivesplus.util.CheckerRole
import be.vives.vivesplus.util.LogoutHandler
import be.vives.vivesplus.util.PreferencesManager
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ModernDashboardFragment : Fragment(), ProfileDAOCallback, MessagesDAOCallback, MembersDAOCallback, StudentDAOCallback, DashboardDAOCallback, NotificationDAOCallback {

    private lateinit var viewModel: ModernDashboardViewModel
    private lateinit var viewModelDocentEducationsViewModel: DocentEducationsViewModel
    private lateinit var binding: FragmentModernDashboardBinding
    private lateinit var bindingDocentEducations: FragmentDocentEducationsBinding
    private val _daoProposals = MutableLiveData<StudyProposalsDAO>()

    //aparte bindings voor aparte cards
    private lateinit var daydelimeterBinding: DashboardCardDaydelimeterBinding
    private lateinit var currentcourseBinding: DashboardCardCurrentcourseBinding
    private lateinit var nextCourseTodayBinding: DashboardCardNextcoursetodayBinding
    private lateinit var laterCourseBinding: DashboardCardLatercoursetodayBinding
    private lateinit var nextCourseLaterBinding: DashboardCardNextcourselaterBinding
    private lateinit var weatherBinding: DashboardCardWeatherBinding
    private lateinit var messageBinding: DashboardCardMessageBinding
    private lateinit var cancelledcourseBinding: DashboardCardCancelledcourseBinding
    private lateinit var eventBinding: DashboardCardEventBinding
    private lateinit var alldayeventBinding: DashboardCardAlldayeventBinding
    private lateinit var trainBinding: DashboardCardTrainBinding
    private lateinit var busBinding: DashboardCardBusBinding
    private lateinit var cateringBinding: DashboardCardCateringBinding
    private lateinit var parkingBinding: DashboardCardParkingBinding

    private var selectedCampus: Campus? = null
    private lateinit var inflater: LayoutInflater
    private var student = false
    private var docent = false
    private var counter = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_modern_dashboard, container, false)
        val application = requireNotNull(this.activity).application
        val factory = ModernDashboardViewmodelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(ModernDashboardViewModel::class.java)


        bindingDocentEducations = DataBindingUtil.inflate(inflater, R.layout.fragment_docent_educations, container, false)

        val factoryDocentEducations = DocentEducationsViewModelFactory(application, requireContext())
        viewModelDocentEducationsViewModel = ViewModelProvider(this, factoryDocentEducations).get(DocentEducationsViewModel::class.java)

        binding.myModel = viewModel

        bindingDocentEducations.viewModel = viewModelDocentEducationsViewModel

        this.inflater = inflater

        setHasOptionsMenu(true)

        if(isAdded){
            //fix not attached to context
            val context = requireContext()

            init(context)
        }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard_nav_menu, menu)
        viewModel.menu.value = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dashboard_notifications -> {
                findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToPresenceQRFragment())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun init(context : Context) {
        setObservers()

        if(!PreferencesManager().getStringFromPreferences(context, "jwt_token").isNullOrBlank()) {
            ProfileDAO(context, this).getProfile()
            MessagesDAO(context, this).getMessages()
        }

        FirebaseApp.initializeApp(context);
    }

    private fun setObservers() {
        viewModel.navigateToQr.observe(viewLifecycleOwner, Observer { if (it) { navigateQr() } })

        /*viewModel.counter.observe(viewLifecycleOwner, Observer {
            if(viewModel.menu.value != null && viewModel.menu.value!!.isNotEmpty()){
                if(it == 1) {
                    viewModel.menu.value!!.get(0).setIcon(R.drawable.new_notification)
                } else if( it == -1){
                    viewModel.menu.value!!.get(0).setIcon(R.drawable.no_new_notification)
                }
            }
        })*/
    }


    @SuppressLint("SetTextI18n")
    override fun dataLoaded(profile: Profile) {
        if(!PreferencesManager().getStringFromPreferences(requireContext(), "jwt_token").isNullOrBlank()) {
            val profileRole : ProfileRole? = CheckerRole(requireContext()).checkRole()
            if (profileRole != null) {
                if(profileRole == ProfileRole.STUDENT) {
                    this.student = true
                    StudentDAO(requireContext(), this).getMe()
                }
                if(profileRole == ProfileRole.MEMBER) {
                    this.docent = true
                    MembersDAO(requireContext(), this).getInfo()
                }
            }
        } else {
            navigateToMainActivity()
        }
    }

    private fun sendDeviceToken(){
        val areNotificationsEnabled = NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
        val token = PreferencesManager().getStringFromPreferences(requireContext(), "deviceToken")
        var BDT = PreferencesManager().getStringFromPreferences(requireContext(), "BDT")

        if(areNotificationsEnabled){
            if(!token.isNullOrBlank()){
                if(!BDT.equals(token)){
                    NotificationDAO(requireContext(), this).post(FirebaseMessaging.getInstance().token.result)
                    PreferencesManager().writeStringToPreferences(requireContext(), "deviceToken", "")
                } else {
                    println("-----------token gekend in backend------------")
                }
            } else {
                println("-----------token is leeg------------")
            }
        } else {
            //controle of notification is geweigerd in het verleden
            val permission = PreferencesManager().getBoolFromPreferences(requireContext(), requireContext().getString(R.string.pref_permission_notifications))
            if (permission == null || permission != true) {
                println("----------geen toestemming------------")
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(getString(R.string.toestemming_helderheid))
                builder.setMessage(getString(R.string.notificatie_message))

                builder.setPositiveButton(getString(R.string.Toestemming_helderheid_ok)) { _, _ ->
                    showNotificationSettings(requireContext())
                }

                builder.setNeutralButton(getString(R.string.annuleer)){ _, _ -> }
                builder.setNegativeButton(R.string.Toestemming_helderheid_niet_ok){ _, _ ->
                    PreferencesManager().writeBoolToPreferences(requireContext(), requireContext().getString(R.string.pref_permission_notifications), true)
                }

                val dialog : AlertDialog = builder.create()
                dialog.show()
            }



        }
    }

    fun showNotificationSettings(context: Context, channelId: String? = null) {
        val notificationSettingsIntent = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O /*26*/ -> Intent().apply {
                action = when (channelId) {
                    null -> Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    else -> Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                }
                channelId?.let { putExtra(Settings.EXTRA_CHANNEL_ID, it) }
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P /*28*/) {
                    flags += Intent.FLAG_ACTIVITY_NEW_TASK
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP /*21*/ -> Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra("app_package", context.packageName)
                putExtra("app_uid", context.applicationInfo.uid)
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT /*19*/ -> Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:${context.packageName}")
            }
            else -> null
        }
        notificationSettingsIntent?.let(context::startActivity)
    }


    override fun setError(error: Int) {
        if(error == 401){
            LogoutHandler.showLogoutAlert(requireContext(), requireActivity())
        }
    }


    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun dataLoaded(members: MutableList<Member>) {}

    override fun dataLoaded(member: Member) {
        val begroeting = getBegroeting(getCellphoneTime())
        if (member.firstName.isNotEmpty() && member.lastName.isNotEmpty()){
            binding.begroetingTv.text = begroeting
            binding.nameTv.text = member.firstName
        }

        selectedCampus = member.campusses.first()
        DashboardDAO(requireContext(), this).getAll()
    }

    private fun navigateQr() {
        requireView().findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToPresenceQRFragment())
        viewModel.btnQrFinished()
    }

    override fun dataLoaded(student: Student) {
        val begroeting = getBegroeting(getCellphoneTime())
        if (student.firstName.isNotEmpty() && student.lastName.isNotEmpty()){
            binding.begroetingTv.text = begroeting
            binding.nameTv.text = student.firstName
        }

        selectedCampus = student.campus
        DashboardDAO(requireContext(), this).getAll()
    }

    override fun putSucces() {}

    override fun dataLoadedFailed() {}

    private fun getCellphoneTime(): Int {
        val now = LocalDateTime.now()
        return now.hour
    }

    private fun getBegroeting(uur: Int): String{
        val morningStart = 5
        val afternoonStart = 12
        val eveningStart = 18
        val nightStart = 22

        return when (uur) {
            in morningStart..afternoonStart -> {
                this.getString(R.string.good_morning)
            }
            in afternoonStart..eveningStart -> {
                this.getString(R.string.good_afternoon)
            }
            in eveningStart..nightStart -> {
                this.getString(R.string.good_evening)
            }
            else -> this.getString(R.string.good_night)
        }
    }

    override fun dataLoaded(messages: ArrayList<Message>) {
        if (context != null && activity != null) {
            if (checkNewMessages(messages)) {
                Toast.makeText(context, this.getString(R.string.nieuwe_berichten), Toast.LENGTH_SHORT).show()
                viewModel.counter.value = 1
            } else {
                viewModel.counter.value = -1
            }
        }
    }

    private fun checkNewMessages(messages: ArrayList<Message>): Boolean {
        var newMessage = false
        var lastdate = ""

        if (!PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_last_date_message_viewed)).isNullOrEmpty()) {
            lastdate = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_last_date_message_viewed))!!
        } else{
            if(messages.size != 0){
                newMessage = true
            }
        }

        for(message in messages) {
            if (lastdate < message.startDate.toString()) {
                newMessage = true
            }
        }
        return newMessage
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun dashboardDataLoaded(dashboardCards: ArrayList<DashboardCard>) {
        sendDeviceToken()
        dashboardCards.forEach {
            when (it.type) {
                DashboardType.DAYDELIMITER -> {
                    val detailcard: DashboardCardDetailDayDelimeter = it.detail as DashboardCardDetailDayDelimeter

                    daydelimeterBinding = DashboardCardDaydelimeterBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)
                    daydelimeterBinding.txtDate.text = detailcard.date
                }
                DashboardType.CURRENTCOURSE -> {
                    val detailcard: DashboardCardDetailCurrentCourse = it.detail as DashboardCardDetailCurrentCourse

                    currentcourseBinding = DashboardCardCurrentcourseBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)
                    currentcourseBinding.txtCourse.text = detailcard.longDescription
                    currentcourseBinding.txtStartLesson.text = detailcard.startTime
                    currentcourseBinding.txtEndLesson.text = detailcard.endTime
                    currentcourseBinding.txtRoomCurrentCourse.text = detailcard.roomName

                    if (it.action != "noaction") {
                        currentcourseBinding.imgActionCourseNow.visibility = View.VISIBLE
                    }
                    if (it.action == "uurrooster") {
                        currentcourseBinding.currentcourseid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToUurroosterFragment(detailcard.date))
                        }
                    }
                }
                DashboardType.NEXTCOURSETODAY -> {
                    val detailcard: DashboardCardDetailNextCourseToday = it.detail as DashboardCardDetailNextCourseToday

                    nextCourseTodayBinding = DashboardCardNextcoursetodayBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)
                    nextCourseTodayBinding.txtCourse.text = detailcard.longDescription
                    nextCourseTodayBinding.txtStartLesson.text = detailcard.startTime
                    nextCourseTodayBinding.txtEndLesson.text = detailcard.endTime
                    nextCourseTodayBinding.txtRoomNextCourseToday.text = detailcard.roomName

                    if (it.action != "noaction") {
                        nextCourseTodayBinding.imgActionNextCourseToday.visibility = View.VISIBLE
                    }
                    if (it.action == "uurrooster") {
                        nextCourseTodayBinding.nexcoursetodaycardid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToUurroosterFragment(detailcard.date))
                        }
                    }
                }
                DashboardType.LATERCOURSETODAY -> {
                    val detailcard: DashboardCardDetailLaterCourseToday = it.detail as DashboardCardDetailLaterCourseToday

                    laterCourseBinding = DashboardCardLatercoursetodayBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)
                    laterCourseBinding.txtCourse.text = detailcard.longDescription
                    laterCourseBinding.txtStartLesson.text = detailcard.startTime
                    laterCourseBinding.txtEndLesson.text = detailcard.endTime
                    laterCourseBinding.txtRoom.text = detailcard.roomName

                    if (it.action != "noaction") {
                        laterCourseBinding.imgActionLaterCourseToday.visibility = View.VISIBLE
                    }
                    if (it.action == "uurrooster") {
                        laterCourseBinding.latercoursetodayid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToUurroosterFragment(detailcard.date))
                        }
                    }
                }
                DashboardType.NEXTCOURSELATER -> {
                    val detailcard: DashboardCardDetailNextCourseLaterData = it.detail as DashboardCardDetailNextCourseLaterData

                    nextCourseLaterBinding = DashboardCardNextcourselaterBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)
                    nextCourseLaterBinding.txtCourse.text = detailcard.longDescription
                    nextCourseLaterBinding.txtStartLesson.text = detailcard.startTime
                    nextCourseLaterBinding.txtEndLesson.text = detailcard.endTime

                    if (it.action != "noaction") {
                        nextCourseLaterBinding.imgActionNextCourseLater.visibility = View.VISIBLE
                    }
                    if (it.action == "uurrooster") {
                        nextCourseLaterBinding.nextcourselaterid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToUurroosterFragment(detailcard.date))
                        }
                    }
                }
                DashboardType.WEATHER -> {
                    val detailcard: DashboardCardDetailWeather = it.detail as DashboardCardDetailWeather

                    weatherBinding = DashboardCardWeatherBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)

                    var imageUrl = detailcard.icon
                    imageUrl = imageUrl!!.replace("http:", "https:")
                    Picasso.with(context)
                        .load(imageUrl)
                        .resize(150, 150)
                        .into(weatherBinding.weatherIcon)

                    weatherBinding.locatieTemperatuurTv.text = "VIVES ${detailcard.campusName} ${detailcard.temp}°"
                }
                DashboardType.MESSAGE -> {
                    val detailcard: DashboardCardDetailMessage = it.detail as DashboardCardDetailMessage
                    messageBinding = DashboardCardMessageBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)

                    if (detailcard.title.count() != 0) {
                        messageBinding.txtTitle.text = detailcard.title
                    } else {
                        messageBinding.txtTitle.visibility = View.GONE
                    }
                    messageBinding.txtDescription.text = detailcard.description

                    if (it.action != "noaction") {
                        messageBinding.imgActionMessage.visibility = View.VISIBLE
                    }
                    if (it.action == "web") {
                        messageBinding.messageid.setOnClickListener {
                            findNavController().navigate(
                                ModernDashboardFragmentDirections.actionModernDashboardFragmentToWebViewFragment(
                                    detailcard.url!!
                                )
                            )
                        }
                    } else if (it.action == "profiel") {
                        if (this.student) {
                            messageBinding.messageid.setOnClickListener {
                                findNavController()
                                    .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToStudentProfileSettingsFragment())
                            }
                        } else if (this.docent) {
                            messageBinding.messageid.setOnClickListener {
                                findNavController()
                                    .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToDocentProfileSettingsFragment())
                            }
                        }
                    } else if (it.action == "catering") {
                        messageBinding.messageid.setOnClickListener {
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToRestaurantFragment())
                        }
                    } else if (it.action == "uurrooster") {
                        messageBinding.messageid.setOnClickListener {
                            findNavController().navigate(
                                ModernDashboardFragmentDirections.actionModernDashboardFragmentToUurroosterFragment(
                                    null
                                )
                            )
                        }
                    } else if (it.action == "bus") {
                        messageBinding.messageid.setOnClickListener {
                            findNavController().navigate(
                                ModernDashboardFragmentDirections.actionModernDashboardFragmentToOpenbaarvervoerTrafficFragment(
                                    0.toString()
                                )
                            )
                        }
                    } else if (it.action == "train") {
                        messageBinding.messageid.setOnClickListener {
                            findNavController().navigate(
                                ModernDashboardFragmentDirections.actionModernDashboardFragmentToOpenbaarvervoerTrafficFragment(
                                    1.toString()
                                )
                            )
                        }
                    } else if (it.action == "mededeling") {
                        messageBinding.messageid.setOnClickListener {
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragment2ToMessagesFragment())
                        }
                    }

                    else if (it.action == "opleiding") {
                        messageBinding.messageid.setOnClickListener {
                            //val studyProposals: Array<StudyProposal> = _daoProposals.value!!.getProposals();
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToDocentStudyProposalFragment(viewModelDocentEducationsViewModel.proposals.value!!.toTypedArray()))
                        }
                    }

                    if (detailcard.severity != Severity.NORMAL) {
                        messageBinding.txtTitle.setTextColor((Color.parseColor(detailcard.severity.fontColor)))
                        messageBinding.txtDescription.setTextColor((Color.parseColor(detailcard.severity.fontColor)))

                        val color = Color.parseColor(detailcard.severity.backgroundColor)
                        val drawable = GradientDrawable()
                        drawable.shape = GradientDrawable.RECTANGLE
                        drawable.setColor(color)
                        drawable.cornerRadius = resources.getDimension(R.dimen.corner_standard)
                        messageBinding.messageid.background = drawable
                    }
                }
                DashboardType.CANCELLEDCOURSE -> {
                    val detailcard: DashboardCardDetailCancelledCourse = it.detail as DashboardCardDetailCancelledCourse

                    cancelledcourseBinding = DashboardCardCancelledcourseBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)
                    cancelledcourseBinding.txtStartLesson.text = detailcard.startTime
                    cancelledcourseBinding.txtEndLesson.text = detailcard.endTime
                    cancelledcourseBinding.txtCancelledMessage.text = detailcard.cancelledMessage

                    val strikethroughSpan = StrikethroughSpan()
                    cancelledcourseBinding.txtCourse.setText(
                        detailcard.longDescription,
                        TextView.BufferType.SPANNABLE
                    );
                    val spannable = (cancelledcourseBinding.txtCourse.text) as Spannable
                    spannable.setSpan(
                        strikethroughSpan,
                        0,
                        cancelledcourseBinding.txtCourse.text.count(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                    if (it.action != "noaction") {
                        cancelledcourseBinding.imgActionCancelledCourse.visibility = View.VISIBLE
                    }
                    if (it.action == "uurrooster") {
                        cancelledcourseBinding.cancelledcourseid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToUurroosterFragment(detailcard.date))
                        }
                    }
                }
                DashboardType.CATERING -> {
                    val detailcard: DashboardCardDetailCatering =
                        it.detail as DashboardCardDetailCatering

                    cateringBinding = DashboardCardCateringBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)

                    cateringBinding.txtText.text = detailcard.text
                    if (it.action == "catering") {
                        cateringBinding.cateringid.setOnClickListener {
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToRestaurantFragment())
                        }
                    }
                }

                DashboardType.BUS -> {
                    val detailcard: DashboardCardDetailBus = it.detail as DashboardCardDetailBus
                    busBinding = DashboardCardBusBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)

                    busBinding.txtText.text = detailcard.text

                    if (it.action == "bus") {
                        busBinding.busid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToOpenbaarvervoerTrafficFragment(0.toString()))
                        }
                    }
                }

                DashboardType.TRAIN -> {
                    val detailcard: DashboardCardDetailTrain = it.detail as DashboardCardDetailTrain
                    trainBinding=DashboardCardTrainBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)

                    trainBinding.txtText.text = detailcard.text

                    if (it.action == "train") {
                        trainBinding.trainid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToOpenbaarvervoerTrafficFragment(1.toString()))
                        }
                    }
                }
                DashboardType.ALLDAYEVENT -> {
                    val detailcard: DashboardCardDetailAllDayEvent = it.detail as DashboardCardDetailAllDayEvent

                    alldayeventBinding = DashboardCardAlldayeventBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)
                    alldayeventBinding.titelEvent.text = detailcard.description

                    if (it.action != "noaction") {
                        alldayeventBinding.imgActionAllDayEvent.visibility = View.VISIBLE
                    }
                    if(it.action == "uurrooster") {
                        alldayeventBinding.alldayeventid.setOnClickListener {
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToUurroosterFragment(detailcard.date))
                        }
                    }
                    if (it.action == "profiel") {
                        if (this.student) {
                            alldayeventBinding.alldayeventid.setOnClickListener {
                                findNavController()
                                    .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToStudentProfileSettingsFragment())
                            }
                        } else if (this.docent) {
                            alldayeventBinding.alldayeventid.setOnClickListener {
                                findNavController()
                                    .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToDocentProfileSettingsFragment())
                            }
                        }
                    }
                    if (it.action == "catering") {
                        alldayeventBinding.alldayeventid.setOnClickListener {
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToRestaurantFragment())
                        }
                    }
                    if (it.action == "bus") {
                        alldayeventBinding.alldayeventid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToOpenbaarvervoerTrafficFragment(0.toString()))
                        }
                    }
                    if (it.action == "train") {
                        alldayeventBinding.alldayeventid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToOpenbaarvervoerTrafficFragment(1.toString()))
                        }
                    }
                    if (it.action == "mededeling") {
                        alldayeventBinding.alldayeventid.setOnClickListener {
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragment2ToMessagesFragment())
                        }
                    }
                }
                DashboardType.EVENT -> {
                    val detailcard: DashboardCardDetailEvent = it.detail as DashboardCardDetailEvent
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    eventBinding = DashboardCardEventBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)

                    if (detailcard.date.equals(LocalDateTime.now().format(formatter))){
                        if (detailcard.startTime.equals("null")) {
                            eventBinding.txtStartUur.text = getString(R.string.eindUur)
                            eventBinding.txtendUur.text = detailcard.endTime
                            eventBinding.imageFlow.setImageResource(R.drawable.timelineblauweind)
                            eventBinding.imageFlow.visibility = View.VISIBLE
                        } else if (detailcard.endTime.equals("null")) {
                            eventBinding.txtStartUur.text = getString(R.string.Start_time)
                            eventBinding.txtendUur.text = detailcard.startTime
                            eventBinding.imageFlow.setImageResource(R.drawable.timelineblauwbegin)
                            eventBinding.imageFlow.visibility = View.VISIBLE
                        } else {
                            eventBinding.txtStartUur.text = detailcard.startTime
                            eventBinding.txtendUur.text = detailcard.endTime
                            eventBinding.imageFlow.setImageResource(R.drawable.timelineblauw)
                            eventBinding.imageFlow.visibility = View.VISIBLE
                        }
                    } else {
                        if(detailcard.startTime.equals("null")) {
                            eventBinding.txtStartUur.text = getString(R.string.eindUur)
                            eventBinding.txtendUur.text = detailcard.endTime
                        } else if (detailcard.endTime.equals("null")) {
                            eventBinding.txtStartUur.text = getString(R.string.Start_time)
                            eventBinding.txtendUur.text = detailcard.startTime
                        } else {
                            eventBinding.txtStartUur.text = detailcard.startTime
                            eventBinding.txtendUur.text = detailcard.endTime
                        }
                    }

                    eventBinding.titelEvent.text = detailcard.description

                    if (it.action != "noaction") {
                        eventBinding.imgActionEvent.visibility = View.VISIBLE
                    }
                    if(it.action == "uurrooster") {
                        eventBinding.eventid.setOnClickListener {
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToUurroosterFragment(detailcard.date))
                        }
                    }
                    if (it.action == "profiel") {
                        if (this.student) {
                            eventBinding.eventid.setOnClickListener {
                                findNavController()
                                    .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToStudentProfileSettingsFragment())
                            }
                        } else if (this.docent) {
                            eventBinding.eventid.setOnClickListener {
                                findNavController()
                                    .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToDocentProfileSettingsFragment())
                            }
                        }
                    }
                    if (it.action == "catering") {
                        eventBinding.eventid.setOnClickListener {
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToRestaurantFragment())
                        }
                    }
                    if (it.action == "bus") {
                        eventBinding.eventid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToOpenbaarvervoerTrafficFragment(0.toString()))
                        }
                    }
                    if (it.action == "train") {
                        eventBinding.eventid.setOnClickListener {
                            findNavController().popBackStack()
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToOpenbaarvervoerTrafficFragment(1.toString()))
                        }
                    }
                    if (it.action == "mededeling") {
                        eventBinding.eventid.setOnClickListener {
                            findNavController()
                                .navigate(ModernDashboardFragmentDirections.actionModernDashboardFragment2ToMessagesFragment())
                        }
                    }
                }
                DashboardType.PARKING -> {
                    val detailcard: DashboardCardDetailParking = it.detail as DashboardCardDetailParking

                    parkingBinding = DashboardCardParkingBinding.inflate(inflater,binding.dynamicscrolviewgroup,true)
                    parkingBinding.txtText.text = detailcard.text

                    if (it.action != "noaction") {
                        parkingBinding.imgActionParking.visibility = View.VISIBLE
                    }
                    if (it.action == "parkeer") {
                        parkingBinding.parkingid.setOnClickListener {
                            findNavController().navigate(ModernDashboardFragmentDirections.actionModernDashboardFragmentToParkingTicketFragment())
                        }
                    }
                }

            }
            binding.progressBar13.visibility = View.GONE
        }

    }

    override fun postOrPutSuccesful() {
        println("-----------------Versturen devicetoken succesvol-------------------")
        PreferencesManager().writeStringToPreferences(requireContext(), "BDT", FirebaseMessaging.getInstance().token.result!!)
    }

    override fun setErrorPost(error: Int) {
        if(error == 400){
            PreferencesManager().writeStringToPreferences(requireContext(), "BDT", "")
        }
    }
}
