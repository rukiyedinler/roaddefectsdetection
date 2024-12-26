package com.rukiyedinler.roaddefectsdetection.data
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.rukiyedinler.roaddefectsdetection.R

class ListAdapter(context: Context, dataArrayList: ArrayList<Pothole>?) :
    ArrayAdapter<Pothole>(context, R.layout.activity_defect_item, dataArrayList!!) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        var view = view
        val listData = getItem(position)

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_defect_item, parent, false)
        }

        val imageBytes = Base64.decode(listData!!.imageBase64, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val listImage = view!!.findViewById<ImageView>(R.id.listImage)
        listImage.setImageBitmap(decodedImage)

        val listConfidence = view!!.findViewById<TextView>(R.id.listConfidence)
        val listLocation = view.findViewById<TextView>(R.id.listLocation)


        listConfidence.text = "%" + String.format("%.2f",  listData!!.confidence).split(".")[1]
        listLocation.text = listData!!.location

        return view
    }
}