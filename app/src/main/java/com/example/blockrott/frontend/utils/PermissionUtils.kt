package com.example.blockrott.frontend.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import backend.Usuario

fun verificarYPedirPermisosIniciales(context: Context, usuario: Usuario) {
    if (!usuario.verificarPermisosUsoEstadistica(context)) {
        solicitarPermisoUsoEstadisticas(context)
        return
    }

    if (!usuario.verificarPermisoSuperposicion(context)) {
        solicitarPermisoSuperposicion(context)
    }
}

private fun solicitarPermisoUsoEstadisticas(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

private fun solicitarPermisoSuperposicion(context: Context) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + context.packageName))
        context.startActivity(intent)
    }
}