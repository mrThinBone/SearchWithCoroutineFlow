package com.example.searchwithcoroutineflow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_user.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var mAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        edtSearch.addTextChangedListener {
            it?.let { editable ->
                viewModel.liveSearchProducer.value = editable.toString()
            }
        }

        /*
        WILL NOT DEAL WITH OWNER LIFECYCLE
        lifecycleScope.launch {
            viewModel.liveSearchResult.collect{
                Log.d("vinhtv", "receive $it on ${Thread.currentThread().name}")
                mAdapter.refresh(it)
            }
        }*/
        viewModel.liveSearchResult.observe(this, Observer {
            Log.d("vinhtv", "receive $it on ${Thread.currentThread().name}")
            mAdapter.refresh(it)
        })

        // trigger first search
        viewModel.liveSearchProducer.value = ""
    }

    private fun setupRecyclerView() {
        mAdapter = UserAdapter()
        rclList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rclList.adapter = mAdapter
    }
}

class UserAdapter : RecyclerView.Adapter<UserViewHolder>() {

    val data = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(parent)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun refresh(payload: List<String>) {
        data.clear()
        data.addAll(payload)
        notifyDataSetChanged()
    }
}


class UserViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

    fun bind(userName: String) {
        txtUserName.text = userName
    }

}
