package com.mackenzie.waifuviewer.di.modules

import com.mackenzie.waifuviewer.di.interfaces.ICryptographyComponent
import java.security.KeyStore

class CryptographyModule : ICryptographyComponent {
    override fun keyStore(): KeyStore {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore
    }
}