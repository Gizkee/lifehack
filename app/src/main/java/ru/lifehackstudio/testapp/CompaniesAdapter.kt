package ru.lifehackstudio.testapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lifehackstudio.testapp.model.Company

class CompaniesAdapter : RecyclerView.Adapter<CompaniesAdapter.CompaniesViewHolder>() {

    var companies: List<Company> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CompaniesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_company, parent, false)
        return CompaniesViewHolder(view)
    }

    override fun getItemCount() = companies.size

    override fun onBindViewHolder(holder: CompaniesViewHolder, position: Int) {
        holder.bindView(companies[position])
    }

    class CompaniesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val BASE_URL = "http://megakohz.bget.ru/test_task/"

        val image = itemView.findViewById<ImageView>(R.id.companyImageView)
        val name = itemView.findViewById<TextView>(R.id.companyNameTextView)

        fun bindView(company: Company) {
            name.text = company.name
            Glide.with(itemView.context)
                .load(BASE_URL + company.img)
                .into(image)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CompanyDetailsActivity::class.java)
                intent.putExtra("COMPANY_ID", company.id)
                itemView.context.startActivity(intent)
            }
        }
    }
}