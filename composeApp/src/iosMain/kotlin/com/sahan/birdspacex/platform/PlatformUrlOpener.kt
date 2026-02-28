package com.sahan.birdspacex.platform

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class PlatformUrlOpener {
    actual fun open(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}
