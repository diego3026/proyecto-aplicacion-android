package com.example.shopu.core.ex

import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox


fun CheckBox.onCheckedChanged(listener: (Boolean) -> Unit) {
    this.setOnCheckedChangeListener { _, isChecked ->
        listener(isChecked)
    }
}