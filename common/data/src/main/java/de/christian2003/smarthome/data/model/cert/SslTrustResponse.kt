package de.christian2003.smarthome.data.model.cert

import java.security.cert.X509Certificate


/**
 * Class implements an SSL trust response.
 */
data class SslTrustResponse(

    /**
     * Attribute indicates the status for the response.
     */
    val status: SslTrustStatus,

    /**
     * Optional attribute stores the certificate for which the trust response has been created.
     */
    val cert: X509Certificate? = null

)
