package com.kosyakoff.distancetrackerapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kosyakoff.distancetrackerapp.R
import com.kosyakoff.distancetrackerapp.databinding.FragmentResultBinding

class ResultFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ResultFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    private fun initViews() {
        with(binding) {
            distanceValueTextView.text = String.format(
                getString(R.string.scr_result_distance_formatted_string),
                args.result.distance
            )
            timeValueTextView.text = args.result.time
            shareButton.setOnClickListener {
                shareResult()
            }
        }
    }

    private fun shareResult() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                String.format(
                    getString(R.string.scr_result_share_result),
                    args.result.distance,
                    args.result.time
                )
            )
        }
        startActivity(shareIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}