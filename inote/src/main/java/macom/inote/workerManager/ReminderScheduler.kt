package macom.inote.workerManager

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * 提醒调度器类，用于调度和取消笔记提醒任务
 * @param context 应用上下文
 */
class ReminderScheduler(private val context: Context) {

    /**
     * 调度笔记提醒
     * @param id 笔记ID，用于标识和取消特定提醒
     * @param title 提醒通知的标题
     * @param content 提醒通知的内容
     * @param delayInMillis 延迟时间（毫秒），从现在开始多久后触发提醒
     */
    fun scheduleReminder(
        id: String,// 提醒ID
        title: String,// 提醒标题
        content: String,// 提醒内容
        repeatPeriod: Long, // 重复间隔（分钟）
        delayInMillis: Long // 延迟时间（毫秒）
    ) {
        // 创建输入数据，将笔记标题和内容传递给工作器
        val inputData = Data.Builder().putString(NotificationWorker.KEY_TITLE, title)
            .putString(NotificationWorker.KEY_CONTENT, content).build()
        // 创建一次性工作请求
        if (repeatPeriod == 0L) {
            val reminderWork = OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(
                delayInMillis,
                TimeUnit.MILLISECONDS
            )  // 设置初始延迟时间
                .setInputData(inputData)  // 设置传递给工作器的数据
                .addTag("${NotificationWorker.WORK_TAG}_$id") // 使用noteId作为tag的一部分以便取消特定笔记的提醒
                .build()
            // 获取WorkManager实例并调度工作
            WorkManager.getInstance(context).enqueue(reminderWork)
        } else {
            val repeatWork = PeriodicWorkRequestBuilder<NotificationWorker>(
                repeatInterval = repeatPeriod, TimeUnit.DAYS
            ).setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                .setInputData(inputData)  // 设置工作所需的数据
                .addTag("${NotificationWorker.WORK_TAG}_$id")  // 添加标签以便后续可以识别和取消该工作
                .build()

            WorkManager.getInstance(context).enqueue(repeatWork)  // 将周期性工作加入执行队列

        }

    }

    /**
     * 取消指定笔记的提醒
     * @param id 笔记ID，用于标识需要取消的提醒任务
     */
    fun cancelReminder(id: String) {
        // 根据标签取消所有匹配的工作，标签由工作标签和笔记ID组成
        WorkManager.getInstance(context)
            .cancelAllWorkByTag("${NotificationWorker.WORK_TAG}_$id")
    }
}
