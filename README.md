# Sympto-Track

## Configuración de la URL base de la API

La URL que utiliza la aplicación para conectarse con el servidor se define en `app/build.gradle.kts` a través de *product flavors*.

### Entornos disponibles

- **emulator**: usa la IP especial `10.0.2.2` que permite que el emulador acceda al servidor en tu máquina local.
- **device**: pensado para un dispositivo físico en la misma red local. Cambia la IP por la de tu ordenador dentro de la red.
- **production**: apunta al servidor en producción.

### Cambiar la URL

1. Abre `app/build.gradle.kts` y localiza la sección `productFlavors`.
2. Ajusta el valor de `BASE_URL` según el entorno que quieras modificar.
3. Compila la aplicación seleccionando el flavor correspondiente. Por ejemplo:
   - `./gradlew assembleEmulatorDebug`
   - `./gradlew assembleDeviceDebug`
   - `./gradlew assembleProductionRelease`

Cada flavor genera una clase `BuildConfig` con su propio `BASE_URL`, utilizado automáticamente por `ApiClient`.

