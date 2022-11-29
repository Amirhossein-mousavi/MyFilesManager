package com.example.myfilesmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myfilesmanager.databinding.FragmentFileBinding
import java.io.File
import java.security.Provider

class FragmentFile (private val path :String) :Fragment() , RecyclerAdapter.FileEvent , BottomSheetAdd.AddFile {
    lateinit var binding: FragmentFileBinding
    lateinit var myAdapter: RecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFileBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnChangeView.setOnClickListener {
            if (MainActivity.myViewType == 0) {
                MainActivity.myViewType = 1
                MainActivity.mySpanCount = 3
                myAdapter.myViewType(MainActivity.myViewType)
                binding.recyclerMain.layoutManager = GridLayoutManager(context , MainActivity.mySpanCount)
                binding.btnChangeView.setImageResource(R.drawable.ic_grid)
            } else if (MainActivity.myViewType == 1) {

                MainActivity.myViewType = 0
                MainActivity.mySpanCount = 1
                myAdapter.myViewType(MainActivity.myViewType)
                binding.recyclerMain.layoutManager = GridLayoutManager(context , MainActivity.mySpanCount)
                binding.btnChangeView.setImageResource(R.drawable.ic_list)

            }
        }
        if (MainActivity.myViewType == 0) {
            binding.btnChangeView.setImageResource(R.drawable.ic_list)
        } else if (MainActivity.myViewType == 1) {
            binding.btnChangeView.setImageResource(R.drawable.ic_grid)

        }
        val myFile = File(path)
        binding.txtName.text = myFile.name + " >"

        val data = arrayListOf<File>()
        data += myFile.listFiles()!!
        data.sortBy {!it.isDirectory }
        myAdapter = RecyclerAdapter(data,this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = GridLayoutManager(context , MainActivity.mySpanCount)
        myAdapter.myViewType(MainActivity.myViewType)

        if (myFile.isDirectory) {

            if (myFile.listFiles().isNotEmpty()) {
                binding.imgEmpty.visibility = View.GONE
                binding.recyclerMain.visibility = View.VISIBLE
            } else {
                binding.imgEmpty.visibility = View.VISIBLE
                binding.recyclerMain.visibility = View.GONE
            }
        }

        binding.btnAddFile.setOnClickListener {
            addFile()
        }
        binding.btnAddFolder.setOnClickListener {
            addFolder()
        }


    }

    private fun addFolder() {
        val bottomSheetAdd = BottomSheetAdd(directory = true ,path ,this )
        bottomSheetAdd.show(childFragmentManager ,null )
    }

    private fun addFile() {
        val bottomSheetAdd = BottomSheetAdd(directory = false ,path ,this )
        bottomSheetAdd.show(childFragmentManager ,null )

    }

    override fun isFileClicked(file: File, type: String) {

        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            val provider = FileProvider.getUriForFile(
                requireContext() ,
                requireActivity().packageName + ".provider" ,
                file)
            intent.setDataAndType(provider , type)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)


        } else {

            intent.setDataAndType(Uri.fromFile(file) , type)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)
        }


    }
    override fun isDirectoryClicked(path: String) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameMainContainer , FragmentFile(path))
        transaction.addToBackStack(null)
        transaction.commit()
    }
    override fun isLongClick(file: File, position: Int) {

        val dialog = SweetAlertDialog(context , SweetAlertDialog.WARNING_TYPE)
        dialog.contentText = "Are you sure about delete this file ?"
        dialog.setCancelButton("cancel") { dialog.dismiss() }
        dialog.setConfirmClickListener {
            if (file.deleteRecursively()){
                myAdapter.removeFile(file , position)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    override fun addFile(file: File) {
        myAdapter.addFile(file)
        binding.recyclerMain.scrollToPosition(0)
    }
    override fun fileCreated(created: Boolean) {
        if (created)
        {
            binding.imgEmpty.visibility = View.GONE
            binding.recyclerMain.visibility = View.VISIBLE

        }
    }



}