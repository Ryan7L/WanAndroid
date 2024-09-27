package per.goweii.basic.utils

import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

fun File.getUri(): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(Utils.context, "${Utils.context.packageName}.fileProvider", this)
    } else {
        Uri.fromFile(this)
    }
}