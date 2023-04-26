package anthony.brenon.realestatemanager.ui.navigation


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.BuildConfig
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentAddEstateBinding
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture
import anthony.brenon.realestatemanager.ui.MainActivity
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage
import anthony.brenon.realestatemanager.utils.EstateStatus
import anthony.brenon.realestatemanager.utils.Utils
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddEstateFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    EasyPermissions.PermissionCallbacks {

    companion object {
        const val AUTOCOMPLETE_REQUEST_CODE = 1
        const val PERMISSIONS_REQUEST_CODE = 101
        const val RESULT_CODE_CAMERA = 21
        const val RESULT_CODE_FOLDER = 22
    }

    private lateinit var activity: MainActivity
    private var _binding: FragmentAddEstateBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var adapter: RecyclerViewImage
    private val calendar = Calendar.getInstance()

    private lateinit var estate: Estate
    private lateinit var estateStatus: EstateStatus
    private val images = mutableListOf<Bitmap>()
    private lateinit var image: Bitmap
    private lateinit var currentAgent: Agent

    //TODO fix bug possible (permissions?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEstateBinding.inflate(inflater, container, false)

        estate = Estate(
            0,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0.00,
            0.00,
            "",
            "",
            "",
            "",
            BitmapFactory.decodeResource(resources, R.drawable.img_estate_test_1)
        )

        observerEstateStatus()
        observerAgentSelected()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasPermission()) requestPermissions()
        if (!Places.isInitialized()) Places.initialize(requireActivity(), BuildConfig.MAPS_API_KEY)

        if (estateStatus == EstateStatus.UPDATE_EXISTING_ESTATE) {
            populateView()
        } else {
            val date = Utils.todayDate
            estate.onSaleDate = date
        }

        binding.agentNameTv.text = estate.agentInChargeName

        initRecyclerViewImage()
        listenerClickView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observerEstateStatus() {
        // observer estate status
        viewModel.estateStatus.observe(activity) { status ->
            estateStatus = status
            if (estateStatus == EstateStatus.UPDATE_EXISTING_ESTATE) {
                viewModel.estateSelected.observe(requireActivity()) {
                    estate = it
                }
            }
        }
    }

    private fun observerAgentSelected() {
        // observer agent selected
        viewModel.agentSelected.observe(activity) {
            currentAgent = it
            estate.agentInChargeName = it.nameAgent
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        binding.layoutMainAdd.visibility = View.VISIBLE

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    estate.lat = place.latLng?.latitude ?: 0.00
                    estate.lng = place.latLng?.longitude ?: 0.00
                    place.addressComponents?.let {
                        binding.btnAddEstateAddress.text = it.asList()[1].name
                        estate.addressStreet = it.asList()[0].name
                        estate.addressCity = it.asList()[1].name
                        estate.addressCode = it.asList()[5].name
                        estate.addressCountry = it.asList()[4].name
                    }
                }
            }

            val pic: Bitmap? =
                when (requestCode) {
                    RESULT_CODE_CAMERA -> data?.getParcelableExtra("data")
                    RESULT_CODE_FOLDER -> MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        data?.data
                    )
                    else -> null
                }
            pic?.let {
                val newImage = it
                images.add(newImage)
                adapter.setData(images)
            }
        }
    }

    private fun autoCompleteLauncher() {
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity(), BuildConfig.MAPS_API_KEY)
        } else {
            val fields: List<Place.Field> =
                listOf(Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setHint(getString(R.string.autocomplete_hint))
                .setCountry("FR")
                .build(requireActivity())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            binding.layoutMainAdd.visibility = View.GONE
        }
    }

    // populate view when edit estate
    private fun populateView() {

        binding.btnAddEstateCreate.setText(R.string.update)

        binding.apply {
            btnAddEstateAddress.text = estate.addressCity
            addEstateEtType.setText(estate.type)
            addEstateEtSurface.setText(estate.surface)
            addEstateEtPrice.setText(estate.price)
            addEstateEtNbRoom.setText(estate.roomsNumber)
            addEstateEtInterestingPoint.setText(estate.interestingPoint)
            addEstateEtDescription.setText(estate.description)

            addEstateBtnSale.visibility = View.VISIBLE
            if (estate.isSold()) addEstateBtnSale.text = estate.dateOfSale
        }
    }

    // picker date result
    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val text = dateFormat.format(calendar.time)
        estate.dateOfSale = text
        binding.addEstateBtnSale.text = text
    }

    // insert new date when click on create/update estate
    private fun insertEstateData() {

        binding.apply {

            val type = addEstateEtType.text.toString()
            val price = addEstateEtPrice.text.toString()
            val surface = addEstateEtSurface.text.toString()
            val roomNb = addEstateEtNbRoom.text.toString()
            val description = addEstateEtDescription.text.toString()
            val interesting = addEstateEtInterestingPoint.text.toString()

            if (images.isNotEmpty()) estate.picture = images[0]
            estate.type = type
            estate.price = price
            estate.surface = surface
            estate.roomsNumber = roomNb
            estate.description = description
            estate.interestingPoint = interesting
        }

        // Condition for create or update estate
        if (estate.type.isNotEmpty() &&
            estate.price.isNotEmpty() &&
            estate.surface.isNotEmpty() &&
            estate.roomsNumber.isNotEmpty() &&
            estate.description.isNotEmpty() &&
            estate.interestingPoint.isNotEmpty() &&
            images.isNotEmpty()
        ) {
            viewModel.insertEstate(requireContext(), estate).observe(activity) {
                estate.id = it
                for (image in images) { viewModel.insertPicture(Picture(image, it)) }
                Navigation.findNavController(binding.root).popBackStack()
            }
        } else {
            Snackbar.make(binding.root, "please enter all information", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun initRecyclerViewImage() {
        adapter = RecyclerViewImage {
            binding.layoutMainAdd.visibility = View.GONE
            binding.layoutImage.visibility = View.VISIBLE
            if (it != null) {
                image = it
            }
            binding.ivDetails.setImageBitmap(it)
        }
        binding.recyclerViewImage.adapter = adapter

        // observer get pictures by estate
        viewModel.getPicturesByEstate(estate.id).observe(activity) {
            images.clear()
            for (picture in it) {
                images.add(picture.picture)
            }
            adapter.setData(images)
        }
    }

    // ADD PICTURE DIALOG START
    private fun showCustomBuilder() {
        val builder = AlertDialog.Builder(activity)
        val customView = LayoutInflater.from(activity)
            .inflate(R.layout.dialog_select_image_ressource, null)
        builder.setView(customView)
        val dialog = builder.create()

        val btnCamera = customView.findViewById<ImageButton>(R.id.dialog_btn_picture_from_camera)
        val btnFolder = customView.findViewById<ImageButton>(R.id.dialog_btn_picture_from_folder)

        btnCamera.setOnClickListener {
            addImageFromCamera()
            dialog.dismiss()
        }
        btnFolder.setOnClickListener {
            addImageFromFolder()
            dialog.dismiss()
        }


        dialog.show()
    }

    private fun addImageFromCamera() {
        if (hasPermission()) {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, RESULT_CODE_CAMERA)
        } else {
            requestPermissions()
        }
    }

    private fun addImageFromFolder() {
        if (hasPermission()) {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i, RESULT_CODE_FOLDER)
        }
    }
    // ADD PICTURE DIALOG END

    // PERMISSIONS START
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(activity).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        binding.apply {
            addEstateBtnPicture.isEnabled = true
        }
    }

    private fun hasPermission() =
        EasyPermissions.hasPermissions(
            activity,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            activity,
            "These permission are required for this application",
            PERMISSIONS_REQUEST_CODE,
            android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    // PERMISSIONS END

    private fun listenerClickView() {

        binding.addEstateBtnPicture.setOnClickListener {
            showCustomBuilder()
        }

        binding.btnAddEstateAddress.setOnClickListener { autoCompleteLauncher() }

        binding.imgAddEstateBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.btnAddEstateCreate.setOnClickListener {
            insertEstateData()
        }

        binding.addEstateBtnSale.setOnClickListener {
            if (!estate.isSold()) DatePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
            else {
                estate.dateOfSale = ""
                binding.addEstateBtnSale.setText(R.string.estate_sold)
            }
        }

        binding.imgImageClose.setOnClickListener {
            binding.layoutMainAdd.visibility = View.VISIBLE
            binding.layoutImage.visibility = View.GONE
        }

        binding.dialIvDelete.setOnClickListener {
            images.remove(image)
            adapter.setData(images)
            binding.layoutMainAdd.visibility = View.VISIBLE
            binding.layoutImage.visibility = View.GONE
        }
    }
}