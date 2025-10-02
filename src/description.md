### Прогноз погоды

* Получение прогноза погоды на 7 дней по выбранным городам.
* Сохранение «избранных городов» пользователя.
* Автоматическое обновление прогноза (scheduler).
* JWT-аутентификация, роли: `USER` (свои города), `ADMIN` (статистика).
* API: [Open-Meteo](https://open-meteo.com/).

Для хранения данных о пользователе используем класс User
-id
- name
- email
- hashPassword
- role
- createDate
- updateDate
- List<City> - favoriteCities

enum Role: ADMIN, USER


Для хранения данных о стране используем класс Country - (only for ADMIN)
-id
-name - уникальный

Для хранения данных о городе используем класс Location
-id
-name - уникальный
-Сountry
-latitude
-longitude


Для хранения данных о прогнозе погоды в городе используем класс Forecast
-id
-City
-forecastDate       // нужно ли ?????
-tempMax
-tempMin
-precipitation
-updateDate         если погода обновлялась, например, не сегодня, то запрашиваем из внешнего API. 
                    Иначе берем из БД

Для хранения данных о текущей погоде используем класс WeatherNow
-id
-City
-tempCurrent
-precipitation
-updateDateTime     // если погода обновлялась, например, ранее, чем 1 час назад, то запрашиваем из внешнего API
Иначе берем из БД


Новый пользователь проходит процедуру регистрации, вводит все свои данные.
Если данные корректны, то мы:
- регистрируем пользователя (то есть заносим его данные в БД)


---------------------- API DESCRIPTION ------------------
полный перечень запросов, которые мы готовы принимать и обрабатывать (список методов контроллеров)

UserController
// добавить нового пользователя
public UserResponseDto addNewUser(UserRequestDto request);

// найти всех пользователей (для ADMIN)
public List<UserResponseDto> findAllFullDetails();

// найти пользователя по ID
public UserResponseDto findUserById(Integer id);

// найти пользователя по email
public UserResponseDto findUserByEmail(String email);

// обновить данные от имени пользователя (пользователь хочет
// поменять какие-то данные в своем профиле)
public UserResponseDto updateUser(UserUpdateRequestDto request);

// удаление User
public UserResponseDto deleteUser(Long id);

-------------------------------------------------------------

CityController
// добавить новый город - (для ADMIN) 
public CytyResponseDto addNewCity(CityRequestDto request);

//Обновить город ( для ADMIN)
public CityResponseDto updateCity(CityRequestDto request);

// найти все города
public List<CityResponseDto> findAllCities();

// найти города по части имени
public List<CityResponseDto> findCityByName(String cityName);

// найти город по ID
public CityResponseDto findCityById(Long cityId);

// добавить город в избранные (мах - 10)
public List<CityResponseDto> addFavoriteCity(Long cityId);

// удалить город из избранных по ID
public List<CityResponseDto> deleteFavoriteCity(Long cityId);

-----------------------------------------------------------

CountryController
// добавить новую страну - (для ADMIN)
public CountryResponseDto addNewCountry(CountryRequestDto request);

// удалить Country by ID
public CountryResponseDto deleteCountry(Long countryId);

// найти Country по имени
public CountryResponseDto findCountryByName(String countryName);

// получить список Countries
public List<CountryResponseDto> findAllCountries();

// обновить Country by ID
public CountryResponseDto updateCountry(CountryRequestDto request);

// список городов по стране
public List<CityResponseDto> findCityByCountry(Long countryId);

-------------------------------------------------------------------------

WeatherForecastController
// получить прогноз по городу на 7 дней
public List<ForecastResponseDto> getForecastByCity(Long cityId);

// получить прогноз по городу на 1 день из диапазона ближайших 7 дней
public ForecastResponseDto getForecastByCity(Long cityId, Date date); //проверка, что дата в допустимом диапазоне

// получить текущую погоду по городу на сейчас
public WeatherNowResponseDto getWeatherNowByCity(Long cityId);

// получить теущую погоду по текущему местоположению пользователя  ---  реализовать получение примерного расположения пользователя
public WeatherNowResponseDto getWeatherNowByThisLocation();



