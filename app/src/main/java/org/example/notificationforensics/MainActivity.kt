package org.example.notificationforensics

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.io.File

class MainActivity : Activity() {
 private lateinit var status: TextView
 private val exportCode = 20
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
  val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(28, 36, 28, 24) }
  layout.addView(TextView(this).apply { text = "Notification Forensics — v1.0\nAuditoria local de notificações Spotify/Xiaomi. Não envia dados à internet."; textSize = 20f })
  layout.addView(Button(this).apply { text = "Autorizar acesso às notificações"; setOnClickListener { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) } })
  layout.addView(Button(this).apply { text = "Atualizar registros"; setOnClickListener { refresh() } })
  layout.addView(Button(this).apply { text = "Exportar evidências JSONL"; setOnClickListener {
   val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply { addCategory(Intent.CATEGORY_OPENABLE); type = "application/json"; putExtra(Intent.EXTRA_TITLE, "notification-evidence.jsonl") }
   startActivityForResult(intent, exportCode)
  } })
  status = TextView(this).apply { textSize = 13f; setTextIsSelectable(true) }
  layout.addView(status)
  setContentView(ScrollView(this).apply { addView(layout) }); refresh()
 }
 override fun onResume() { super.onResume(); if (::status.isInitialized) refresh() }
 private fun refresh() { val f = File(filesDir, "evidence.jsonl"); val lines = if (f.exists()) f.readLines() else emptyList(); status.text = "Registros capturados: ${lines.size}\n\n" + lines.takeLast(12).reversed().joinToString("\n\n") }
 @Deprecated("Legacy activity result")
 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { super.onActivityResult(requestCode, resultCode, data)
  if (requestCode == exportCode && resultCode == RESULT_OK) data?.data?.let { uri ->
   contentResolver.openOutputStream(uri)?.use { out -> val f = File(filesDir, "evidence.jsonl"); if (f.exists()) f.inputStream().use { it.copyTo(out) } }
  }
 }
}
