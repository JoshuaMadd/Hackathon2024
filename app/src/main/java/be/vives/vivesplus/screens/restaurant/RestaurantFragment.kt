package be.vives.vivesplus.screens.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.databinding.FragmentRestaurantBinding
import be.vives.vivesplus.enum.ProfileRole
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.Member
import be.vives.vivesplus.model.Restaurant
import be.vives.vivesplus.model.Student
import be.vives.vivesplus.util.CheckerRole
import be.vives.vivesplus.util.PreferencesManager
import java.util.*
import kotlin.collections.ArrayList

class RestaurantFragment : Fragment(), RestaurantDAOCallback, CampusDAOCallback, StudentDAOCallback, MembersDAOCallback {

    private lateinit var viewModel: RestaurantViewModel
    private lateinit var binding: FragmentRestaurantBinding
    private var firstCampusFromUser: Campus? = null
    private lateinit var campusAdapter: ArrayAdapter<Campus>
    private var lastselectedcampusid : Int? = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = RestaurantViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(RestaurantViewModel::class.java)

        binding.restaurantList.layoutManager = LinearLayoutManager(context)
        binding.restaurantList.adapter = RestaurantAdapter(requireContext(), this, ArrayList())
        binding.myModel = viewModel

        init()

        return binding.root
    }

    private fun init() {
        loadPageInfo()
    }

    private fun loadPageInfo() {
        val profileRole : ProfileRole? = CheckerRole(requireContext()).checkRole()
        if (profileRole != null) {
            if(profileRole == ProfileRole.STUDENT) {
                StudentDAO(requireContext(), this).getMe()
            }
            else if(profileRole == ProfileRole.MEMBER) {
                MembersDAO(requireContext(), this).getInfo()
            }
        }
    }

    override fun dataLoaded(members: MutableList<Member>) {}

    override fun dataLoaded(member: Member) {
        firstCampusFromUser = member.campusses.first()
        CampusDAO(requireContext(), this).get()
    }

    override fun dataLoaded(student: Student) {
        firstCampusFromUser = student.campus
        CampusDAO(requireContext(), this).get()
    }

    override fun putSucces() {}

    override fun dataLoadedFailed() {}

    override fun campusDataLoaded(campusList: ArrayList<Campus>) {
        if (PreferencesManager().getIntFromPreferences(requireContext(), requireContext().getString(R.string.lastselectedcampusrestaurant)) != null){
            this.lastselectedcampusid = PreferencesManager().getIntFromPreferences(requireContext(), requireContext().getString(R.string.lastselectedcampusrestaurant))
        }
        createSpinnerAdapter(campusList)
        createListener()
        if (lastselectedcampusid == -1) {
            RestaurantDAO(requireContext(), this).getRestos(firstCampusFromUser!!.id)
        } else {
            RestaurantDAO(requireContext(), this).getRestos(lastselectedcampusid!!)
        }
    }

    private fun createSpinnerAdapter(campusList: java.util.ArrayList<Campus>) {
        if (lastselectedcampusid != -1) {
            var position = 0
            for (campus in campusList) {
                if (campus.id == lastselectedcampusid) {
                    Collections.swap(campusList, 0, position)
                }
                position++
            }
        }
        else {
            var position = 0
            for (campus in campusList) {
                if (campus.id == firstCampusFromUser!!.id) {
                    Collections.swap(campusList, 0, position)
                }
                position++
            }
        }
        campusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, campusList)
        binding.campusspinner.adapter = campusAdapter
    }

    private fun createListener() {
        binding.campusspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val selectedObject = binding.campusspinner.selectedItem as Campus
                    PreferencesManager().writeIntToPreferences(requireContext(), requireContext().getString(R.string.lastselectedcampusrestaurant), selectedObject.id)
                    RestaurantDAO(requireContext(), this@RestaurantFragment).getRestos(selectedObject.id)
                }
            }
    }

    override fun dataLoaded(restaurants: ArrayList<Restaurant>) {
        binding.indicator.visibility = View.GONE

        if(restaurants.size == 0) {
            binding.nofound.visibility = View.VISIBLE
        }
        else if (restaurants.size > 0){
            binding.nofound.visibility = View.GONE
        }

        binding.restaurantList.adapter = RestaurantAdapter(requireContext(), this, restaurants)
    }

    fun navigateToWebView(link: String) {
        this.findNavController().navigate(RestaurantFragmentDirections.actionRestaurantFragmentToWebViewFragment(link))
    }
}