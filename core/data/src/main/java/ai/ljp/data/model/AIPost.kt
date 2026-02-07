package ai.ljp.data.model

import ai.ljp.network.model.SyncWordItem
import com.ljp.model.Word

data class AIPost(
    val content : String,
    val suggestions : List<String>
)


data class AISearch(val aiWordItems : List<Word>? = null, val searchOk : Boolean,val answer : String? = null)