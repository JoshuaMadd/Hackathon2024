package be.vives.vivesplus.screens.profilesettings.docent

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.MembersDAO
import be.vives.vivesplus.dao.MembersDAOCallback
import be.vives.vivesplus.databinding.FragmentDocentProfileSettingsBinding
import be.vives.vivesplus.model.Member
import be.vives.vivesplus.util.PreferencesManager
import be.vives.vivesplus.util.UploadUtility
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class DocentProfileSettingsFragment : Fragment(), MembersDAOCallback {

    private lateinit var viewModel: DocentProfileSettingsViewModel
    private lateinit var binding: FragmentDocentProfileSettingsBinding


    val root =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .toString()
    private val REQUEST_CODE_GALLERY = 1001
    private val REQUEST_CODE_CAMERA = 1002


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_docent_profile_settings,
            container,
            false
        )

        val application  = requireNotNull(this.activity).application
        val fact = DocentProfileSettingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(DocentProfileSettingsViewModel::class.java)
        binding.myModel = viewModel
        init()

        return binding.root
    }

    private fun init() {
        setObservers()
        loadPageInfo()
    }

    private fun setObservers() {
        viewModel.name.observe(viewLifecycleOwner, Observer { name ->
            binding.txtName.text = name.toString()
        })

        viewModel.kulNumber.observe(viewLifecycleOwner, Observer { kulnumber ->
            binding.txtKulNumber.text = kulnumber.toString()
        })

        viewModel.btnGeneralInfoClicked.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                this.findNavController()
                    .navigate(DocentProfileSettingsFragmentDirections.actionDocentProfileSettingsFragmentToDocentGeneralInfoFragment())
                viewModel.btnGeneralInfoClickedFinished()
            }
        })

        viewModel.btnFOSClicked.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                this.findNavController()
                    .navigate(DocentProfileSettingsFragmentDirections.actionDocentProfileSettingsFragmentToDocentFieldOfStudySettingsFragment())
                viewModel.btnFOSClickedFinished()
            }
        })

        viewModel.btnCampusClicked.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                this.findNavController()
                    .navigate(DocentProfileSettingsFragmentDirections.actionDocentProfileSettingsFragmentToDocentCampusSettingsFragment())
                viewModel.btnCampusClickedFinished()
            }
        })

        viewModel.btnEducationsClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController()
                    .navigate(DocentProfileSettingsFragmentDirections.actionDocentProfileSettingsFragmentToDocentEductionsFragment())
                viewModel.btnEducationsClickedFinished()
            }
        })

        viewModel.btnTransportClicked.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                this.findNavController()
                    .navigate(DocentProfileSettingsFragmentDirections.actionDocentProfileSettingsFragmentToDocentTransportSettingsFragment())
                viewModel.btnTransportClickedFinished()
            }
        })

        viewModel.imageProfileClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
                } else {
                    openCameraOrGallery()
                }
                viewModel.btnImageProfileClickedFinished()
            }
        })

        viewModel.turnProfileImgClicked.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                binding.turnClockwisePf.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN)
                binding.turnClockwisePf.isClickable = false
                turnPicture()
                viewModel.btnTurnProfileImgClickedFinished()
            }
        })

        viewModel.btnDeleteAccountClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it){
                this.findNavController().navigate(DocentProfileSettingsFragmentDirections.actionDocentProfileSettingsFragmentToDeleteAccountFragment())
                viewModel.btnDeleteAccountClickedFinished()
            }
        })
    }

    private fun turnPicture() {
        MembersDAO(requireContext(), this).putRotateProfile()
    }

    private fun loadPageInfo() {
        val role: String? = PreferencesManager().getStringFromPreferences(
            requireContext(), requireContext().getString(
                R.string.pref_role
            )
        )
        if (!role.isNullOrEmpty()) {
            if (role == "MEMBER") {
                MembersDAO(requireContext(), this).getInfo()
            }
        }
    }

    override fun dataLoaded(members: MutableList<Member>) {}

    override fun dataLoaded(member: Member) {
        val name = member.firstName + " " + member.lastName
        viewModel.setName(name)
        val kulNumber: String? = PreferencesManager().getStringFromPreferences(
            requireContext(), requireContext().getString(
                R.string.pref_kul_number
            )
        )
        viewModel.setKulNumber(kulNumber)
        var imageUrl = member.photoUrl
        if (imageUrl != null) {
            if (   PreferencesManager().getIntFromPreferences(requireContext(),"aantalRotaties") != -1) {
                Picasso.with(context).invalidate(member.photoUrl)
                PreferencesManager().writeIntToPreferences(requireContext(),"aantalRotaties",0)
                imageUrl =  imageUrl!!.plus(("?=" + System.currentTimeMillis()))
            }
            Picasso.with(context)
                .load(imageUrl)
                .into(binding.imageProfile)
        }
    }


    private fun openCameraOrGallery(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.select_an_option))
        builder.setMessage(getString(R.string.select_option_change_photo))
        builder.setCancelable(true)
            .setPositiveButton(getString(R.string.open_gallery)) { _, _ ->
                openGalleryForImage()
            }
            .setNegativeButton(getString(R.string.open_camera)) {_, _ ->
                openCamera()
            }

        builder.show()
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_CANCELED) {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAMERA && data != null) {
                val photo: Bitmap = data.extras?.get("data") as Bitmap
                val path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val myDir = File("$root/saved_images")
                myDir.mkdirs()
                val generator = Random()
                var n = 10000
                n = generator.nextInt(n)
                val fname = "Image-$n.jpeg"
                val file = File(myDir, fname)
                if (file.exists()) file.delete()
                try {
                    val out = FileOutputStream(file)
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    out.flush()
                    out.close()
                    data.putExtra(MediaStore.EXTRA_OUTPUT, path)
                    binding.imageProfile.setImageBitmap(photo)
                    UploadUtility(requireActivity(), requireContext()).uploadFile(
                        file.absolutePath,
                        "profilepicture"
                    )


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GALLERY) {
                if (data!!.data != null) {
                    var uri = data.data!!

                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? = requireActivity().contentResolver.query(
                        uri!!,
                        filePathColumn,
                        null,
                        null,
                        null
                    )
                    cursor!!.moveToFirst();
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    cursor.close()
                    var loadedBitmap = BitmapFactory.decodeFile(picturePath)
                    var exif: ExifInterface? = null
                    try {
                        val pictureFile = File(picturePath)
                        exif = ExifInterface(pictureFile.absolutePath)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    var orientation = ExifInterface.ORIENTATION_NORMAL

                    if (exif != null) orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )

                    when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> loadedBitmap =
                            rotateBitmap(loadedBitmap, 90)
                        ExifInterface.ORIENTATION_ROTATE_180 -> loadedBitmap =
                            rotateBitmap(loadedBitmap, 180)
                        ExifInterface.ORIENTATION_ROTATE_270 -> loadedBitmap =
                            rotateBitmap(loadedBitmap, 270)
                    }
                    val path: File = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                    )

                    val myDir = File("$root/saved_images")
                    myDir.mkdirs()
                    val generator = Random()
                    var n = 10000
                    n = generator.nextInt(n)
                    val fname = "Image-$n.jpeg"
                    val file = File(myDir, fname)
                    if (file.exists()) file.delete()
                    try {
                        val out = FileOutputStream(file)
                        loadedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                        out.flush()
                        out.close()
                        data.putExtra(MediaStore.EXTRA_OUTPUT, path)
                        binding.imageProfile.setImageBitmap(loadedBitmap)
                        UploadUtility(requireActivity(), requireContext()).uploadFile(
                            file.absolutePath,
                            "profilepicture.jpg"
                        )
                        UploadUtility(requireActivity(), requireContext()).uploadFile(uri)
                        binding.imageProfile.setImageURI(uri)

                    }
                    catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun putSucces() {
        //turn fronted profiel 90degrees
        binding.imageProfile.rotation += 90f
        binding.turnClockwisePf.clearColorFilter()
        binding.turnClockwisePf.isClickable = true

    }


    override fun setError(error: Int) {
        if (error == 500) Toast.makeText(requireContext(), "Profielfoto niet gedraaid in backend", Toast.LENGTH_SHORT).show()
    }

}