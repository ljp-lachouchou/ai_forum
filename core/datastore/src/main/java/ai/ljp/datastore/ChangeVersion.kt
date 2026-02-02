package ai.ljp.datastore

import javax.annotation.concurrent.Immutable

@Immutable
@JvmInline
value class ChangeVersion(
    val syncVersion : Long = -1
)