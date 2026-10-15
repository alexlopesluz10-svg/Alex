# Notification Forensics (protótipo, não compilado)
Aplicativo Android Kotlin para registrar metadados de notificações relacionadas ao Spotify e aos serviços de anúncios Xiaomi. Sem acesso à rede, sem root e sem telemetria. Dados permanecem no armazenamento privado do app até exportação manual.

## Build
Abra o projeto no Android Studio com Android SDK 35 e JDK 17; execute `gradle assembleDebug` (ou a ação Build APK no Android Studio). O projeto ainda NÃO foi compilado nem testado em dispositivo real.

## Uso
Instale o APK gerado, abra o app, toque em "Autorizar acesso às notificações", habilite o serviço, aguarde a notificação e exporte o arquivo JSONL. Ajuste restrições de bateria/autostart do HyperOS conforme necessário.

## Limitações
O serviço registra apenas as notificações visíveis à API NotificationListenerService após autorização. Não intercepta URL aberta, conteúdo de tráfego, PendingIntent nem prova autoria da campanha. `package` e `opPkg` ajudam na análise, mas podem refletir delegação ou publicação em nome de outro pacote. O app registra texto de notificações filtradas; revise o arquivo antes de compartilhar.
