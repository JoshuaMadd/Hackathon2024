package be.vives.vivesplus.screens.profilesettings.docent.searchstudies

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentDocentSearchStudiesBinding
import be.vives.vivesplus.model.Study

class DocentSearchStudiesFragment : Fragment() {

    private lateinit var binding : FragmentDocentSearchStudiesBinding
    private lateinit var viewModel: DocentSearchStudiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docent_search_studies, container, false)

        val application = requireNotNull(this.activity).application
        val factory = DocentSearchStudiesViewModelFactory(application, requireContext())
        viewModel = ViewModelProvider(this, factory).get(DocentSearchStudiesViewModel::class.java)

        binding.viewModel = viewModel

        val searchStudyAdapter = SearchStudyAdapter(AddListener {
            addStudyFromList(it)
        })

        binding.searchStudiesList.layoutManager = LinearLayoutManager(activity)
        binding.searchStudiesList.adapter = searchStudyAdapter

        setObservers(searchStudyAdapter)

        binding.txtSearchStudy.setOnKeyListener(View.OnKeyListener{_, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                viewModel.onPressEnter()
                viewModel.onPressEnterFinished()
                return@OnKeyListener true
            }
            false
        })

        binding.setLifecycleOwner(this)
        return binding.root
    }

    private fun setObservers(searchStudyAdapter: SearchStudyAdapter){
        viewModel.errorSearchName.observe(viewLifecycleOwner, Observer {
            it?.let{
                binding.txtSearchStudy.setError(it)
                binding.labelNoStudiesFound.visibility = View.GONE
                searchStudyAdapter.submitList(null)
                (binding.searchStudiesList.adapter as SearchStudyAdapter).notifyDataSetChanged()
            }
        })
        viewModel.studies.observe(viewLifecycleOwner, Observer{
            if(it != null){
                if(it.isEmpty()){
                    binding.labelNoStudiesFound.visibility = View.VISIBLE
                    searchStudyAdapter.submitList(null)
                    (binding.searchStudiesList.adapter as SearchStudyAdapter).notifyDataSetChanged()
                }else{
                    searchStudyAdapter.submitList(it)
                    binding.labelNoStudiesFound.visibility = View.GONE
                    (binding.searchStudiesList.adapter as SearchStudyAdapter).notifyDataSetChanged()
                }
            } else {
                binding.labelNoStudiesFound.visibility = View.VISIBLE
                searchStudyAdapter.submitList(null)
                (binding.searchStudiesList.adapter as SearchStudyAdapter).notifyDataSetChanged()
            }
        })
        viewModel.addStudy.observe(viewLifecycleOwner, Observer{
            it?.let {
                (binding.searchStudiesList.adapter as SearchStudyAdapter).notifyDataSetChanged()
                viewModel.onAddStudyClickedFinished()
            }
        })
        viewModel.errorAlreadyHaveStudy.observe(viewLifecycleOwner, Observer {
            if(it!!){
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(getString(R.string.cant_add_study))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                val alert = builder.create()
                alert.show()
            }
        })
    }


    private fun addStudyFromList(study: Study){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.zeker))
        builder.setMessage(getString(R.string.sure_add_study))

        builder.setPositiveButton(getString(R.string.yes_add_study)) { _, _ ->
            viewModel.onAddStudyclicked(study)
            Toast.makeText(requireContext(), getString(R.string.study_was_added), Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(getString(R.string.annuleer)){ _, _ -> }

        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

}