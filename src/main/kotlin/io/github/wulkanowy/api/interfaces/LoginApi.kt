package io.github.wulkanowy.api.interfaces

import io.github.wulkanowy.api.login.ADFSFormResponse
import io.github.wulkanowy.api.login.CertificateResponse
import io.reactivex.Single
import retrofit2.http.*

interface LoginApi {

    @POST("Account/LogOn")
    @FormUrlEncoded
    fun sendCredentials(@Query("ReturnUrl") returnUrl: String, @FieldMap credentials: Map<String, String>): Single<CertificateResponse>

    @POST
    @FormUrlEncoded
    fun sendCertificate(@Url url: String, @FieldMap certificate: Map<String, String>): Single<CertificateResponse>

    // ADFS

    @GET("Account/LogOn")
    fun getForm(@Query("ReturnUrl") returnUrl: String): Single<ADFSFormResponse>

    @POST
    @FormUrlEncoded
    fun sendADFSFormStandardChoice(@Url url: String, @FieldMap formState: Map<String, String>): Single<ADFSFormResponse>

    @POST
    @FormUrlEncoded
    fun sendADFSCredentials(@Url url: String, @FieldMap credentials: Map<String, String>): Single<CertificateResponse>
}
