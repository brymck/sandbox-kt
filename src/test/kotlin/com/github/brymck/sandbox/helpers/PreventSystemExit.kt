package com.github.brymck.sandbox.helpers

import java.security.Permission

internal class CheckExitCalled(status: Int) : SecurityException("Tried to exit with status $status.")

internal class NoExitSecurityManager : SecurityManager() {
    private val originalSecurityManager = System.getSecurityManager()

    override fun checkExit(status: Int) {
        throw CheckExitCalled(status)
    }

    override fun checkPermission(perm: Permission) {
        originalSecurityManager?.checkPermission(perm)
    }
}

internal fun preventSystemExit(block: () -> Unit) {
    val originalSecurityManager = System.getSecurityManager()
    val securityManager = NoExitSecurityManager()
    try {
        System.setSecurityManager(securityManager)
        block()
    } finally {
        System.setSecurityManager(originalSecurityManager)
    }
}
