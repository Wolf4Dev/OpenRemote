package com.example.openremote.login

class LoginRepository {
    fun login(
        username: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFail: (String) -> Unit
    ) {
        if (username == "user1" && password == "Abc@@123") {
            onSuccess("Đăng nhập thành công")
        } else {
            onFail("Vui lòng kiểm tra lại username và password")
        }
    }
}