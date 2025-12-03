# BLOCKROTT
> Aplicacion mobil de interferencia de aplicaciones

#  Instalacion
Sigue estos pasos para tener una copia local y funcionando:

### Precaución
Tener instalado Android Studio, para ejecutar la demo del programa, ya que facilita la navegacion dentro de las carpetas, ademas que permite la ejecucion del emulador

### Pasos para dentro de Android Studio
1.  Clona el repositorio
2.  Abrir el proyecto dentro de Android Studio
3.  Ejecutar "Sync Now" Gradle dentro del proyecto.

# Backend
1. Ejecutar el main de springboot usando el boton run de la IDE 

# EMULACION
Ejecutar el emulador dentro de  Android Studio o por otro lado ir a el archivo "MainActivity.kt"
com.example.blockrott.frontend.ui
1. clic derecho
2. seleccionar Run 'MainActivity'

# GENERACION DE APK
1. Main menu (3 barras, esquina superior izquiera)
2. Build / Generated App bundles or APKs / Generated APKs
3. Revisar las notificaciones y encontrar "Generate APKs"
4. Dentro del mensaje hacer clic en "locate", para acceder a la ubicacion del archivo
5. Esto abra generado un archivo APK

# Inicializacion de pruebas
1. Abrir una terminal
2. Ejecutar ./gradlew connectedAndroidTest
3. Esperar ejecucion de los tests

# Como funciona la aplicacion
1. Al abrir la aplicacion por primera vez se le tendra que otorgar los permisos para sobreponerse en segundo plano, Y el permiso para leer datos de uso de aplicaciones
2. Para bloquear todas las aplicaciones se tendra que apretar el boton del centro
3. Para ver las estadisticas apretar el boton de abajo
