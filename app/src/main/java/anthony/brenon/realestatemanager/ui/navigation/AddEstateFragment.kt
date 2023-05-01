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
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture
import anthony.brenon.realestatemanager.ui.MainActivity
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage
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
class AddEstateFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    EasyPermissions.PermissionCallbacks {

    companion object {
        const val AUTOCOMPLETE_REQUEST_CODE = 1
        const val PERMISSIONS_REQUEST_CODE = 101
        const val RESULT_CODE_CAMERA = 21
        const val RESULT_CODE_FOLDER = 22
    }

    private var _binding: FragmentAddEstateBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private lateinit var activity: MainActivity
    private lateinit var adapter: RecyclerViewImage
    private lateinit var estate: Estate
    private lateinit var image: Bitmap
    private val calendar = Calendar.getInstance()
    private var isNewEstate = true
    private val images = mutableListOf<Bitmap>()

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        initRecyclerViewImage()
        listenerClickView()
    }

    init {
        set()
    }

    private fun set() {
        isNewEstate = viewModel.isNewEstate
        estate =
            Estate(picture = BitmapFactory.decodeResource(resources, R.drawable.img_estate_test_1))

        if (!hasPermission()) requestPermissions()
        if (!Places.isInitialized()) Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
    }

    private fun observe() {
        when {
            isNewEstate -> {
                estate.onSaleDate = Utils.todayDate
                viewModel.agentSelected.observe(viewLifecycleOwner) {
                    estate.agentInChargeName = it.nameAgent
                    binding.agentNameTv.text = it.nameAgent
                }
            }

            else -> viewModel.estateSelected.observe(viewLifecycleOwner) {
                estate = it; populateView(); initRecyclerViewImage()
            }
        }
    }

    // populate view when edit estate
    private fun populateView() {
        binding.btnAddEstateCreate.setText(R.string.update)
        binding.apply {
            agentNameTv.text = estate.agentInChargeName
            btnAddAddress.text = estate.addressCity
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

    // insert new date when click on create/update estate
    private fun insertEstateData() {
        binding.apply {
            if (images.isNotEmpty()) estate.picture = images[0]
            estate.type = addEstateEtType.text.toString()
            estate.price = addEstateEtPrice.text.toString()
            estate.surface = addEstateEtSurface.text.toString()
            estate.roomsNumber = addEstateEtNbRoom.text.toString()
            estate.description = addEstateEtDescription.text.toString()
            estate.interestingPoint = addEstateEtInterestingPoint.text.toString()
            estate.numberOfPicture = images.size
        }
        // Condition for create or update estate
        if (listOf(
                estate.type,
                estate.price,
                estate.surface,
                estate.roomsNumber,
                estate.description,
                estate.interestingPoint
            ).any { it.isNotEmpty() } || images.isNotEmpty()
        ) {
            viewModel.insertEstate(requireContext(), estate).observe(viewLifecycleOwner) {
                estate.id = it
                for (image in images) {
                    viewModel.insertPicture(Picture(image, it))
                }
                Navigation.findNavController(binding.root).popBackStack()
            }
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.please_enter_all_information),
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun initRecyclerViewImage() {
        adapter = RecyclerViewImage {
            binding.layoutMainAdd.visibility = View.GONE
            binding.layoutImage.visibility = View.VISIBLE
            it?.let { image = it }
            binding.ivDetails.setImageBitmap(it)
        }
        binding.recyclerViewImage.adapter = adapter

        viewModel.getPicturesByEstate(estate.id).observe(viewLifecycleOwner) { pictures ->
            images.clear()
            for (picture in pictures) {
                images.add(picture.picture)
            }
            adapter.setData(images)
        }
    }

    private fun autoCompleteLauncher() {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
        } else {
            val fields: List<Place.Field> =
                listOf(Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setHint(getString(R.string.autocomplete_hint))
                .setCountry("FR")
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            binding.layoutMainAdd.visibility = View.GONE
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.layoutMainAdd.visibility = View.VISIBLE
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AUTOCOMPLETE_REQUEST_CODE -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        estate.lat = place.latLng?.latitude ?: 0.00
                        estate.lng = place.latLng?.longitude ?: 0.00
                        place.addressComponents?.let {
                            binding.btnAddAddress.text = it.asList()[1].name
                            estate.addressStreet = it.asList()[0].name
                            estate.addressCity = it.asList()[1].name
                            estate.addressCode = it.asList()[5].name
                            estate.addressCountry = it.asList()[4].name
                        }
                    }
                }
            }
            val newImage: Bitmap? = when (requestCode) {
                RESULT_CODE_CAMERA -> data?.getParcelableExtra("data")
                RESULT_CODE_FOLDER -> MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    data?.data
                )

                else -> null
            }
            newImage?.let {
                images.add(it)
                adapter.setData(images)
            }
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
            if (hasPermission()) onSelectAddPictureSource("camera")
            else requestPermissions()
            dialog.dismiss()
        }
        btnFolder.setOnClickListener {
            if (hasPermission()) onSelectAddPictureSource("folder")
            else requestPermissions()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun onSelectAddPictureSource(source: String) {
        when (source) {
            "camera" -> {
                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, RESULT_CODE_CAMERA)
            }

            "folder" -> {
                val i = Intent(Intent.ACTION_PICK)
                i.type = "image/*"
                startActivityForResult(i, RESULT_CODE_FOLDER)
            }
        }
    }
    // ADD PICTURE DIALOG END

    // PERMISSIONS START
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
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        binding.addEstateBtnPicture.isEnabled = true
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
        binding.addEstateBtnPicture.setOnClickListener { showCustomBuilder() }
        binding.btnAddAddress.setOnClickListener { autoCompleteLauncher() }
        binding.imgAddEstateBack.setOnClickListener { Navigation.findNavController(binding.root).popBackStack() }
        binding.btnAddEstateCreate.setOnClickListener { insertEstateData() }

        binding.addEstateBtnSale.setOnClickListener {
            if (estate.isSold()) {
                    estate.dateOfSale = ""
                    binding.addEstateBtnSale.setText(R.string.estate_sold)
                } else {
                    DatePickerDialog(
                        requireContext(),
                        this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
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