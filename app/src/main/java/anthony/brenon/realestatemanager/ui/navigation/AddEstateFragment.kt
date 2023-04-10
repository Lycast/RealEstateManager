package anthony.brenon.realestatemanager.ui.navigation

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class AddEstateFragment : Fragment() {

    private var _binding: FragmentAddEstateBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var adapter: RecyclerViewImage
    private var estate = Estate(0,null,null,null,null,null,null,null,null,null,null,null)
    private val images = mutableListOf<Bitmap>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEstateBinding.inflate(inflater, container, false)

        viewModel.insertEstate(estate)
        getCurrentEstateId()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonListener()
        initRVImage()
    }

    //todo how get estate id created


    private fun getCurrentEstateId() {
        viewModel.allEstates.observe(requireActivity()) {
            Log.i("MY_TAG","id estate 'getCurrentEstateId' = ${it[it.lastIndex]}")
            estate = it[it.lastIndex]
            initRVImage()
        }
    }

    private fun initRVImage() {

        adapter = RecyclerViewImage{
            //todo add listener image
        }

        binding.recyclerViewImage.adapter = adapter
        Log.i("MY_TAG","id estate 'initRVImage' = ${estate.id}")
        viewModel.getPicturesByEstate(estate.id).observe(requireActivity()) {
            for (picture in it) {
                images.add(picture.picture)
            }
            adapter.setData(images)
        }
    }

    private fun addImageFromCamera() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), 111)
        } else {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i,101)
        }
    }

    private fun addImageFromFolder() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 121)
        } else {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,120)
        }
    }

    private fun getNewsData() : Estate {
        binding.apply {
            val type = addEstateEtType.text.toString()
            val price = addEstateEtPrice.text.toString()
            val surface = addEstateEtSurface.text.toString()
            val roomNb = addEstateEtNbRoom.text.toString()
            val description = addEstateEtDescription.text.toString()
            val address = addEstateEtAddress.text.toString()
            val interesting = addEstateEtInterestingPoint.text.toString()
            val isSale = true
            val startSale = addEstateEtStartSale.text.toString()
            val endSale = addEstateEtEndSale.text.toString()
            val agent = addEstateEtAgent.text.toString()

            estate.type = type
            estate.price = price
            estate.surface = surface
            estate.roomsNumber = roomNb
            estate.description = description
            estate.address = address
            estate.interestingPoint = interesting
            estate.isSale = isSale
            estate.onSaleDate = startSale
            estate.dateOfSale = endSale
            estate.agentInChargeName = agent
        }
        return estate
    }

    private fun buttonListener() {

        binding.btnAddEstateCancel.setOnClickListener {
            viewModel.deleteEstate(estate)
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }

        binding.btnAddEstateCreate.setOnClickListener {
            getNewsData()
            viewModel.insertEstate(estate)
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }

        binding.addEstateBtnCamera.setOnClickListener {
            addImageFromCamera()
        }

        binding.addEstateBtnFolder.setOnClickListener {
            addImageFromFolder()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            val pic = data?.getParcelableExtra<Bitmap>("data")
            val newPicture = Picture(pic!!, estate.id)
            Log.i("MY_TAG","id picture camera = ${newPicture.estateId}")
            viewModel.insertPicture(newPicture)
        }
        if (requestCode == 120) {
            val pic = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data?.data)
            val newPicture = Picture(pic!!, estate.id)
            Log.i("MY_TAG","id picture folder = ${newPicture.estateId}")
            viewModel.insertPicture(newPicture)
        }
    }
}