package com.example.core.utils

import android.util.Log
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.Security
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun getTimeStamp(): String {
    val currentDatetime = Date()

    val localDatetime = Date(currentDatetime.time)

    val timestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

    timestampFormat.timeZone = TimeZone.getTimeZone("GMT+7")
    return "${timestampFormat.format(localDatetime)}+07:00"
}

fun generateSHA256withRSA(data: ByteArray, privateKey: PrivateKey): String {
    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)
    signature.update(data)
    val signedBytes = signature.sign()
    return Base64.getEncoder().encodeToString(signedBytes)
}

fun getPrivateKey(privateKeyPEM: String): PrivateKey {
    Security.addProvider(BouncyCastleProvider())

    val keyBytes = Base64.getDecoder().decode(removePEMHeaders(privateKeyPEM))
    val keySpec = PKCS8EncodedKeySpec(keyBytes)
    val keyFactory = KeyFactory.getInstance("RSA")
    return keyFactory.generatePrivate(keySpec)
}

fun removePEMHeaders(key: String): String {
    return key
        .replace("-----BEGIN RSA PRIVATE KEY-----", "")
        .replace("-----END RSA PRIVATE KEY-----", "")
        .replace("\n", "")
        .replace("\r", "") // Jika newline menggunakan carriage return
        .trim()
}

fun generateHMACSHA512(data: String, key: String): String {
    try {
        val hmacSHA512 = Mac.getInstance("HmacSHA512")
        val secretKey = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "HmacSHA512")
        hmacSHA512.init(secretKey)

        val hmacBytes = hmacSHA512.doFinal(data.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(hmacBytes)
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: InvalidKeyException) {
        e.printStackTrace()
    }
    return ""
}

fun minify(data: String): String {
    // Implementasi minify sesuai kebutuhan
    // Misalnya, menghapus spasi dan karakter-karakter yang tidak diinginkan
    return data.replace("\\s".toRegex(), "")
}

fun sha256HexEncode(data: String): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val hashBytes = messageDigest.digest(data.toByteArray())
    return bytesToHex(hashBytes)
}

fun bytesToHex(bytes: ByteArray): String {
    val hexChars = CharArray(bytes.size * 2)
    for (i in bytes.indices) {
        val v = bytes[i].toInt() and 0xFF
        hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
        hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
    }
    return String(hexChars)
}

fun generateFormattedString(requestBody: String): String {
    val minifiedData = minify(requestBody)
    val sha256Hex = sha256HexEncode(minifiedData)
    val lowercaseHex = sha256Hex.toLowerCase()

    return lowercaseHex
}

fun generateDataSignatureGetBalance(token: String, requestBody: String, timeStamp: String): String {
    return "POST:/snap/v1.0/balance-inquiry:$token:${generateFormattedString(requestBody)}:$timeStamp"
}

fun calculateSHA256(data: String): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val hashBytes = messageDigest.digest(data.toByteArray(StandardCharsets.UTF_8))
    return bytesToHexLowerCase(hashBytes)
}

fun bytesToHexLowerCase(bytes: ByteArray): String {
    return bytes.joinToString("") { "%02x".format(it) }
}

fun generateRequest(
    clientSecret: String,
    method: String,
    timestamp: String,
    accessToken: String,
    bodyRequest: String,
    path: String
): String {
    val minifiedData = minify(bodyRequest)
    val sha256 = calculateSHA256(bodyRequest)
    val stringToSign = "$method:$path:$accessToken:$sha256:$timestamp"
    Log.e("dataToken",stringToSign)
    val hmac = Mac.getInstance("HmacSHA512")
    val secretKey = SecretKeySpec(clientSecret.toByteArray(StandardCharsets.UTF_8), "HmacSHA512")
    hmac.init(secretKey)
    val signatureBytes = hmac.doFinal(stringToSign.toByteArray())
    return signatureBytes.joinToString("") { "%02x".format(it) }
}


fun main() {
    val time = getTimeStamp() // Mendapatkan kunci privat dari format PEM
    println(time)

//    Security.addProvider(BouncyCastleProvider())
//    println(generateSHA256withRSA("${Const.CLIENT_KEY}|${time}".toByteArray(), getPrivateKey(Const.PRIVATE_KEY)))

    val requestBody = """{"accountNo": "111231271284142"}"""
    val secretKey = Const.CLIENT_SECRET
//    val dataToEncrypt = generateDataSignatureGetBalance("om87sivxuh3BCBLhIbQwdAmSpXmK",requestBody.toString(),time)
//
    val req=generateRequest(secretKey,"POST",time,"HQLRz9wuzhp4GPp3NH31HjKZHwrO",requestBody,"/snap/v1.0/balance-inquiry")

//    val hmac = generateHMACSHA512(dataToEncrypt, secretKey)
    println(" ${req.toString()}")
//    println(" ${dataToEncrypt.toString()}")
//    println("HMAC_SHA512: $hmac")
}
