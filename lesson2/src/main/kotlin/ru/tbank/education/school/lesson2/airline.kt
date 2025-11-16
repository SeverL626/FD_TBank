package ru.tbank.education.school.lesson2
import java.time.*

// Объяснение структуры в Structure.md
// Перечень доступных функций в Functions.md

abstract class Person(
    protected val firstname: String,
    protected val surname: String,
    val age: Int,
    val sex: String,
    private val passport_id: String,
    private val registration_date: String,
    private val birth_date: String,
    private val country_of_birth: String,
    protected val adress: String
) {
    abstract fun getInfo(): String
    fun getPrivateInfo(): String {
        return "Passport: $passport_id, Registration date: $registration_date, Birth date: $birth_date, Country: $country_of_birth, Address: $adress"
    }
}

open class Client(
    firstname: String,
    surname: String,
    age: Int,
    sex: String,
    passport_id: String,
    registration_date: String,
    birth_date: String,
    country_of_birth: String,
    adress: String,
    var children: Int,
    var spouse: Boolean
) : Person(firstname, surname, age, sex, passport_id, registration_date, birth_date, country_of_birth, adress) {

    val activeFlights: MutableList<Flights> = mutableListOf()

    fun bookFlight(flight: Flights) {
        activeFlights.add(flight)
        println("$firstname $surname booked flight ${flight.flight_international_code}")
    }

    fun addLoyaltyPoints(flight: SheduledFlights) {
        val points = (100 * flight.loyalty_points_coef).toInt()
        println("$firstname $surname got $points points for flight ${flight.flight_international_code}")
    }

    override fun getInfo(): String {
        return "$firstname $surname, sex $sex, age $age, active flights: ${activeFlights.size}"
    }
}

class CommonClients(
    firstname: String,
    surname: String,
    age: Int,
    sex: String,
    passport_id: String,
    registration_date: String,
    birth_date: String,
    country_of_birth: String,
    adress: String,
    children: Int,
    spouse: Boolean,
    var service_class: String,
    var loayal_points: Int,
    var last_flight: LocalDateTime,
    var first_flight: LocalDateTime,
    var number_of_flights: Int
) : Client(firstname, surname, age, sex, passport_id, registration_date, birth_date, country_of_birth, adress, children, spouse)

class VIPClient(
    firstname: String,
    surname: String,
    age: Int,
    sex: String,
    passport_id: String,
    registration_date: String,
    birth_date: String,
    country_of_birth: String,
    adress: String,
    children: Int,
    spouse: Boolean,
    var vip_category: String,
    val vip_reason: String,
    var rest_room_number: Int,
    val plane_type: String,
    var personal_plane: Airplane
) : Client(firstname, surname, age, sex, passport_id, registration_date, birth_date, country_of_birth, adress, children, spouse) {

    fun createPersonalFlight(
        flightCode: String,
        departure: LocalDateTime,
        arrival: LocalDateTime,
        pilot1: String,
        pilot2: String,
        stewardess1: String,
        stewardess2: String
    ): PersonalFlights {
        return PersonalFlights(
            flightCode,
            departure,
            arrival,
            personal_plane,
            pilot1,
            pilot2,
            stewardess1,
            stewardess2
        )
    }
}

data class Airplane(
    val plane_number: String,
    val model: String,
    val manufacturer: String,
    val seats: Int,
    val production_year: Int,
    val range_km: Int,
    val max_takeoff_weight: Int,
    val engines: Int,
    val engine_type: String,
    val wifi: Boolean,
    val need_repair: Boolean
)

sealed class Flights(
    val flight_international_code: String,
    val departure: LocalDateTime,
    val arrival: LocalDateTime,
    val airplane: Airplane
)

class SheduledFlights(
    flight_international_code: String,
    val flight_number: String,
    departure: LocalDateTime,
    arrival: LocalDateTime,
    airplane: Airplane,
    val international: Boolean,
    var loyalty_points_coef: Double = 1.0,
    var demand: Double = 1.0,
    val creation_date: LocalDateTime,
    var any_technical_problems: Boolean = false,
    var any_passengers_problems: Boolean = false,
    var any_staff_problems: Boolean = false,
    var any_weather_problems: Boolean = false,
    var any_authority_problems: Boolean = false,
    var cancelled: Boolean = false
) : Flights(flight_international_code, departure, arrival, airplane) {

    constructor(flightCode: String, airplane: Airplane) : this (
        flightCode,
        "TMP001",
        LocalDateTime.now(),
        LocalDateTime.now().plusHours(3),
        airplane,
        false,
        1.0,
        1.0,
        LocalDateTime.now()
    )
}

class PersonalFlights(
    flight_international_code: String,
    departure: LocalDateTime,
    arrival: LocalDateTime,
    airplane: Airplane,
    var personal_pilot_1: String,
    var personal_pilot_2: String,
    var personal_stewardess_1: String,
    var personal_stewardess_2: String
) : Flights(flight_international_code, departure, arrival, airplane)

fun main() {


    // Подробнее см. Functions.md


    // Создаём самолёты
    val plane1 = Airplane("A001", "Boeing 737", "Boeing", 180, 2015, 5000, 79000, 2, "Turbofan", true, false)
    val plane2 = Airplane("VIP01", "SF50", "Cirrus", 5, 2020, 1790, 1000, 1, "Turbofan", false, false)

    // Создаём клиентов (обычный)
    val client1 = CommonClients(
        "Ivan", "Ivanov", 30, "M", "P12345", "2020-01-01", "1990-06-12",
        "Russia", "Moscow", 1, true, "Economy", 1200,
        LocalDateTime.now().minusMonths(1),
        LocalDateTime.now().minusYears(5),
        25
    )

    // Создаём клиентов (VIP)
    val vipClient = VIPClient(
        "Anna", "Petrova", 20, "F", "P54321", "2015-03-10", "2000-09-20",
        "Russia", "Moscow", 0, true, "S++", "A DME Director's child",
        101, "PrivateJet", plane2
    )

    // Создание регулярного рейса (через дополнительный конструктор)
    val flight1 = SheduledFlights("SU123", plane1)

    // Бронирование регулярного рейса обычным клиентом
    client1.bookFlight(flight1) // Бронирование
    client1.addLoyaltyPoints(flight1) // Баллы лояльности
    println(client1.getInfo())
    println(client1.getPrivateInfo())

    // Создание частного рейса
    val vipFlight = vipClient.createPersonalFlight(
        "VIP001",
        LocalDateTime.now().plusDays(1),
        LocalDateTime.now().plusDays(1).plusHours(2),
        "PilotA", "PilotB", "Stewardess1", "Stewardess2"
    )

    // Назначение частного рейса на VIP-клиента
    vipClient.bookFlight(vipFlight)
    println(vipClient.getInfo())
    println(vipClient.getPrivateInfo())
}