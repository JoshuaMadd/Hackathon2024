package be.vives.vivesplus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import be.vives.vivesplus.databinding.ActivityMainBinding
import be.vives.vivesplus.util.PreferencesManager
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.tasks.Task
import android.content.IntentSender.SendIntentException
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.enum.ProfileRole
import be.vives.vivesplus.model.Member
import be.vives.vivesplus.model.Profile
import be.vives.vivesplus.model.Student
import be.vives.vivesplus.screens.modernDashboard.ModernDashboardFragment
import be.vives.vivesplus.screens.uurrooster.UurroosterFragment
import be.vives.vivesplus.util.CheckerRole
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity(), StudentDAOCallback, MembersDAOCallback, ProfileDAOCallback{

    lateinit var binding: ActivityMainBinding
    private val IMMEDIATE_APP_UPDATE_REQ_CODE: Int = 124
    private lateinit var appUpdateManager: AppUpdateManager
    private val prefManager: PreferencesManager = PreferencesManager()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkUpdate()
        loadCorrectScreenOnStart()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.modernDashboardFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.modernDashboardFragment)
                    true
                }
                R.id.uurroosterFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.uurroosterFragment)
                    true
                }
                R.id.menuFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.menuFragment)
                    true
                }
                else -> false
            }
        }

         val intent = intent
         val fragmentId = intent.getStringExtra("fragmentid")

         if(fragmentId != null) {
             navigateToFragment(fragmentId)
         }

        try {
            val token: String? = FirebaseMessaging.getInstance().token.result
            if(token != null){
                PreferencesManager().writeStringToPreferences(this, "deviceToken", token)
            }
        } catch (ex: Exception){

        }
    }

    private fun navigateToFragment(fragmentId: String){
        var fragment : Fragment? = null

        when(fragmentId) {
            "home" -> {
                fragment = ModernDashboardFragment()
            }
            "test" -> {
                fragment = UurroosterFragment()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkUpdate() {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo)
            }
        }
        appUpdateInfoTask.addOnFailureListener { ex ->
            Log.i("TEST", ex.toString())
        }
        appUpdateInfoTask.addOnCompleteListener {
            Log.i("TEST", it.toString())
        }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, IMMEDIATE_APP_UPDATE_REQ_CODE)
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                checkUpdate()
            }
            else
                if (resultCode == RESULT_OK) {
                } else {
                    checkUpdate()
                }
        }
    }

    private fun loadCorrectScreenOnStart() {
        val jwt = prefManager.getStringFromPreferences(applicationContext, applicationContext.getString(R.string.pref_jwt))
        if(!jwt.isNullOrBlank()) {
            val profileRole : ProfileRole? = CheckerRole(this).checkRole()
            if (profileRole != null) {
                if(profileRole == ProfileRole.STUDENT) {
                    StudentDAO(this, this).getMe()
                }
                else if(profileRole == ProfileRole.MEMBER) {
                    MembersDAO(this, this).getInfo()
                }
            } else {
                ProfileDAO(this, this).getProfile()
            }
        } else {
            showNavigationAndStartScreen(R.id.loginFragment)
        }
    }


    fun showNavigationAndStartScreen(screen: Int) {
        if(screen == R.id.loginFragment || screen == R.id.studentTutorialFragment || screen == R.id.docentTutorialFragment) {
            binding.bottomNav.visibility = View.GONE
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation)

        graph.setStartDestination(screen)

        navHostFragment.navController.graph = graph
    }

    override fun dataLoaded(members: MutableList<Member>) {}

    override fun dataLoaded(member: Member) {
        PreferencesManager().writeIntToPreferences(applicationContext,"aantalRotaties",0)
        if(member.campusses.size == 0 || member.fieldOfStudies.size == 0) {
            showNavigationAndStartScreen(R.id.docentTutorialFragment)
        }
        else {
            showNavigationAndStartScreen(R.id.modernDashboardFragment)
        }
    }

    override fun dataLoaded(student: Student) {
        if(student.campus == null || student.fieldOfStudy == null) {
            showNavigationAndStartScreen(R.id.studentTutorialFragment)
        } else {
            showNavigationAndStartScreen(R.id.modernDashboardFragment)
        }
    }

    override fun dataLoaded(profile: Profile) {
        loadCorrectScreenOnStart()
    }

    override fun putSucces() {

    }

    override fun dataLoadedFailed() {
        showNavigationAndStartScreen(R.id.loginFragment)
    }

    override fun setError(error: Int) {
    }
}
