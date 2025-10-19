# ProyectoBJApp

ProyectoBJApp es una aplicación Android escrita en Kotlin. Este repositorio contiene el código fuente, configuración de Gradle y recursos necesarios para construir y ejecutar la aplicación en un emulador o dispositivo Android.

## Tecnologías
- Lenguaje: Kotlin
- Plataforma: Android (Gradle)
- Build: Gradle Wrapper (./gradlew)
- IDE recomendado: Android Studio

## Requisitos
- JDK 11 o superior
- Android Studio (recomendado) o herramientas de build de Android
- Android SDK y componentes relacionados (configurables desde Android Studio)
- Dispositivo Android o emulador configurado

## Instalación y ejecución

1. Clona el repositorio:
   git clone https://github.com/l0l3eth/ProyectoBJApp.git

2. Abre el proyecto en Android Studio:
   - File → Open → selecciona la carpeta del proyecto
   - Deja que Android Studio sincronice y descargue dependencias

3. Construir desde la línea de comandos:
   - En macOS/Linux:
     ./gradlew assembleDebug
   - En Windows:
     gradlew.bat assembleDebug

4. Instalar en un dispositivo/emulador conectado:
   - ./gradlew installDebug

5. Ejecutar pruebas (si existen):
   - Pruebas unitarias: ./gradlew test
   - Pruebas instrumentadas en dispositivo: ./gradlew connectedAndroidTest

## Estructura del proyecto (resumen)
- app/
  - src/main/java/  -> Código Kotlin
  - src/main/res/   -> Recursos (layouts, drawables, values)
  - src/androidTest/ -> Pruebas instrumentadas
  - src/test/        -> Pruebas unitarias
- build.gradle (proyecto y módulos)
- gradle/wrapper/   -> Wrapper de Gradle

## Buenas prácticas y estilo
- Sigue las guías oficiales de Kotlin y Android (nombres de paquetes, arquitectura clara, separación de responsabilidades).
- Añade linters y formateadores (Detekt, ktlint, etc.) si aún no están configurados.
- Escribe pruebas unitarias y de integración para las partes críticas de la app.

## Contacto
Repositorio: https://github.com/l0l3eth/ProyectoBJApp  
