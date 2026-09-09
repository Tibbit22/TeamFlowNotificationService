# TeamFlow Notification Service

Микросервис уведомлений для проекта **TeamFlow**.

Сервис получает события о регистрации пользователей через **Apache Kafka** и обрабатывает их для отправки уведомлений.

## Архитектура

```text
                    ┌─────────────────────┐
                    │      TeamFlow       │
                    │                     │
                    │  User registration  │
                    └──────────┬──────────┘
                               │
                               │ UserRegisteredEvent
                               ▼
                    ┌─────────────────────┐
                    │       Kafka         │
                    │                     │
                    │   topic:            │
                    │   user-events       │
                    └──────────┬──────────┘
                               │
                               ▼
              ┌──────────────────────────────┐
              │ TeamFlow Notification Service│
              │                              │
              │      UserEventConsumer       │
              │              │               │
              │              ▼               │
              │      NotificationService     │
              └──────────────────────────────┘
```

## Основные возможности

* получение событий из Kafka;
* обработка события регистрации пользователя;
* десериализация JSON-событий в `UserRegisteredEvent`;
* передача события в сервис уведомлений;
* подготовленная архитектура для дальнейшего подключения реального канала уведомлений.

На текущем этапе отправка уведомления реализована через вывод информации в консоль.

## Технологии

* **Java 17**
* **Spring Boot 4.1.1**
* **Spring Kafka**
* **Apache Kafka**
* **Jackson**
* **Gradle**
* **JUnit**

## Структура проекта

```text
src/
├── main/
│   ├── java/
│   │   └── org/example/teamflownotificationservice/
│   │       ├── event/
│   │       │   └── UserRegisteredEvent.java
│   │       ├── kafka/
│   │       │   └── UserEventConsumer.java
│   │       ├── service/
│   │       │   └── NotificationService.java
│   │       └── TeamFlowNotificationServiceApplication.java
│   │
│   └── resources/
│       └── application.properties
│
└── test/
    └── java/
        └── org/example/teamflownotificationservice/
            └── DemoApplicationTests.java
```

## Kafka

Сервис подключается к Kafka по адресу:

```text
localhost:9092
```

Используемый топик:

```text
user-events
```

Consumer Group:

```text
notification-service-v2
```

### Обрабатываемое событие

Сервис получает событие регистрации пользователя:

```json
{
  "userId": 1,
  "username": "test1",
  "email": "user1@example.com"
}
```

Событие преобразуется в объект:

```java
UserRegisteredEvent
```

После получения события `UserEventConsumer` передаёт его в:

```java
NotificationService
```

## Запуск

### Требования

Перед запуском необходимо установить:

* Java 17;
* Apache Kafka;
* Gradle Wrapper используется непосредственно из проекта.

### 1. Запустить Kafka

Kafka должна быть доступна по адресу:

```text
localhost:9092
```

### 2. Запустить TeamFlow

Основное приложение TeamFlow должно отправлять события в Kafka topic:

```text
user-events
```

### 3. Запустить Notification Service

В IntelliJ IDEA запустить:

```text
TeamFlowNotificationServiceApplication
```

Или через Gradle:

```bash
./gradlew bootRun
```

В Windows:

```bash
gradlew.bat bootRun
```

Микросервис запускается на порту:

```text
8081
```

## Проверка работы

После регистрации пользователя в TeamFlow событие отправляется в Kafka.

Notification Service получает его и обрабатывает через `NotificationService`.

В консоли появляется сообщение:

```text
Sending notification to: user1@example.com for user: test1
```

Таким образом, цепочка обработки выглядит следующим образом:

```text
Регистрация пользователя
        ↓
TeamFlow
        ↓
Kafka: user-events
        ↓
UserEventConsumer
        ↓
NotificationService
        ↓
Notification
```

## Конфигурация

Основные настройки находятся в:

```text
src/main/resources/application.properties
```

Текущая конфигурация использует:

```properties
server.port=8081
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=notification-service-v2
```

Для JSON-десериализации используется:

```properties
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
```

Доверенным пакетом для события является:

```text
org.example.teamflownotificationservice.event
```

## Тестирование

Для запуска тестов:

```bash
./gradlew test
```

В Windows:

```bash
gradlew.bat test
```

Для полной сборки проекта:

```bash
./gradlew clean build
```

В Windows:

```bash
gradlew.bat clean build
```

## Связь с TeamFlow

`TeamFlowNotificationService` является отдельным микросервисом проекта **TeamFlow**.

Основное приложение отвечает за бизнес-логику и регистрацию пользователей, а Notification Service вынесен в отдельный сервис.

Взаимодействие между сервисами осуществляется асинхронно через Apache Kafka.

Это позволяет отделить обработку уведомлений от основного приложения и независимо развивать сервис уведомлений.
