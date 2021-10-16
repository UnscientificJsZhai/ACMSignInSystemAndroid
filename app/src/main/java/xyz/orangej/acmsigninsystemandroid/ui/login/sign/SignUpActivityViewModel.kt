package xyz.orangej.acmsigninsystemandroid.ui.login.sign

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpActivityViewModel : ViewModel() {

    var userName = MutableLiveData("")
    var password = MutableLiveData("")
    var passwordConfirm = MutableLiveData("")
    var name = MutableLiveData("")
    var department = MutableLiveData("")
    var admin = MutableLiveData(false)
    var adminVerify = MutableLiveData("")
    var email = MutableLiveData("")
    var emailVerify = MutableLiveData("")
}