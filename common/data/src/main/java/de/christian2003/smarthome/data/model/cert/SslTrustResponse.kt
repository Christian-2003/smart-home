package de.christian2003.smarthome.data.model.cert

import java.security.cert.X509Certificate


data class SslTrustResponse(

    val status: SslTrustStatus,

    val cert: X509Certificate? = null

)
