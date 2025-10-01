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


Для хранения данных о погоде используем класс Forecast
-id
-City
-forecastDate
-tempMax
-tempMin
-tempCurrent
-rainSum
-updateDate


Новый пользователь проходит процедуру регистрации, вводит все свои данные.
Если данные кооректны, то мы:
- регистрируем пользователя (то есть заносим его данные в БД)


---------------------- API DESCRIPTION ------------------
полный перечень запросов, которые мы готовы принимать и обрабатывать (список методов контроллеров)

UserController
// добавить нового пользователя
public UserResponseDto addNewUser(UserRequestDto request);

// найти всех пользователей (для ADMIN)
public List<User> findAllFullDetails();

// найти пользователя по ID
public UserResponseDto findUserById(Integer id);

// найти пользователя по email
public UserResponseDto findUserByEmail(String email);

// обновить данные от имени пользователя (пользователь хочет
// поменять какие-то данные в своем профиле)

метод put/post с использованием dto
когда мы получаем такой запрос то из тела запроса данные попадают в
UserUpdateRequestDto и ЕСЛИ в этом объекте КАКОЕ-ТО поле присутствует
и его значение удовлетворяет требованиям валидации, то мы должны
заменить в нашем пользователе значение этого поля на новое

public UserResponseDto updateUser(UserUpdateRequestDto request);

// удаление записи
public boolean deleteUser(Integer id);



CityController
// добавить новый город - (для ADMIN) 
public UserResponseDto addNewUser(UserRequestDto request);

// получить прогноз по городу на 7 дней

// получить прогноз по городу на 1 день из диапазона ближайших 7 дней

// получить текущую погоду по городу на сейчас

// добавить город в избранные (мах - 10)

// удалить город из избранных по ID

// найти город по имени

// список городов по стране

CountryController
// добавить новую страну - (для ADMIN)
public CountryResponseDto addNewCountry(CountryRequestDto request);

// удалить Country

// найти Country по имени

// получить список Countries




