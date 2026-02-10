# Доступные операции

---

Если остались какие-либо вопросы можете написать мне в лс (@kaedeze)

Все примеры также есть в ```fun main()``` airline.kt

---

### 1. Бронирование рейса клиентом

Синтаксис: `bookFlight(flight: Flights)`

Описание: Добавляет указанный рейс в список активных рейсов клиента (`activeFlights`)

Пример:
```kotlin
val flight = SheduledFlights("UA123G3", plane1)
```

### 2. Список активных рейсов у клиента

Синтаксис: `activeFlights`

Описание: Содержит все рейсы, которые клиент забронировал и которые сейчас активны

Пример:

```kotlin
f.flight_international_code
```

### 3. Начисление баллов клиенту за рейс

Синтаксис: `addLoyaltyPoints(flight: SheduledFlights)`

Описание: Начисляет баллы клиенту, используя коэффициент рейса (`loyalty_points_coef`)

Пример:

```kotlin
client1.addLoyaltyPoints(flight)
```

### 4. Получение не приватной информации о клиенте

Синтаксис: `getInfo()`

Описание: Выводит имя, фамилию, пол, возраст и количество активных рейсов

Пример:

```kotlin
client1.getInfo()
```

### 5. Получение приватной информации о клиенте

Синтаксис: `getPrivateInfo()`

Описание: Возвращает паспортные данные, дату регистрации, дату рождения, страну рождения и адрес

Пример:

```kotlin
client1.getPrivateInfo()
```

### 6. Возможность создания частных (персональных) рейсов

Синтаксис: `createPersonalFlight(...)`

Описание: Создает персональный рейс для VIP-клиента с назначением пилотов и стюардесс

Пример:

```kotlin
val vip_flight = vipClient.createPersonalFlight(
    "VIP001",
    LocalDateTime.now().plusDays(1),
    LocalDateTime.now().plusDays(1).plusHours(2),
    "PilotA", "PilotB", "Stewardess1", "Stewardess2"
)
```

### 7. Возможность создания регулярного рейса

Синтаксис: `SheduledFlights(...)`

Описание: Создает обычный регулярный рейс с указанием всех характеристик (код, время, самолет, международный/внутренний)

Пример:

```kotlin
val regular_flight = SheduledFlights(
    "SU123",
    "SU001",
    LocalDateTime.now(),
    LocalDateTime.now().plusHours(3),
    plane1,
    true,
    1.0,
    1.5,
    LocalDateTime.now()
)
```

### 8. Возможность инициализации регулярного рейса по шаблону

Синтаксис: `SheduledFlights(flightCode: String, airplane: Airplane)`

Описание: Создает рейс с дефолтными значениями времени, номера рейса и других параметров (подходит для многократного создания одинаковых рейсов)

Пример:

```kotlin
SheduledFlights("TMP001", plane1)
```