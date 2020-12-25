package io.github.wulkanowy.sdk.scrapper.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @see <a href="https://deviceatlas.com/blog/most-popular-android-smartphones#poland">The most popular Android phones - 2018</a>
 * @see <a href="http://www.tera-wurfl.com/explore/?action=wurfl_id&id=samsung_sm_j500h_ver1">Tera-WURFL Explorer - Samsung SM-J500H (Galaxy J5)</a>
 *
 * @see <a href="https://github.com/jhy/jsoup/blob/220b77140bce70dcf9c767f8f04758b09097db14/src/main/java/org/jsoup/helper/HttpConnection.java#L59">JSoup default user agent</a>
 * @see <a href="https://developer.chrome.com/multidevice/user-agent#chrome_for_android_user_agent">User Agent Strings - Google Chrome</a>
 */
class UserAgentInterceptor(
    private val androidVersion: String,
    private val buildTag: String,
    private val webKitRev: String = "537.36",
    private val chromeRev: String = "83.0.4103.96"
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder()
            .addHeader("User-Agent",
                "Mozilla/5.0 (Linux; Android $androidVersion; $buildTag) " +
                    "AppleWebKit/$webKitRev (KHTML, like Gecko) " +
                    "Chrome/$chromeRev Mobile " +
                    "Safari/$webKitRev")
            .build()
        )
    }
}
