package anthony.brenon.realestatemanager.ui.navigation


import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.BuildConfig
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentAddEstateBinding
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*


class AddEstateFragment : Fragment(), DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private var _binding: FragmentAddEstateBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var adapter: RecyclerViewImage
    private lateinit var estate: Estate
    private val agentsData: MutableList<Agent> = mutableListOf()
    private val images = mutableListOf<Bitmap>()
    private val calendar = Calendar.getInstance()
    private lateinit var date : String

    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val CAMERA_REQUEST_CODE = 101
    private val FOLDER_REQUEST_CODE = 120

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEstateBinding.inflate(inflater, container, false)

        estate = Estate(
            0, "type", "0", "0", "0", "description", "address", "interesting point", false, "", "", "agent"
            , BitmapFactory.decodeResource(resources, R.drawable.img_estate_test_1)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initRecyclerViewImage()
        initSpinnerAgents()
        listenerClickView()
    }

    private fun autoCompleteLauncher() {
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity(), BuildConfig.MAPS_API_KEY)
        } else {
            val fields: List<Place.Field> = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setHint(getString(R.string.autocomplete_hint))
                .setCountry("FR")
                .build(requireActivity())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            binding.layoutMainAdd.visibility = View.GONE
        }
    }

    private fun initSpinnerAgents() {
        // access the spinner
        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            agentsData
        )

        binding.agentSpinner.adapter = adapter
        binding.agentSpinner.onItemSelectedListener = this


        viewModel.allAgents.observe(requireActivity()) { agents ->
            // access the items of the list
            agentsData.addAll(agents)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerViewImage() {
        adapter = RecyclerViewImage {
            binding.layoutMainAdd.visibility = View.GONE
            binding.layoutImage.visibility = View.VISIBLE
            binding.ivDetails.setImageBitmap(it)
        }
        binding.recyclerViewImage.adapter = adapter
        adapter.setData(images)
    }

    private fun addImageFromCamera() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                111
            )
        } else {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, CAMERA_REQUEST_CODE)
        }
    }

    private fun addImageFromFolder() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                121
            )
        } else {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i, FOLDER_REQUEST_CODE)
        }
    }

    private fun insertEstateData() {

        binding.apply {
            val type = addEstateEtType.text.toString()
            val price = addEstateEtPrice.text.toString()
            val surface = addEstateEtSurface.text.toString()
            val roomNb = addEstateEtNbRoom.text.toString()
            val description = addEstateEtDescription.text.toString()
            val interesting = addEstateEtInterestingPoint.text.toString()
            val isSale = false

            if(images.isNotEmpty()) estate.picture = images[0]
            estate.type = type
            estate.price = price
            estate.surface = surface
            estate.roomsNumber = roomNb
            estate.description = description
            estate.interestingPoint = interesting
            estate.isSale = isSale
        }

        viewModel.insertEstate(requireContext(),estate).observe(requireActivity()) {
            estate.id = it

            for (image in images) {
                viewModel.insertPicture(Picture(image,it))
            }
        }
    }

    private fun listenerClickView() {
        binding.addEstateBtnCamera.setOnClickListener { addImageFromCamera() }
        binding.addEstateBtnFolder.setOnClickListener { addImageFromFolder() }
        binding.btnAddEstateAddress.setOnClickListener { autoCompleteLauncher() }
        binding.imgAddEstateBack.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment) }
        binding.btnAddEstateCreate.setOnClickListener {
            insertEstateData()
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }
        binding.addEstateBtnStartSale.setOnClickListener {date = "Start of sale"
            DatePickerDialog(requireContext(), this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.addEstateBtnEndSale.setOnClickListener {date = "End of sale"
            DatePickerDialog(requireContext(), this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.imgImageClose.setOnClickListener {
            binding.layoutMainAdd.visibility = View.VISIBLE
            binding.layoutImage.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        binding.layoutMainAdd.visibility = View.VISIBLE
        Log.i("TAG", "request code: $requestCode")

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    Log.i("TAG", "Place: " + place.name + "  " + place.latLng)
                    estate.address = place.address as String
                    Log.i("TAG", "estate address : " + estate.address )
                }
            }

            val pic: Bitmap? =
                when (requestCode) {
                    CAMERA_REQUEST_CODE -> data?.getParcelableExtra("data")
                    FOLDER_REQUEST_CODE -> MediaStore.Images.Media.getBitmap(
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year,month,dayOfMonth)
        setDate()
    }

    private fun setDate() {
        val text: String
        when(date) {
            "Start of sale" -> {
                    text = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}"
                    estate.onSaleDate = text
                    binding.addEstateBtnStartSale.text = text
                }
            "End of sale" -> {
                    text = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}"
                    estate.dateOfSale = text
                    binding.addEstateBtnEndSale.text = text
                }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        estate.agentInChargeName = agentsData[position].nameAgent
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}