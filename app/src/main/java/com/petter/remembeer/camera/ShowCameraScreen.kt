package com.petter.remembeer.camera

/*@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowCameraScreen(viewModel: BeerViewModel){
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    MainContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest,
        viewModel
    )
}

@Composable
private fun MainContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    viewModel: BeerViewModel
) {

    if (hasPermission) {
        CameraScreen(viewModel)
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}

 */