### Прогноз погоды

* Получение прогноза погоды на 7 дней по выбранным городам.
* Сохранение «избранных городов» пользователя.
* Автоматическое обновление прогноза (scheduler).
* JWT-аутентификация, роли: `USER` (свои города), `ADMIN` (статистика).
* API: (https://www.weatherbit.io/).

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


Для хранения данных о городе используем класс City
-id
-name - уникальный
-latitude
-longitude


Для хранения данных о прогнозе погоды в городе используем класс Forecast
-id
-City
-forecastDate  
-tempMax
-tempMin
-precipitation -- осадки суммарно
-updateDate         если погода обновлялась, например, не сегодня, то запрашиваем из внешнего API. 
                    Иначе берем из БД

Для хранения данных о текущей погоде используем класс WeatherNow --- by the our request
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

// найти города по части имени
public List<CityResponseDto> findCityByNameContains(String cityName);

// добавить город в избранные (мах - 10)
public List<CityResponseDto> addCityToFavorite(String cityName);

// удалить город из избранных 
public List<CityResponseDto> deleteCityFromFavorite(String cityName);

------------------------------------------------------------------------

ForecastController
// получить прогноз по городу на 7 дней
public List<ForecastResponseDto> getForecastByCityName(String cityName);

--- not now ---------// получить текущую погоду по городу на сейчас
--- not now ---------public WeatherNowResponseDto getWeatherNowByCity(Long cityId);

--- not now ---------// получить теущую погоду по текущему местоположению пользователя  ---  реализовать получение примерного расположения пользователя
--- not now ---------public WeatherNowResponseDto getWeatherNowByThisLocation();

------------------------------------------------------------------------
statisticController

//получить список пользователей
public List<UserResponseDto> getAllUsers();

//получить список городов, добавленных и избранное всеми пользователями
public List<CityResponseDto> getAllCitiesInFavorites();

//получить список городов, добавленных и избранное конкретным пользователем
public List<CityResponseDto> getAllCitiesInFavoriteByUserId(Long userId);

//самый холодный город на сегодня
public CityResponseDto getColdestCity();

//самый теплый город на сегодня
public CityResponseDto getWarmestCity();

// город с максимумом осадков нам сегодня
public CityResponseDto getCityWithMaxPrecipitation();
