package com.example.moeen.ui.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.moeen.R
import com.example.moeen.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {

    private var image : Int = 0
    private var title : Int = 0
    private lateinit var binding: FragmentOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image = requireArguments().getInt(ARG_PARAM1)
            title = requireArguments().getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_on_boarding, container, false)

        binding.clParent.setBackgroundResource(image)
        binding.tvOnboardingTitle.setText(title)

        return binding.root
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance(image: Int, title : Int) : OnBoardingFragment{
            val fragment = OnBoardingFragment()
            val args = Bundle()

            args.putInt(ARG_PARAM1, image)
            args.putInt(ARG_PARAM2, title)
            fragment.arguments = args
            return fragment
        }
    }
}