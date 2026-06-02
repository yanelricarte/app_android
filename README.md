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

## Tecnologías

- **Lenguaje:** Kotlin
- **Red:** Retrofit
- **UI:** Android Views + Fragments + Bottom Navigation

## Estructura

```
app/src/main/java/ar/edu/gymdemo/
├── MainActivity.kt
├── data/
│   ├── dto/          # Modelos (Socio, AsistenciaDatos)
│   └── remote/       # GymApi + RetrofitClient
└── ui/               # DatosFragment, AsistenciaFragment
```

## Cómo ejecutar

1. Abrir el proyecto en **Android Studio**.
2. Configurar la URL base de la API en `data/remote/RetrofitClient.kt`.
3. Ejecutar en un emulador o dispositivo (Android 8.0+).

## Licencia

Distribuido bajo licencia MIT. Ver [`LICENSE`](LICENSE).
