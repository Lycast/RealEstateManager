package anthony.brenon.realestatemanager.ui.navigation


import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import anthony.brenon.realestatemanager.ui.MainActivity
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage
import anthony.brenon.realestatemanager.utils.Code
import anthony.brenon.realestatemanager.utils.Utils
import anthony.brenon.realestatemanager.utils.DataConverters.bitmapsToByteArrayList
import anthony.brenon.realestatemanager.utils.DataConverters.stringsToByteArrayList
import anthony.brenon.realestatemanager.utils.DataConverters.toBase64List
import anthony.brenon.realestatemanager.utils.DataConverters.toBitmapList
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddEstateFragment : Fragment(),
    DatePickerDialog.OnDateSetListener {

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
    private var agent = ""
    private var addressStreet = ""
    private var addressCode = ""
    private var addressCity = ""
    private var addressCountry = ""
    private var lat = 0.00
    private var lng = 0.00

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
        set()
        observe()
        initRecyclerViewImage()
        listenerClickView()
    }

    private fun set() {
        isNewEstate = viewModel.isNewEstate

        estate = Estate(picture = BitmapFactory.decodeResource(resources, R.drawable.no_image))

        if (!Places.isInitialized()) Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
    }

    private fun observe() {
        when {
            isNewEstate -> {
                viewModel.agentSelected.observe(viewLifecycleOwner) {
                    if (it != null) agent = it.nameAgent
                }
                estate.agentInChargeName = agent
                binding.agentNameTv.text = agent
            }

            else -> viewModel.estateSelected.observe(viewLifecycleOwner) {
                estate = it
                populateView()
                initRecyclerViewImage()
            }
        }
    }

    // populate view when edit estate
    private fun populateView() {
        binding.btnAddEstateCreate.setText(R.string.update)
        binding.apply {
            val imagesByte = estate.pictures.stringsToByteArrayList()
            CoroutineScope(Dispatchers.Main).launch {
                images.addAll(imagesByte.toBitmapList())
            }
            agentNameTv.text = estate.agentInChargeName
            btnAddAddress.text = estate.addressCity
            addEstateEtType.setText(estate.type)
            addEstateEtSurface.setText(estate.surface)
            addEstateEtPrice.setText(estate.price)
            addEstateEtNbRoom.setText(estate.roomsNumber)
            addEstateEtInterestingPoint.setText(estate.interestingPoint)
            addEstateEtDescription.setText(estate.description)
            addEstateBtnSold.visibility = View.VISIBLE
            if (estate.isSold()) addEstateBtnSold.text = estate.soldDate
        }
    }

    // insert new date when click on create/update estate
    private fun insertEstateData() {

        binding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                val imagesByte = images.bitmapsToByteArrayList()
                // Condition for create or update estate
                if (listOf(
                        addEstateEtType.text.toString(),
                        addEstateEtPrice.text.toString(),
                        addEstateEtSurface.text.toString(),
                        addEstateEtNbRoom.text.toString(),
                        addEstateEtDescription.text.toString(),
                        addressStreet,
                        addEstateEtInterestingPoint.text.toString(),
                        Utils.todayDate,
                        agent,
                    ).any { it.isNotEmpty() } || images.isNotEmpty()
                ) {
                    estate = Estate(
                        id = 0,
                        type = addEstateEtType.text.toString(),
                        price = addEstateEtPrice.text.toString(),
                        surface = addEstateEtSurface.text.toString(),
                        roomsNumber = addEstateEtNbRoom.text.toString(),
                        description = addEstateEtDescription.text.toString(),
                        addressStreet = addressStreet,
                        addressCity = addressCity,
                        addressCode = addressCode,
                        addressCountry = addressCountry,
                        lat = lat,
                        lng = lng,
                        interestingPoint = addEstateEtInterestingPoint.text.toString(),
                        saleDate = Utils.todayDate,
                        agentInChargeName = agent,
                        pictures = imagesByte.toBase64List(),
                        picture = images[0],
                        numberOfPicture = images.size
                    )
                    viewModel.insertEstate(estate)
                    Navigation.findNavController(binding.root).popBackStack()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.please_enter_all_information),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
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
        adapter.setData(images)
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
            startActivityForResult(intent, Code.AUTOCOMPLETE_REQUEST_CODE)
            binding.layoutMainAdd.visibility = View.GONE
        }
    }

    // picker date result
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val text = dateFormat.format(calendar.time)
        estate.soldDate = text
        binding.addEstateBtnSold.text = text
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.layoutMainAdd.visibility = View.VISIBLE
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Code.AUTOCOMPLETE_REQUEST_CODE -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        lat = place.latLng?.latitude ?: 0.00
                        lng = place.latLng?.longitude ?: 0.00
                        place.addressComponents?.let {
                            binding.btnAddAddress.text = it.asList()[1].name
                            addressStreet = it.asList()[0].name
                            addressCity = it.asList()[1].name
                            addressCode = it.asList()[5].name
                            addressCountry = it.asList()[4].name
                        }
                    }
                }
            }
            val newImage: Bitmap? = when (requestCode) {
                Code.RESULT_CODE_CAMERA -> data?.getParcelableExtra("data")
                Code.RESULT_CODE_FOLDER -> {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        data?.data
                    )

                }

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
        Log.i("MYTAG", "viewmodel permission = ${viewModel.hasAllPermissions}")
        val builder = AlertDialog.Builder(activity)
        val customView = LayoutInflater.from(activity)
            .inflate(R.layout.dialog_select_image_ressource, null)
        builder.setView(customView)
        val dialog = builder.create()
        val btnCamera = customView.findViewById<ImageButton>(R.id.dialog_btn_picture_from_camera)
        val btnFolder = customView.findViewById<ImageButton>(R.id.dialog_btn_picture_from_folder)
        btnCamera.setOnClickListener {
            if (viewModel.hasAllPermissions) onSelectAddPictureSource("camera")
            dialog.dismiss()
        }
        btnFolder.setOnClickListener {
            if (viewModel.hasAllPermissions) onSelectAddPictureSource("folder")
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun onSelectAddPictureSource(source: String) {
        when (source) {
            "camera" -> {
                Log.i("MYTAG", "onSelectAddPictureSource = camera")
                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, Code.RESULT_CODE_CAMERA)
            }

            "folder" -> {
                Log.i("MYTAG", "onSelectAddPictureSource = folder")
                val i = Intent(Intent.ACTION_PICK)
                i.type = "image/*"
                startActivityForResult(i, Code.RESULT_CODE_FOLDER)
            }
        }
    }
    // ADD PICTURE DIALOG END

    private fun listenerClickView() {
        binding.addEstateBtnPicture.setOnClickListener { if (viewModel.hasAllPermissions) showCustomBuilder() }
        binding.btnAddAddress.setOnClickListener { autoCompleteLauncher() }
        binding.imgAddEstateBack.setOnClickListener { Navigation.findNavController(binding.root).popBackStack() }
        binding.btnAddEstateCreate.setOnClickListener { insertEstateData() }

        binding.addEstateBtnSold.setOnClickListener {
            if (estate.isSold()) {
                estate.soldDate = ""
                binding.addEstateBtnSold.setText(R.string.estate_sold)
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