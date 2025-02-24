package com.sait.tawajudpremiumplusnewfeatured.ui.requests

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.SelfServiceFragment
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.adapters.Spinner_Adapter
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentRequestsBinding
import com.sait.tawajudpremiumplusnewfeatured.items.SpinnerItem_BO
import com.sait.tawajudpremiumplusnewfeatured.items.SpinnerItem_BO_Type
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.models.ReasonsData
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.viewmodels.reasons.ReasonsViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.LeaveTypes1
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.PermissionsTypes1
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave.RequestSaveLeaveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual.ManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.requestType.RequestTypeData
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.SelfRequestViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.leave.RequestSaveLeaveViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.manual.ManualEntryViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.permission.PermissionSaveViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.requestType.RequestTypeViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationData
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op.getSimpleDateFormat
import com.sait.tawajudpremiumplusnewfeatured.util.EnumUtils
import com.sait.tawajudpremiumplusnewfeatured.util.FileUtils
import com.sait.tawajudpremiumplusnewfeatured.util.FileUtils.getFileNameFromUri
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class RequestsFragment : BaseFragment(), View.OnClickListener, AdapterView.OnItemSelectedListener,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var binding: FragmentRequestsBinding
    private lateinit var viewModel: SelfRequestViewModel
    private lateinit var viewModel_Permissions: PermissionSaveViewModel
    private lateinit var viewModel_RequestType: RequestTypeViewModel
    private lateinit var viewModel_Reasons: ReasonsViewModel

    private lateinit var viewModel_saveLeave: RequestSaveLeaveViewModel
    private lateinit var viewModel_ManualEntry: ManualEntryViewModel

    var handler: Handler = Handler()

    private var mContext: Context? = null
    var spinnerItemBO: SpinnerItem_BO? = null
    var spinnerItemBO_type: SpinnerItem_BO? = null
    var spinnerItemBo_reasons: ReasonsData? = null
    var spinnerItemBO_requsettype: SpinnerItem_BO_Type? = null

    var strPermissionType: String? = null
    var strPermissionTypeLetter: String? = null

    var isFullday = false
    var isForPeriod = false
    var isFlexible: Boolean? = null

    var type_id: Int? = 0
    var duration: Int = 0
    var isFileMandatory :Boolean =false
    var isRemarks:Boolean =false
    var permissionSaveRequest: RequestSavePermissionRequest? = null

    val arrayList_type = ArrayList<SpinnerItem_BO_Type>()
    val arrayList_reasons = ArrayList<ReasonsData>()
    val arrayList_type_leave_permission = ArrayList<SpinnerItem_BO>()
    var violationsDataModel: ViolationData? = null

    var permission_all_id: Int? = 0
    private val FILE_PICK_REQUEST_CODE = 1
    private val REQUEST_IMAGE_CAPTURE: Int = 2

    var base64String: String? = ""
    var fileTypeStr: String? = ""
    var uploadPermissionStr: String? = ""
    var attachedFileStr: String? = ""
    var selected_tab: Int? = 0
    var isMandatoryFile :ArrayList<SpinnerItem_BO> = arrayListOf()
    @SuppressLint("LongLogTag")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestsBinding.inflate(inflater, container, false)
        mContext = inflater.context
        viewModel = ViewModelProvider(this)[SelfRequestViewModel::class.java]
        viewModel_saveLeave = ViewModelProvider(this)[RequestSaveLeaveViewModel::class.java]
        viewModel_RequestType = ViewModelProvider(this)[RequestTypeViewModel::class.java]
        viewModel_Reasons = ViewModelProvider(this)[ReasonsViewModel::class.java]
        viewModel_ManualEntry = ViewModelProvider(this)[ManualEntryViewModel::class.java]
        viewModel_Permissions = ViewModelProvider(this)[PermissionSaveViewModel::class.java]
        val activity = this.activity as MainActivity?
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setClickListeners(activity)


        /*
                binding.txtUploadfile.setOnClickListener {
                    openFilePicker()

                }
        */


        /*  binding.spSelectRequest.isEnabled =false
          binding.spSelectRequest.isClickable =false*/

        /*  setLeaveRequestLayoutsVisible()

          callRequestsAPI("L")
  */


        if (arguments != null) {
            violationsDataModel = arguments?.getParcelable("violations") as? ViolationData
            Log.e("violationsDataModel", violationsDataModel!!.type)
            Log.e("violationsDataModel_remark", violationsDataModel!!.remark)
            permission_all_id = violationsDataModel!!.permissionId

            selected_tab = arguments?.getInt("selected_tab")
            //   binding.edtRemarks.setText(violationsDataModel!!.remark)
        }




        callRequestTypeAPI()

        binding.edtRemarks.setOnEditorActionListener { v, actionId, event ->
            if (actionId === EditorInfo.IME_ACTION_NEXT) {
                // Hide the keyboard
                //     Keyboard_Op.hide(mContext)

                return@setOnEditorActionListener true
            }
            false
        }

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->

                val selectedPdfFileUri: Uri = uri
                base64String = FileUtils.uriToBase64(mContext!!, selectedPdfFileUri)
                val mimeType = FileUtils.getMimeType(mContext!!, selectedPdfFileUri)
                val fileName = getFileNameFromUri(requireContext(), selectedPdfFileUri)
                println("File Name: $fileName")
                binding.selectedFileImageView.visibility = View.VISIBLE
                binding.selectedFilecancelbtn.visibility=View.VISIBLE
                binding.txtFilename.visibility = View.VISIBLE
                binding.txtFilename.text = fileName.toString()
                val fileExtension = getFileExtension(fileName)
                if (fileExtension.isNotEmpty()) {
                    fileTypeStr = fileExtension
                    getDocumentIconResId(fileTypeStr)
                    isFileMandatory=false
                    println("File extension: $fileExtension")
                } else {
                    fileTypeStr = ""
                    println("No file extension found.")
                }

                if (base64String != null) {

                    Log.e("base64String", base64String!!)

                } else {
                    // Handle the case where conversion failed
                }

            }
        }else
            if(requestCode ==  REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                if (data != null && data.extras != null) {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    // we can now use the Bitmap object as needed

                    val fileName = "captured_image"
                    val fileExtension = getFileExtensionFromBitmap(imageBitmap)

                    base64String = FileUtils.fileToBase64FromBitmap(mContext!!, imageBitmap,"$fileName.$fileExtension")
                    binding.selectedFileImageView.visibility = View.VISIBLE
                    binding.selectedFilecancelbtn.visibility = View.VISIBLE
                    binding.txtFilename.visibility = View.VISIBLE
                    binding.txtFilename.text = "$fileName.$fileExtension"
                    if (fileExtension.isNotEmpty()) {
                        fileTypeStr = fileExtension
                        getDocumentIconResId(fileTypeStr)
                        isFileMandatory=false
                        println("File extension: $fileExtension")
                    } else {
                        fileTypeStr = ""
                        println("No file extension found.")
                    }

                    if (base64String != null) {

                        Log.e("base64String", base64String!!)

                    } else {
                        // Handle the case where conversion failed
                    }
                }
            }
            else if (requestCode ==  REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_CANCELED ||
                requestCode ==  FILE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
                // User pressed back, dismiss the dialogue
                SnackBarUtil.dismissCurrentDialog()
            }
    }

    fun getFileExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf('.')

        if (dotIndex == -1 || dotIndex == fileName.length - 1) {
            // No dot in the file name or dot is at the end of the filename
            return ""
        } else {
            // Extract the substring after the dot
            return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFileExtensionFromBitmap(bitmap: Bitmap): String {
        return when (bitmap.config) {
            Bitmap.Config.ARGB_8888, Bitmap.Config.RGBA_F16 -> "png"
            else -> "jpg"
        }
    }
    private fun getDocumentIconResId(fileTypeStr: String?) {
        if (fileTypeStr!!.contains("pdf")) {
            //   fileTypeStr = "pdf"
            binding.selectedFileImageView.setImageResource(R.drawable.pdf)

        } else if (fileTypeStr!!.contains("jpeg")) {
            // fileTypeStr = "jpeg"
            binding.selectedFileImageView.setImageResource(R.drawable.jpeg)
        } else if (fileTypeStr!!.contains("jpg")) {
            // fileTypeStr = "jpg"/**/
            binding.selectedFileImageView.setImageResource(R.drawable.jpeg)
        } else if (fileTypeStr!!.contains("png")) {
            //  fileTypeStr = "png"
            binding.selectedFileImageView.setImageResource(R.drawable.png)
        } else if ((fileTypeStr!!.contains("xlsx")) || (fileTypeStr!!.contains("xls")) ||
            (fileTypeStr!!.contains("xlsm")) || (fileTypeStr!!.contains("xlsb"))
        ) {
            // fileTypeStr = "xlsx"
            binding.selectedFileImageView.setImageResource(R.drawable.excel)
        } else if (fileTypeStr!!.contains("docx") || (fileTypeStr!!.contains("doc"))

            || fileTypeStr!!.contains("txt")
        ) {
            // fileTypeStr = "docx"
            binding.selectedFileImageView.setImageResource(R.drawable.document)
        } else if (fileTypeStr!!.contains("msg")) {
            // fileTypeStr = "msg"
            binding.selectedFileImageView.setImageResource(R.drawable.msg)
        }else if (fileTypeStr!!.contains("mp4")){
            binding.selectedFilecancelbtn.visibility=View.GONE
            binding.txtFilename.visibility=View.GONE
            binding.selectedFileImageView.setImageResource(R.drawable.document)
            showDialog(mContext!!,
                resources.getString(R.string.title_upload_file_not_support_txt),
                resources.getString(R.string.upload_file_not_support_txt),
                R.drawable.app_icon,
                resources.getColor(R.color.red))
        } else
        {
            binding.selectedFilecancelbtn.visibility=View.GONE
            binding.txtFilename.visibility=View.GONE
            binding.selectedFileImageView.setImageResource(R.drawable.document)
            showDialog(mContext!!,
                resources.getString(R.string.title_upload_file_not_support_txt),
                resources.getString(R.string.upload_file_not_support_txt),
                R.drawable.app_icon,
                resources.getColor(R.color.red))


        }

    }
    private fun showDialog(
        context: Context,
        messageheading: String,

        message: String,
        drawable_icon: Int,
        color: Int,
    ) {
        // Create a new instance of the AlertDialog.Builder class
        /* val builder = AlertDialog.Builder(context)
         builder.setTitle(title)//Set the tittle
         builder.setMessage(mess)//Set the message of the dialog
         builder.setPositiveButton("OK") { dialog, _ ->
             // Implement the Code when OK Button is Clicked
             dialog.dismiss()

             b_show_out_office_dialog = false
         }
         *//* builder.setNegativeButton("Cancel") { dialog, _ ->
             // Implement the Code when Cancel Button is CLiked
             dialog.dismiss() //Close or dismiss the alert dialog
         }*//*
        b_show_out_office_dialog = true
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()*/




        try {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var alertDialogView: View? = null
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null)
                alertDialog.setView(alertDialogView)
                val textDialog = alertDialogView.findViewById<TextView>(R.id.diag_text)
                textDialog.text = message
                val diag_ok = alertDialogView.findViewById<TextView>(R.id.diag_ok)

                val icon = alertDialogView.findViewById<ImageView>(R.id.icon)

                //   icon.setImageResource(drawable_icon)

                val resourceName = context.resources.getResourceEntryName(drawable_icon)
                Log.e("resourceName", resourceName)
                if (resourceName.contains("caution")) {
                    icon.setImageResource(drawable_icon)
                }
                diag_ok.setBackgroundColor(color)
                val textHeadingDialog =
                    alertDialogView.findViewById<TextView>(R.id.diag_text_heading)
                textHeadingDialog.text = messageheading


                val show = alertDialog.show()
                val window = show.window
                if (window != null) {
                    window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    //TODO Dialog Size
                    show.window!!.setLayout(
                        SnackBarUtil.getWidth(context) / 100 * 90,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    show.window!!.setGravity(Gravity.CENTER)
                }
                diag_ok.setOnClickListener {
                    show.dismiss()


                }

                alertDialog.setCancelable(false)
            }
//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }


    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*" // All file types
        startActivityForResult(intent, FILE_PICK_REQUEST_CODE)
    }

    private fun callRequestTypeAPI() {

        addObserverRequestType()
        getRequestTypeDetails()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun callManualEntryAPI() {

        addObserverManualEntry()
        getManualEntryDetails()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getManualEntryDetails() {

        val manualEntryRequest = ManualEntryRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            binding.txtReasonsDate.text.toString(),
            permission_all_id!!,
            convertToDateTime(
                binding.txtReasonsDate.text.toString(), binding.txtReasonsTime.text.toString()
            ),
            type_id!!,
            UserShardPrefrences.getUniqueId(mContext).toString(),
            binding.edtRemarks.text.toString(),
            removeSpaces(base64String.toString()),
            fileTypeStr.toString()
        )


        viewModel_ManualEntry.getManualEntryData(
            mContext!!, manualEntryRequest
        )

        Log.e("Request_M",manualEntryRequest.toString())


    }

    private fun addObserverManualEntry() {

        viewModel_ManualEntry.manualEntryResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { manualEntryResponse ->

                            if (manualEntryResponse.data != null) {

                                /*  SnackBarUtil.showSnackbar(
                                      context, manualEntryResponse.data.success
                                  )

                                  (activity as MainActivity).onBackPressed()
  */


                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,
                                    manualEntryResponse.data.msg,
                                    R.drawable.app_icon,
                                    colorPrimary,
                                    SnackBarUtil.OnClickListenerNew {
                                        replaceFragment(
                                            SelfServiceFragment(),
                                            R.id.flFragment,
                                            false
                                        )


                                    })


                            }
                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                mContext,
                                message,
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }


    }

    private fun addObserverRequestType() {
        viewModel_RequestType.requestTypeResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse
                            //  val arrayList_type = ArrayList<SpinnerItem_BO_Type>()

                            if (requestResponse.data != null) {


                                //   SnackBarUtil.showSnackbar(mContext, requestResponse.data.toString())
                                if (requestResponse.data.isNotEmpty()) {
                                    arrayList_type.clear()
                                    val permissionTypes: List<RequestTypeData> =
                                        requestResponse.data

                                    for (i in 0 until requestResponse.data.size) {
                                        val permissionType: RequestTypeData = permissionTypes[i]

                                        if(!permissionType.desc_En.contains("Enrollment")){
                                            if (UserShardPrefrences.getLanguage(mContext).equals("1")) {

                                                arrayList_type.add(
                                                    SpinnerItem_BO_Type(
                                                        permissionType.formID,
                                                        permissionType.desc_Ar,
                                                        permissionType.desc_Ar,
                                                        permissionType.requestType
                                                    )
                                                )

                                            } else {
                                                arrayList_type.add(
                                                    SpinnerItem_BO_Type(
                                                        permissionType.formID,
                                                        permissionType.desc_En,
                                                        permissionType.desc_En,
                                                        permissionType.requestType
                                                    )
                                                )

                                            }

                                        }



                                    }
                                }


                                setUpDropDown_RequestType(arrayList_type)


                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            //    SnackBarUtil.showSnackbar(context, message, false)

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }


    private fun getRequestTypeDetails() {


        val commonRequest = CommonRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            //172,
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )


        viewModel_RequestType.getRequestTypeData(
            mContext!!, commonRequest
        )


    }

    private fun callRequestsAPI(strPermissionTypeLetter: String?) {
        addObserver()
        getRequestsDetails(strPermissionTypeLetter)

    }

    private fun callSaveLeaveRequestsAPI() {
        addObserver_SaveLeave()
        getRequestsDetails_SaveLeave()

    }

    private fun addObserver_SaveLeave() {
        viewModel_saveLeave.requestSaveLeaveResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestSaveLeaveResponse ->

                            if (requestSaveLeaveResponse.data != null) {


                                /*  SnackBarUtil.showSnackbar(context, requestSaveLeaveResponse.data.success)


                                  (activity as MainActivity).onBackPressed()*/

                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,
                                    requestSaveLeaveResponse.data.msg,
                                    R.drawable.app_icon,
                                    colorPrimary,
                                    SnackBarUtil.OnClickListenerNew {


                                        replaceFragment(
                                            SelfServiceFragment(),
                                            R.id.flFragment,
                                            false
                                        )


                                    })


                                //  replaceFragment(SelfServiceFragment(), R.id.flFragment, false)

                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                mContext,
                                message,
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }

    private fun getRequestsDetails_SaveLeave() {



        val requestSaveLeaveRequest = RequestSaveLeaveRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            binding.txtFromDate.text.toString(),
            UserShardPrefrences.getLanguage(mContext)!!.toInt()!!,
            type_id!!,
            permission_all_id!!,
            UserShardPrefrences.getUniqueId(mContext).toString(),
            binding.edtRemarks.text.toString(),
            binding.txtToDate.text.toString(),
            removeSpaces(base64String.toString()),
            fileTypeStr.toString(),
        )

        viewModel_saveLeave.getRequestSaveLeaveData(mContext, requestSaveLeaveRequest)

        Log.e("Request_L",requestSaveLeaveRequest.toString())

    }

    private fun addObserver() {
        viewModel.requestResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse
                            // val arrayList = ArrayList<SpinnerItem_BO>()

                            if (requestResponse.data != null) {

                                arrayList_type_leave_permission.clear()
                                if (requestResponse.data.permissionsTypes1.isNotEmpty()) {
                                    val permissionTypes: List<PermissionsTypes1> =
                                        requestResponse.data.permissionsTypes1

                                    for (i in 0 until requestResponse.data.permissionsTypes1.size) {
                                        val permissionType: PermissionsTypes1 = permissionTypes[i]
                                        arrayList_type_leave_permission.add(
                                            SpinnerItem_BO(
                                                permissionType.permId,
                                                permissionType.permId,
                                                permissionType.permName,
                                                permissionType.hasFullDayPermission,
                                                permissionType.hasPermissionForPeriod,
                                                permissionType.hasFlexiblePermission,
                                                permissionType.maxDuration,
                                                permissionType.attachmentIsMandatory,
                                                permissionType.remarksIsMandatory,
                                            )
                                        )


                                    }
                                } else {
                                    val leaveTypes: List<LeaveTypes1> =
                                        requestResponse.data.leaveTypes1
                                    for (i in 0 until requestResponse.data.leaveTypes1.size) {
                                        val leaveType: LeaveTypes1 = leaveTypes[i]
                                        arrayList_type_leave_permission.add(
                                            SpinnerItem_BO(
                                                leaveType.permId,
                                                leaveType.permId,
                                                leaveType.permName,
                                                false,
                                                false, false, 0,  leaveType.attachmentIsMandatory, leaveType.remarksIsMandatory
                                            )
                                        )


                                    }


                                }
                                setUpDropDown(arrayList_type_leave_permission)


                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                mContext,
                                message,
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }

    private fun getRequestsDetails(strPermissionTypeLetter: String?) {
        if (strPermissionTypeLetter != null) {
            viewModel.getRequestData(
                mContext!!,
                UserShardPrefrences.getUserInfo(context).fKEmployeeId,
                UserShardPrefrences.getLanguage(mContext)!!,
                strPermissionTypeLetter,
            )
        }

    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.rlUploadFile.setOnClickListener(this)
        binding.rlCaptureImg.setOnClickListener(this)
        binding.txtSave.setOnClickListener(this)
        binding.txtToDate.setOnClickListener(this)
        binding.txtFromDate.setOnClickListener(this)
        binding.txtDate.setOnClickListener(this)
        binding.txtToTime.setOnClickListener(this)
        binding.txtFromTime.setOnClickListener(this)

        binding.txtCancel.setOnClickListener(this)

        binding.txtReasonsDate.setOnClickListener(this)
        binding.txtReasonsTime.setOnClickListener(this)
        binding.selectedFileImageView.setOnClickListener(this)
        binding.selectedFilecancelbtn.setOnClickListener(this)
        binding.txtDuration.setOnClickListener(this)

        binding.isForPeriodSwitch.setOnCheckedChangeListener(this)
        binding.isForFullDayCheckBox.setOnCheckedChangeListener(this)
        binding.isFlexibleSwitch.setOnCheckedChangeListener(this)

        binding.spSelectRequest.onItemSelectedListener = this
        binding.spSelectRequestType.onItemSelectedListener = this

        /*  binding.spSelectRequestType.onItemSelectedListener =
              object : AdapterView.OnItemSelectedListener {
                  override fun onItemSelected(
                      parent: AdapterView<*>?,
                      view: View,
                      position: Int,
                      id: Long
                  ) {

                      if (position != 0) {
                          strPermissionType = binding.spSelectRequestType.selectedItem.toString()

                          if (strPermissionType.equals("Permission Requests")) {
                              strPermissionTypeLetter = "P"
                              setPermissionRequestLayoutsVisible()


                          }
                          if (strPermissionType.equals("Manual Entry Requests")) {
                              strPermissionTypeLetter = "M"
                              setReasonLayoutsVisible()

                          }
                          else {
                              strPermissionTypeLetter = "L"

                              setLeaveRequestLayoutsVisible()


                          }

                          callRequestsAPI(strPermissionTypeLetter)

                      }
                  }

                  override fun onNothingSelected(parent: AdapterView<*>?) {


                  }
              }*/


    }

    private fun setReasonLayoutsVisible() {
        binding.lLeavePermission.visibility = View.VISIBLE
        binding.spSelectRequest.visibility = View.VISIBLE
        binding.lReasons.visibility = View.VISIBLE
        binding.lRemarks.visibility = View.VISIBLE
        binding.lReasonsDate.visibility = View.VISIBLE
        binding.lReasonsTime.visibility = View.VISIBLE




        binding.lToTime.visibility = View.GONE
        binding.lToDate.visibility = View.GONE
        binding.lFromTime.visibility = View.GONE
        binding.lFromDate.visibility = View.GONE
        binding.lDate.visibility = View.GONE

        binding.lPermissiontype.visibility = View.GONE

        /* binding.lPermissiontype.visibility = View.GONE
         binding.lLeavePermission.visibility = View.GONE

 */
        binding.lFlexible.visibility = View.GONE


    }

    private fun setLeaveRequestLayoutsVisible() {
        binding.lFlexible.visibility = View.GONE
        binding.lReasons.visibility = View.GONE

        binding.lLeavePermission.visibility = View.VISIBLE
        binding.lFromDate.visibility = View.VISIBLE
        binding.lToDate.visibility = View.VISIBLE
        binding.lRemarks.visibility = View.VISIBLE


        binding.lPermissiontype.visibility = View.GONE
        binding.lToTime.visibility = View.GONE
        binding.lFromTime.visibility = View.GONE
        binding.lDate.visibility = View.GONE

    }

    private fun setPermissionRequestLayoutsVisible() {

        binding.lReasons.visibility = View.GONE
        binding.lLeavePermission.visibility = View.VISIBLE
        binding.lPermissiontype.visibility = View.VISIBLE
        binding.lRemarks.visibility = View.VISIBLE
        binding.lFlexible.visibility = View.VISIBLE


        /*  binding.lToTime.visibility = View.VISIBLE
          binding.lFromTime.visibility = View.VISIBLE
          binding.lFromDate.visibility = View.GONE
          binding.lToDate.visibility = View.GONE
          binding.lDate.visibility = View.VISIBLE*/

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onResume() {
        super.onResume()

        /*
                (activity as MainActivity).show_BackButton()
                (activity as MainActivity).hide_alert()
                (activity as MainActivity).hide_info()
                (activity as MainActivity).hide_userprofile()
                (activity as MainActivity).show_app_title("Calendar")*/




        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(requireActivity().resources.getString(R.string.self_service))


        if (GlobalVariables.from_background) {
            handler.post(checkInternetRunnable)
        }


    }

    private val checkInternetRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun run() {
            val application = requireActivity().application as TawajudApplication

            if (application.hasInternetConnection()) {
                onRefresh()
            } else {
                // Repeat the check after 1 second if there is no internet connection
                handler.postDelayed(this, 1000)
            }
        }
    }
    private fun onRefresh() {
      //  callRequestTypeAPI()


    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.selectedFileImageView -> {
                //uploadedFileClicked()
            }

            R.id.rl_upload_file -> {
                openFilePicker()
               //pickOptionForUploading()
            }
            R.id.rl_capture_img ->{
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
            R.id.txt_cancel -> (activity as MainActivity).onBackPressed()

            R.id.img_back -> try {
                (activity as MainActivity).onBackPressed()
            } catch (e: Exception) {
                Log.d("onBackClick", "onClick: $e")
            }

            //   replaceFragment(RequestsFragment(), R.id.flFragment, false)


            R.id.txt_save -> {

                if (strPermissionTypeLetter != null) {
                    if (strPermissionTypeLetter.equals("P")) {
                        checkValidations_PermissionRequest(type_id!!,isFileMandatory)
                    } else if (strPermissionTypeLetter.equals("L")) {
                        checkValidations_LeaveRequest(isFileMandatory)
                    } else if (strPermissionTypeLetter.equals("M")) {
                        checkValidations_ManualRequest(isFileMandatory)
                    }


                }
                /* else{
                     if (spinnerItemBO_type == null) {
                         SnackBarUtil.showSnackbar(
                             context,
                             "Please select Request Type",
                             R.drawable.allert
                         )
                         //  cancel = true;
                     }
                 }*/


            }

            R.id.txt_from_date -> {

                datePicker(binding.txtFromDate.text.toString(), binding.txtFromDate)

            }
            R.id.txt_to_date -> {

                datePicker(binding.txtToDate.text.toString(), binding.txtToDate)


            }
            R.id.txt_date -> {
                datePicker(binding.txtDate.text.toString(), binding.txtDate)

            }

            R.id.txt_reasons_date -> {
                datePicker(binding.txtReasonsDate.text.toString(), binding.txtReasonsDate)
            }

            R.id.txt_to_time -> {
                timePicker(binding.txtToTime)
            }


            R.id.txt_duration -> {
                timePicker(binding.txtDuration)
            }
            R.id.txt_from_time -> {
                timePicker(binding.txtFromTime)

            }


            R.id.txt_reasons_time -> {
                timePicker(binding.txtReasonsTime)

            }
            R.id.selectedFilecancelbtn ->{
                binding.selectedFilecancelbtn.visibility=View.GONE
                binding.selectedFileImageView.visibility=View.GONE
                binding.txtFilename.visibility=View.GONE
                //  isFileMandatory = type_id!! == 5
                isFileMandatory = true
                base64String = ""
                binding.txtFilename.text=""
                fileTypeStr =""
            }

        }
    }


    private fun clearAfterSelection(){
        binding.selectedFilecancelbtn.visibility=View.GONE
        binding.selectedFileImageView.visibility=View.GONE
        binding.txtFilename.visibility=View.GONE
        //  isFileMandatory = type_id!! == 5
        isFileMandatory = true
        base64String = ""
        binding.txtFilename.text=""
        fileTypeStr =""
    }
    private fun uploadedFileClicked() {
        var uploadFileStr = DateTime_Op.removeApiSegment(
            UserShardPrefrences.getBaseUrl(mContext!!)
                .toString() + "/$uploadPermissionStr/" + violationsDataModel!!.permissionId + violationsDataModel!!.attachedFile
        )

        downloadFile(
            mContext!!,
            uploadFileStr,
            binding.txtFilename.text.toString(),
            FileUtils.getFileExtensionUpload(uploadFileStr)
        )

        /*  val helpFragment = HelpFragment()
          val args_requests = Bundle()
        //  args_requests.putString("uploadFile",  "https://sgi.software/TawajudAPIs/PermissionRequestFiles/20473.pdf")
          args_requests.putString("uploadFile",uploadFileStr)

          helpFragment.arguments = args_requests
          replaceFragment(helpFragment, R.id.flFragment, true)*/
    }


    fun downloadFile(context: Context, fileUrl: String, title: String, mimeType: String) {

        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
            context,
            "Downloading File..",
            R.drawable.app_icon,
            colorPrimary
        )

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // Create a DownloadManager.Request with the file URL
        val request = DownloadManager.Request(Uri.parse(fileUrl))

        // Set the title and description for the download
        request.setTitle(title)
        request.setDescription("Downloading File..")

        // Set the destination folder and file name
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "$title.${getFileExtension(fileUrl)}"
        )

        // Set the MIME type of the file
        request.setMimeType(mimeType)

        // Enqueue the download and get a download ID
        val downloadId = downloadManager.enqueue(request)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkValidations_ManualRequest(isFileMandatory: Boolean) {
        //callPermissionRequestsAPI(type_id!!)
        if(isRemarks) {
            binding.txtReasonsDate.error = null
        }
        binding.txtReasonsTime.error = null
        binding.edtRemarks.error = null

        val date: String = binding.txtReasonsDate.text.toString()
        val time: String = binding.txtReasonsTime.text.toString()
        val remarks: String = binding.edtRemarks.text.toString()

        var cancel = false
        var focusView: View? = null

        if (time.equals("Time")) {
            binding.txtReasonsTime.error = getString(R.string.error_field_required)
            focusView = binding.txtReasonsTime
            cancel = true
        }
        if (TextUtils.isEmpty(date)) {
            binding.txtReasonsDate.error = getString(R.string.error_field_required)
            focusView = binding.txtReasonsDate
            cancel = true
        }
        if(isRemarks) {
            if (TextUtils.isEmpty(remarks)) {
                binding.edtRemarks.error = getString(R.string.error_field_required)
                focusView = binding.edtRemarks
                cancel = true
            }
        }


        /*  if (type_id == null) {
              SnackBarUtil.showSnackbar(
                  context, "Please select Request Type", R.drawable.allert
              )
              //  cancel = true;
          }

          if (permission_all_id == null) {
              SnackBarUtil.showSnackbar(
                  context, "Please select Leave Type", R.drawable.allert
              )
              //  cancel = true;
          }*/


        if (cancel) {
            focusView!!.requestFocus()
        } else {
            try {
                if (EnumUtils.isNetworkConnected(mContext) && !isFileMandatory) {

                    if(isRemarks){
                        if(binding.edtRemarks.text.toString()!=null){
                            callManualEntryAPI()
                        }
                        else{
                            binding.edtRemarks.error =
                                getString(R.string.error_field_required)
                            focusView = binding.edtRemarks
                            cancel = true
                        }
                    }
                    else{
                        callManualEntryAPI()
                    }





                } else {

                    if(isFileMandatory){
                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                            mContext,
                            getString(R.string.file_mandatory_txt),
                            R.drawable.caution,
                            resources.getColor(R.color.red)
                        )
                    }else
                        if(!EnumUtils.isNetworkConnected(mContext)){
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                mContext,
                                getString(R.string.no_internet_connection),
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )
                        }

                }
            } catch (e: Exception) {

            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkValidations_PermissionRequest(id: Int, isFileMandatory: Boolean) {
        binding.txtToDate.error = null
        binding.txtFromDate.error = null
        if(isRemarks) {
            binding.edtRemarks.error = null
        }
        binding.txtDate.error = null
        binding.txtFromTime.error = null
        binding.txtToTime.error = null
        binding.edtRemarks.error = null
        binding.txtDuration.error = null

        val fromDate: String = binding.txtFromDate.text.toString()
        val toDate: String = binding.txtToDate.text.toString()
        val remarks: String = binding.edtRemarks.text.toString()
        val duration: String = binding.txtDuration.text.toString()
        val date: String = binding.txtDate.text.toString()
        val fromTime: String = binding.txtFromTime.text.toString()
        val toTime: String = binding.txtToTime.text.toString()

        var cancel = false
        var focusView: View? = null

        if ((!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked)) {
            if (binding.isFlexibleSwitch.isChecked) {
                if (TextUtils.isEmpty(duration)) {
                    binding.txtDuration.error = getString(R.string.error_field_required)
                    focusView = binding.txtDuration
                    cancel = true

                }
                if (TextUtils.isEmpty(date)) {
                    binding.txtDate.error = getString(R.string.error_field_required)
                    focusView = binding.txtDate
                    cancel = true
                }
                if(isRemarks) {
                    if (TextUtils.isEmpty(remarks)) {
                        binding.edtRemarks.error = getString(R.string.error_field_required)
                        focusView = binding.edtRemarks
                        cancel = true
                    }
                }
            } else {

                if (TextUtils.isEmpty(date)) {
                    binding.txtDate.error = getString(R.string.error_field_required)
                    focusView = binding.txtDate
                    cancel = true
                }
                if (TextUtils.isEmpty(fromTime)) {
                    binding.txtFromTime.error = getString(R.string.error_field_required)
                    focusView = binding.txtFromTime
                    cancel = true
                }
                if (TextUtils.isEmpty(toTime)) {
                    binding.txtToTime.error = getString(R.string.error_field_required)
                    focusView = binding.txtToTime
                    cancel = true
                }
                if(isRemarks) {
                    if (TextUtils.isEmpty(remarks)) {
                        binding.edtRemarks.error = getString(R.string.error_field_required)
                        focusView = binding.edtRemarks
                        cancel = true
                    }
                }
            }
        }
        //||( !binding.isForFullDayCheckBox.isChecked&&binding.isForPeriodSwitch.isChecked)
        else
            if (!binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
                if (binding.isFlexibleSwitch.isChecked) {

                    if (TextUtils.isEmpty(fromDate)) {
                        binding.txtFromDate.error = getString(R.string.error_field_required)
                        focusView = binding.txtFromDate
                        cancel = true
                    }
                    if (TextUtils.isEmpty(toDate)) {
                        binding.txtToDate.error = getString(R.string.error_field_required)
                        focusView = binding.txtToDate
                        cancel = true
                    }
                    if (TextUtils.isEmpty(duration)) {
                        binding.txtDuration.error = getString(R.string.error_field_required)
                        focusView = binding.txtDuration
                        cancel = true
                    }
                    if(isRemarks) {
                        if (TextUtils.isEmpty(remarks)) {
                            binding.edtRemarks.error = getString(R.string.error_field_required)
                            focusView = binding.edtRemarks
                            cancel = true
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(fromDate)) {
                        binding.txtFromDate.error = getString(R.string.error_field_required)
                        focusView = binding.txtFromDate
                        cancel = true
                    }
                    if (TextUtils.isEmpty(toDate)) {
                        binding.txtToDate.error = getString(R.string.error_field_required)
                        focusView = binding.txtToDate
                        cancel = true
                    }
                    if (TextUtils.isEmpty(fromTime)) {
                        binding.txtFromTime.error = getString(R.string.error_field_required)
                        focusView = binding.txtFromTime
                        cancel = true
                    }
                    if (TextUtils.isEmpty(toTime)) {
                        binding.txtToTime.error = getString(R.string.error_field_required)
                        focusView = binding.txtToTime
                        cancel = true
                    }
                    if(isRemarks) {
                        if (TextUtils.isEmpty(remarks)) {
                            binding.edtRemarks.error = getString(R.string.error_field_required)
                            focusView = binding.edtRemarks
                            cancel = true
                        }
                    }
                }
            } else
                if (!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
                    if (binding.isFlexibleSwitch.isChecked) {
                        if (TextUtils.isEmpty(date)) {
                            binding.txtDate.error = getString(R.string.error_field_required)
                            focusView = binding.txtDate
                            cancel = true
                        }
                        if (TextUtils.isEmpty(fromTime)) {
                            binding.txtFromTime.error = getString(R.string.error_field_required)
                            focusView = binding.txtFromTime
                            cancel = true
                        }
                        if (TextUtils.isEmpty(toTime)) {
                            binding.txtToTime.error = getString(R.string.error_field_required)
                            focusView = binding.txtToTime
                            cancel = true
                        }
                        if(isRemarks) {
                            if (TextUtils.isEmpty(remarks)) {
                                binding.edtRemarks.error = getString(R.string.error_field_required)
                                focusView = binding.edtRemarks
                                cancel = true
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(date)) {
                            binding.txtDate.error = getString(R.string.error_field_required)
                            focusView = binding.txtDate
                            cancel = true
                        }
                        if (TextUtils.isEmpty(duration)) {
                            binding.txtDuration.error = getString(R.string.error_field_required)
                            focusView = binding.txtDuration
                            cancel = true
                        }
                        if(isRemarks) {
                            if (TextUtils.isEmpty(remarks)) {
                                binding.edtRemarks.error = getString(R.string.error_field_required)
                                focusView = binding.edtRemarks
                                cancel = true
                            }
                        }
                    }
                } else
                    if (!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
                        if (!binding.isFlexibleSwitch.isChecked) {
                            if (TextUtils.isEmpty(duration)) {
                                binding.txtDuration.error = getString(R.string.error_field_required)
                                focusView = binding.txtDuration
                                cancel = true
                            }
                            if (TextUtils.isEmpty(date)) {
                                binding.txtDate.error = getString(R.string.error_field_required)
                                focusView = binding.txtDate
                                cancel = true
                            }
                            if(isRemarks) {
                                if (TextUtils.isEmpty(remarks)) {
                                    binding.edtRemarks.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.edtRemarks
                                    cancel = true
                                }
                            }
                        } else {
                            if (TextUtils.isEmpty(date)) {
                                binding.txtDate.error = getString(R.string.error_field_required)
                                focusView = binding.txtDate
                                cancel = true
                            }
                            if (TextUtils.isEmpty(fromTime)) {
                                binding.txtFromTime.error = getString(R.string.error_field_required)
                                focusView = binding.txtFromTime
                                cancel = true
                            }
                            if (TextUtils.isEmpty(toTime)) {
                                binding.txtToTime.error = getString(R.string.error_field_required)
                                focusView = binding.txtToTime
                                cancel = true
                            }
                            if(isRemarks) {
                                if (TextUtils.isEmpty(remarks)) {
                                    binding.edtRemarks.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.edtRemarks
                                    cancel = true
                                }
                            }
                        }

                    } else
                        if (!binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
                            if (binding.isFlexibleSwitch.isChecked) {
                                if (TextUtils.isEmpty(fromDate)) {
                                    binding.txtFromDate.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtFromDate
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(toDate)) {
                                    binding.txtToDate.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtToDate
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(duration)) {
                                    binding.txtDuration.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtDuration
                                    cancel = true
                                }
                                if(isRemarks) {
                                    if (TextUtils.isEmpty(remarks)) {
                                        binding.edtRemarks.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.edtRemarks
                                        cancel = true
                                    }
                                }
                            } else {
                                if (TextUtils.isEmpty(fromDate)) {
                                    binding.txtFromDate.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtFromDate
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(toDate)) {
                                    binding.txtToDate.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtToDate
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(fromTime)) {
                                    binding.txtFromTime.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtFromTime
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(toTime)) {
                                    binding.txtToTime.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtToTime
                                    cancel = true
                                }
                                if(isRemarks) {
                                    if (TextUtils.isEmpty(remarks)) {
                                        binding.edtRemarks.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.edtRemarks
                                        cancel = true
                                    }
                                }
                            }
                        } else
                            if (!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
                                if (binding.isFlexibleSwitch.isChecked) {
                                    if (TextUtils.isEmpty(date)) {
                                        binding.txtDate.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtDate
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(duration)) {
                                        binding.txtDuration.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtDuration
                                        cancel = true
                                    }
                                    if(isRemarks) {
                                        if (TextUtils.isEmpty(remarks)) {
                                            binding.edtRemarks.error =
                                                getString(R.string.error_field_required)
                                            focusView = binding.edtRemarks
                                            cancel = true
                                        }
                                    }
                                } else {
                                    if (TextUtils.isEmpty(date)) {
                                        binding.txtDate.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtDate
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(fromTime)) {
                                        binding.txtFromTime.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtFromTime
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(toTime)) {
                                        binding.txtToTime.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtToTime
                                        cancel = true
                                    }
                                    if(isRemarks) {
                                        if (TextUtils.isEmpty(remarks)) {
                                            binding.edtRemarks.error =
                                                getString(R.string.error_field_required)
                                            focusView = binding.edtRemarks
                                            cancel = true
                                        }
                                    }
                                }
                            } else
                                if (binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked && !binding.isFlexibleSwitch.isChecked) {
                                    if (TextUtils.isEmpty(fromDate)) {
                                        binding.txtFromDate.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtFromDate
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(toDate)) {
                                        binding.txtToDate.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtToDate
                                        cancel = true
                                    }
                                    if(isRemarks) {
                                        if (TextUtils.isEmpty(remarks)) {
                                            binding.edtRemarks.error =
                                                getString(R.string.error_field_required)
                                            focusView = binding.edtRemarks
                                            cancel = true
                                        }
                                    }
                                } else
                                    if (binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked && !binding.isFlexibleSwitch.isChecked) {
                                        if (TextUtils.isEmpty(date)) {
                                            binding.txtDate.error =
                                                getString(R.string.error_field_required)
                                            focusView = binding.txtDate
                                            cancel = true
                                        }
                                        if(isRemarks) {
                                            if (TextUtils.isEmpty(remarks)) {
                                                binding.edtRemarks.error =
                                                    getString(R.string.error_field_required)
                                                focusView = binding.edtRemarks
                                                cancel = true
                                            }
                                        }
                                    } else {
                                        cancel = false
                                    }


        /*   if (type_id == null) {
               SnackBarUtil.showSnackbar(
                   context, "Please select Request Type", R.drawable.allert
               )
               //  cancel = true;
           }

           if (permission_all_id == null) {
               SnackBarUtil.showSnackbar(
                   context, "Please select Leave Type", R.drawable.allert
               )
               //  cancel = true;
           }*/


        if (cancel) {
            focusView!!.requestFocus()
        } else {
            try {
                if (EnumUtils.isNetworkConnected(mContext) && !isFileMandatory) {

                    if(isRemarks){
                        if(binding.edtRemarks.text.toString()!=null){
                            callPermissionRequestsAPI(id)
                        }
                        else{
                            binding.edtRemarks.error =
                                getString(R.string.error_field_required)
                            focusView = binding.edtRemarks
                            cancel = true
                        }
                    }
                    else{
                        callPermissionRequestsAPI(id)
                    }



                } else
                    if(isFileMandatory){
                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                            mContext!!,
                            getString(R.string.file_mandatory_txt),
                            R.drawable.caution,
                            resources.getColor(R.color.red)
                        )
                    }else
                        if(!EnumUtils.isNetworkConnected(mContext)){
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                mContext!!,
                                getString(R.string.no_internet_connection),
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )
                        }
            } catch (e: Exception) {
            }
        }

        //  callPermissionRequestsAPI(id)

        /*  binding.txtDate.error=null
         binding.txtToDate.error = null
         binding.txtFromDate.error=null
         binding.edtRemarks.error = null
         binding.txtFromTime.error=null
         binding.txtToTime.error =null

         var date: String = binding.txtDate.text.toString()
         val toDate: String = binding.txtToDate.text.toString()
         val remarks: String = binding.edtRemarks.text.toString()
         val fromDate: String = binding.txtFromDate.text.toString()
         val fromTime: String = binding.txtFromTime.text.toString()
       val toTime: String = binding.txtToTime.text.toString()

       var cancel = false
       var focusView: View? = null

       if (TextUtils.isEmpty(date)) {
           tv_date.setError(getString(R.string.error_field_required));
           focusView = tv_date;
           cancel = true;
       }
       if (binding.isForPeriodSwitch.isChecked && binding.isForFullDayCheckBox.isChecked) {
           if (fromTime == "From Time") {
               binding.txtFromTime.error = getString(R.string.error_field_required)
               focusView = binding.txtFromTime
               cancel = true
           }
           if (toTime == "To Time") {
               binding.txtToTime.error = getString(R.string.error_field_required)
               focusView = binding.txtToTime
               cancel = true
           }
           if (TextUtils.isEmpty(date)) {
               binding.txtDate.error = getString(R.string.error_field_required)
               focusView = binding.txtDate
               cancel = true
           }
           if (TextUtils.isEmpty(remarks)) {
               binding.edtRemarks.error = getString(R.string.error_field_required)
               focusView = binding.edtRemarks
               cancel = true
           }

       }
       if (binding.isForPeriodSwitch.isChecked && !binding.isForFullDayCheckBox.isChecked){


           if (TextUtils.isEmpty(toDate)) {
               binding.txtToDate.error = getString(R.string.error_field_required)
               focusView = binding.txtToDate
               cancel = true
           }
           if (TextUtils.isEmpty(remarks)) {
               binding.edtRemarks.error = getString(R.string.error_field_required)
               focusView = binding.edtRemarks
               cancel = true
           }

           binding.txtDate.text=fromDate
       }
       if (binding.isForFullDayCheckBox.isChecked&&!binding.isForPeriodSwitch.isChecked)
       {
           if (TextUtils.isEmpty(date)) {
               binding.txtDate.error = getString(R.string.error_field_required)
               focusView = binding.txtDate
               cancel = true
           }
           if (TextUtils.isEmpty(remarks)) {
               binding.edtRemarks.error = getString(R.string.error_field_required)
               focusView = binding.edtRemarks
               cancel = true
           }

       }


       if (cancel) {
           focusView!!.requestFocus()
       } else {
           try {
               if (EnumUtils.isNetworkConnected(context)) {

                   callPermissionRequestsAPI(id)

               } else {
                   SnackBarUtil.showSnackbar(
                       context, getString(R.string.no_connection), R.drawable.allert
                   )
               }
           } catch (e: Exception) {

           }
       }*/


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun callPermissionRequestsAPI(id: Int) {
// Need to call Save Permission API
        addObserverPermissionRequest()
        getPermissionRequestsDetails(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPermissionRequestsDetails(typeId: Int) {

        if (binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
            /*  from date ,to date,remark*/
            permissionSaveRequest = RequestSavePermissionRequest(
                permission_all_id!!,
                /* UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId*/
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId.toString(),
                typeId,
                binding.txtFromDate.text.toString(),
                convertToDateTime(binding.txtFromDate.text.toString(), "00:00"),
                convertToDateTime(binding.txtToDate.text.toString(), "00:00"),
                binding.isForFullDayCheckBox.isChecked,
                binding.edtRemarks.text.toString(),
                binding.isForPeriodSwitch.isChecked,
                convertToDateTime(binding.txtToDate.text.toString(), "00:00"),
                binding.isForFullDayCheckBox.isChecked!!,
                if (binding.isFlexibleSwitch.isChecked) {
                    convertToMinutes(binding.txtDuration.text.toString())
                } else {
                    0
                },

                removeSpaces(base64String.toString()),
                fileTypeStr.toString(),
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                0,
                true,
                false,
                false,
                0,
                convertToDateTime(binding.txtToDate.text.toString(), "00:00"),
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId
            )


        } else if (!binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
// from date,to date,to time,from time

            permissionSaveRequest = RequestSavePermissionRequest(
                permission_all_id!!,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId.toString(),
                typeId,
                binding.txtFromDate.text.toString(),
                /* convertToDateTime(
                     binding.txtFromDate.text.toString(),
                     binding.txtFromTime.text.toString()
                 ),
                 convertToDateTime(
                     binding.txtToDate.text.toString(),
                     binding.txtToTime.text.toString()
                 ),*/


                if (binding.isFlexibleSwitch.isChecked) {
                    convertToDateTime(
                        binding.txtFromDate.text.toString(),
                        "00:00"
                    )
                } else {
                    convertToDateTime(
                        binding.txtFromDate.text.toString(),
                        binding.txtFromTime.text.toString()
                    )
                },

                if (binding.isFlexibleSwitch.isChecked) {
                    convertToDateTime(
                        binding.txtToDate.text.toString(),
                        "00:00"
                    )
                } else {
                    convertToDateTime(
                        binding.txtToDate.text.toString(),
                        binding.txtToTime.text.toString()
                    )
                },


                binding.isForFullDayCheckBox.isChecked,
                binding.edtRemarks.text.toString(),
                binding.isForPeriodSwitch.isChecked,
                if (binding.isFlexibleSwitch.isChecked) {
                    convertToDateTime(
                        binding.txtToDate.text.toString(),
                        "00:00"
                    )
                } else {
                    convertToDateTime(
                        binding.txtToDate.text.toString(),
                        binding.txtToTime.text.toString()
                    )
                },

                binding.isFlexibleSwitch.isChecked!!,
                if (binding.isFlexibleSwitch.isChecked) {
                    convertToMinutes(binding.txtDuration.text.toString())
                } else {
                    0
                },
                removeSpaces(base64String.toString()),
                fileTypeStr.toString(),
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                0,
                true,
                false,
                false,
                0,
                convertToDateTime(binding.txtToDate.text.toString(), "00:00"),
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId
            )


        } else if (binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
            /*
            * Date , Remarks
            * */
            permissionSaveRequest = RequestSavePermissionRequest(
                permission_all_id!!,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId.toString(),
                typeId,
                binding.txtDate.text.toString(),
                convertToDateTime(binding.txtDate.text.toString(), "00:00"),
                convertToDateTime(binding.txtDate.text.toString(), "00:00"),
                binding.isForFullDayCheckBox.isChecked,
                binding.edtRemarks.text.toString(),
                binding.isForPeriodSwitch.isChecked,
                convertToDateTime(binding.txtDate.text.toString(), "00:00"),
                binding.isForFullDayCheckBox.isChecked,

                //  binding.isFlexibleSwitch.isChecked!!,
                if (binding.isFlexibleSwitch.isChecked) {
                    convertToMinutes(binding.txtDuration.text.toString())
                } else {
                    0
                },
                removeSpaces(base64String.toString()),
                fileTypeStr.toString(),
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                0, true, false, false, 0,
                convertToDateTime(binding.txtDate.text.toString(), "00:00"), 0, 0, 0, 0, 0, 0, 0, 0,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId
            )

        } else if (!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
            /* Date,   From Time, To Time , Remarks */
            permissionSaveRequest = RequestSavePermissionRequest(
                permission_all_id!!,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId.toString(),
                typeId,
                binding.txtDate.text.toString(),

                if (binding.isFlexibleSwitch.isChecked) {
                    convertToDateTime(
                        binding.txtDate.text.toString(),
                        "00:00"
                    )
                } else {


                    convertToDateTime(
                        binding.txtDate.text.toString(),
                        binding.txtFromTime.text.toString()
                    )


                },

                if (binding.isFlexibleSwitch.isChecked) {

                    convertToDateTime(
                        binding.txtDate.text.toString(),
                        "00:00"
                    )


                } else {

                    convertToDateTime(
                        binding.txtDate.text.toString(),
                        binding.txtToTime.text.toString()
                    )

                },

                /* convertToDateTime(
                      binding.txtDate.text.toString(),
                      binding.txtFromTime.text.toString()
                  ),
                  convertToDateTime(
                      binding.txtDate.text.toString(),
                      binding.txtToTime.text.toString()
                  ),*/
                binding.isForFullDayCheckBox.isChecked,
                binding.edtRemarks.text.toString(),
                binding.isForPeriodSwitch.isChecked,
                convertToDateTime(
                    binding.txtDate.text.toString(),
                    "00:00"
                ),
                binding.isFlexibleSwitch.isChecked!!,
                if (binding.isFlexibleSwitch.isChecked) {
                    convertToMinutes(binding.txtDuration.text.toString())
                } else {
                    0
                },
                removeSpaces(base64String.toString()),
                fileTypeStr.toString(),
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                0, true, false, false, 0,
                convertToDateTime(binding.txtDate.text.toString(), "00:00"), 0, 0, 0, 0, 0, 0, 0, 0,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId
            )


        }





        viewModel_Permissions.getRequestSavePermissionData(
            mContext!!,
            permissionSaveRequest!!
        )

        Log.e("Request_P",permissionSaveRequest.toString())

    }

    fun convertToMinutes(timeStr: String): Int {
        // Split the time string into hours and minutes
        val (hours, minutes) = timeStr.split(":").map { it.toInt() }

        // Calculate total minutes
        val totalMinutes = hours * 60 + minutes

        return totalMinutes
    }

    private fun removeSpaces(input: String): String {
        return input.replace("\\s".toRegex(), "")
    }

    private fun addObserverPermissionRequest() {
        viewModel_Permissions.requestSavePermissionResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse
                            // val arrayList = ArrayList<SpinnerItem_BO>()

                            if (requestResponse.data != null) {
                                if (requestResponse.message.equals("Succes")) {


                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,
                                        requestResponse.data.msg,
                                        R.drawable.app_icon,
                                        colorPrimary,
                                        null/* SnackBarUtil.OnClickListenerNew {

                                            replaceFragment(
                                                SelfServiceFragment(),
                                                R.id.flFragment,
                                                false
                                            )


                                        }*/)


                                } else {
                                    if (requestResponse.message.equals("Error")) {
                                        // SnackBarUtil.showSnackbar(mContext,requestResponse.data.msg,false)
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,
                                            requestResponse.data.msg,
                                            R.drawable.caution,
                                            resources.getColor(R.color.red),
                                            SnackBarUtil.OnClickListenerNew {


                                            })


                                    }


                                }


                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                message,
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }


    private fun datePicker(date: String, txt_Date: TextView) {
        try {
            // val todate: String = binding.txtToDate.getText().toString()
            if (TextUtils.isEmpty(date)) {
                val c = Calendar.getInstance()
                val mDay = c[Calendar.DAY_OF_MONTH]
                val mMonth = c[Calendar.MONTH] + 1
                val mYear = c[Calendar.YEAR]
                pickDate(mDay, mMonth, mYear, txt_Date)
            } else {
                val dateString = date.split("-").toTypedArray()
                pickDate(
                    dateString[2].toInt(), dateString[1].toInt(), dateString[0].toInt(), txt_Date
                )
            }
        } catch (e: Exception) {
            e.message
        }

    }

    private fun timePicker(textView: TextView) {
        val now = Calendar.getInstance()
        var hours: Int
        val timePickerDialog = TimePickerDialog.newInstance(
            { view, hourOfDay, minute, second -> //  textView.setText(hourOfDay + ":" + minute);
                if (hourOfDay > 12) {
                    hours = hourOfDay
                } else if (hourOfDay == 12) {
                    hours = 12
                } else if (hourOfDay == 0) {
                    hours = 12
                } else {
                    hours = hourOfDay
                }
                textView.text =
                    LocaleHelper.arabicToEnglish(
                        String.format(
                            "%02d",
                            hours
                        ) + ":" + String.format("%02d", minute)
                    )
            }, now[Calendar.HOUR_OF_DAY], now[Calendar.MINUTE], false
        )
        timePickerDialog.setLocale(Locale.ENGLISH)
        timePickerDialog.setOkText("OK")
        timePickerDialog.setCancelText("Cancel")
        timePickerDialog.version = TimePickerDialog.Version.VERSION_2
        timePickerDialog.accentColor = ContextCompat.getColor(mContext!!, R.color.mdtp_accent_color)
        timePickerDialog.show(requireActivity().supportFragmentManager, "TimePickerDialog")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDateTime(date: String, time: String): String {

        // Combine the date and time to create a LocalDateTime object
        try {
            val localDateTime =
                LocalDateTime.parse(
                    "$date $time".trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                )

            // Format the LocalDateTime object in the desired format
            val formattedDateTime =
                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

            return formattedDateTime
        } catch (e: Exception) {
            return "Invalid date or time format"
        }
    }

    private fun pickDate(day: Int, month: Int, year: Int, textView: TextView) {
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"RequestsFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
        /* val now = Calendar.getInstance()
            now[year, month - 1] = day
            val dpd = DatePickerDialog.newInstance(
                { view, year, monthOfYear, dayOfMonth ->
                    var monthOfYear = monthOfYear
                    try {
                        val yearNew = year
                        monthOfYear = monthOfYear + 1
                        var monthnew = ""
                        var daynew = ""
                        monthnew = if (monthOfYear < 10) {
                            "0$monthOfYear"
                        } else {
                            "" + monthOfYear
                        }
                        daynew = if (dayOfMonth < 10) {
                            "0$dayOfMonth"
                        } else {
                            "" + dayOfMonth
                        }
                        textView.text = "$yearNew-$monthnew-$daynew"
                    } catch (e: java.lang.Exception) {
                    }
                }, now[Calendar.YEAR], now[Calendar.MONTH], now[Calendar.DAY_OF_MONTH]
            )
            dpd.locale = Locale.ENGLISH
            dpd.setOkText("OK")
            dpd.setCancelText("Cancel")
            dpd.version = DatePickerDialog.Version.VERSION_2
            dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")*/

    }


    private fun checkValidations_LeaveRequest(isFileMandatory: Boolean) {

        binding.txtToDate.error = null
        binding.txtFromDate.error = null
        if(isRemarks) {
            binding.edtRemarks.error = null
        }
        val fromDate: String = binding.txtFromDate.text.toString()
        val toDate: String = binding.txtToDate.text.toString()
        val remarks: String = binding.edtRemarks.text.toString()

        var cancel = false
        var focusView: View? = null

        /*if (TextUtils.isEmpty(date)) {
            tv_date.setError(getString(R.string.error_field_required));
            focusView = tv_date;
            cancel = true;
        }*/if (TextUtils.isEmpty(fromDate)) {
            binding.txtFromDate.error = getString(R.string.error_field_required)
            focusView = binding.txtFromDate
            cancel = true
        }
        if (TextUtils.isEmpty(toDate)) {
            binding.txtToDate.error = getString(R.string.error_field_required)
            focusView = binding.txtToDate
            cancel = true
        }
        if(isRemarks) {
            if (TextUtils.isEmpty(remarks)) {
                binding.edtRemarks.error = getString(R.string.error_field_required)
                focusView = binding.edtRemarks
                cancel = true
            }
        }


        if (cancel) {
            focusView!!.requestFocus()
        } else {
            try {
                if (EnumUtils.isNetworkConnected(mContext) && !isFileMandatory) {
                    if(isRemarks){
                        if(binding.edtRemarks.text.toString()!=null){
                            callSaveLeaveRequestsAPI()
                        }
                        else{
                            binding.edtRemarks.error =
                                getString(R.string.error_field_required)
                            focusView = binding.edtRemarks
                            cancel = true
                        }
                    }
                    else{
                        callSaveLeaveRequestsAPI()
                    }


                } else {

                    if(isFileMandatory){
                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                            mContext,
                            getString(R.string.file_mandatory_txt),
                            R.drawable.caution,
                            resources.getColor(R.color.red)
                        )
                    }else
                        if(!EnumUtils.isNetworkConnected(mContext)){
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                mContext,
                                getString(R.string.no_internet_connection),
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )
                        }

                }
            } catch (e: Exception) {
            }
        }
    }


    private fun setUpDropDown(empList: ArrayList<SpinnerItem_BO>) {
        val arrayList = ArrayList<String>()

        val arrayListFulldayPermission = ArrayList<String>()
        val arrayListPeriodPermission = ArrayList<String>()
        isMandatoryFile = empList
        // arrayList.add("Please select")
        for (i in empList.indices) {
            arrayList.add(empList[i].title)

            arrayListFulldayPermission.add(empList[i].hasFullDayPermission.toString())
            arrayListPeriodPermission.add(empList[i].hasPermissionForPeriod.toString())
        }
        val customAdapter = Spinner_Adapter(mContext, arrayList)
        binding.spSelectRequest.adapter = customAdapter
        binding.spSelectRequest.setSelection(0)



        if (GlobalVariables.updateRequest) {
            for (i in empList.indices) {
                val perm: String = violationsDataModel?.fK_PermId.toString()
                val permList = empList[i].id.toString()
                if (perm.equals(permList, ignoreCase = true)) {
                    binding.spSelectRequest.setSelection(i)
                }
            }
        }
    }



    private fun setUpDropDown_RequestType(empList: ArrayList<SpinnerItem_BO_Type>) {
        val arrayList = ArrayList<String>()
        //arrayList.add("Please select")
        for (i in empList.indices) {

            arrayList.add(empList[i].desc_en)

        }
        val customAdapter = Spinner_Adapter(context, arrayList)
        binding.spSelectRequestType.adapter = customAdapter


        if (GlobalVariables.updateRequest) {
            //  disAbledAllFields()

            binding.spSelectRequestType.isEnabled = false
            binding.spSelectRequestType.isClickable = false

            binding.spSelectRequestType.background =
                resources.getDrawable(R.drawable.edit_disable_text)

            val perm: String = violationsDataModel!!.type
            permission_all_id = violationsDataModel!!.permissionId
            for (i in empList.indices) {


                val permList: String = empList[i].requestType
                if (perm.equals(permList, ignoreCase = true)) {
                    binding.spSelectRequestType.setSelection(i)

                    if (perm.equals("M")) {

                        uploadPermissionStr = "ManualEntryRequestFiles"
                        attachedFileStr = violationsDataModel!!.attachedFile
                        strPermissionTypeLetter = "M"
                        setReasonLayoutsVisible()
                        callReasonsAPI()

                        binding.txtReasonsDate.text =
                            getSimpleDateFormat(violationsDataModel!!.permStartDate)
                        // binding.txtReasonsTime.text = violationsDataModel!!.permStart
                        //    LocaleHelper.arabicToEnglish(DateTime_Op.simpleTimeConversion(violationsDataModel!!.inTime))

                        binding.edtRemarks.setText(violationsDataModel!!.remark)
                        //  binding.txtReasonsTime.text =  LocaleHelper.arabicToEnglish(DateTime_Op.simpleTimeConversion(violationsDataModel!!.inTime))
                        binding.txtReasonsTime.text =DateTime_Op.simpleTimeConversionNew(violationsDataModel!!.inTime)

                        if ((violationsDataModel!!.attachedFile != null) && (!violationsDataModel!!.attachedFile!!.isEmpty())) {
                            binding.selectedFileImageView.visibility = View.VISIBLE
                            binding.txtFilename.visibility = View.VISIBLE
                            binding.txtFilename.text =
                                violationsDataModel!!.permissionId.toString() + violationsDataModel!!.attachedFile
                            getDocumentIconResId(binding.txtFilename.text.toString())
                        } else {
                            binding.selectedFileImageView.visibility = View.GONE
                            binding.txtFilename.visibility = View.GONE
                        }


                    } else
                        if (perm.equals("P")) {

                            uploadPermissionStr = "PermissionRequestFiles"
                            attachedFileStr = violationsDataModel!!.attachedFile
                            strPermissionTypeLetter = "P"

                            callRequestsAPI(perm)
                            setPermissionRequestLayoutsVisible()


                            /*
                            for (j in 0 until arrayList_type_leave_permission.size) {
                                val permListNew: Int = arrayList_type_leave_permission[j].id

                                if (violationsDataModel?.fK_PermId == permListNew) {
                                    println("Match found for permissionId ${violationsDataModel?.permissionId}")


                                }
                            }
*/



                            if ((violationsDataModel!!.attachedFile != null) && (!violationsDataModel!!.attachedFile!!.isEmpty())) {
                                binding.selectedFileImageView.visibility = View.VISIBLE
                                binding.txtFilename.visibility = View.VISIBLE
                                binding.txtFilename.text =
                                    violationsDataModel!!.permissionId.toString() + violationsDataModel!!.attachedFile
                                getDocumentIconResId(binding.txtFilename.text.toString())
                            } else {
                                binding.selectedFileImageView.visibility = View.GONE
                                binding.txtFilename.visibility = View.GONE
                            }


                            binding.isForFullDayCheckBox.isChecked = violationsDataModel!!.isfullDay
                            binding.isForPeriodSwitch.isChecked = violationsDataModel!!.isForPeriod

                            binding.isFlexibleSwitch.isChecked = violationsDataModel!!.isFlexible



                            if (binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
                                //  from date,to date,remarks


                                binding.lFromDate.visibility = View.VISIBLE
                                binding.lToDate.visibility = View.VISIBLE
                                binding.lDate.visibility = View.GONE



                                binding.lToTime.visibility = View.GONE
                                binding.lFromTime.visibility = View.GONE



                                binding.txtFromDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permStartDate)
                                binding.txtToDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permEndDate)

                                binding.edtRemarks.setText(violationsDataModel!!.remark)

                            } else if (!binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
                                //  from date,to date,to time,from time


                                binding.lFromDate.visibility = View.VISIBLE
                                binding.lToDate.visibility = View.VISIBLE
                                binding.lDate.visibility = View.GONE


                                binding.lToTime.visibility = View.VISIBLE
                                binding.lFromTime.visibility = View.VISIBLE


                                binding.txtFromDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permStartDate)
                                binding.txtToDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permEndDate)


                                binding.txtFromTime.text = violationsDataModel!!.permStart
                                binding.txtToTime.text = violationsDataModel!!.permEnd

                                binding.edtRemarks.setText(violationsDataModel!!.remark)


                            } else if (binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
                                //  Date ,Remarks


                                binding.lFromDate.visibility = View.GONE
                                binding.lToDate.visibility = View.GONE
                                binding.lDate.visibility = View.VISIBLE


                                binding.lToTime.visibility = View.GONE
                                binding.lFromTime.visibility = View.GONE
                                binding.txtDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permStartDate)


                                binding.edtRemarks.setText(violationsDataModel!!.remark)

                            } else {

// !binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked
                                //  Date,from time,to time,Remarks


                                if (violationsDataModel!!.isFlexible) {
                                    binding.isFlexibleSwitch.isChecked = true
                                    binding.isFlexibleSwitch.background =
                                        resources.getDrawable(R.drawable.edit_text)
                                    binding.isFlexibleSwitch.isEnabled = true
                                    binding.isFlexibleSwitch.isClickable = true

                                    binding.lDuration.visibility = View.VISIBLE

                                    binding.lFromDate.visibility = View.GONE
                                    binding.lToDate.visibility = View.GONE
                                    binding.lToTime.visibility = View.GONE
                                    binding.lFromTime.visibility = View.GONE


                                    binding.txtDate.text =
                                        getSimpleDateFormat(violationsDataModel!!.permStartDate )

                                    binding.txtDuration.text = violationsDataModel!!.duration
                                } else {
                                    binding.isFlexibleSwitch.isChecked = false
                                    binding.lDuration.visibility = View.GONE
                                    binding.lFromDate.visibility = View.GONE
                                    binding.lToDate.visibility = View.GONE



                                    binding.lToTime.visibility = View.VISIBLE
                                    binding.lFromTime.visibility = View.VISIBLE





                                    binding.txtFromTime.text = violationsDataModel!!.permStart
                                    binding.txtToTime.text = violationsDataModel!!.permEnd

                                }




                                binding.lDate.visibility = View.VISIBLE
                                binding.txtDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permStartDate )
                                binding.edtRemarks.setText(violationsDataModel!!.remark)


                            }


                        } else
                            if (perm.equals("L")) {
                                uploadPermissionStr = "LeaveRequestFiles"
                                attachedFileStr = violationsDataModel!!.attachedFile
                                strPermissionTypeLetter = "L"
                                setLeaveRequestLayoutsVisible()
                                callRequestsAPI(perm)
                                binding.edtRemarks.setText(violationsDataModel!!.remark)

                                binding.txtFromDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permStartDate)
                                binding.txtToDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permEndDate)





                                if ((violationsDataModel!!.attachedFile != null) && (!violationsDataModel!!.attachedFile!!.isEmpty())) {
                                    binding.selectedFileImageView.visibility = View.VISIBLE
                                    binding.txtFilename.visibility = View.VISIBLE
                                    binding.txtFilename.text =
                                        violationsDataModel!!.permissionId.toString() + violationsDataModel!!.attachedFile
                                    getDocumentIconResId(binding.txtFilename.text.toString())
                                } else {
                                    binding.selectedFileImageView.visibility = View.GONE
                                    binding.txtFilename.visibility = View.GONE
                                }

                            }

                } else if (perm.equals("V")) {


                    if ((violationsDataModel!!.remark.contains("Missing")
                                //    || (violationsDataModel!!.remark.contains("Early"))
                                )
                    ) {


                        if ("M".equals(permList, ignoreCase = true)) {
                            binding.spSelectRequestType.setSelection(i)



                            strPermissionTypeLetter = "M"

                            setReasonLayoutsVisible()
                            callReasonsAPI()



                            binding.txtReasonsDate.text =
                                getSimpleDateFormat(violationsDataModel!!.permStartDate)
                            binding.txtReasonsTime.text = DateTime_Op.simpleTimeConversionNew(violationsDataModel!!.inTime)
                            // violationsDataModel!!.permStart
                            binding.edtRemarks.setText(violationsDataModel!!.remark)
                        }

                    }

                    /*      else if(violationsDataModel!!.remark.contains("Absent")){

                              if ("L".equals(permList, ignoreCase = true)) {
                                  binding.spSelectRequestType.setSelection(i)

                                  strPermissionTypeLetter = "L"

                                  setLeaveRequestLayoutsVisible()
                                  callRequestsAPI("L")
                              }

                          }*/
                    else {

                        if ("P".equals(permList, ignoreCase = true)) {
                            binding.spSelectRequestType.setSelection(i)

                            strPermissionTypeLetter = "P"
                            callRequestsAPI("P")
                            setPermissionRequestLayoutsVisible()





                            binding.isForFullDayCheckBox.isChecked = violationsDataModel!!.isfullDay
                            binding.isForPeriodSwitch.isChecked = violationsDataModel!!.isForPeriod



                            if (binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
                                //  from date,to date,remarks


                                binding.lFromDate.visibility = View.VISIBLE
                                binding.lToDate.visibility = View.VISIBLE
                                binding.lFromTime.visibility = View.GONE
                                binding.lToTime.visibility = View.GONE
                                binding.lDate.visibility = View.GONE


                                binding.txtFromDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permStartDate)
                                binding.txtToDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permEndDate)

                                binding.edtRemarks.setText(violationsDataModel!!.remark)

                            } else if (!binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
                                //  from date,to date,to time,from time

                                binding.lFromDate.visibility = View.VISIBLE
                                binding.lToDate.visibility = View.VISIBLE
                                binding.lFromTime.visibility = View.VISIBLE
                                binding.lToTime.visibility = View.VISIBLE
                                binding.lDate.visibility = View.GONE

                                binding.txtFromDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permStartDate)
                                binding.txtToDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.permEndDate)

                                binding.txtFromTime.text = violationsDataModel!!.permStart
                                binding.txtToTime.text = violationsDataModel!!.permEnd


                                binding.edtRemarks.setText(violationsDataModel!!.remark)


                            } else if (binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
                                //  Date ,Remarks


                                binding.lFromDate.visibility = View.GONE
                                binding.lToDate.visibility = View.GONE
                                binding.lFromTime.visibility = View.GONE
                                binding.lToTime.visibility = View.GONE
                                binding.lDate.visibility = View.VISIBLE
                                binding.txtDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.moveDate)


                                binding.edtRemarks.setText(violationsDataModel!!.remark)

                            } else {

// !binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked
                                //  Date,from time,to time,Remarks


                                binding.lFromDate.visibility = View.GONE
                                binding.lToDate.visibility = View.GONE
                                binding.lFromTime.visibility = View.VISIBLE
                                binding.lToTime.visibility = View.VISIBLE
                                binding.lDate.visibility = View.VISIBLE

                                binding.txtDate.text =
                                    getSimpleDateFormat(violationsDataModel!!.moveDate)

                                binding.txtFromTime.text = violationsDataModel!!.permStart
                                binding.txtToTime.text = violationsDataModel!!.permEnd


                                binding.edtRemarks.setText(violationsDataModel!!.remark)


                            }

                        }

                        /*
                                                     if ("M".equals(permList, ignoreCase = true)) {
                                                        binding.spSelectRequestType.setSelection(i)

                                                        strPermissionTypeLetter = "M"
                                                        setReasonLayoutsVisible()
                                                        callReasonsAPI()

                                                    }*/


                    }
                } else if (perm.equals("A")) {
                    if (violationsDataModel!!.remark.contains("Absent")) {

                        if ("L".equals(permList, ignoreCase = true)) {
                            binding.spSelectRequestType.setSelection(i)

                            strPermissionTypeLetter = "L"

                            setLeaveRequestLayoutsVisible()
                            callRequestsAPI("L")

                            binding.edtRemarks.setText(violationsDataModel!!.remark)
                            binding.txtToDate.text =
                                getSimpleDateFormat(violationsDataModel!!.permEndDate)

                            binding.txtFromDate.text =
                                getSimpleDateFormat(violationsDataModel!!.permStartDate)
                        }

                    }
                } else if (perm.equals("L")) {

                    if ("L".equals(permList, ignoreCase = true)) {
                        binding.spSelectRequestType.setSelection(i)

                        strPermissionTypeLetter = "L"

                        setLeaveRequestLayoutsVisible()
                        callRequestsAPI("L")

                        binding.edtRemarks.setText(violationsDataModel!!.remark)

                        binding.txtFromDate.text =
                            getSimpleDateFormat(violationsDataModel!!.permStartDate)
                        binding.txtToDate.text =
                            getSimpleDateFormat(violationsDataModel!!.permEndDate)


                    }
                } else if (perm.equals("MS")) {
                    if ("M".equals(permList, ignoreCase = true)) {
                        binding.spSelectRequestType.setSelection(i)
                        strPermissionTypeLetter = "M"
                        setReasonLayoutsVisible()
                        callReasonsAPI()
                        binding.txtReasonsDate.text =
                            getSimpleDateFormat(violationsDataModel!!.permStartDate)
                        binding.txtReasonsTime.text = DateTime_Op.simpleTimeConversionNew(violationsDataModel!!.inTime)
                        binding.edtRemarks.setText(violationsDataModel!!.remark)
                    }

                }


            }
        }


    }


    private fun disAbledAllFields() {

        binding.isForFullDayCheckBox.background =
            resources.getDrawable(R.drawable.edit_disable_text)
        binding.isForFullDayCheckBox.isEnabled = false
        binding.isForFullDayCheckBox.isClickable = false


        binding.isForPeriodSwitch.background =
            resources.getDrawable(R.drawable.edit_disable_text)
        binding.isForPeriodSwitch.isEnabled = false
        binding.isForPeriodSwitch.isClickable = false



        binding.isFlexibleSwitch.background =
            resources.getDrawable(R.drawable.edit_disable_text)
        binding.isFlexibleSwitch.isEnabled = false
        binding.isFlexibleSwitch.isClickable = false



        binding.txtReasonsDate.background =
            resources.getDrawable(R.drawable.edit_disable_text)
        binding.txtReasonsDate.isEnabled = false
        binding.txtReasonsDate.isClickable = false

        binding.txtReasonsTime.background =
            resources.getDrawable(R.drawable.edit_disable_text)
        binding.txtReasonsTime.isEnabled = false
        binding.txtReasonsTime.isClickable = false


        binding.txtDate.background =
            resources.getDrawable(R.drawable.edit_disable_text)
        binding.txtDate.isEnabled = false
        binding.txtDate.isClickable = false



        binding.txtFromDate.background =
            resources.getDrawable(R.drawable.edit_disable_text)
        binding.txtFromDate.isEnabled = false
        binding.txtFromDate.isClickable = false



        binding.txtToDate.background =
            resources.getDrawable(R.drawable.edit_disable_text)
        binding.txtToDate.isEnabled = false
        binding.txtToDate.isClickable = false

        /*
                binding.txtFromTime.background =
                    resources.getDrawable(R.drawable.edit_disable_text)
                binding.txtFromTime.isEnabled =false
                binding.txtFromTime.isClickable =false


                binding.txtToTime.background =
                    resources.getDrawable(R.drawable.edit_disable_text)
                binding.txtToTime.isEnabled =false
                binding.txtToTime.isClickable =false*/

    }

    private fun callReasonsAPI() {
        addObserverReasonAPI()
        getRequestsDetailsReasonBased()
    }

    private fun getRequestsDetailsReasonBased() {
        viewModel_Reasons.getReasonsData(mContext!!)
    }

    private fun addObserverReasonAPI() {
        viewModel_Reasons.reasonsResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { ReasonsResponse ->
                            val reasonsResponse = ReasonsResponse
                            //  val arrayList_reasons = ArrayList<ReasonsData>()
                            if (reasonsResponse != null) {
                                arrayList_reasons.clear()

                                if (reasonsResponse.data != null && reasonsResponse.data.isNotEmpty()) {

                                    for (i in 0 until reasonsResponse.data.size) {
                                        //   if (reasonsResponse.data[i].isDisplayType) {
                                        arrayList_reasons.add(reasonsResponse.data[i])
                                        // }
                                    }

                                }

                            }
                            setUpDropDownReasons(arrayList_reasons)
                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                message,
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }

    private fun setUpDropDownReasons(arrListReasons: ArrayList<ReasonsData>) {
        val arrayList = ArrayList<String>()
        for (i in arrListReasons.indices) {

            if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                arrayList.add(arrListReasons[i].reasonName)
            } else {
                arrayList.add(arrListReasons[i].reasonArabicName)

            }

        }
        val customAdapter = Spinner_Adapter(mContext, arrayList)

        //   binding.lLeavePermission.visibility= View.VISIBLE
        binding.spSelectRequest.visibility = View.VISIBLE
        binding.spSelectRequest.adapter = customAdapter
        binding.spSelectRequest.setSelection(0)




        if (GlobalVariables.updateRequest) {
            for (i in arrListReasons.indices) {
                val perm: String = violationsDataModel?.fK_PermId.toString()
                val permList = arrListReasons[i].reasonCode.toString()
                if (perm.equals(permList, ignoreCase = true)) {
                    binding.spSelectRequest.setSelection(i)
                }
            }
        }


    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {

            R.id.is_for_period_switch -> {
                if (isChecked) {
                    binding.lFromDate.visibility = View.VISIBLE
                    binding.lToDate.visibility = View.VISIBLE
                    binding.lDate.visibility = View.GONE

                } else {
                    binding.lFromDate.visibility = View.GONE
                    binding.lToDate.visibility = View.GONE
                    binding.lDate.visibility = View.VISIBLE
                }
            }

            R.id.is_for_full_day_check_box -> {
                if (isChecked) {
                    binding.lToTime.visibility = View.GONE
                    binding.lFromTime.visibility = View.GONE
                    binding.lDuration.visibility = View.GONE
                    binding.isFlexibleSwitch.isChecked = false
                    binding.isFlexibleSwitch.isClickable = false
                    binding.isFlexibleSwitch.background =
                        resources.getDrawable(R.drawable.edit_disable_text)

                } else {
                    binding.lToTime.visibility = View.VISIBLE
                    binding.lFromTime.visibility = View.VISIBLE
                    binding.lDuration.visibility = View.GONE

                    binding.isFlexibleSwitch.isChecked = false
                    binding.isFlexibleSwitch.isClickable = true
                    binding.isFlexibleSwitch.background =
                        resources.getDrawable(R.drawable.edit_text)


                }
            }


            R.id.is_flexible_switch -> {
                if (isChecked) {
                    binding.lDuration.visibility = View.VISIBLE

                    binding.lToTime.visibility = View.GONE
                    binding.lFromTime.visibility = View.GONE

                } else {


                    if (binding.isForFullDayCheckBox.isChecked) {
                        binding.lDuration.visibility = View.GONE

                        binding.lToTime.visibility = View.GONE
                        binding.lFromTime.visibility = View.GONE
                    } else {
                        binding.lDuration.visibility = View.GONE

                        binding.lToTime.visibility = View.VISIBLE
                        binding.lFromTime.visibility = View.VISIBLE
                    }

                }
            }


        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent?.id) {
            R.id.sp_select_request_type -> {

                spinnerItemBO_requsettype = arrayList_type[position]


                if (spinnerItemBO_requsettype!!.requestType.equals("P")) {
                    strPermissionTypeLetter = "P"
                    binding.permTxt.text= resources.getString(R.string.select_permission_txt)

                    setPermissionRequestLayoutsVisible()
                    callRequestsAPI(spinnerItemBO_requsettype!!.requestType)

                } else if (spinnerItemBO_requsettype!!.requestType.equals("L")) {
                    strPermissionTypeLetter = "L"
                    binding.permTxt.text= resources.getString(R.string.select_leave_txt)

                    setLeaveRequestLayoutsVisible()
                    callRequestsAPI(spinnerItemBO_requsettype!!.requestType)

                } else if (spinnerItemBO_requsettype!!.requestType.equals("M")) {
                    strPermissionTypeLetter = "M"
                    binding.permTxt.text= resources.getString(R.string.select_manual_entry_txt)

                    // setLeaveRequestLayoutsVisible()
                    callReasonsAPI()
                    setReasonLayoutsVisible()

                }
                clearAfterSelection()
            }

            R.id.sp_select_request -> {
                if (strPermissionTypeLetter.equals("P")) {
                    isFileMandatory=arrayList_type_leave_permission[position].attachmentIsMandatory
                    type_id = arrayList_type_leave_permission[position].id
                    duration = arrayList_type_leave_permission[position].maxDuration
                    isFlexible = arrayList_type_leave_permission[position].hasFlexiblePermission
                    isRemarks = arrayList_type_leave_permission[position].remarksIsMandatory

                    // if (!GlobalVariables.updateRequest) {
                    if (arrayList_type_leave_permission[position].hasPermissionForPeriod) {
                        binding.isForPeriodSwitch.isChecked = false
                        binding.isForPeriodSwitch.isClickable = true
                        binding.isForPeriodSwitch.background =
                            resources.getDrawable(R.drawable.edit_text)
                    } else {
                        binding.isForPeriodSwitch.isChecked = false
                        binding.isForPeriodSwitch.isClickable = false
                        binding.isForPeriodSwitch.background =
                            resources.getDrawable(R.drawable.edit_disable_text)
                    }

                    if (arrayList_type_leave_permission[position].hasFullDayPermission) {
                        binding.isForFullDayCheckBox.isChecked = false
                        binding.isForFullDayCheckBox.isClickable = true
                        binding.isForFullDayCheckBox.background =
                            resources.getDrawable(R.drawable.edit_text)
                    } else {
                        binding.isForFullDayCheckBox.isChecked = false
                        binding.isForFullDayCheckBox.isClickable = false
                        binding.isForFullDayCheckBox.background =
                            resources.getDrawable(R.drawable.edit_disable_text)

                    }


                    if (arrayList_type_leave_permission[position].hasFlexiblePermission) {
                        binding.isFlexibleSwitch.isChecked = false
                        binding.isFlexibleSwitch.isClickable = true
                        binding.isFlexibleSwitch.background =
                            resources.getDrawable(R.drawable.edit_text)
                    } else {
                        binding.isFlexibleSwitch.isChecked = false
                        binding.isFlexibleSwitch.isClickable = false
                        binding.isFlexibleSwitch.background =
                            resources.getDrawable(R.drawable.edit_disable_text)
                    }


                    //}

                    /* Toast.makeText(
                         mContext,
                         "type_id Permission: " + arrayList_type_leave_permission[position].id.toString(),
                         Toast.LENGTH_SHORT
                     ).show()*/

                } else if (strPermissionTypeLetter.equals("L")) {
                    type_id = arrayList_type_leave_permission[position].id
                    isFileMandatory=arrayList_type_leave_permission[position].attachmentIsMandatory
                    isRemarks = arrayList_type_leave_permission[position].remarksIsMandatory
                   /* binding.selectedFilecancelbtn.visibility=View.GONE
                    binding.selectedFileImageView.visibility=View.GONE
                    binding.txtFilename.visibility=View.GONE
                    binding.txtFilename.text=""
                    base64String = ""*/

                    /*  Toast.makeText(
                          mContext,
                          "type_id Leave: " + arrayList_type_leave_permission[position].id.toString(),
                          Toast.LENGTH_SHORT
                      ).show()*/

                } else if (strPermissionTypeLetter.equals("M")) {
                    type_id = arrayList_reasons[position].reasonCode
                    isFileMandatory=arrayList_reasons[position].attachmentIsMandatory
                    isRemarks = arrayList_reasons[position].remarksIsMandatory
                    binding.selectedFilecancelbtn.visibility=View.GONE
                    binding.selectedFileImageView.visibility=View.GONE
                    binding.txtFilename.visibility=View.GONE
                    base64String = ""
                    binding.txtFilename.text=""
                    /* Toast.makeText(
                         mContext,
                         "type_id Manual: " + arrayList_reasons[position].reasonCode.toString(),
                         Toast.LENGTH_SHORT
                     ).show()*/
                }
                /* if(spinnerItemBO_requsettype!!.equals("M")){
                     spinnerItemBo_reasons = arrayList_reasons[position]
                 }



                 else{
                     spinnerItemBO_type = arrayList_type_leave_permission[position]
                 }
*/

            }
        }


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}