# 🔄 Automatización de Compilación y Publicación: LEXICO PRO

Este repositorio cuenta con un flujo de trabajo continuo mediante **GitHub Actions** que compila, verifica y publica automáticamente un paquete APK instalable de Android cada vez que se sincroniza código desde Google AI Studio hacia la rama principal de GitHub.

---

## 🗺️ Diagrama del Flujo de Trabajo

```text
Google AI Studio
       ↓ (Push / Sincronización al repositorio)
    GitHub
       ↓ (Trigger: Push en main/master)
GitHub Actions
       ↓ (Ubuntu Runner + JDK 17 + Gradle 9.3)
Verificación y Pruebas (:app:testDebugUnitTest)
       ↓
Compilación Android (:app:assembleDebug / :app:assembleRelease)
       ↓
Generación y Renombrado del APK (Lexico-Pro-v{major.minor.build}.apk)
       ↓
Guardado de Artifact (Upload Artifact v4)
       ↓
Creación de GitHub Release con notas detalladas y APK adjunto
```

---

## ⚙️ Características del Flujo

1. **Detección Automática**: Se dispara con cada commit enviado a `main` o `master`, además de admitir ejecución manual (`workflow_dispatch`).
2. **Entorno Compatible**:
   - **Sistema Operativo:** Ubuntu Latest.
   - **Java:** JDK 17 (Eclipse Temurin), compatible con AGP 9.x y Kotlin 2.2.
   - **Gradle:** Configurado con Gradle Wrapper 9.3 y cache inteligente de dependencias.
3. **Pruebas Automatizadas**:
   - Ejecuta `./gradlew testDebugUnitTest` para asegurar que las reglas lexicográficas, mappers y componentes pasen todas las validaciones antes de empaquetar.
4. **Versionado Automático**:
   - Extrae el `versionName` definido en `app/build.gradle.kts` (por ejemplo `1.0`).
   - Añade el número de ejecución único de GitHub (`github.run_number`) para generar números semánticos correlativos (`1.0.1`, `1.0.2`, `1.0.3`...).
   - Asigna un tag único `v1.0.X` para evitar sobrescritura de versiones anteriores.
5. **Generación del APK**:
   - Por defecto compila `assembleDebug`, lo cual permite probar e instalar la aplicación de inmediato sin requerir configuración previa de certificados.
   - El archivo se renombra con la convención requerida: `Lexico-Pro-vVERSION.apk`.
6. **Artifact y Release**:
   - Sube el APK a los Artifacts del flujo de trabajo (disponible por 30 días).
   - Genera una **GitHub Release** con título `Lexico Pro vVERSION`, adjuntando el APK para descarga directa.
   - Incluye notas de la versión con commit, rama, fecha, estado de pruebas e instrucciones de instalación en Android.

---

## 🔐 Configuración de Firma de Producción (Opcional)

Para generar versiones firmadas para producción (`assembleRelease`), el workflow está preparado para leer los siguientes secretos en **GitHub Settings > Secrets and variables > Actions**:

- `ANDROID_KEYSTORE_BASE64`: Archivo `.jks` codificado en base64 (`base64 -w 0 mi-keystore.jks`).
- `ANDROID_KEYSTORE_PASSWORD`: Contraseña del almacén de claves.
- `ANDROID_KEY_ALIAS`: Alias de la clave de firma.
- `ANDROID_KEY_PASSWORD`: Contraseña del alias de la clave.

Si estos secretos no están configurados, el workflow automáticamente compilará en modo Debug garantizando que el proceso nunca falle.
