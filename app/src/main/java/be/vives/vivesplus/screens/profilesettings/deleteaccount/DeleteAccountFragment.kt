package be.vives.vivesplus.screens.profilesettings.deleteaccount

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import be.vives.vivesplus.MainActivity
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentDeleteAccountBinding


class DeleteAccountFragment : Fragment() {

    private lateinit var viewModel: DeleteAccountViewModel
    private lateinit var binding: FragmentDeleteAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delete_account, container, false)

        val application = requireNotNull(this.activity).application
        val factory = DeleteAccountViewModelFactory(application, requireContext())
        viewModel = ViewModelProvider(this, factory).get(DeleteAccountViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.btnDelete.observe(viewLifecycleOwner, Observer { it ->
            if(it){
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(R.string.sureDelete)
                    .setCancelable(true)
                    .setPositiveButton(R.string.deleteUserTitle) { _, _ ->
                        viewModel.deleteAccount()
                        val a = activity as MainActivity
                        a.showNavigationAndStartScreen(R.id.loginFragment)
                    }.setNegativeButton(R.string.no) { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
                viewModel.btnDeleteAccountClickedFinished()
            }
        })

        return binding.root
    }

}