package me.jhwang.reactivespringcloud

data class Customer(var id: Int, var name: String, var telephone: Telephone?) {
    data class Telephone (var countryCode: String, var phoneNumber: String)
}