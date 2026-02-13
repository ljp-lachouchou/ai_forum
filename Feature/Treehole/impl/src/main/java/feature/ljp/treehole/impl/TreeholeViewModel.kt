package feature.ljp.treehole.impl

import ai.ljp.data.repository.TreeholeRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ljp.common.result.Result
import com.ljp.common.result.asResult
import com.ljp.model.Treehole
import com.ljp.model.TreeholeProfileSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreeholeViewModel @Inject constructor(
    private val treeholeRepository: TreeholeRepository
) : ViewModel() {
    val treeholeFeedUiState : StateFlow<TreeholeFeedUiState> = treeholeFeedUiState(treeholeRepository)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TreeholeFeedUiState.Loading
        )
    fun onCreateTreeholeClick(content : String,isAnonymous : Boolean) {
        viewModelScope.launch {
            treeholeRepository.createTreehole(content,isAnonymous)
        }
    }

}
private fun treeholeFeedUiState(
    treeholeRepository: TreeholeRepository
) : Flow<TreeholeFeedUiState> {
    val feedStream = treeholeRepository.getTreeholes()
    return feedStream.asResult()
        .map { result->
            when(result) {
                is Result.Error -> TreeholeFeedUiState.Error
                is Result.Loading -> TreeholeFeedUiState.Loading
                is Result.Success -> {
                    val data = result.data
                    TreeholeFeedUiState.Success(feed = flowOf(data))
                }
            }
        }
}
sealed interface TreeholeFeedUiState {
    data class Success(val feed : Flow<PagingData<TreeholeProfileSource>>) : TreeholeFeedUiState
    data object Loading : TreeholeFeedUiState
    data object Error : TreeholeFeedUiState
}