package iq.tiptapp.expected

import android.os.Build

actual fun isTiramisuOrHigher(): Boolean = Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2