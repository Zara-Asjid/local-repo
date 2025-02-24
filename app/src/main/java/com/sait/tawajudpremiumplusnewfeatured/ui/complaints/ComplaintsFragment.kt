package com.sait.tawajudpremiumplusnewfeatured.ui.complaints

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentContactBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.complaints.viewmodels.ComplaintsViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences


class ComplaintsFragment : BaseFragment(), OnClickListener {
    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!
    var handler: Handler = Handler()

    private lateinit var viewModel: ComplaintsViewModel
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactBinding.inflate(inflater, container, false)
        mContext = inflater.context

        viewModel = ViewModelProvider(this).get(ComplaintsViewModel::class.java)


        val activity = this.activity as MainActivity?

        setClickListeners(activity)
        callCompanyInfoAPI()

        setData()
        return binding.root
    }

    private fun setData() {

        binding.lOrg.visibility = View.VISIBLE
        binding.copyRightTxt.visibility=View.GONE
        binding.txtOrgPhone.text = UserShardPrefrences.getMobile(requireActivity())
        binding.txtOrgEmail.text = UserShardPrefrences.getEmail(requireActivity())


    }

    private fun callCompanyInfoAPI() {
        addObserver()
        getContactDetails()
    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.phoneLlOrg.setOnClickListener(this)
        binding.mailLlOrg.setOnClickListener(this)
        binding.phoneNoLayout.setOnClickListener(this)
        binding.emailLayout.setOnClickListener(this)
    }


    override fun onResume() {
        super.onResume()


        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.complains_txt))


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
        callCompanyInfoAPI()

    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()
            R.id.phoneNo_layout -> {
                if (ContextCompat.checkSelfPermission(
                        mContext!!,
                        android.Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+962-6-5338565"))
                    startActivity(intent)
                }
            }
            R.id.email_layout -> {
                val to = "info@aman-me.com"
                val subject = "Support Help "
                //val body = ": "

                val uriBuilder = StringBuilder("mailto:" + Uri.encode(to))
                uriBuilder.append("?subject=" + Uri.encode(subject))
                //uriBuilder.append("&body=" + Uri.encode(body))
                val uriString = uriBuilder.toString()

                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(uriString))

                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    // Log.e("LOG_TAG", e.localizedMessage)

                    // If there is no email client application, than show error message for the user.
                    if (e is ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "No application can handle this request. Please install an email client app.",
                            Toast.LENGTH_LONG).show()

                    }


                }
            }
            R.id.phone_ll_org ->{
                if (ContextCompat.checkSelfPermission(
                        mContext!!,
                        android.Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserShardPrefrences.getMobile(requireActivity())))
                    startActivity(intent)
                }
            }
            R.id.mail_ll_org ->{
                val to = UserShardPrefrences.getEmail(requireActivity())
                val subject = "Support Help "
                //val body = ": "

                val uriBuilder = StringBuilder("mailto:" + Uri.encode(to))
                uriBuilder.append("?subject=" + Uri.encode(subject))
                //uriBuilder.append("&body=" + Uri.encode(body))
                val uriString = uriBuilder.toString()

                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(uriString))

                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    // Log.e("LOG_TAG", e.localizedMessage)

                    // If there is no email client application, than show error message for the user.
                    if (e is ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "No application can handle this request. Please install an email client app.",
                            Toast.LENGTH_LONG).show()

                    }


                }
            }
           /*     val emailIntent =
                    Intent(Intent.ACTION_VIEW, Uri.fromParts("mailto", "info@aman-me.com", null))
                emailIntent.data = Uri.parse("http://com.android.email.provider/message/1")
                emailIntent.type = "text/plain"
                *//*emailIntent.setType("message/rfc822");
                emailIntent.setData(Uri.parse("https://www.gmail.com"))
*//*
               *//* emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("info@aman-me.com"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject") // Set your subject
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body") // Set your email body
                *//*
                startActivity(Intent.createChooser(emailIntent, "  "))
*/

        }
    }


    private fun addObserver() {
        viewModel.complaintsResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()
                        // binding.progressOverlay.visibility = View.GONE

                        response.data?.let { contactResponse ->
                            val contactData = contactResponse
                            if (contactData != null) {
                               binding.companyNameTxtComplaints.text=UserShardPrefrences.getName(mContext!!).toString()
                                if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                    if (contactData.data.companyName!=null) {
                                        binding.txtCompanyName.text =
                                            contactData.data.companyName.toString()
                                    }
                                } else {
                                    binding.txtOrgEmail.textDirection = View.TEXT_DIRECTION_RTL
                                    if (contactData.data.companyArabicName!=null) {
                                        binding.txtCompanyName.text =
                                            contactData.data.companyArabicName.toString()
                                    }
                                    binding.txtCompanyName.textDirection = View.TEXT_DIRECTION_RTL

                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        // binding.progressOverlay.visibility = View.GONE
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))

                          //  SnackBarUtil.showSnackbar(context, message, false)

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

    private fun getContactDetails() {
        viewModel.getContactData(
            mContext!!,
            UserShardPrefrences.getUserInfo(context).id.toString(),
            UserShardPrefrences.getLanguage(mContext)!!
        )

    }

}