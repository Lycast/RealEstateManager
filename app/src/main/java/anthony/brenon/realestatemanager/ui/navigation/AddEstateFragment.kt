package anthony.brenon.realestatemanager.ui.navigation


import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentAddEstateBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage
import java.util.Calendar

class AddEstateFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentAddEstateBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var adapter: RecyclerViewImage
    private lateinit var estate: Estate
    private val images = mutableListOf<Bitmap>()
    private val calendar = Calendar.getInstance()
    private lateinit var date : String

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

        viewModel.insertEstate(requireContext(),estate).observe(requireActivity()) {
            estate.id = it
            setDataRecyclerView()
        }



        initRecyclerViewImage()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPicturesByEstate(estate.id).observe(requireActivity()) {
            if(it.isNotEmpty()) estate.picture = it[0].picture
        }

        buttonListener()
    }

    private fun initRecyclerViewImage() {
        adapter = RecyclerViewImage {
            binding.layoutMainAdd.visibility = View.GONE
            binding.layoutImage.visibility = View.VISIBLE
            binding.ivDetails.setImageBitmap(it)
        }
        binding.recyclerViewImage.adapter = adapter
    }

    private fun setDataRecyclerView() {
        viewModel.getPicturesByEstate(estate.id).observe(requireActivity()) {
            images.clear()
            for (picture in it) {
                images.add(picture.picture)
            }
            adapter.setData(images)
            if (images.isNotEmpty()) estate.picture = images[0]
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
            startActivityForResult(i, 101)
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
            startActivityForResult(i, 120)
        }
    }

    private fun getNewsData(): Estate {
        binding.apply {
            val type = addEstateEtType.text.toString()
            val price = addEstateEtPrice.text.toString()
            val surface = addEstateEtSurface.text.toString()
            val roomNb = addEstateEtNbRoom.text.toString()
            val description = addEstateEtDescription.text.toString()
            val address = addEstateEtAddress.text.toString()
            val interesting = addEstateEtInterestingPoint.text.toString()
            val isSale = false
            val agent = addEstateEtAgent.text.toString() // list agent

            estate.type = type
            estate.price = price
            estate.surface = surface
            estate.roomsNumber = roomNb
            estate.description = description
            estate.address = address
            estate.interestingPoint = interesting
            estate.isSale = isSale
            estate.agentInChargeName = agent
        }
        return estate
    }

    private fun buttonListener() {

        binding.addEstateBtnCamera.setOnClickListener { addImageFromCamera() }
        binding.addEstateBtnFolder.setOnClickListener { addImageFromFolder() }

        binding.btnAddEstateCancel.setOnClickListener {
            viewModel.deleteEstate(estate)
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }
        binding.btnAddEstateCreate.setOnClickListener {
            getNewsData()
            viewModel.updateEstate(estate)
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }

        binding.addEstateBtnStartSale.setOnClickListener {
            DatePickerDialog(requireContext(), this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
            date = "Start of sale"

        }
        binding.addEstateBtnEndSale.setOnClickListener {
            DatePickerDialog(requireContext(), this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
            date = "End of sale"

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
        val pic : Bitmap? =
        when (requestCode) {
            101 -> data?.getParcelableExtra("data")
            120 -> MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data?.data)
            else -> null
        }
        pic?.let {
            val newPicture = Picture(it, estate.id)
            viewModel.insertPicture(newPicture)
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
}