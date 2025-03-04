package de.christian2003.smarthome.data.model.cert


/**
 * Enum values indicate the status of a certificate. I.e. whether it is trusted or not.
 */
enum class SslTrustStatus {

    /**
     * Certificate is trusted.
     */
    Trusted,

    /**
     * Certificate is not trusted.
     */
    Untrusted

}