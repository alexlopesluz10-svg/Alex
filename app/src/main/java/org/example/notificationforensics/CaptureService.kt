package org.example.notificationforensics

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import org.json.JSONObject
import java.io.File

class CaptureService : NotificationListenerService() {
 override fun onNotificationPosted(sbn: StatusBarNotification?) {
  if (sbn == null || sbn.packageName == packageName) return
  val n = sbn.notification
  val e = n.extras
  val title = e.getCharSequence(Notification.EXTRA_TITLE)?.toString().orEmpty()
  val body = e.getCharSequence(Notification.EXTRA_TEXT)?.toString().orEmpty()
  val pkg = sbn.packageName
  // Default is deliberately narrow; users can explicitly opt in to all packages later.
  val targets = listOf("spotify", "com.miui.msa", "com.xiaomi", "com.miui.systemad")
  if (targets.none { pkg.contains(it, ignoreCase = true) } &&
      !title.contains("spotify", ignoreCase = true) && !body.contains("spotify", ignoreCase = true)) return
  val j = JSONObject().apply {
   put("timestamp", System.currentTimeMillis()); put("package", pkg); put("uid", sbn.uid)
   put("id", sbn.id); put("tag", sbn.tag ?: JSONObject.NULL); put("key", sbn.key)
   put("channel", n.channelId ?: JSONObject.NULL); put("title", title); put("text", body)
   put("category", n.category ?: JSONObject.NULL); put("postTime", sbn.postTime)
   put("opPkg", try { sbn.opPkg } catch (_: Exception) { JSONObject.NULL })
  }
  synchronized(CaptureService::class.java) {
   File(filesDir, "evidence.jsonl").appendText(j.toString() + "\n")
  }
 }
}
