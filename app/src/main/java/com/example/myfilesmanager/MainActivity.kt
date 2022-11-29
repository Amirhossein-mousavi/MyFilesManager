package com.example.myfilesmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfilesmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    companion object {
        var myViewType = 0
        var mySpanCount = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val file = getExternalFilesDir(null)!!
        val path = file.path

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameMainContainer , FragmentFile(path))
        transaction.commit()

    }
}