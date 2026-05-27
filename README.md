# Smart House System

Система управления умным домом с поддержкой устройств, датчиков и сценариев.  
Реализована полноценная JWT-аутентификация с ролевой моделью, логирование, отправка уведомлений в Telegram, экспорт отчётов, импорт сценариев из YAML-файлов, симуляция неисправностей датчиков.

---

## 🛠 Технологии

- **Java 17**
- **Spring Boot 3.5.x** (Web, Data JPA, Security, Validation, DevTools)
- **Spring Security + JWT** (токены в HttpOnly cookies, stateless)
- **PostgreSQL** (база данных)
- **Hibernate / JPA**
- **Swagger / OpenAPI** (документация API)
- **Telegram Bot API** (уведомления о событиях)
- **Maven** (сборка)
- **Git / GitHub**

---

## 🚀 Требования для запуска

- **Java 17** (JDK 17 или выше)
- **PostgreSQL 14** (или выше)
- **Maven** (можно использовать встроенный `mvnw`)
- **Git** (для клонирования)

---

🌐 API Endpoints
🔐 Аутентификация (/api/auth)
Метод	Путь	Описание
POST	/api/auth/register	Регистрация нового пользователя (тело: {"username":"user","password":"pass"})
POST	/api/auth/login	Логин, установка access_token и refresh_token в cookies
POST	/api/auth/refresh	Обновление access_token по refresh_token
POST	/api/auth/logout	Выход из системы (очистка cookies)
GET	/api/auth/info	Получение информации о текущем авторизованном пользователе
PUT	/api/auth/change_password	Смена пароля
🖥️ Устройства (/api/devices)
Метод	Путь	Описание
GET	/api/devices	Получить список всех устройств
GET	/api/devices/{id}	Получить устройство по ID
POST	/api/devices	Создать новое устройство (тело: {"name":"Лампа","type":"LAMP","isOn":true,"value":80})
PUT	/api/devices/{id}	Обновить существующее устройство
DELETE	/api/devices/{id}	Удалить устройство (требуется право device:delete)
🌡️ Датчики (/api/sensors)
Метод	Путь	Описание
GET	/api/sensors	Список всех датчиков
GET	/api/sensors/{id}	Получить датчик по ID
POST	/api/sensors	Добавить показания датчика (тело: {"type":"TEMP","value":22.5})
PUT	/api/sensors/{id}	Обновить данные датчика
DELETE	/api/sensors/{id}	Удалить датчик
POST	/api/sensors/simulate/{id}/fault	Симулировать неисправность датчика (параметр errorMessage). Генерируется CSV-отчёт с проблемой и решением.
📜 Сценарии (/api/scenarios)
Метод	Путь	Описание
GET	/api/scenarios	Список всех сценариев
GET	/api/scenarios/{id}	Получить сценарий по ID
POST	/api/scenarios	Создать сценарий (тело: {"name":"Утро","type":"AUTO","targetTemp":22,"targetLight":80,"turnOnMusic":true})
PUT	/api/scenarios/{id}	Обновить сценарий
DELETE	/api/scenarios/{id}	Удалить сценарий
POST	/api/scenarios/import	Импорт сценариев из YAML‑файла (формат multipart/form-data, поле file)
GET	/api/scenarios/import-url	Вернуть ссылку на HTML‑страницу для импорта (/upload.html)
📊 Отчёты (/api/reports)
Метод	Путь	Описание
GET	/api/reports/sensors/csv	Скачать CSV‑отчёт со всеми датчиками (ID, тип, значение, время, статус)
Также отчёты генерируются автоматически каждые 30 минут и сохраняются в папку reports/.

🧪 Тестовый контроллер (/api/test)
Метод	Путь	Описание
GET	/api/test/hello	Проверка работоспособности сервера (не требует аутентификации)
🗃️ Вспомогательные страницы
Страница	Описание
/upload.html	HTML‑форма для загрузки YAML‑файла сценариев
/swagger-ui.html	Документация Swagger (интерактивная)

---

ссылка на видео работы программы: https://disk.yandex.ru/i/kLyJGaqSmelFpQ
