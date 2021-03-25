package com.droid.offlineStorage.ui.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.droid.offlineStorage.R
import com.droid.offlineStorage.databinding.ActivityEditBinding
import com.droid.offlineStorage.local.ExampleDatabase
import com.droid.offlineStorage.ui.main.MainRepository
import com.droid.offlineStorage.utils.Constants

/**
 * Created by Akhtar
 */
class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var editViewModel: EditViewModel

    companion object {
        fun start(mActivity: Activity, id: Int, isEditable: Boolean = true) {
            val intent = Intent(mActivity, EditActivity::class.java)
            intent.putExtra(Constants.INTENT_ID, id)
            mActivity.startActivity(intent)
            mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_edit)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)

        supportActionBar?.let {
            it.apply {
                setDisplayHomeAsUpEnabled(true)
                title = ""
            }
        }


        val dao = ExampleDatabase.getInstance(application).exampleDao
        val mainRepository = MainRepository(dao)
        val factory = EditViewModelFactory(mainRepository)
        editViewModel = ViewModelProvider(this, factory).get(EditViewModel::class.java)

        binding.editViewModel = editViewModel
        binding.lifecycleOwner = this


        intent.extras?.let { it ->
            val id = it.getInt(Constants.INTENT_ID, 0)
            editViewModel.getById(id)
        }


        editViewModel.isUpdated.observe(this, {
            if (it) {
                hideKeyboard()
                finish()
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun Context.appToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}