package com.sait.tawajudpremiumplusnewfeatured.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentViewMoreBinding


class ViewMoreFragment : BaseFragment(),OnClickListener {
    private var _binding: FragmentViewMoreBinding? = null
    private val binding get() = _binding!!
    var str_view_more : String?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentViewMoreBinding.inflate(inflater, container, false)
        val activity = this.activity as MainActivity?
       // setTopbar(activity)
        setClickListeners(activity)


        if(arguments!=null){
            if(requireArguments().getString("view_more")!=null){
                str_view_more = requireArguments().getString("view_more")
                binding.txtMoreDetails.text = HtmlCompat.fromHtml(str_view_more!!, HtmlCompat.FROM_HTML_MODE_LEGACY)

            }


        }

        return binding.root

    }




    private fun setClickListeners(activity: MainActivity?) {

        activity?.binding!!.layout.imgBack.setOnClickListener(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).show_BackButton_title(resources.getString(R.string.view_more))
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).hide_app_title()


    }

    override fun onPause() {
        super.onPause()

    }

    override fun onClick(p0: View?) {

        when(p0!!.id){

R.id.img_back -> (activity as MainActivity).onBackPressed()

        }
    }



}