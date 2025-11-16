# Доступные операции

---

Если остались какие-либо вопросы можете написать мне в лс (@kaedeze)

---

### 1. Бронирование рейса клиентом

Метод: `bookFlight(flight: Flights)`

Описание: Добавляет указанный рейс в список активных рейсов клиента (`activeFlights`)

Пример:
```kotlin
val flight = SheduledFlights("SU123", plane1)
client1.bookFlight(flight)
```

### 2. Список активных рейсов у клиента

Свойство: `activeFlights`

Описание: Содержит все рейсы, которые клиент забронировал и которые сейчас активны

Пример:

```kotlin
val flightsList = client1.activeFlights
for (f in flightsList) {
    println(f.flight_international_code)
}
```

### 3. Начисление баллов клиенту за рейс

Метод: `addLoyaltyPoints(flight: SheduledFlights)`

Описание: Начисляет баллы клиенту, используя коэффициент рейса (`loyalty_points_coef`)

Пример:

```kotlin
client1.addLoyaltyPoints(flight)
```

### 4. Получение не приватной информации о клиенте

Метод: `getInfo()`

Описание: Выводит имя, фамилию, пол, возраст и количество активных рейсов

Пример:

```kotlin
println(client1.getInfo())
```

### 5. Получение приватной информации о клиенте

Метод: `getPrivateInfo()`

Описание: Возвращает паспортные данные, дату регистрации, дату рождения, страну рождения и адрес

Пример:

```kotlin
println(client1.getPrivateInfo())
```

### 6. Возможность создания частных (персональных) рейсов

Метод: `createPersonalFlight(...)`

Описание: Создает персональный рейс для VIP-клиента с назначением пилотов и стюардесс

Пример:

```kotlin
val vipFlight = vipClient.createPersonalFlight(
    "VIP001",
    LocalDateTime.now().plusDays(1),
    LocalDateTime.now().plusDays(1).plusHours(2),
    "PilotA", "PilotB", "Stewardess1", "Stewardess2"
)
vipClient.bookFlight(vipFlight)
```

### 7. Возможность создания регулярного рейса

Класс: `SheduledFlights`

Описание: Создает обычный регулярный рейс с указанием всех характеристик (код, время, самолет, международный/внутренний)

Пример:

```kotlin
val regularFlight = SheduledFlights(
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

Конструктор: `SheduledFlights(flightCode: String, airplane: Airplane)`

Описание: Создает рейс с дефолтными значениями времени, номера рейса и других параметров — удобно для тестирования и шаблонных полетов

Пример:

```kotlin
val templateFlight = SheduledFlights("TMP001", plane1)
```