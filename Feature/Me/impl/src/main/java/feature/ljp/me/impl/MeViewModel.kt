package feature.ljp.me.impl

import ai.ljp.data.repository.BookmarkRepository
import ai.ljp.data.repository.InteractionWordRepository
import ai.ljp.data.repository.LikeRepository
import ai.ljp.data.repository.ProfileRepository
import ai.ljp.data.repository.UserDataRepository
import ai.ljp.data.repository.WordRepository
import ai.ljp.ui.ProfileUiState
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ljp.common.result.Result
import com.ljp.common.result.asResult
import com.ljp.model.DarkThemeConfig
import com.ljp.model.MoodThemeConfig
import com.ljp.model.Profile
import com.ljp.model.Settings
import com.ljp.model.ThemeBrand
import com.ljp.model.WordCommentsResource
import com.ljp.model.asSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository,
    private val profileRepository: ProfileRepository,
    private val interactionWordRepository: InteractionWordRepository,
    private val wordRepository: WordRepository,
    private val likeRepository: LikeRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {
    val currentMood : StateFlow<MoodThemeConfig> =
        userDataRepository.userData.map { it.moodThemeConfig }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MoodThemeConfig.Normal
            )
    val useDynamicColor : StateFlow<Boolean> =
        userDataRepository.userData.map { it.useDynamicColor }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )
    val themeBrand : StateFlow<ThemeBrand> =
        userDataRepository.userData.map { it.themeBrand }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ThemeBrand.DEFAULT
            )
    val darkThemeConfig : StateFlow<DarkThemeConfig> =
        userDataRepository.userData.map { it.darkThemeConfig }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DarkThemeConfig.FOLLOW_SYSTEM
            )
    val currentUserId : Flow<String> =
        userDataRepository.userData.map { it.currentUserId!! }
    val profileUiState : StateFlow<ProfileUiState> =
        currentUserId.flatMapLatest { profileId ->
            profileUiState(
                profileId = profileId,
                profileRepository = profileRepository
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Loading
        )
    val currentActionState : StateFlow<ActionState> =
        savedStateHandle.getStateFlow(ACTION_KEY, ActionState.Empty)
    val userName : StateFlow<String> = combine(
        profileUiState,
        savedStateHandle.getStateFlow(USERNAME_KEY, "")
    ) { uiState, savedBio ->
        val state = uiState as? ProfileUiState.Success
        savedBio.ifEmpty {
            state?.profile?.userName ?: ""
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = "",
        started = SharingStarted.WhileSubscribed(5_000)
    )

    val bio: StateFlow<String> = combine(
        profileUiState,
        savedStateHandle.getStateFlow(BIO_KEY, "")
    ) { uiState, savedBio ->
        val state = uiState as? ProfileUiState.Success

        savedBio.ifEmpty {
            state?.profile?.bio ?: ""
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = "",
        started = SharingStarted.WhileSubscribed(5_000)
    )
    val currentPostIds : Flow<List<String>> = combine(
        currentActionState,
        currentUserId
    ) {actionState,profileId ->
        return@combine when(actionState) {
            is ActionState.MyPost -> {
                wordRepository.getPostIds(profileId)
            }

            is ActionState.BookmarksPost -> {
                bookmarkRepository.getBookmarksPostId(profileId)
            }

            is ActionState.LikesPost -> {
                likeRepository.getLikesPostId(profileId)
            }

            else -> emptyList()
        }
    }

    val sheetContentUiState : StateFlow<SheetContentUiState> = combine(
        flow = currentPostIds,
        flow2 = currentUserId,
        flow3 = currentActionState,

    ){ postIds, profileId, actionState ->
        Triple(postIds, profileId, actionState)
    }.flatMapLatest{(postIds, profileId, actionState) ->
        sheetContentUiState(
            actionState = actionState,
            profileId = profileId,
            postIds = postIds,
            profileRepository = profileRepository,
            interactionWordRepository = interactionWordRepository,
            userDataRepository = userDataRepository
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SheetContentUiState.Loading
    )
    fun moodChanged(mood : MoodThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setMoodThemeConfig(mood)
        }
    }

    fun actionChanged(actionState: ActionState) {
        savedStateHandle[ACTION_KEY] = actionState
    }
    fun onUpdateProfile(userName: String?,
                        avatarUrl: String?,
                        bio: String?) {
        viewModelScope.launch {
            profileRepository.updateProfile(
                userName = userName,
                avatarUrl = avatarUrl,
                bio = bio
            )
        }
    }
    fun darkModeChanged(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository
                .setDarkThemeConfig(darkThemeConfig)
        }
    }
    fun themeBrandChanged(themeBrand: ThemeBrand) {
        viewModelScope.launch {
            userDataRepository
                .setThemeBrand(themeBrand)
        }
    }
    fun dynamicColorPreferenceChanged(useDynamicColor : Boolean) {
        viewModelScope.launch {
            userDataRepository
                .setDynamicColorPreference(useDynamicColor)
        }
    }
    fun onUsernameChanged(input: String) {
        savedStateHandle[USERNAME_KEY] = input
    }
    fun onBioChanged(input: String) {
        savedStateHandle[BIO_KEY] = input
    }


}
private fun profileUiState(
    profileId : String,
    profileRepository: ProfileRepository
) : Flow<ProfileUiState>{
    val profileStream = profileRepository.getProfile(profileId)
    return profileStream.asResult()
        .map { profileResult ->
            when(profileResult) {
                is Result.Success -> {
                    val result = profileResult.data
                    ProfileUiState.Success(profile = result)
                }
                is Result.Loading -> ProfileUiState.Loading
                is Result.Error -> ProfileUiState.Error
            }

        }
}
private fun sheetContentUiState(
    actionState: ActionState,
    postIds : List<String>?,
    profileId : String,
    profileRepository: ProfileRepository,
    interactionWordRepository: InteractionWordRepository,
    userDataRepository: UserDataRepository,
) : Flow<SheetContentUiState> {
    return when(actionState) {
        is ActionState.BookmarksPost,
        is ActionState.MyPost, // 记得加上 is
        is ActionState.LikesPost -> {
            if (postIds == null) {
                flowOf(SheetContentUiState.Error) // 统一：使用 flowOf
            } else {
                interactionWordRepository.getPosts(postIds)
                    .asResult()
                    .map { postResult ->
                        when(postResult) {
                            is Result.Loading -> SheetContentUiState.Loading
                            is Result.Error -> SheetContentUiState.Error
                            is Result.Success -> SheetContentUiState.PostFeed(feed = flowOf(postResult.data))
                        }
                    }
            }
        }

        is ActionState.Empty -> {
            flowOf(SheetContentUiState.Error) // 统一：使用 flowOf
        }

        is ActionState.Settings -> {
            userDataRepository.userData.map {
                SheetContentUiState.SettingsChange(it.asSetting())
            } // 这本身就是 Flow，没问题
        }

        is ActionState.UpdateProfile -> {
            // 这里假设你已经拿到了 profileId
            profileRepository.getProfile(profileId)
                .asResult()
                .map { profileResult ->
                    when(profileResult) {
                        is Result.Success -> SheetContentUiState.ProfileUpdate(profile = profileResult.data)
                        is Result.Loading -> SheetContentUiState.Loading
                        is Result.Error -> SheetContentUiState.Error
                    }
                } // 这也是 Flow，没问题
        }
    }
}
private const val ACTION_KEY = "actionKey"
private const val USERNAME_KEY = "usernameKey"
private const val BIO_KEY = "bioKey"
@Parcelize
sealed interface ActionState : Parcelable {
    @Parcelize
    data object Empty : ActionState
    @Parcelize
    data object MyPost : ActionState
    @Parcelize
    data object UpdateProfile : ActionState
    @Parcelize
    data object BookmarksPost : ActionState
    @Parcelize
    data object LikesPost : ActionState
    @Parcelize
    data object Settings : ActionState
}
sealed interface SheetContentUiState {
    data class PostFeed(val feed : Flow<PagingData<WordCommentsResource>>) : SheetContentUiState
    data class ProfileUpdate(val profile : Profile) : SheetContentUiState

    data class SettingsChange(val settings: Settings) : SheetContentUiState
    data object Loading : SheetContentUiState
    data object Error : SheetContentUiState

}