Remote->

WeatherRepositoryImpl
↓
WeatherRemoteDataSource
↓
WeatherApiService
↓
WeatherDto
↓
toDomain()
↓
Weather


local->

PreferencesRepositoryImpl
↓
PreferencesDataSource
↓
DataStore<Preferences>


Location
AndroidLocationRepository
↓
FusedLocationProviderClient
↓
android.location.Location
↓
Coordinates




                         DOMAIN
                            │
              ┌─────────────┼─────────────┐
              │             │             │
       WeatherRepository   Location    Preferences
              ↑           Repository    Repository
              │             ↑             ↑
              │             │             │
     WeatherRepositoryImpl  │   PreferencesRepositoryImpl
              │             │             │
              ↓             │             ↓
   WeatherRemoteDataSource  │    PreferencesDataSource
              │             │             │
              ↓             │             ↓
      WeatherApiService     │         DataStore
              │             │
              ↓             │
         WeatherDto         │
              ↓             │ 
           Mapper           │
              ↓             │
          Weather           │
                            │
                AndroidLocationRepository
                            │
                            ↓
              FusedLocationProviderClient
                            ↓
                     Android Location
                            ↓
                      Coordinates



Why doesn't `WeatherApiService` return `Weather` directly?
Answer: Because `Weather` belongs to the Domain layer,
whereas the API has its own structure; the API response must be converted into a DTO,
and the DTO is then converted into the Domain Model via a Mapper.

Why doesn't the Repository interact directly with `WeatherApiService`?
Answer: To decouple the Repository from network implementation details and
to place a `DataSource` layer between the Repository and the API.

Why doesn't `AndroidLocationRepository` pass the Android `Location` object directly to the Domain layer?

Answer: Because the Domain layer should not depend on the Android SDK;
the Android `Location` object is converted into domain-specific `Coordinates` within the Data layer.

What is the purpose of `suspendCancellableCoroutine` here?
Answer: To convert a callback-based Android API into a `suspend` function compatible with Coroutines,
while preserving cancellation support.

What does the Mapper do?
Answer: It acts as the boundary between the Data and Domain layers,
converting the external data structure/representation into the model required by the Domain.