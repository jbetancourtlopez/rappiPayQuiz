## Movie App RappiPay
App demo que consume la api de peliculas (themoviedb.org) la cual presenta las peliculas en categorias de popular y top-rate, ademas de poder hacer una busqueda por nombre de pelicula y ver el detalle de cada pelicula.

<p align="center">
<img style="display: inline-block;" src='https://i.imgur.com/sEX3E0V.jpg' width='33%'/>
<img style="display: inline-block;"  src='https://i.imgur.com/ra69egC.jpg' width='33%'/>
<img src='https://i.imgur.com/rdg504n.jpg' width='33%'/>
</p>


## Puntos
* OK - 100% Kotlin
* OK - MVVM
* OK - Películas y/o Series categorizadas por Popular y Top Rated.
* OK - Detalle de Película y/o Serie.
* OK - Buscador de Películas y/o Series por nombre.
* OK - Visualización de Videos en el detalle.
* OK - La App debe poder funcionar offline.
* OK - Pruebas Unitarias.
* OK - Transiciones/Animaciones.


## Mejoras
* Injeccion de dependencias con Hilt
* Mejorar el diseño

## Librerias
* ViewModel
* Retrofit
* Data Binding 
* LiveData
* Corrutinas
* Room
* Piccaso
* SwipeFreshLayout
* Androidyoutubeplayer
* Mockk para pruebas unitarias

## Arquitectura
<img src="https://i.imgur.com/VdTYK0P.jpg" title="source: imgur.com" />

## Distribución de carpetas

```
├── data                     # Capa de datos
│   ├── local                # Persistencia de datos con Room
|   │   ├── dao              # Acceso a datos
|   |   ├── entities         # Entidades
|   |   ├── mappers          # Transformacion de entidades a modelos del dominio
│   ├── remote               # Datos remotos
|   |   ├── api              # Retrofit e Interfaces de retrofit
|   |   ├── interceptor      # Interceptores
|   |   ├── mapper           # Transformacion de modelos a modelos del dominio
|   |   ├── model            # Modelos de remote
|   |   ├── service          # Invocacion de los servicios de retrofit
│   └── repo                 # Repositorios
|
├── domain                   # Capa de dominio
│   ├── interator            # Casos de uso
│   ├── model                # Modelos 
|
├── ui                       # Capa de Presentacion Activity / Fragment
│   ├── adapters             # Adapters
│   ├── home                 # Pantalla de inicio de listado de peliculas
│   └── detailMovie          # Pantala de detalle de pelicula
|
├── core                     # Utilidades y Extenciones
├── RappiPatQuizApp          # Application


```



