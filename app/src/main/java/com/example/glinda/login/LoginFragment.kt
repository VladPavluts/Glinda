package com.example.glinda.login

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.glinda.R
import com.example.glinda.databinding.FragmentLoginBinding
import com.example.glinda.util.FirestoreUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.firestore.core.FirestoreClient


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.registationEvent.observe(viewLifecycleOwner, Observer { event ->
            if (event) {
                showSignInOptions()
                viewModel.resetEvent()
            }
        })
        viewModel.logInEvent.observe(viewLifecycleOwner,Observer{event ->
            if (event){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChatFragment())
            }
            else{
                Toast.makeText(activity,"Wrong email or password",Toast.LENGTH_LONG).show()
            }
        })
        viewModel.emailIsValid.observe(viewLifecycleOwner, Observer { isValid ->
            if (isValid) {
                binding.emailLayout.isErrorEnabled = false
            } else {
                binding.emailLayout.error = "Wrong e-mail"
            }
        })

        viewModel.passwordIsValid.observe(viewLifecycleOwner, Observer { isValid ->
            if (isValid) {
                binding.passwordLayout.isErrorEnabled = false
            } else {
                binding.passwordLayout.error = "Wrong password"
            }
        })

        return binding.root
    }
    private val MY_LOGIN_REQ=1234
    val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build())

    fun showSignInOptions() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.logo)
                .setTheme(R.style.GlindaTheme)
                .build(),MY_LOGIN_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==MY_LOGIN_REQ){
            val response=IdpResponse.fromResultIntent(data)
            if(resultCode==RESULT_OK)
            {
                FirestoreUtil.initCurrentUserFirstTime {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChatFragment())
                }

            }
            else if(resultCode== RESULT_CANCELED){
                if(response == null) return

                when(response.error?.errorCode){
                    ErrorCodes.NO_NETWORK -> Toast.makeText(context,"No network",Toast.LENGTH_LONG).show()
                    ErrorCodes.UNKNOWN_ERROR -> Toast.makeText(context,"Unknown error",Toast.LENGTH_LONG).show()
                }
            }
        }
    }




}
