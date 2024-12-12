package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.di.interfaces.ICryptographyComponent
import com.mackenzie.waifuviewer.di.modules.CryptographyModule
import okhttp3.TlsVersion
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Arrays
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class NetworkModule: ICryptographyComponent by CryptographyModule() {

    fun sslContext(): SSLContext {
        val trustAllCerts: Array<TrustManager> = arrayOf(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {

                }
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {

                }
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )
        // val sslContext = SSLContext.getInstance(TlsVersion.SSL_3_0.javaName)
        val tlsContext = SSLContext.getInstance(TlsVersion.TLS_1_2.javaName)
        tlsContext.init(null, trustAllCerts, SecureRandom())
        return tlsContext
    }

    fun x509TrustManager(): X509TrustManager {
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(keyStore())
        val tm = tmf.trustManagers
        if (tm.size != 1 || tm[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(tm))
        }
        return tm[0] as X509TrustManager
    }
}