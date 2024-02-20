package be.vives.vivesplus.screens.traffic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import be.vives.vivesplus.R
import be.vives.vivesplus.screens.traffic.bus.BusFragment
import be.vives.vivesplus.screens.traffic.train.TrainFragment
import com.google.android.material.tabs.TabLayout

private const val NUM_PAGES = 2

class OpenbaarvervoerTrafficFragment : Fragment() {

    private lateinit var mPager: ViewPager
    private var toPageString : String? = null
    private var toPageInt : Int? = null
    private var screensetCounter = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_openbaarvervoer_traffic, container, false)

        toPageString = arguments?.getString("toPage")
        if(toPageString != null){
            toPageInt = Integer.parseInt(toPageString!!)
        }

        mPager = view.findViewById(R.id.openbaarvervoerPager)
        mPager.adapter = OpenbaarvervoerSlidePagerAdapter(requireActivity().supportFragmentManager)
        val tabLayout = view.findViewById(R.id.trafficTabs) as TabLayout
        tabLayout.setupWithViewPager(mPager, true)

        return view
    }
    private inner class OpenbaarvervoerSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            if(toPageInt == 0 && position == 0){
                return BusFragment()
            }
            if(toPageInt == 0 && position == 1){
                return TrainFragment()
            }
            if(toPageInt == 1 && position == 0){
                return TrainFragment()
            }
            if(toPageInt == 1 && position == 1){
                return BusFragment()
            }

            if(toPageInt == 0 && position == 0){
                screensetCounter++
                return BusFragment()
            } else if(toPageInt == 1 && position == 0){
                screensetCounter++
                return TrainFragment()
            } else if(toPageInt == 0 && position == 1){
                screensetCounter++
                return BusFragment()
            } else if(toPageInt == 1 && position == 1){
                screensetCounter++
                return TrainFragment()
            }


            return if(toPageInt != null) {

                if(position == toPageInt){
                    if(toPageInt == 0){
                        screensetCounter ++
                        return  BusFragment()
                    }
                    if(toPageInt == 1){
                        screensetCounter ++
                        return TrainFragment()
                    }
                }
                if(position != toPageInt) {
                    if(toPageInt == 0 && screensetCounter > 0){
                        screensetCounter ++
                        if (toPageInt == 0) {
                            return  TrainFragment()
                        } else {
                            return BusFragment()
                        }
                    }
                    if(toPageInt == 1 && screensetCounter > 0){
                        screensetCounter ++
                        if (toPageInt == 0) {
                            return  TrainFragment()
                        } else {
                            return BusFragment()
                        }
                    }
                }

                if(toPageInt == 0 && position == 0){
                    BusFragment()
                } else if( toPageInt == 0 && position != 0) {
                    TrainFragment()
                } else if( toPageInt == 1 && position == 1) {
                    TrainFragment()
                } else if( toPageInt == 1 && position != 1) {
                    BusFragment()
                } else {
                    BusFragment()
                }
            } else {
                when(position) {
                    0 -> BusFragment()
                    else -> TrainFragment()
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? { // Generate title based on item position
            return if(toPageInt != null) {
                if(toPageInt == 0 && position == 0){
                    requireContext().getString(R.string.Bus)
                } else if( toPageInt == 0 && position != 0) {
                    requireContext().getString(R.string.trein)
                } else if( toPageInt == 1 && position == 1) {
                    requireContext().getString(R.string.Bus)
                } else if( toPageInt == 1 && position != 1) {
                    requireContext().getString(R.string.trein)
                } else {
                    null
                }
            } else {
                when (position) {
                    0 -> requireContext().getString(R.string.Bus)
                    1 -> requireContext().getString(R.string.trein)
                    else -> null
                }
            }
        }
    }

}
