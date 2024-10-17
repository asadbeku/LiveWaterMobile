package uz.prestige.livewater.dayver.users.view_model

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.prestige.livewater.dayver.users.types.DayverUserType
import uz.prestige.livewater.utils.UsersDayverPagingSource
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UsersRepository,
    private val usersDayverPagingSource: UsersDayverPagingSource
) : ViewModel() {

    private var _userData = MutableLiveData<DayverUserType>()
    val userData: LiveData<DayverUserType> get() = _userData

    fun fetchUsers() = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { usersDayverPagingSource }
    ).flow.cachedIn(viewModelScope)

    fun saveUserId(id: String) = repository.saveUserId(id)

    fun getUserId(position: Int): String = repository.getUserId(position)
    fun getUserDataById(userId: String) {
        viewModelScope.launch {
            repository.getUserDataById(userId)
                .catch {
                    Log.e("UserViewModel", "getUserDataById: ${it.message}")
                }
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    _userData.postValue(it)
                }
        }

    }

}