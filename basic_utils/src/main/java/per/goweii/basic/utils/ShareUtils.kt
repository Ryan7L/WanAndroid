//package per.goweii.basic.utils
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import per.goweii.basic.utils.bitmap.BitmapUtils
//
//fun shareBitmap(context: Context, bitmap: Bitmap) {
//    val file = BitmapUtils.saveBitmapToCache(bitmap)
//    bitmap.recycle()
//    file?.let {
//        Intent(Intent.ACTION_SEND).run {
//            type = "image/*"
//            putExtra(Intent.EXTRA_STREAM, it.getUri())
//            try {
//                context.startActivity(Intent.createChooser(this, "分享图片"))
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//}
//
//fun shareLink(context: Context, url: String) {
//    Intent(Intent.ACTION_SEND).run {
//        type = "text/plain"
//        putExtra(Intent.EXTRA_TEXT, url)
//        try {
//            context.startActivity(Intent.createChooser(this, "分享链接"))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//}