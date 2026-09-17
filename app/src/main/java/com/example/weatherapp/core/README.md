core
│
├── network
│ └── NetworkConfig
│
└── di
├── NetworkModule
│ │
│ ├── Retrofit
│ └── WeatherApiService
│
└── DataModule
│
├── WeatherRepository
│ ↑
│ WeatherRepositoryImpl
│
├── LocationRepository
│ ↑
│ AndroidLocationRepository
│
├── PreferencesRepository
│ ↑
│ PreferencesRepositoryImpl
│
└── DataSources



WHEN APP RUN------->

User
↓
Compose UI
↓
ViewModel
↓
GetCurrentWeatherUseCase
↓
WeatherRepository
↓
WeatherRepositoryImpl
↓
WeatherRemoteDataSource
↓
WeatherRemoteDataSourceImpl
↓
WeatherApiService
↓
Retrofit
↓
WeatherAPI



AND RETURN------->

WeatherAPI
↓
JSON
↓
Retrofit Converter
↓
WeatherDto
↓
Mapper
↓
Weather
↓
Repository
↓
UseCase
↓
ViewModel
↓
StateFlow
↓
Compose