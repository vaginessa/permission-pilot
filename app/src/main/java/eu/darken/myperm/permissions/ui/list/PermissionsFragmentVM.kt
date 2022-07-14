package eu.darken.myperm.permissions.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.myperm.common.coroutine.DispatcherProvider
import eu.darken.myperm.common.debug.logging.log
import eu.darken.myperm.common.uix.ViewModel3
import eu.darken.myperm.main.ui.main.MainFragmentDirections
import eu.darken.myperm.permissions.core.PermissionRepo
import eu.darken.myperm.permissions.core.types.NormalPermission
import eu.darken.myperm.permissions.ui.list.permissions.NormalPermissionVH
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PermissionsFragmentVM @Inject constructor(
    @Suppress("UNUSED_PARAMETER") handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    permissionRepo: PermissionRepo,
) : ViewModel3(dispatcherProvider = dispatcherProvider) {

    val listData: LiveData<List<PermissionsAdapter.Item>> = permissionRepo.permissions
        .map { permissions ->
            permissions
                .sortedByDescending { it.grantedApps.size }
                .map { permission ->
                    when (permission) {
                        is NormalPermission -> NormalPermissionVH.Item(
                            perm = permission,
                            onClickAction = {
                                log(TAG) { "Navigating to $permission" }
                                MainFragmentDirections.actionMainFragmentToPermissionDetailsFragment(
                                    permissionId = permission.id
                                ).navigate()
                            }
                        )
                        else -> throw IllegalArgumentException()
                    }
                }
        }
        .asLiveData2()
}