package ru.lifehackstudio.testapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.CollapsingToolbarLayout


class CompanyDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: CompanyDetailsViewModel

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var imageView: ImageView
    private lateinit var content: NestedScrollView
    private lateinit var descriptionTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var webSiteTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_details)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar)
        imageView = findViewById(R.id.companyDetailsImageView)
        content = findViewById(R.id.content)
        descriptionTextView = findViewById(R.id.companyDescriptionTextView)
        phoneTextView = findViewById(R.id.companyPhoneTextView)
        webSiteTextView = findViewById(R.id.companyWebSiteTextView)
        progressBar = findViewById(R.id.progressBarDetails)

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        val bundle = intent.extras
        if (bundle != null) {
            val id = bundle.getInt("COMPANY_ID")
            viewModel = ViewModelProvider(this, CompanyDetailsViewModelFactory(id))
                .get(CompanyDetailsViewModel::class.java)
        }

        subscribeUi()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun subscribeUi() {
        viewModel.companyDetails.observe(this, Observer { company ->
            showTitle(company.name)
            showImage(company.img)
            showDescription(company.description)
            showPhone(company.phone)
            showWebSite(company.www)
            setupMap(company.lat.toDouble(), company.lon.toDouble())
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            showErrorMessage(message)
        })
    }

    private fun showTitle(title: String) {
        collapsingToolbarLayout.title = title
    }

    private fun showImage(imageUrl: String) {
        Glide.with(this).load(getString(R.string.base_url) + imageUrl).into(imageView)
    }

    private fun showDescription(description: String) {
        if (description.isNotEmpty()) {
            descriptionTextView.text = description
        } else {
            descriptionTextView.text = getString(R.string.descriptionIsEmpty)
        }
    }

    private fun showPhone(phone: String) {
        if (phone.isNotEmpty()) {
            phoneTextView.text = phone
            phoneTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel: $phone")
                startActivity(intent)
            }
        } else {
            phoneTextView.text = getString(R.string.phoneIsEmpty)
        }
    }

    private fun showWebSite(url: String) {
        if (url.isNotEmpty()) {
            webSiteTextView.text = url
            webSiteTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("http://$url")
                startActivity(intent)
            }
        } else {
            webSiteTextView.text = getString(R.string.webSiteIsEmpty)
        }
    }

    private fun setupMap(lat: Double, lon: Double) {
        if (lat == 0.0 || lon == 0.0) {
            supportFragmentManager.beginTransaction().hide(mapFragment).commit()
        }

        mapFragment.getMapAsync { map ->
            val latLng = LatLng(lat, lon)
            map.addMarker(MarkerOptions().position(latLng))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
    }

    private fun showLoading() {
        content.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        content.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun showErrorMessage(message: String) {
        collapsingToolbarLayout.title = ""
        content.visibility = View.INVISIBLE
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
