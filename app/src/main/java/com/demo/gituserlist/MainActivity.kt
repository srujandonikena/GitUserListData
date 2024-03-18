package com.demo.gituserlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.gituserlist.adapter.GitHubUserListAdapter
import com.demo.gituserlist.data.api.StatusCalled
import com.demo.gituserlist.databinding.ActivityMainBinding
import com.demo.gituserlist.model.GetUserListItem
import dagger.hilt.android.AndroidEntryPoint

private var userArrayList: List<GetUserListItem> = ArrayList()
private var favoriteArrayList: ArrayList<GetUserListItem> = ArrayList()
private lateinit var mAdapter: GitHubUserListAdapter
private lateinit var mBindingView : ActivityMainBinding
private lateinit var viewModel: MainActivityViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBindingView = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBindingView.root)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        setAdapter(mBindingView)


        viewModel.getUserList()
        viewModel.userInfo.observe(this) {
            when (it.status) {
                StatusCalled.SUCCESS -> {
                    mBindingView.progress.visibility = View.GONE
                    it.data?.let { getUserList -> renderList(getUserList) }
                    mBindingView.userListId.visibility = View.VISIBLE
                }
                StatusCalled.LOADING -> {
                    mBindingView.progress.visibility = View.VISIBLE
                    mBindingView.userListId.visibility = View.GONE
                }
                StatusCalled.ERROR -> {
                    //Handle Error
                    mBindingView.progress.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.filterUserList.observe(this) {
            if (it.isEmpty()) {
                mBindingView.noInternetTxt.text = getString(R.string.no_data_found)
                showMessageHideList(showMessage = View.VISIBLE, HideList = View.GONE)
                Toast.makeText(this, getString(R.string.no_data_found), Toast.LENGTH_LONG).show()
            } else {
                showMessageHideList(showMessage = View.GONE, HideList = View.VISIBLE)
                mAdapter.filterList(it)
            }
        }

        viewModel.isOnLine.observe(this) {
            if (it) {
                showMessageHideList(showMessage = View.GONE, HideList = View.VISIBLE)
            } else {
                showMessageHideList(showMessage = View.VISIBLE, HideList = View.GONE)
            }
        }
    }

    private fun showMessageHideList(showMessage: Int, HideList: Int) {
        mBindingView.noInternetTxt.visibility = showMessage
        mBindingView.userListId.visibility = HideList
    }

    private fun renderList(userList: List<GetUserListItem>?) {
        userList?.let {
            mAdapter.addItems(it)
            userArrayList = it
        }
    }

    private fun setAdapter(mBinding: ActivityMainBinding) {
        mAdapter = GitHubUserListAdapter()
        mBinding.userListId.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        mBinding.userListId.layoutManager = layoutManager
        mBinding.userListId.adapter = mAdapter
        mAdapter.addItems(ArrayList())

        mAdapter.listener = { item ->
            if (!favoriteArrayList.contains(item)) {
                if (item.isCheck) {
                    item.isCheck = false
                    favoriteArrayList.remove(item)
                } else {
                    favoriteArrayList.add(item)
                }
            }
            item.isCheck = !item.isCheck
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                viewModel.filter(msg, userArrayList)
                return false
            }
        })
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.actionFavorite) {
            mAdapter.addItems(favoriteArrayList)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}