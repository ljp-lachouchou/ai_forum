package ai.ljp.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable


@Composable
fun rememberLauncherImageForActivityResult(
    onImagePicked: (Uri) -> Unit
) : ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->
            uri?.let { onImagePicked(it) }
        }
    )
}
val PickOnly = PickVisualMediaRequest(
    ActivityResultContracts.PickVisualMedia.ImageOnly
)
