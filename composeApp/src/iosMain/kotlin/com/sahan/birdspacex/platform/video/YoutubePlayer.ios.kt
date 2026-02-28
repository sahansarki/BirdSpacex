package com.sahan.birdspacex.platform.video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@Composable
@OptIn(ExperimentalForeignApi::class)
internal fun YoutubePlayerView(
    videoId: String,
    modifier: Modifier = Modifier,
) {
    val webView = remember(videoId) {
        val configuration = WKWebViewConfiguration().apply {
            allowsInlineMediaPlayback = true
        }
        WKWebView(frame = CGRectZero.readValue(), configuration = configuration).apply {
            val url = NSURL.URLWithString(videoId)
            if (url != null) {
                loadRequest(NSURLRequest.requestWithURL(url))
            }
        }
    }

    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.navigationDelegate = null
            webView.UIDelegate = null
        }
    }

    UIKitView(
        modifier = modifier,
        factory = { webView },
    )
}
