package ai.ljp.network.di

import android.content.Context
import androidx.tracing.trace
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import com.russhwolf.settings.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {
    @Provides
    @Singleton
    fun okHttpCallFactory() : Call.Factory =trace("NiaOkHttpClient") {
        OkHttpClient.Builder()
            .build()
    }
    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactory: dagger.Lazy<Call.Factory>,
        @ApplicationContext application: Context,
    ) : ImageLoader = trace("NiaImageLoader") {
        ImageLoader.Builder(application)
            .callFactory { okHttpCallFactory.get() }
            .components { add(SvgDecoder.Factory()) }
            // 防止图片版本出现问题
            .respectCacheHeaders(false)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }



}