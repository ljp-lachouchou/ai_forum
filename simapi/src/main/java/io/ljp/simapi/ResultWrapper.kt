package io.ljp.simapi


import com.ljp.common.log.core.printer.w
import com.ljp.common.log.core.priority.LogcatPriorityInstance
import io.ktor.client.plugins.ResponseException
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(val code:Int,val msg:String,val data:T?) {
    companion object {
        const val SUCCESS_CODE = 200
    }
}
sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(
        // 区分业务错误码和HTTP状态码
        val businessCode: Int? = null,  // 后端返回的code
        val httpCode: Int? = null,      // HTTP状态码（如404/500）
        val message: String? = null,     // 后端返回的错误信息
        val cause: Throwable? = null    // 原始异常
    ) : ResultWrapper<Nothing>()
}
/**
如果你要把 Lambda 丢进“别人”（如 Runnable、suspend 闭包、匿名对象）的怀抱里运行，就给它打上 crossinline 标签。
当你在参数前加了 crossinline：

- 允许间接调用：你可以在匿名内部类或协程作用域里调用这个 Lambda。

- 禁止非局部返回：在调用这个函数的地方，传进去的 Lambda 里面不能直接写 return。只能写 return@label（局部返回）。
 */
suspend inline fun <reified T> safeApiCall(crossinline call:suspend () -> ApiResponse<T>): ResultWrapper<T> {
    return try {
        val response = call()
        if (response.code == ApiResponse.SUCCESS_CODE) {
            response.data?.let {
                ResultWrapper.Success(it)
            } ?:  ResultWrapper.Error(businessCode = response.code,
                message = response.msg)
        }else {
            ResultWrapper.Error(businessCode = response.code,
                message = response.msg)
        }
    }catch (e: ResponseException) {
        ResultWrapper.Error(httpCode = e.response.status.value,
            message = e.message, cause = e)
    }catch (e: Exception) {
        ResultWrapper.Error(message = e.message ?: "未知异常", cause = e)
    }.let { it->
        it.w(LogcatPriorityInstance, "[APiClient] $it")
        it
    }

}