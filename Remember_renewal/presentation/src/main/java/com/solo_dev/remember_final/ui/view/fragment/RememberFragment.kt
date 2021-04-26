package com.solo_dev.remember_final.ui.view.fragment

import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ScrollView
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.databinding.FragmentRememberBinding


class RememberFragment : Fragment() {

    private lateinit var rememberBinding : FragmentRememberBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        rememberBinding = FragmentRememberBinding.inflate(layoutInflater)

        return rememberBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rememberBinding.scroll.fullScroll(ScrollView.FOCUS_UP)
        rememberBinding.scroll.post { ObjectAnimator.ofInt(rememberBinding.scroll, "scrollY", rememberBinding.scroll.bottom + 100).setDuration(80000).start() }
    }
}