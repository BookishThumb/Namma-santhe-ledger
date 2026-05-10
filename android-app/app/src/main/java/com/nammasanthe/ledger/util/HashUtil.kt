package com.nammasanthe.ledger.util

import java.security.MessageDigest

/** SHA-256 PIN hashing — far more secure than the JS prototype. */
object HashUtil {

    fun hashPin(pin: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(pin.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyPin(pin: String, hashed: String): Boolean = hashPin(pin) == hashed
}
