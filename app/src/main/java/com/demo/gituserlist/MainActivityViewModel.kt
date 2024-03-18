package com.demo.gituserlist

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.gituserlist.data.api.MainRepository
import com.demo.gituserlist.data.api.Resource
import com.demo.gituserlist.model.GetUserListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiHelper: MainRepository,
    private val application: MyApplication
) : ViewModel() {
    val userInfo = MutableLiveData<Resource<List<GetUserListItem>>>()
    val filterUserList = MutableLiveData<ArrayList<GetUserListItem>>()
    val isOnLine = MutableLiveData<Boolean>()

    fun getUserList() = viewModelScope.launch {
        if (NetworkConnection().isOnline(application.applicationContext)) {
            isOnLine.value = true
            userInfo.postValue(Resource.loading(null))
            apiHelper.getUserList()
                .catch { e ->
                    userInfo.postValue(Resource.error(e.toString(), null))
                }
                .collect {
                    userInfo.postValue(Resource.success(it))
                }
        } else {
            isOnLine.value = false
            Toast.makeText(application.applicationContext, application.applicationContext.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show()
        }
    }

    fun filter(text: String, userArrayList: List<GetUserListItem>) {
        val filteredlist: ArrayList<GetUserListItem> = ArrayList()
        for (item in userArrayList) {
            item.login?.lowercase(Locale.getDefault())?.let {
                if (it.contains(text.lowercase(Locale.getDefault()))) {
                    filteredlist.add(item)
                }
            }
        }
        filterUserList.value = filteredlist
    }

}