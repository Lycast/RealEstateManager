package anthony.brenon.realestatemanager.ui.navigation


import android.app.Activity
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
import anthony.brenon.realestatemanager.utils.EstateStatus
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
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
    private lateinit var image: Bitmap
    private val calendar = Calendar.getInstance()
    private lateinit var estateStatus: EstateStatus

    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val CAMERA_REQUEST_CODE = 101
    private val FOLDER_REQUEST_CODE = 120

    //TODO address button need improve

    //TODO implement status 'sold' for estate sold  -add background sold

    //TODO surface/price/room must be numbers not string
    //TODO fix why we need 2 click for open address/picture

    //TODO we can't create or update an estate if we don't have all data we needed



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEstateBinding.inflate(inflater, container, false)

        estate = Estate(
            0, "type", "0", "0", "0", "description", "address",0.00,0.00,"interesting point", false, "", "", "agent"
            , BitmapFactory.decodeResource(resources, R.drawable.img_estate_test_1)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.estateStatus.observe(requireActivity()) {
            estateStatus = it
            if (estateStatus == EstateStatus.UPDATE_EXISTING_ESTATE) setUpdateEstate()
        }

        initRecyclerViewImage()
        initSpinnerAgents()
        listenerClickView()
    }

    private fun setUpdateEstate() {
        viewModel.estateSelected.observe(requireActivity()) {
            estate = it
            populateView()
        }
        binding.btnAddEstateCreate.setText(R.string.update)
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

    private fun populateView() {

        viewModel.getPicturesByEstate(estate.id).observe(requireActivity()) {
            for (picture in it) {
                images.add(picture.picture)
            }
            adapter.setData(images)
        }

        binding.apply {
            btnAddEstateAddress.text = estate.address
            //agent
            addEstateEtType.setText(estate.type)
            addEstateEtSurface.setText(estate.surface)
            addEstateEtPrice.setText(estate.price)
            addEstateEtNbRoom.setText(estate.roomsNumber)
            addEstateEtInterestingPoint.setText(estate.interestingPoint)
            addEstateEtDescription.setText(estate.description)

            addEstateBtnSale.visibility = View.VISIBLE
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
                    estate.lat = place.latLng?.latitude ?: 0.00
                    estate.lng = place.latLng?.longitude ?: 0.00
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
        val text = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}"
        estate.dateOfSale = text
        binding.addEstateBtnSale.text = text
        estate.isSale = true
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        estate.agentInChargeName = agentsData[position].nameAgent
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun listenerClickView() {
        binding.addEstateBtnPicture.setOnClickListener {
            //TODO add dialog select folder or camera
            addImageFromCamera()
        }
        binding.btnAddEstateAddress.setOnClickListener { autoCompleteLauncher() }
        binding.imgAddEstateBack.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment) }
        binding.btnAddEstateCreate.setOnClickListener {
            insertEstateData()
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }
        binding.addEstateBtnSale.setOnClickListener {
            if (!estate.isSale) DatePickerDialog(requireContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            else {
                estate.isSale = false
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

    private fun insertEstateData() {

        /*
        Le type de bien (appartement, loft, manoir, etc) ;
        Le prix du bien (en dollar) ;
        La surface du bien (en m2) ;
        Le nombre de pièces ;
        La description complète du bien ;
        Au moins une photo, avec une description associée. Vous devez gérer le cas où plusieurs photos sont présentes pour un bien ! La photo peut être récupérée depuis la galerie photos du téléphone, ou prise directement avec l'équipement ;
        L’adresse du bien ;
        Les points d’intérêts à proximité (école, commerces, parc, etc) ;
        Le statut du bien (toujours disponible ou vendu) ;
        La date d’entrée du bien sur le marché ;
        La date de vente du bien, s’il a été vendu ;
        L'agent immobilier en charge de ce bien.
        */

        binding.apply {
            val date = "${Calendar.DAY_OF_MONTH}/${Calendar.MONTH}/${Calendar.YEAR}"
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
            estate.dateOfSale = date
        }

        viewModel.insertEstate(requireContext(), estate).observe(requireActivity()) {
            estate.id = it
            for (image in images) {
                viewModel.insertPicture(Picture(image, it))
            }
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
            if (it != null) { image = it }
            binding.ivDetails.setImageBitmap(it)
        }
        binding.recyclerViewImage.adapter = adapter
        adapter.setData(images)
    }
}