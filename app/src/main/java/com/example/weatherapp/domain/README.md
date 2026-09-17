DOMAIN
│
├── model
│   ├── Coordinates
│   ├── CurrentWeather
│   ├── Location
│   └── Weather
│
├── repository
│   ├── LocationRepository
│   ├── PreferencesRepository
│   └── WeatherRepository
│
├── usecase
│   ├── GetCurrentLocationUseCase
│   ├── GetCurrentWeatherUseCase
│   ├── ObserveSavedQueryUseCase
│   └── SaveQueryUseCase
│
└── location
└── LocationDisabledException



Requirement
↓
Domain Model
↓
Repository Contract
↓
UseCase
↓
Data Implementation
↓
Presentation


What is a Model?
A model that represents the actual concept of the application.
What is a Repository?
A contract defined by the Domain for data access.
Why is the Repository an interface?
To decouple the Domain from the implementation.
What is a UseCase?
A specific operation from the perspective of the application or domain.
Why use `operator fun invoke`?
To allow the UseCase to be called like a function.
Why shouldn't the Domain know about Retrofit?
Because Retrofit is an implementation detail, and the Domain must remain independent of it.
What is the difference between Coordinates and Location?
The former refers only to coordinates; the latter encompasses complete location information.
What is the difference between a Permission issue and Location Disabled?
Permission relates to the app's access authorization; "Location Disabled" means the device's location service is turned off.