//package per.goweii.basic.ui.widget
//
//import android.content.Context
//import android.util.AttributeSet
//import androidx.annotation.IntDef
//import androidx.appcompat.widget.AppCompatImageView
//import per.goweii.basic.ui.R
//
//class PercentImageView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : AppCompatImageView(context, attrs, defStyleAttr) {
//    companion object {
//        const val BASICS_WIDTH = 0
//        const val BASICS_HEIGHT = 1
//    }
//
//    private var measureWidth = 0
//    private var measureHeight = 0
//
//    @Basics
//    var basics: Int? = null
//        set(value) {
//            if (value == field) return
//            if (value == null) return
//            field = value
//            setUpNewSize()
//        }
//    var percent: Float? = null
//        set(value) {
//            if (value == field) return
//            if (value == null) return
//            field = value
//            setUpNewSize()
//        }
//
//    init {
//        context.obtainStyledAttributes(attrs, R.styleable.PercentImageView).use {
//            basics = it.getInt(R.styleable.PercentImageView_piv_basics, BASICS_WIDTH)
//            percent = it.getFloat(R.styleable.PercentImageView_piv_percent, 0f)
//        }
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        measureWidth = MeasureSpec.getSize(widthMeasureSpec)
//        measureHeight = MeasureSpec.getSize(heightMeasureSpec)
//        val size = calculateSize()
//        if (size == null) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        } else {
//            setMeasuredDimension(size[0], size[1])
//        }
//    }
//
//    private fun setUpNewSize() {
//        val size = calculateSize() ?: return
//        layoutParams.width = size[0]
//        layoutParams.height = size[1]
//        requestLayout()
//    }
//
//    private fun calculateSize(): IntArray? {
//        val size = intArrayOf(measureWidth, measureHeight)
//        percent ?: return null
//        if (basics == BASICS_WIDTH) {
//            size[1] = (size[0] * percent!!).toInt()
//        } else {
//            size[0] = (size[1] * percent!!).toInt()
//        }
//        return size
//    }
//}
//
//@IntDef(PercentImageView.BASICS_WIDTH, PercentImageView.BASICS_HEIGHT)
//@Retention(AnnotationRetention.SOURCE)
//annotation class Basics {}