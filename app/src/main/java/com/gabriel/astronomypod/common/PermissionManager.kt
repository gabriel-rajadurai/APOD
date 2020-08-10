package com.gabriel.astronomypod.common

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionManager(private val activity: Activity) { //Use default constructor to request permissions from an activity.

    private var fragment: Fragment? = null
    private var permissions = setOf<PERMISSION>()
    private var permissionsListener: PermissionsRequestListener? = null

    //Use this constructor if you want to request permissions from a fragment
    constructor(fragment: Fragment) : this(fragment.requireActivity()) {
        this.fragment = fragment
    }

    //Use this method to request permissions. Response will be provided through a callback
    fun requestPermissions(vararg permissions: PERMISSION, listener: PermissionsRequestListener) {

        if (permissions.isEmpty()) {
            Log.e(TAG, "Ignoring permission request, No arguments provided")
            return
        }

        permissionsListener = listener

        this@PermissionManager.permissions = permissions.toSet()
        val permissionsArray = permissions.map { it.permission }.toTypedArray()
        var permissionsAlreadyGranted = false
        for (permission in permissionsArray) {
            permissionsAlreadyGranted = ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            if (!permissionsAlreadyGranted) {
                break
            }
        }

        if (permissionsAlreadyGranted) {
            listener.onPermissionGranted()
            return
        }

        // Requesting permission using the fragment instance if available
        fragment?.let {
            it.requestPermissions(
                permissionsArray,
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
            return
        }

        //If fragment instance is not available, then the permission was requested from an activity.
        ActivityCompat.requestPermissions(
            activity,
            permissionsArray,
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    //Call this method in the onRequestPermissionsResult() method of the requesting activity/fragment.
    //This will handle the response and provide you with the result through a callback.
    fun handleRequestPermissionResult(requestCode: Int) {
        if (permissions.isEmpty()) {
            Log.e(TAG, "Skipping permission request, No arguments provided")
            return
        }

        var permissionsGranted = false

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {

            permissions.map { it.permission }.forEach {
                permissionsGranted = ContextCompat.checkSelfPermission(
                    activity,
                    it
                ) == PackageManager.PERMISSION_GRANTED

                if (!permissionsGranted) {
                    permissionsListener?.onPermissionDenied()
                    return
                }
            }

            if (permissionsGranted) {
                permissionsListener?.onPermissionGranted()
            } else {
                permissionsListener?.onPermissionDenied()
            }
        }
    }

    //Enum of Permissions. You can extend this enum by adding other dangerous permissions
    enum class PERMISSION(val permission: String) {
        LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
        STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        CAMERA(Manifest.permission.CAMERA)
    }

    //Callback to notify permission request result
    interface PermissionsRequestListener {
        fun onPermissionGranted()
        fun onPermissionDenied()
    }

    companion object {
        private val TAG = PermissionManager::class.java.simpleName
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

}