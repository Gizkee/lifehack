package ru.lifehackstudio.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.lifehackstudio.testapp.model.Company

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CompaniesViewModel
    private lateinit var adapter: CompaniesAdapter

    private lateinit var progressBar: ProgressBar
    private lateinit var companiesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(CompaniesViewModel::class.java)

        companiesRecyclerView = findViewById(R.id.companiesRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        subscribeUi()
        setupAdapter()
    }

    private fun subscribeUi() {
        viewModel.companies.observe(this, Observer { showCompanies(it) })
        viewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading)
                showLoading()
            else
                hideLoading()
        })
        viewModel.message.observe(this, Observer { showErrorMessage(it) })
    }

    private fun setupAdapter() {
        adapter = CompaniesAdapter()
        companiesRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        companiesRecyclerView.adapter = this.adapter
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showCompanies(companies: List<Company>) {
        adapter.companies = companies
        adapter.notifyDataSetChanged()
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
