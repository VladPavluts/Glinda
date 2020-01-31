package com.example.glinda.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.glinda.R
import com.example.glinda.databinding.FragmentLoginBinding


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
                Toast.makeText(activity,"Registration",Toast.LENGTH_LONG).show()
                viewModel.resetEvent()
            }
        })
        viewModel.logInEvent.observe(viewLifecycleOwner,Observer{event ->
            if (event){
                Toast.makeText(activity,"Login",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(activity,"Error",Toast.LENGTH_LONG).show()
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


}
