package feature.ljp.me.impl

import ai.ljp.designsystem.component.AIForumBottomSheetScaffold
import ai.ljp.designsystem.component.AIForumButton
import ai.ljp.designsystem.icon.AIForumIcon
import ai.ljp.designsystem.theme.Amber100
import ai.ljp.designsystem.theme.Blue100
import ai.ljp.designsystem.theme.LocalTintTheme
import ai.ljp.designsystem.theme.Purple100
import ai.ljp.designsystem.theme.Slate600
import ai.ljp.ui.Dot
import ai.ljp.ui.ProfileCard
import ai.ljp.ui.ProfileUiState
import ai.ljp.ui.wordsItem
import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.ljp.model.DarkThemeConfig
import com.ljp.model.MoodThemeConfig
import com.ljp.model.ThemeBrand
import feature.ljp.me.api.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MeScreen(
    onPostClick : (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MeViewModel = hiltViewModel()
) {
    val profileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()
    val sheetContentUiState by viewModel.sheetContentUiState.collectAsStateWithLifecycle()
    val currentMood by viewModel.currentMood.collectAsStateWithLifecycle()
    val username by viewModel.userName.collectAsStateWithLifecycle()
    val bio by viewModel.bio.collectAsStateWithLifecycle()
    val scaffoldState = rememberBottomSheetScaffoldState()
    MeScreen(
        profileUiState = profileUiState,
        currentMood = currentMood,
        username = username,
        bio =bio,
        sheetContentUiState = sheetContentUiState,
        scaffoldState = scaffoldState,
        modifier = modifier,
        onPostClick = onPostClick,
        moodChanged = viewModel::moodChanged,
        actionChanged = viewModel::actionChanged,
        onUpdateProfile = viewModel::onUpdateProfile,
        onBioChanged = viewModel::onBioChanged,
        onUsernameChanged = viewModel::onUsernameChanged,
        darkModeChanged = viewModel::darkModeChanged,
        themeBrandChanged = viewModel::themeBrandChanged,
        dynamicColorPreferenceChanged = viewModel::dynamicColorPreferenceChanged
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MeScreen(
    currentMood : MoodThemeConfig,
    profileUiState : ProfileUiState,
    username : String,
    bio : String,
    sheetContentUiState: SheetContentUiState,
    scaffoldState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
    scope : CoroutineScope = rememberCoroutineScope(),
    onPostClick : (String) -> Unit,
    moodChanged : (MoodThemeConfig) -> Unit,
    actionChanged : (ActionState) -> Unit,
    onUpdateProfile : (String?, String?,String?) -> Unit,
    onUsernameChanged : (String) -> Unit,
    onBioChanged : (String) -> Unit,
    darkModeChanged : (DarkThemeConfig) -> Unit,
    themeBrandChanged : (ThemeBrand) -> Unit,
    dynamicColorPreferenceChanged : (Boolean) -> Unit
) {
    val onSheetShowClickTriggered : (ActionState) -> Unit = {actionState->
        actionChanged(actionState)
        scope.launch {
            scaffoldState.bottomSheetState.show()
        }
    }
    val resources = LocalResources.current
    AIForumBottomSheetScaffold(
        sheetContent = {
            MeSheetContent(
                sheetContentUiState = sheetContentUiState,
                bio = bio,
                username = username,
                onPostClick = onPostClick,
                onUpdateProfile = onUpdateProfile,
                onUsernameChanged = onUsernameChanged,
                onBioChanged = onBioChanged,
                darkModeChanged = darkModeChanged,
                themeBrandChanged = themeBrandChanged,
                dynamicColorPreferenceChanged = dynamicColorPreferenceChanged,
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    ProfileCard(
                        profileUiState = profileUiState,
                        dotContent = {
                            Dot(color = Color.Blue)
                        },
                        onDotClick = {
                            onSheetShowClickTriggered(ActionState.UpdateProfile)
                        }
                    )
                }
                item {
                    ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                        Text(
                            text = stringResource(
                            R.string.feature_me_api_current_mood
                            )
                        )
                    }
                }
                item {
                    MoodSelector(
                        options = moodOptions(resources),
                        currentMood = currentMood,
                        moodChanged = moodChanged,
                    )
                }
                item {
                    ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                        Text(
                            text = stringResource(
                                R.string.feature_me_api_personal
                            )
                        )
                    }
                }
                items(items = meItems(resources), key = {it.itemTitle}) {item ->
                    MeListItem(
                        itemTitle = item.itemTitle,
                        icon = item.icon,
                        onClick = {
                            onSheetShowClickTriggered(item.actionState)
                        },
                    )

                }
            }
        },
        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    )
}
@Stable
data class MeItem(
    val itemTitle: String,
    val icon: ImageVector,
    val actionState: ActionState,
)
private fun moodOptions(resources: Resources) : List<MoodOption> = listOf(
    MoodOption(
        mood = MoodThemeConfig.Normal,
        label = resources.getString(
            R.string.feature_me_api_normal
        ),
        activeColor = Blue100
    ),
    MoodOption(
        mood = MoodThemeConfig.Happy,
        label = resources.getString(
            R.string.feature_me_api_happy
        ),
        activeColor = Amber100
    ),
    MoodOption(
        mood = MoodThemeConfig.Sad,
        label = resources.getString(
            R.string.feature_me_api_sad
        ),
        activeColor = Slate600
    ),
    MoodOption(
        mood = MoodThemeConfig.Anxiety,
        label = resources.getString(
            R.string.feature_me_api_anxiety
        ),
        activeColor = Purple100
    ),
)
private fun meItems(resources: Resources) : List<MeItem> = listOf(
    MeItem(
        itemTitle = resources.getString(
            R.string.feature_me_api_my_posts
        ),
        icon = AIForumIcon.Post,
        actionState = ActionState.MyPost,
    ),
    MeItem(
        itemTitle = resources.getString(
            R.string.feature_me_api_my_bookmarks
        ),
        icon = AIForumIcon.Bookmarks,
        actionState = ActionState.BookmarksPost,
    ),
    MeItem(
        itemTitle = resources.getString(
            R.string.feature_me_api_my_likes
        ),
        icon = AIForumIcon.Like,
        actionState = ActionState.LikesPost,
    ),
    MeItem(
        itemTitle = resources.getString(
            R.string.feature_me_api_settings
        ),
        icon = AIForumIcon.Settings,
        actionState = ActionState.Settings,
    ),

)
@Composable
private fun MeListItem(
    itemTitle : String,
    icon : ImageVector,
    onClick : () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = LocalTintTheme.current.iconTint
    ListItem(
        headlineContent = {
            ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
                Text(text = itemTitle)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = true,onClick = onClick),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (tint== Color.Unspecified) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }else {
                    tint
                }
            )
        },
        trailingContent = {
            Icon(
                imageVector = AIForumIcon.ChevronRight,
                contentDescription = null,
                tint = if (tint== Color.Unspecified) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }else {
                    tint
                }
            )
        }
    )
}
@Stable
data class MoodOption internal constructor(
    val mood: MoodThemeConfig,
    val label : String,
    val activeColor : Color
)
fun moodOption(
    mood: MoodThemeConfig,
    label : String,
    activeColor : Color
) = MoodOption(
    mood = mood,
    label = label,
    activeColor = activeColor
)
@Composable
private fun MoodSelector(
    options : List<MoodOption>,
    currentMood : MoodThemeConfig,
    moodChanged : (MoodThemeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { option ->
                val isSelected = option.mood == currentMood
                MoodItem(
                    isSelected = isSelected,
                    label = option.label,
                    activeColor = option.activeColor,
                    onClick = {
                        moodChanged(option.mood)
                    },
                )
            }
        }
    }

}
@Composable
private fun MoodItem(
    isSelected : Boolean,
    label : String,
    activeColor : Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        color = if (isSelected) {
            activeColor
        }else {
            activeColor.copy(alpha = 0.6f)
        },
        modifier = modifier
            .selectable(
                selected = isSelected,
                role = Role.RadioButton,
                onClick = onClick
            ),
    ) {
        ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
            Text(text = label)
        }
    }
}
@Composable
fun MeSheetContent(
    sheetContentUiState: SheetContentUiState,
    bio: String,
    username: String,
    onPostClick: (String) -> Unit,
    onUpdateProfile: (String?, String?, String?) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onBioChanged: (String) -> Unit,
    darkModeChanged: (DarkThemeConfig) -> Unit,
    themeBrandChanged: (ThemeBrand) -> Unit,
    dynamicColorPreferenceChanged: (Boolean) -> Unit,

) {
    when(sheetContentUiState) {
        is SheetContentUiState.Error -> Unit
        is SheetContentUiState.Loading -> {
            Text(text = "加载中...")
        }
        is SheetContentUiState.SettingsChange -> {
            SettingsChangeSheetContent(
                sheetContentUiState = sheetContentUiState,
                modifier = Modifier.fillMaxWidth().padding(16.dp).imePadding(),
                darkModeChanged = darkModeChanged,
                themeBrandChanged = themeBrandChanged,
                dynamicColorPreferenceChanged = dynamicColorPreferenceChanged,
            )
        }
        is SheetContentUiState.PostFeed -> {
            PostFeedSheetContent(
                sheetContentUiState = sheetContentUiState,
                modifier = Modifier.fillMaxWidth().padding(16.dp).imePadding(),
                onPostClick = onPostClick
            )
        }
        is SheetContentUiState.ProfileUpdate -> {
            ProfileUpdateSheetContent(
                username = username,
                bio = bio,
                onUsernameChanged = onUsernameChanged,
                onBioChanged = onBioChanged,
                modifier = Modifier.fillMaxWidth().padding(16.dp).imePadding(),
                onUpdateProfile = onUpdateProfile,
            )
        }
    }
}
@Composable
private fun SettingsChangeSheetContent(
    sheetContentUiState: SheetContentUiState.SettingsChange,
    modifier: Modifier = Modifier,
    darkModeChanged : (DarkThemeConfig) -> Unit,
    themeBrandChanged : (ThemeBrand) -> Unit,
    dynamicColorPreferenceChanged : (Boolean) -> Unit
) {
    val darkThemeConfig = sheetContentUiState.settings.darkThemeConfig
    val themeBrand = sheetContentUiState.settings.themeBrand
    val useDynamicColor = sheetContentUiState.settings.useDynamicColor
    val dark = isSystemInDarkTheme()
    val darkModeSelected = when(darkThemeConfig) {
        DarkThemeConfig.DARK -> true
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.FOLLOW_SYSTEM -> dark
    }
    val themeBrandSelected = when(themeBrand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingItem(
            settingTitle = stringResource(
                R.string.feature_me_api_dark_mode
            ),
            isSelected = darkModeSelected,
            onSettingChange = {selected ->
                if (selected) {
                    darkModeChanged(DarkThemeConfig.FOLLOW_SYSTEM)
                }else {
                    darkModeChanged(DarkThemeConfig.DARK)
                }

            }
        )
        SettingItem(
            settingTitle = stringResource(
                R.string.feature_me_api_dynamic_color
            ),
            isSelected = useDynamicColor,
            onSettingChange = {selected ->
                dynamicColorPreferenceChanged(!selected)
            }
        )
        SettingItem(
            settingTitle = stringResource(
                R.string.feature_me_api_theme_brand
            ),
            isSelected = themeBrandSelected,
            onSettingChange = {selected ->
                if (selected) {
                    themeBrandChanged(ThemeBrand.DEFAULT)
                }else {
                    themeBrandChanged(ThemeBrand.ANDROID)
                }
            }
        )
    }
}
@Composable
private fun SettingItem(
    settingTitle : String,
    isSelected : Boolean,
    onSettingChange : (Boolean) -> Unit,
) {
    ListItem(
        headlineContent = {
            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                Text(text = settingTitle)
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primary,
            headlineColor = MaterialTheme.colorScheme.onPrimary
        ),
        trailingContent = {
            Switch(
                checked = isSelected,
                onCheckedChange = onSettingChange,
                thumbContent = {
                    Canvas(onDraw = {
                        drawCircle(
                            radius = size.minDimension / 2F,
                            color = Color.White
                        )
                    }, modifier = Modifier.fillMaxSize())
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor= MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                ),
            )
        }
    )
}
@Composable
private fun ProfileUpdateSheetContent(
    username: String,
    bio: String,
    modifier: Modifier = Modifier,
    onUpdateProfile: (String?, String?, String?) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onBioChanged: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                Text(text = stringResource(
                    R.string.feature_me_api_personal_username
                ))
            }
        }
        item {
            UpdateInfoTextField(
                info = username,
                focusRequester = focusRequester,
                onInfoChanged = onUsernameChanged
            )
        }
        item {
            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                Text(text = stringResource(
                    R.string.feature_me_api_personal_bio
                ))
            }
        }
        item {
            UpdateInfoTextField(
                info = bio,
                focusRequester = focusRequester,
                onInfoChanged = onBioChanged
            )
        }
        item {
            AIForumButton(
                onClick = {
                    onUpdateProfile(username,null,bio)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(
                    R.string.feature_me_api_update_profile
                ), color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
@Composable
private fun PostFeedSheetContent(
    sheetContentUiState : SheetContentUiState.PostFeed,
    modifier: Modifier = Modifier,
    onPostClick: (String) -> Unit,
) {
    val wordsLazyItems = sheetContentUiState.feed.collectAsLazyPagingItems()
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        wordsItem(
            wordsLazyItems = wordsLazyItems,
            onPostClick = onPostClick,
        )
    }
}

@Composable
private fun UpdateInfoTextField(
    info : String,
    focusRequester : FocusRequester,
    onInfoChanged :(String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val onExplicitlyTriggered = {
        keyboardController?.hide()
    }
    TextField(
        value = info,
        onValueChange = {
            if ("\n" !in it) {
                onInfoChanged(it)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        trailingIcon = {
            if (info.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onInfoChanged("")
                    }
                ) {
                    Icon(
                        imageVector = AIForumIcon.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        singleLine = true,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    if (info.isBlank()) return@onKeyEvent false
                    onExplicitlyTriggered()
                    true
                } else {
                    false
                }
            },
        shape = RoundedCornerShape(16.dp),
        keyboardActions = KeyboardActions(
            onSend = {
                if (info.isBlank()) return@KeyboardActions
                onExplicitlyTriggered()
            }
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)

    )
}