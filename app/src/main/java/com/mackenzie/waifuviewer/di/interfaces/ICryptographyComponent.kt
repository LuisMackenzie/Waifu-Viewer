package com.mackenzie.waifuviewer.di.interfaces

import java.security.KeyStore

interface ICryptographyComponent {

    fun keyStore(): KeyStore
}