package com.example.tungtung.presentation.camera.model

import androidx.annotation.AnimRes

/**
 * Created by josephmagara on 9/10/20.
 */

data class ViewOpacityTransition(@AnimRes val transitionId: Int, val endOpacity: Int){

    companion object {
        const val FULL_OPACITY = 255
        const val HALF_OPACITY = 128
    }
}