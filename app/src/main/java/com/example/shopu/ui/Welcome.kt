package com.example.shopu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shopu.R


class Welcome : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bienvenida, container, false)

        val button: Button = root!!.findViewById(R.id.boton_bienvenida)
        button.setOnClickListener { handleLoginButtonClick(root) }
        return root
    }

    private fun handleLoginButtonClick(root: View?) {
        val navController = Navigation.findNavController(root!!)
        navController.navigate(R.id.action_bienvenida_to_login)
    }
}