package pt.amn.goalsmaker

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import android.graphics.BitmapFactory


class Utils(val context : Context) {

    fun saveImageToInternalStorage(selectedImage : Uri?, fileName : String) {

        // Get the bitmap from drawable object
        val bitmap = uriToBitmap(selectedImage!!)

        // Get the context wrapper instance
        val wrapper = ContextWrapper(context)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var imageFile = wrapper.getDir("images", Context.MODE_PRIVATE)

        // Create a file to save the image
        //val image : File = File(selectedImage?.path)
        imageFile = File(imageFile, "big_goal_${fileName}.jpg")

        try {
            // Get the file output stream
            val stream : OutputStream = FileOutputStream(imageFile)

            // Compress bitmap

            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun getImagePathFromInternalStorage(fileName : String) : File  {

        // Get the context wrapper instance
        val wrapper = ContextWrapper(context)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var imageFile = wrapper.getDir("images", Context.MODE_PRIVATE)

        imageFile = File(imageFile, "big_goal_${fileName}.jpg")

        return imageFile

    }

    private fun uriToBitmap(selectedFileUri: Uri) : Bitmap? {
        try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor = parcelFileDescriptor?.getFileDescriptor()
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()

            return image

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null

    }

}