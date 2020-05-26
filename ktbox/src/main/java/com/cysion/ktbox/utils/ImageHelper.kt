package com.cysion.ktbox.utils

import com.lzy.imagepicker.ImagePicker

fun useSingleMode() {
    val imagePicker = ImagePicker.getInstance()
    imagePicker.imageLoader = MyImageLoader() //设置图片加载器
    imagePicker.isShowCamera = false
    imagePicker.isCrop = false
    imagePicker.isMultiMode = false
    imagePicker.selectLimit = 1 //选中数量限制
}