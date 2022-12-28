package com.pioneer.ete.finalproject2022.ext

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty


fun Context.toastInfo(message: String) {
    Toasty.info(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastSuccess(message: String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastWarning(message: String) {
    Toasty.warning(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastError(message: String) {
    Toasty.error(this, message, Toast.LENGTH_SHORT).show()
}