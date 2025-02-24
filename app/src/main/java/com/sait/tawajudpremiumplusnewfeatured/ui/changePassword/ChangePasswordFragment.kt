package com.sait.tawajudpremiumplusnewfeatured.ui.changePassword


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentChangePasswordBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.viewmodels.ChangePasswordViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.Keyboard_Op
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class ChangePasswordFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentChangePasswordBinding
    private var mContext: Context? = null
    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        mContext = inflater.context

        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)

        val activity = this.activity as MainActivity?


        if(UserShardPrefrences.getLanguage(mContext)!!.equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setClickListeners(activity)

        return binding.root
    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.txtSubmit.setOnClickListener(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onResume() {
        super.onResume()


        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.change_pswrd_txt))

    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()

            R.id.txt_submit -> checkValidations()

        }
    }

    private fun checkValidations() {
        binding.edtOldPassword.setError(null)
        binding.edtNewPassword.setError(null)
        binding.edtConfirmPassword.setError(null)
        val old_password: String = binding.edtOldPassword.getText().toString()
        val new_password: String = binding.edtNewPassword.getText().toString()
        val confirm_password: String = binding.edtConfirmPassword.getText().toString()

        Log.e("password_old", old_password)
        Log.e("password_new", new_password)
        Log.e("password_check", UserShardPrefrences.getPassword_new(mContext!!).toString())

        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(old_password)) {
            binding.edtOldPassword.setError(getString(R.string.error_field_required))
            focusView = binding.edtOldPassword
            cancel = true
        }


        else if(old_password != UserShardPrefrences.getPassword_new(mContext!!)){
            binding.edtOldPassword.setError(getString(R.string.pawrd_icorrect))
            focusView = binding.edtOldPassword
            cancel = true

        }

       else if (TextUtils.isEmpty(new_password)) {
            binding.edtNewPassword.setError(getString(R.string.error_field_required))
            focusView = binding.edtNewPassword
            cancel = true
        }

        else if(new_password == UserShardPrefrences.getPassword_new(mContext!!)){
            binding.edtNewPassword.setError(getString(R.string.old_new_pswd))
            focusView = binding.edtNewPassword
            cancel = true
        }

        else if (TextUtils.isEmpty(confirm_password)) {
            binding.edtConfirmPassword.setError(getString(R.string.error_field_required))
            focusView = binding.edtConfirmPassword
            cancel = true
        }

        else if (!isPasswordValid(confirm_password)) {
            binding.edtConfirmPassword.setError(getString(R.string.error_invalid_password))
            focusView = binding.edtConfirmPassword
            cancel = true
        }
        else if  (!new_password.equals(confirm_password)) {
            binding.edtConfirmPassword.setError(getString(R.string.new_cnfrm_pswrd_error))
            focusView = binding.edtConfirmPassword
            cancel = true

        }




        if (cancel) {
            focusView!!.requestFocus()
        } else {
            try {
                Keyboard_Op.hide(mContext, binding.edtOldPassword)
                callChangePasswordAPI()



            } catch (e: Exception) {
                e.printStackTrace()
            }
        }






    }

    private fun callChangePasswordAPI() {
        addObserver()
        postChangePasswordDetails()
    }



    private fun addObserver() {
        viewModel.changePasswordResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()
                        // binding.progressOverlay.visibility = View.GONE

                        response.data?.let { changepasswordResponse ->
                            val changepasswordData = changepasswordResponse
                            if (changepasswordData != null) {




                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(context, changepasswordData.data.msg,R.drawable.app_icon,colorPrimary,
                                    SnackBarUtil.OnClickListenerNew {

                                     //   (activity as MainActivity).onBackPressed()
                                     //   UserShardPrefrences.setPassword_new(mContext!!,binding.edtNewPassword.text.toString())
                                        UserShardPrefrences.setuserLogin(context, false)
                                        UserShardPrefrences.clearUserInfo(context)
                                        startActivity(
                                            Intent(
                                                context,
                                                MainActivity::class.java
                                            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        )
                                        requireActivity().finish()

                                    })


                               // restartActivity()
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
    private fun postChangePasswordDetails() {
        viewModel.getchangePasswordData(mContext!!, UserShardPrefrences.getUserInfo(mContext).id,UserShardPrefrences.getUsername_new(mContext)!!,
            binding.edtNewPassword.text.toString(),binding.edtOldPassword.text.toString(),
            UserShardPrefrences.getLanguage(mContext)!!.toInt())

    }
    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length >= 3
    }
}
