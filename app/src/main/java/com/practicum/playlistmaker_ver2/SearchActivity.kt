package com.practicum.playlistmaker_ver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val onPressBackToMain = findViewById<ImageView>(R.id.buttonBackToMain)
        onPressBackToMain.setOnClickListener {
            finish()
        }

        searchEditText = findViewById<TextInputEditText>(R.id.search_edit_text)
        if (savedInstanceState != null) {
            searchEditText.setText(savedInstanceState.getString("SEARCH_QUERY"))
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_QUERY", searchEditText.text.toString())
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}