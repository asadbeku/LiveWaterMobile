package uz.prestige.livewater.level.users.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.level.users.types.UserType
import uz.prestige.livewater.utils.UsersPagingSource
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UsersRepository,
    private val pagingSource: UsersPagingSource
) : ViewModel() {

    private var _userInfo = MutableLiveData<UserType>()
    val userInfo: LiveData<UserType> get() = _userInfo

    fun fetchUsersData() = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { pagingSource }
    ).flow.cachedIn(viewModelScope)

    fun getUserInfoById(id: String) {
        viewModelScope.launch {
            repository.getUserInfoById(id)
                .catch {
                    Log.e("ViewModel", it.message.toString())
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _userInfo.postValue(it)
                }
        }
    }

    fun saveUserId(id: String) = repository.saveUserId(id)

    fun getUserIdByPosition(position: Int): String = repository.getUserIdByPosition(position)

}