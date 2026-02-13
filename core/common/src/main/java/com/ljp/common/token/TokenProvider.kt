package com.ljp.common.token

interface TokenProvider {
    suspend fun getToken(): String?
    suspend fun setAuthToken(token: String?)
    suspend fun clearToken() = setAuthToken(null)
}
