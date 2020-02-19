package com.awkris.watchamovie.utils

import java.security.GeneralSecurityException
import java.security.Key
import javax.crypto.Cipher

private const val CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"

fun encrypt(plainText: ByteArray, key: Key): ByteArray? {
    return try {
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherText = cipher.doFinal(plainText)
        cipherText
    } catch (exception: GeneralSecurityException) {
        exception.printStackTrace()
        throw exception
    }
}

fun decrypt(cipherText: ByteArray, key: Key): ByteArray? {
    val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, key)
    return cipher.doFinal(cipherText)
}