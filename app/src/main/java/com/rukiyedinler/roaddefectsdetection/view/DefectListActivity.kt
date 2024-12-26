package com.rukiyedinler.roaddefectsdetection.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.rukiyedinler.roaddefectsdetection.R
import com.rukiyedinler.roaddefectsdetection.data.ListAdapter
import com.rukiyedinler.roaddefectsdetection.data.Pothole
import com.rukiyedinler.roaddefectsdetection.data.User
import com.rukiyedinler.roaddefectsdetection.data.UserBody
import com.rukiyedinler.roaddefectsdetection.databinding.ActivityDefectListBinding
import com.rukiyedinler.roaddefectsdetection.repository.AuthRepository
import com.rukiyedinler.roaddefectsdetection.utils.APIService
import com.rukiyedinler.roaddefectsdetection.utils.AuthToken
import com.rukiyedinler.roaddefectsdetection.utils.RequestStatus
import com.rukiyedinler.roaddefectsdetection.view_model.DefectListActivityViewModel
import com.rukiyedinler.roaddefectsdetection.view_model.DefectListActivityViewModelFactory
import com.rukiyedinler.roaddefectsdetection.view_model.DefectReportActivityViewModel
import com.rukiyedinler.roaddefectsdetection.view_model.DefectReportViewModelFactory

class DefectListActivity : AppCompatActivity(){
    private lateinit var binding: ActivityDefectListBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var user: User
    private lateinit var mViewModel: DefectListActivityViewModel
    var defectList = ArrayList<Pothole>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDefectListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra("user") ?: return

        val userId = UserBody(
            user.id
        )
        mViewModel = ViewModelProvider(
            this, DefectListActivityViewModelFactory(
                AuthRepository(APIService.getService()),
                application
            )
        ).get(DefectListActivityViewModel::class.java)
        mViewModel.getDefectList(userId)
        mViewModel.getPotholeList().observe(this){status ->
            when (status) {
                is RequestStatus.Waiting -> {
                    Toast.makeText(this, "Yükleniyor...", Toast.LENGTH_SHORT).show()
                }
                is RequestStatus.Success -> {
                    defectList.addAll(status.data)
                    listAdapter = ListAdapter(this@DefectListActivity, defectList)
                    binding.listview.adapter = listAdapter
                    binding.listview.isClickable = true
                    binding.listview.onItemClickListener =
                        AdapterView.OnItemClickListener { adapterView, view, i, l ->
                            val intent = Intent(this@DefectListActivity, DefectDetailActivity::class.java)
                            intent.putExtra("confidence", defectList[i].confidence)
                            intent.putExtra("location", defectList[i].location)
                            intent.putExtra("imageBase64", defectList[i].imageBase64)
                            intent.putExtra("userName", defectList[i].userName)
                            intent.putExtra("createdAt", defectList[i].createdAt)
                            startActivity(intent)
                        }
                }
                is RequestStatus.Error -> {
                    Toast.makeText(this, "Hata: ${status.message}", Toast.LENGTH_LONG).show()
                }
            }

        }

    }
}