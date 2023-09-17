package com.proalekse1.callboard.frag

import android.graphics.Bitmap

interface FragmentCloseInterface { //интерфейс для фрагмента ImageListFrag
    fun onFragClose(list : ArrayList<Bitmap>)
}