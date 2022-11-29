package com.example.myfilesmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.net.URLConnection

class RecyclerAdapter(private val data : ArrayList<File> , private val fileEvent: FileEvent) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    var myViewType = 0

    inner class RecyclerViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

        val txt = itemView.findViewById<TextView>(R.id.txtItemFile)
        val img = itemView.findViewById<ImageView>(R.id.imgItemFile)

        fun bindFile(file: File) {
            var type = ""
            txt.text = file.name
            if (file.isDirectory) {
                img.setImageResource(R.drawable.ic_folder)
            }else {
                when {
                    isImage(file.path) -> {
                        img.setImageResource(R.drawable.ic_image)
                        type = "image/*"
                    }
                    isVideo(file.path) -> {
                        img.setImageResource(R.drawable.ic_video)
                        type = "video/*"
                    }
                    isZip(file.path) -> {
                        img.setImageResource(R.drawable.ic_zip)
                        type = "application/zip"
                    }
                    else -> {
                        img.setImageResource(R.drawable.ic_file)
                        type = "text/plain"
                    }
                }
            }

            itemView.setOnClickListener {
                if (file.isDirectory)
                {
                    fileEvent.isDirectoryClicked(file.path)
                } else {
                    fileEvent.isFileClicked(file , type )
                }
            }
            itemView.setOnLongClickListener {
                fileEvent.isLongClick(file , adapterPosition)
                true
            }





        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view : View
        if (viewType == 0 ) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_linear , parent , false)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_grid , parent , false)
        }
        return RecyclerViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindFile(data[position])
    }
    override fun getItemViewType(position: Int): Int {
        return myViewType
    }
    override fun getItemCount(): Int = data.size

    private fun isImage (path : String) : Boolean {
        val mimeType : String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("image")
    }
    private fun isVideo (path : String) : Boolean {
        val mimeType : String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("video")
    }
    private fun isZip (path: String) :Boolean {
        return path.contains(".zip") || path.contains(".rar")
    }

    fun addFile (file :File){
        data.add(0 , file)
        notifyItemInserted(0)
    }
    fun removeFile (file: File , position: Int) {
        data.remove(file)
        notifyItemRemoved(position)

    }
    fun myViewType (type : Int) {
        myViewType = type
        notifyDataSetChanged()
    }

    interface FileEvent {
        fun isFileClicked (file: File , type :String)
        fun isDirectoryClicked (path: String)
        fun isLongClick (file: File , position: Int)

    }


}