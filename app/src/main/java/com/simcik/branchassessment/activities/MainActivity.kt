package com.simcik.branchassessment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.simcik.branchassessment.R

class MainActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private val titleObserver = Observer<String>{string->
        title = string
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedViewModel.title.observe(this, titleObserver)
    }


}