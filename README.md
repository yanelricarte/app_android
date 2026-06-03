# GymDemo 📱

Aplicación Android (Kotlin) de ejemplo para la gestión de un gimnasio. Consume una API REST mediante Retrofit y muestra los datos de los socios y su asistencia, con navegación entre secciones por medio de una barra inferior.

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-48B983)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

## Características

- Consumo de una **API REST** de gimnasio con **Retrofit**.
- Pantalla de **datos del socio** y pantalla de **asistencia**, como fragments.
- Navegación con **bottom navigation**.

## Arquitectura (MVVM)

La app separa responsabilidades en capas:

```
data/
  dto/          Modelos de datos (Socio, EstadoSocio, Asistencia) con @SerializedName
  remote/       GymApi (interface Retrofit) + RetrofitClient
  repository/   GymRepository: aísla a la UI de la red
ui/
  UiState        sealed (Idle / Loading / Success / Error)
  *ViewModel     exponen StateFlow<UiState<...>>; sobreviven a rotación
  *Fragment      solo observan el StateFlow y pintan
```

Los Fragments no hacen llamadas de red: delegan en el ViewModel, que usa el Repository y publica el estado vía `StateFlow`. Así el estado sobrevive a cambios de configuración (rotación) y la lógica es testeable.

## Tecnologías

- **Lenguaje:** Kotlin · coroutines + `StateFlow`
- **Red:** Retrofit + Gson + OkHttp
- **UI:** Android Views + ViewBinding + Fragments + Bottom Navigation
- **Tests:** JUnit + MockWebServer

## API que consume

Requiere un backend corriendo (en emulador, `http://10.0.2.2/api/` apunta al `localhost` de la PC). Endpoints:

| Método | Endpoint | Respuesta |
|--------|----------|-----------|
| `GET`  | `/clientes` | `{ "items": [ { id, nombre, membresia_vence, activo } ] }` |
| `GET`  | `/clientes/estado?dni=...` | `{ dni, nombre, vence, dias_restantes, activa }` |
| `POST` | `/asistencia` (body `{ "dni": "..." }`) | `{ ok, dni, momento }` · 402 si membresía vencida, 404 si no existe |
| `GET`  | `/asistencias?dni=...&page=&size=` | `{ page, size, total, items: [ { id, dni, momento } ] }` |

## Cómo ejecutar

1. Abrir el proyecto en **Android Studio**.
2. La URL base se define por `buildType` en `app/build.gradle.kts` (`BASE_URL` vía `BuildConfig`), no hay que editar código.
3. Ejecutar en un emulador o dispositivo (Android 8.0+).

## Tests

```bash
./gradlew testDebugUnitTest
```

## Licencia

Distribuido bajo licencia MIT. Ver [`LICENSE`](LICENSE).
