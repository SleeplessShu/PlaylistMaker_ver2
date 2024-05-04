package com.practicum.playlistmaker_ver2

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class SearchActivity : BaseActivity() {
    companion object {
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
    }

    private lateinit var editText: EditText
    private lateinit var clearButton: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupStatusBar(androidx.appcompat.R.attr.colorPrimary)

        val onPressBackToMain = findViewById<LinearLayout>(R.id.buttonBackToMain)
        onPressBackToMain.setOnClickListener {
            finish()
        }
        editText = findViewById(R.id.text_SearchInput)
        clearButton = findViewById(R.id.clear_text)

        savedInstanceState?.getString(SEARCH_TEXT_KEY)?.let {
            editText.setText(it)
            if (it.isNotEmpty()) clearButton.visibility = View.VISIBLE
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                clearButton.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })

        clearButton.setOnClickListener {
            editText.text.clear()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.searchResult)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        val trackAdapter = TrackAdapter(trackList)
        recyclerView.adapter = trackAdapter
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, editText.text.toString())
    }
}