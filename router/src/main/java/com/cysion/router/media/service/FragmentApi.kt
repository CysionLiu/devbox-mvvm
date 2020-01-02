package com.cysion.router.media.service

import androidx.fragment.app.Fragment

interface FragmentApi {

    fun createNewsFragment(): Fragment
    fun createMusicFragment():Fragment

}