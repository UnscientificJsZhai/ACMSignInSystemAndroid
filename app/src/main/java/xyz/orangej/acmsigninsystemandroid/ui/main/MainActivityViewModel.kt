package xyz.orangej.acmsigninsystemandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.orangej.acmsigninsystemandroid.data.user.CurrentUser
import xyz.orangej.acmsigninsystemandroid.data.user.TrainingRecord
import xyz.orangej.acmsigninsystemandroid.data.user.database.UserDao

class MainActivityViewModel(
    var currentUser: LiveData<CurrentUser>,
    var trainingRecords: LiveData<List<TrainingRecord>>
) : ViewModel() {

    class Factory(private val dao: UserDao, private val sessionHash: Int) :
        ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityViewModel(
                dao.getCurrentUser(sessionHash),
                dao.getTrainingRecords()
            ) as T
        }
    }
}