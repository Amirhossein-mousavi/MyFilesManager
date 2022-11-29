package com.example.myfilesmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myfilesmanager.databinding.DialogAddFileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File

class BottomSheetAdd(private val directory : Boolean , private val path : String , private val addFile: AddFile) :BottomSheetDialogFragment() {
    lateinit var binding: DialogAddFileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddFileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (directory) {
            binding.txtHeaderDialogAdd.text = "Create new Folder"

        } else {
            "Create new file"
        }
        binding.btnCancelDialogAdd.setOnClickListener {
            dismiss()
        }
        binding.btnConfirmDialogAdd.setOnClickListener {

            val fileName = binding.edtDialogAdd.text
            if (fileName == null) {
                Toast.makeText(context, "please enter the file's name", Toast.LENGTH_SHORT).show()
            } else {

                if (directory) {
                    val newFile = File(path + File.separator + fileName)
                    newFile.mkdir()
                    addFile.addFile(newFile)


                } else{

                    val newFile = File(path + File.separator + fileName)
                    newFile.createNewFile()
                    addFile.addFile(newFile)
                }
                addFile.fileCreated(true)
                dismiss()

            }

        }


    }
    interface AddFile {
        fun addFile(file :File)
        fun fileCreated (created : Boolean)
    }

}