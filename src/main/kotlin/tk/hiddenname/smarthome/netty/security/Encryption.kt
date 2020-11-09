package tk.hiddenname.smarthome.netty.security

import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.SecretKeySpec

class Encryption {

    private val log = LoggerFactory.getLogger(Encryption::class.java)

    private lateinit var clientKeyPair: KeyPair
    private lateinit var clientAesKey: SecretKeySpec

    fun isKeySet() = false

    fun encode(s: String): ByteArray {
        return try {
            val serverCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            serverCipher.init(Cipher.ENCRYPT_MODE, clientAesKey)
            val data = serverCipher.doFinal(s.toByteArray(Charset.forName("UTF-8")))
            val params = serverCipher.parameters.encoded
            params.plus(data)
        } catch (e: Exception) {
            log.error(e.message)
            ByteArray(0)
        }
    }

    fun decode(data: ByteArray, params: ByteArray): String {
        return try {
            val clientCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            clientCipher.init(Cipher.DECRYPT_MODE, clientAesKey, getAesParamsForDecode(params))
            String(clientCipher.doFinal(data))
        } catch (e: Exception) {
            log.error(e.message)
            ""
        }
    }

    private fun getAesParamsForDecode(params: ByteArray): AlgorithmParameters {
        val aesParams = AlgorithmParameters.getInstance("AES")
        aesParams.init(params)
        return aesParams
    }

    fun getPublicKey(): ByteArray {
        return try {
            val clientKeyPairGenerator = KeyPairGenerator.getInstance("DH")
            clientKeyPair = clientKeyPairGenerator.generateKeyPair()
            clientKeyPair.public.encoded
        } catch (e: NoSuchAlgorithmException) {
            log.error(e.message)
            ByteArray(0)
        }
    }

    fun createSharedKey(serverPublicKeyEncoded: ByteArray) {
        try {
            val clientKeyAgreement = KeyAgreement.getInstance("DH")
            clientKeyAgreement.init(clientKeyPair.private)
            clientKeyAgreement.doPhase(generateServerPublicKey(serverPublicKeyEncoded), true)
            val clientSharedSecretKey = clientKeyAgreement.generateSecret()
            clientAesKey = SecretKeySpec(clientSharedSecretKey, 0, 16, "AES")
        } catch (e: Exception) {
            log.error(e.message)
        }
    }

    private fun generateServerPublicKey(serverPublicKeyEncoded: ByteArray): PublicKey {
        val clientKeyFactory = KeyFactory.getInstance("DH")
        val keySpec = X509EncodedKeySpec(serverPublicKeyEncoded)
        return clientKeyFactory.generatePublic(keySpec)
    }
}