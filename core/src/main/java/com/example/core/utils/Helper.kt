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
        .replace("\r", "")
        .trim()
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

//    val requestBody = """{"accountNo": "111231271284142"}"""
//    val secretKey = Const.CLIENT_SECRET
////    val dataToEncrypt = generateDataSignatureGetBalance("om87sivxuh3BCBLhIbQwdAmSpXmK",requestBody.toString(),time)
////
//    val req=generateRequest(secretKey,"POST",time,"HQLRz9wuzhp4GPp3NH31HjKZHwrO",requestBody,"/snap/v1.0/balance-inquiry")

//    val hmac = generateHMACSHA512(dataToEncrypt, secretKey)
//    println(" ${req.toString()}")
//    println(" ${dataToEncrypt.toString()}")
//    println("HMAC_SHA512: $hmac")
}
