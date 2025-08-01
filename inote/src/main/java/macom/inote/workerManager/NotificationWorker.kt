package macom.inote.workerManager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import macom.inote.R

/**
 * 通知工作器类，用于在后台显示通知提醒
 * 继承自CoroutineWorker，支持协程操作
 */
class NotificationWorker(
    context: Context,           // 应用上下文
    workerParams: WorkerParameters  // 工作参数
) : CoroutineWorker(context, workerParams) {

    /**
     * 执行工作的方法，在后台线程中运行
     * @return Result 成功或失败的结果
     */
    override suspend fun doWork(): Result {
        // 从输入数据中获取笔记标题，如果为空则使用默认值"Reminder"
        val noteTitle = inputData.getString(KEY_TITLE) ?: "标题"
        // 从输入数据中获取笔记内容，如果为空则使用默认值"You have a scheduled note reminder"
        val noteContent = inputData.getString(KEY_CONTENT) ?: "通知内容"
        // 创建并显示通知
        showNotification(noteTitle, noteContent)
        // 返回成功结果
        return Result.success()
    }

    /**
     * 显示通知的方法
     * @param title 通知标题
     * @param content 通知内容
     */
    private fun showNotification(title: String, content: String) {
        // 获取系统通知服务
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 创建通知渠道 (Android 8.0+ 需要)
        val channel = NotificationChannel(
            CHANNEL_ID,           // 渠道ID
            "iNote通知",     // 渠道名称
            NotificationManager.IMPORTANCE_DEFAULT  // 渠道重要性
        ).apply {
            description = "iNote应用通知渠道"  // 渠道描述
        }
        // 创建通知渠道
        notificationManager.createNotificationChannel(channel)

        // 构建通知
        val notification: Notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.icon) // 设置小图标
            .setContentTitle(title)      // 设置标题
            .setContentText(content)     // 设置内容
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 设置优先级
            .setAutoCancel(true)         // 设置点击后自动取消
            .build()

        // 显示通知 (使用当前时间作为通知ID以确保唯一性)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    /**
     * 伴生对象，定义常量
     */
    companion object {
        const val KEY_TITLE = "iNote_title"        // 笔记标题键名
        const val KEY_CONTENT = "iNote_content"    // 笔记内容键名
        const val CHANNEL_ID = "iNote_reminder_channel" // 通知渠道ID
        const val WORK_TAG = "iNote_reminder"           // 工作标签
    }
}
