package be.vives.vivesplus.screens.admin.searchdocent

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.MembersDAO
import be.vives.vivesplus.dao.MembersDAOCallback
import be.vives.vivesplus.databinding.FragmentSearchDocentDetailBinding
import be.vives.vivesplus.model.Member
import be.vives.vivesplus.util.PreferencesManager
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class SearchDocentDetailFragment : Fragment(), MembersDAOCallback{

    lateinit var binding: FragmentSearchDocentDetailBinding
    private lateinit var member: Member

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_docent_detail, container, false)
        binding.telLayout.setOnClickListener {
            val number = binding.docentTel.text
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: $number")
            startActivity(intent)
        }
        binding.mailLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
                .setType("plain/text")
                .putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.docentEmail.text.toString()))
            startActivity(Intent.createChooser(intent,"Send mail..."))
        }
        binding.buttonCallDocent.setOnClickListener {
            val number = binding.docentTel.text
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: $number")
            startActivity(intent)
        }

        binding.buttonMailDocent.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
                .setType("plain/text")
                .putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.docentEmail.text.toString()))
            startActivity(Intent.createChooser(intent,"Send mail..."))
        }

        binding.addToContactLayout.setOnClickListener {
            //
        }

        setHasOptionsMenu(true)

        getMemberToBeSearched()

        return binding.root
    }

    private fun getMemberToBeSearched() {
        val kulnumberToBeSearched = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.docent_to_be_searched_kul_number))
        MembersDAO(requireContext(), this).getInfoBasedOnKulNumber(kulnumberToBeSearched!!)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.absences_nav_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(requireContext(), getString(R.string.searchDocentInfo), Toast.LENGTH_LONG).show()
        return super.onOptionsItemSelected(item)
    }

    override fun dataLoaded(members: MutableList<Member>) {

    }

    @SuppressLint("SetTextI18n")
    override fun dataLoaded(member: Member) {
        val contactIntent = Intent(Intent.ACTION_INSERT_OR_EDIT).apply {
            type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
        }
        if (member.functieOmschrijving.isNotEmpty()) {
            binding.docentDescription.text = member.functieOmschrijving
        }
        else {
            binding.lblDocentDescription.visibility = View.GONE
            binding.docentDescription.visibility = View.GONE
        }
        if (member.email.isNotEmpty()) {
            binding.docentEmail.text = member.email
            contactIntent.apply {
                putExtra(ContactsContract.Intents.Insert.EMAIL, member.email)
                putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
            }
        }
        if (member.phoneNumber.isNotEmpty()) {
            binding.docentTel.text = member.phoneNumber
            contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, member.phoneNumber)
        }

        val docentName =  "${member.firstName} ${member.lastName}"
        binding.docentName.text = docentName
        contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, docentName)


        if(member.photoUrl != null){
            Picasso.with(context)
                .load(member.photoUrl)
                .resize(300,300)
                .into(binding.image)
        }
        else {
            binding.fade.visibility = View.GONE
            binding.image.visibility = View.GONE
        }

        binding.addToContact.setOnClickListener {
            try {
                addImage(contactIntent)
            } catch (e: Exception){
                print(e.localizedMessage)
            }

            startActivity(contactIntent)
        }
    }

    override fun putSucces() {

    }

    fun addImage(contactIntent: Intent){
        val bitmap = (binding.image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()

        val row = ContentValues().apply {
            put(ContactsContract.CommonDataKinds.Photo.PHOTO, image)
            put(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
        }
        val data = arrayListOf(row)
        contactIntent.putParcelableArrayListExtra(
            ContactsContract.Intents.Insert.DATA, data)
    }


}


