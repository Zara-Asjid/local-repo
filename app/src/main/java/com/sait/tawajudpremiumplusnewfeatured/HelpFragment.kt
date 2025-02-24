package com.sait.tawajudpremiumplusnewfeatured

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentHelpBinding
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class HelpFragment : BaseFragment(), View.OnClickListener {
    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null

private var uploadFileStr:String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        mContext = inflater.context

        val activity = this.activity as MainActivity?

        setClickListeners(activity)
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        initializeWebView()
        return binding.root
    }

    private fun initializeWebView() {



   /*     if (arguments != null) {

            (activity as MainActivity).show_app_title("Uploaded Attachment")

            uploadFileStr = arguments?.getString("uploadFile")
           // binding.webViewFragmentHelp.loadUrl(uploadFileStr!!)

            binding.webViewFragmentHelp.loadUrl("https://docs.google.com/gview?embedded=true&url=$uploadFileStr")
        }

else{*/


            if (UserShardPrefrences.getLanguage(mContext)=="0"){
                binding.webViewFragmentHelp.loadUrl("file:///android_asset/html/help.html")
            }else
                if (UserShardPrefrences.getLanguage(mContext)=="1")
                {
                    binding.webViewFragmentHelp.loadUrl("file:///android_asset/html/help_ar.html")
                }
     //   }


        // this will enable the javascript.
        binding.webViewFragmentHelp.settings.javaScriptEnabled = true

        binding.webViewFragmentHelp.webViewClient = WebViewClient()
    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)

    }


    override fun onResume() {
        super.onResume()

        (activity as MainActivity).hideProgressBar()
        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.title_help))

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> (activity as MainActivity).onBackPressed()
        }
    }


}