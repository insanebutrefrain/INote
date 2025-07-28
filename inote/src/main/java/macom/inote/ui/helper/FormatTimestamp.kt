package macom.inote.ui.helper

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * 用于计算修改时间的显示格式
 * 针对不同的时间采用不同的显示格式
 */
fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val duration = now - timestamp

    return when {
        duration < 60_000 -> "刚刚"  // 小于一分钟
        duration < 3_600_000 -> "${duration / 60_000} 分钟前"  // 小于一小时
        duration < 86_400_000 -> "${duration / 3_600_000} 小时前"  // 小于一天
        duration < 2_592_000_000 -> "${duration / 86_400_000} 天前"  // 小于一个月
//        duration < 31_536_000_000 -> {
//            val months = duration / 2_592_000_000
//            "$months 个月前"
//        }

        else -> {
            // Use Calendar and SimpleDateFormat for older API levels
            val calendar = Calendar.getInstance().apply {
                timeInMillis = timestamp
            }
            val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
            dateFormat.format(calendar.time)
        }
    }
}