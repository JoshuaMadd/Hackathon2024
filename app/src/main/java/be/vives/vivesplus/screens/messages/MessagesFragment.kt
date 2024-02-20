package be.vives.vivesplus.screens.messages

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.CategoryDAO
import be.vives.vivesplus.dao.CategoryDAOCallback
import be.vives.vivesplus.dao.MessagesDAO
import be.vives.vivesplus.dao.MessagesDAOCallback
import be.vives.vivesplus.databinding.FragmentMessagesBinding
import be.vives.vivesplus.model.Category
import be.vives.vivesplus.model.Message
import be.vives.vivesplus.util.PreferencesManager
import java.time.LocalDateTime


class MessagesFragment : Fragment(), MessagesDAOCallback, CategoryDAOCallback,MessagesAdapter.onItemClickListener, OnBackPressedDispatcherOwner {

    private lateinit var messages: ArrayList<Message>
    private lateinit var lijstjecat: ArrayList<String>
    lateinit var binding: FragmentMessagesBinding
    private lateinit var categorySpinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false)
        binding.rvMessages.layoutManager = LinearLayoutManager(context)
        binding.rvMessages.adapter = MessagesAdapter(ArrayList(), requireContext(), this)
        CategoryDAO(requireContext(), this).getCategory()
        MessagesDAO(requireContext(), this).getMessages()

        val dateTimeNow = LocalDateTime.now().toString()
        PreferencesManager().writeStringToPreferences(
            requireContext(),
            requireContext().getString(R.string.pref_last_date_message_viewed),
            dateTimeNow
        )

        binding.rvMessages.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(
                MessagesFragmentDirections.actionGlobalModernDashboardFragment()
            )
            isEnabled = true
        }

        return binding.root
    }

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
    }

    override fun onItemClick(message: Message) {
        this.findNavController().navigate(
            MessagesFragmentDirections.actionMessagesFragmentToMessagesDetailFragment(message)
        )
    }
    private fun selector(m: Message): LocalDateTime? = m.startDate




    override fun dataLoaded(messages: ArrayList<Message>) {
        this.messages = messages

        if (this.messages.size == 0) {
            binding.geenberichten.visibility = View.VISIBLE
        }
        else{
            binding.messagesSpinner.visibility = View.VISIBLE
        }
       binding.rvMessages.adapter = MessagesAdapter(this.messages, requireContext(), this)

        createListener(this.messages)
    }

    private fun createListener(messages: ArrayList<Message>) {
        binding.messagesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedObject = binding.messagesSpinner.selectedItem
                    print(selectedObject)

                    if(selectedObject == "Alle berichten")
                    {
                        binding.rvMessages.adapter = MessagesAdapter(
                            messages,
                            requireContext(),
                            this@MessagesFragment
                        )
                        binding.rvMessages.adapter?.notifyDataSetChanged()
                    }
                    else{
                      val  mijnLijstje = ArrayList<Message>()
                        for(i in 0 until messages.size)
                        {
                            if(messages[i].categorieBeschrijving.contains(selectedObject.toString()))
                                {
                                    mijnLijstje.add(messages[i])
                                }
                            }

                        binding.rvMessages.adapter = MessagesAdapter(
                            mijnLijstje,
                                requireContext(),
                         this@MessagesFragment
                        )
                        binding.rvMessages.adapter?.notifyDataSetChanged()
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
    }
    private fun createSpinnerAdapter(categorieslst: ArrayList<Category>) {

         lijstjecat = ArrayList()
        for(i in 0 until categorieslst.size)
        {

            val icoontje = categorieslst[i].icoon?.replace("-", "_").plus("_solid")
            val id = requireContext().resources.getIdentifier(
                icoontje,
                "string",
                requireContext().packageName
            )
                lijstjecat.add(categorieslst[i].categorieBeschrijving)

        }

        lijstjecat.add(0, "Alle berichten")

        categorySpinnerAdapter = ArrayAdapter(
           requireContext(), android.R.layout.simple_list_item_1,
            lijstjecat
        )
        binding.messagesSpinner.adapter = categorySpinnerAdapter

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.category_menu, menu)
    }

    override fun dataLoaded(categories: MutableList<Category>) {
        createSpinnerAdapter(categories as ArrayList<Category>)
    }

    override fun onStop() {
        val locatdatetime = LocalDateTime.now().toString()
        PreferencesManager().writeStringToPreferences(
            requireContext(),
            requireContext().getString(R.string.pref_date_messages),
            locatdatetime
        )
        super.onStop()
    }
}





