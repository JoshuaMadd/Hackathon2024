package be.vives.vivesplus.screens.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentNoInternetBinding
import be.vives.vivesplus.util.CheckerConnection

class NoInternetFragment : Fragment() {

    private lateinit var viewModel: NoInternetViewModel
    private lateinit var binding: FragmentNoInternetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_no_internet, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = NoInternetViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(NoInternetViewModel::class.java)
        binding.myModel = viewModel

        setObservers()

        return binding.root
    }

    private fun setObservers() {
        viewModel.btnTryAgain.observe(viewLifecycleOwner, Observer {
            if(it){
                if(CheckerConnection(requireContext()).hasInternetConnection()){
                    requireActivity().onBackPressed()
                }
                viewModel.btnTryAgainFinished()
            }
        })
    }
}