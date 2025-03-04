package de.christian2003.smarthome.data.model.cert

import java.security.PrivateKey
import java.security.cert.X509Certificate

data class ClientCert(

    val key: PrivateKey,

    val chain: Array<X509Certificate>

)
