package com.sait.tawajudpremiumplusnewfeatured.ui.pdf

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentPdfViewBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models.PdfReportRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.viewmodels.PdfReportViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.io.File
import java.util.Base64
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.IOException

import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PdfGenaratorFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentPdfViewBinding
    private var mContext: Context? = null
    private var form_id: String? = null
    private var from_date: String? = null
    private var to_date: String? = null
    private var str_emp_id:String?=null
    private var manager_id:String?=null
    private var isFromAdmin:Boolean?=false
    var selected_tab:Int?=0

    private lateinit var viewModel_pdfReport: PdfReportViewModel
    var pdfFilePath : String?= null
    var base64String:String?=null
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPdfViewBinding.inflate(inflater, container, false)

        viewModel_pdfReport = ViewModelProvider(this)[PdfReportViewModel::class.java]

        mContext = inflater.context
        val activity = this.activity as MainActivity?
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        arguments?.let {
            form_id = it.getString("form_id")
            from_date =  it.getString("from_date")
            to_date =  it.getString("to_date")

        }

        if(arguments!=null){
            if(requireArguments().getString("emp_id")!=null){
                str_emp_id = requireArguments().getString("emp_id")
                selected_tab = arguments?.getInt("selected_tab")
                Log.e("emp_id:str_emp_id",str_emp_id.toString())
            }

            if(requireArguments().getString("manager_id")!=null){
                manager_id = requireArguments().getString("manager_id")
                selected_tab = arguments?.getInt("selected_tab")
                Log.e("emp_id:manager_id",manager_id.toString())
            }

            if(requireArguments().getBoolean("isFromAdmin")){
                isFromAdmin = requireArguments().getBoolean("isFromAdmin")
                selected_tab = arguments?.getInt("selected_tab")

            }
        }

        callPdfReportAPI()
        setClickListeners(activity)






        return binding.root
    }





    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertBase64ToPdfNew(base64String: String, outputPath: String) {
        try {
            // Decode the Base64 string
            val pdfData = Base64.getDecoder().decode(base64String)

            // Write the decoded data to a PDF file
            val pdfFile = File(outputPath)

            if (!pdfFile.exists()) {
                pdfFile.createNewFile()
            }

            val fileOutputStream = FileOutputStream(pdfFile)
            fileOutputStream.write(pdfData)
            fileOutputStream.close()



            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                // External storage is accessible
                println("External storage is accessible.")
            } else {
                println("External storage is not accessible.")
            }
            pdfFilePath = outputPath
            //  displayPdf(outputPath)

            displayPdfNew()

            println("Conversion successful. PDF saved to: $outputPath")
        } catch (e: Exception) {
            println("Error converting Base64 to PDF: ${e.message}")
        }
    }


    private fun displayFromUri(file: File) {
        binding.pdfView.fromFile(file)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .onPageChange { page, pageCount -> /* Handle page change */ }
            .onLoad { nbPages -> /* Handle PDF load */ }
            .scrollHandle(DefaultScrollHandle(mContext))
            .load()
    }
    private fun displayPdfNew() {
        try {
            val pdfFile = File(pdfFilePath)
            displayFromUri(pdfFile)



        } catch (e: IOException) {
            e.printStackTrace()
        }    }



    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.downloadBtn.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callPdfReportAPI() {
        addObserver()
        postPdfReportDetails()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addObserver() {
        viewModel_pdfReport.pdfReportResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()
                        if(response.data!!.isSuccess){


                            if(response.data!=null){

                              if(response.data.data.msg!=null){
                                  if (response.data.data.msg == mContext!!.resources.getString(R.string.response_no_record_found_txt)){
                                      binding.imageView.visibility = View.GONE
                                      binding.txtNoRecord.visibility = View.VISIBLE
                                      binding.downloadBtn.visibility = View.GONE
                                  }
                              }
                               else {
                                    binding.imageView.visibility = View.VISIBLE
                                    binding.txtNoRecord.visibility = View.GONE
                                    binding.downloadBtn.visibility = View.VISIBLE
                                    base64String = response.data.data.pdfFile_base
                                    val outputPath =
                                        mContext!!.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath + "/sample.pdf"

                                    convertBase64ToPdfNew(base64String!!, outputPath)
                                }
                                // outputPath?.let { base64ToPdf(str_base64!!, it) }

                            }
                            else{
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,"not found",R.drawable.caution,resources.getColor(R.color.red))

                              /*  binding.imageView.visibility = View.GONE
                                binding.txtNoRecord.visibility = View.VISIBLE
                                binding.downloadBtn.visibility = View.GONE*/


                            }

                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        // binding.progressOverlay.visibility = View.GONE
                        response.message?.let { message ->


/*    if(message.contains(resources.getString(R.string.network_failure))){


    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,resources.getString(R.string.report_too_long),R.drawable.app_icon,colorPrimary,
        SnackBarUtil.OnClickListenerNew {

            (activity as MainActivity).onBackPressed()


        })
}*/

                           /* if(message.contains(resources.getString(R.string.network_failure))){

                                (activity as MainActivity).onBackPressed()

                            }*/




    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
        context,
        message,
        R.drawable.caution,
        resources.getColor(R.color.red)
    )

                           // SnackBarUtil.showSnackbar(context, message, false)

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                    // binding.progressOverlay.visibility=View.VISIBLE                    }
                }
            }
        }
    }

    private fun postPdfReportDetails() {


        if(isFromAdmin!!){
            val pdfReportRequest = PdfReportRequest(
                str_emp_id!!.toInt(),
                from_date.toString(),
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
               0,
                "",
                form_id!!.toInt(),
                to_date.toString(), 0,60,10

            )
            viewModel_pdfReport.getpdfReportResponseData(mContext!!, pdfReportRequest)
        }
        else {

            if (str_emp_id == null && manager_id == null) {
                val pdfReportRequest = PdfReportRequest(
                    UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
                    from_date.toString(),
                    UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                    0,
                    "",
                    form_id!!.toInt(),
                    to_date.toString(), 0,60,10

                )
                viewModel_pdfReport.getpdfReportResponseData(mContext!!, pdfReportRequest)
            } else if (str_emp_id != null && str_emp_id != "0" && manager_id != null && manager_id != "0") {


                val pdfReportRequest = PdfReportRequest(
                    str_emp_id!!.toInt(),
                    from_date.toString(),
                    UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                    UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
                    "",
                    form_id!!.toInt(),
                    to_date.toString(), 0,60,10

                )



                viewModel_pdfReport.getpdfReportResponseData(mContext!!, pdfReportRequest)
            } else {
                val pdfReportRequest = PdfReportRequest(
                    0,
                    from_date.toString(),
                    UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                    manager_id!!.toInt(),
                    "",
                    form_id!!.toInt(),
                    to_date.toString(), 0,60,10

                )
                viewModel_pdfReport.getpdfReportResponseData(mContext!!, pdfReportRequest)
            }

        }

    }




    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back ->{ (activity as MainActivity).onBackPressed() }


            R.id.download_btn -> {
                //  downloadPdfFile()
try {
    downloadBase64AsPdf(base64String!! , "/storage/emulated/0/Download/ExportedReport.pdf")
}catch (e:Exception){
    Log.e("TAG", "onClick: $e")
}


            }


        }
    }


    private fun downloadBase64AsPdf(base64String: String, destinationPath: String) {
        // Get the current date in "yyyyMMdd" format
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val fileName = "ExportedReport_$currentDate.pdf" // Dynamically generated file name

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
                val resolver = mContext!!.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                uri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        outputStream.write(decodedBytes)
                    }
                    val downloadPath = "${Environment.DIRECTORY_DOWNLOADS}/$fileName"
                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                        mContext, "File downloaded successfully to: $downloadPath",
                        R.drawable.app_icon, colorPrimary
                    )
                } ?: run {
                    throw Exception("Failed to create URI for the file.")
                }
            } else {
                // Handle for Android 9 and below
                val decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
                val destinationFile = File(destinationPath, fileName)
                val outputStream = FileOutputStream(destinationFile)
                outputStream.write(decodedBytes)
                outputStream.close()
                val downloadPath = destinationFile.absolutePath
                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                    mContext, "File downloaded successfully to: $downloadPath",
                    R.drawable.app_icon, colorPrimary
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                mContext, "Failed to download file: ${e.message}",
                R.drawable.app_icon, colorPrimary
            )
        }
    /*    try {

           // SnackBarUtil.showSnackbar(mContext,"Downloading the file. Please wait...",false)
            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,resources.getString(R.string.download_plz_wait_txt),R.drawable.app_icon,colorPrimary)

            // Decode the base64 string
            val decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val externalFilesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(externalFilesDir, "sample_file.pdf")

                // Create the file output stream
                val outputStream = FileOutputStream(file)

                // Write the decoded bytes to the file
                outputStream.write(decodedBytes)

                // Close the output stream
                outputStream.close()
                SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.download_successfully_txt),R.drawable.app_icon,colorPrimary)
            } else{
                // Create a file at the destination path with a .pdf extension
                val destinationFile = File(destinationPath)

                // Create the file output stream
                val outputStream = FileOutputStream(destinationFile)

                // Write the decoded bytes to the file
                outputStream.write(decodedBytes)

                // Close the output stream
                outputStream.close()
                SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.download_successfully_txt),R.drawable.app_icon,colorPrimary)
                // SnackBarUtil.showSnackbar(mContext,"PDF file downloaded successfully to: $destinationPath",false)

            }
        } catch (e: Exception) {
            e.printStackTrace()
            SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.download_error_for_pdf_txt),R.drawable.app_icon,colorPrimary)
           // SnackBarUtil.showSnackbar(mContext,"Failed to download PDF file. Error: ${e.message}",false)

        }*/
    }


    @Throws(IOException::class)
    private fun copyFile(sourceFile: File, destFile: File) {
        if (!destFile.exists()) {
            destFile.createNewFile()
        }

        val sourceChannel = FileInputStream(sourceFile).channel
        val destChannel = FileOutputStream(destFile).channel

        try {
            sourceChannel.transferTo(0, sourceChannel.size(), destChannel)
        } finally {
            sourceChannel.close()
            destChannel.close()
        }
    }
}