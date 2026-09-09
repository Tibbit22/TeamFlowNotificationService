# TeamFlow Notification Service

**TeamFlow Notification Service** — отдельный микросервис для обработки уведомлений в системе TeamFlow.

Сервис получает события от основного приложения через **Apache Kafka** и обрабатывает их независимо от основной бизнес-логики.

## Стек

* **Java 17**
* **Spring Boot 4.1.1**
* **Spring Kafka**
* **Apache Kafka**
* **Jackson**
* **Gradle**
* **JUnit**

## Основные возможности

* получение событий из Kafka;
* обработка события регистрации пользователя `UserRegisteredEvent`;
* JSON-десериализация сообщений;
* асинхронное взаимодействие с основным приложением;
* отдельная обработка логики уведомлений;
* независимый запуск и масштабирование микросервиса.

На текущем этапе отправка уведомления реализована через вывод информации в консоль. Архитектура позволяет в дальнейшем подключить реальные каналы уведомлений, например email или push.

## Архитектура

```text id="8q1f7z"
                    ┌─────────────────────┐
                    │      TeamFlow       │
                    │                     │
                    │ User registration   │
                    └──────────┬──────────┘
                               │
                               │ UserRegisteredEvent
                               ▼
                    ┌─────────────────────┐
                    │      Apache Kafka   │
                    │                     │
                    │   user-events       │
                    └──────────┬──────────┘
                               │
                               ▼
              ┌──────────────────────────────┐
              │ TeamFlow Notification Service│
              │                              │
              │     UserEventConsumer        │
              │              │               │
              │              ▼               │
              │     NotificationService      │
              └──────────────────────────────┘
```

## Kafka

Микросервис использует Kafka topic:

```text id="z1w3u5"
user-events
```

Consumer Group:

```text id="f7t8pa"
notification-service-v2
```

После регистрации пользователя TeamFlow публикует `UserRegisteredEvent`, который микросервис получает и передаёт в `NotificationService`.

Пример события:

```json id="4h8x3q"
{
  "userId": 1,
  "username": "test1",
  "email": "user1@example.com"
}
```

## Структура

```text id="4w1q3e"
src/main/java/org/example/teamflownotificationservice/
├── event/
│   └── UserRegisteredEvent.java
├── kafka/
│   └── UserEventConsumer.java
├── service/
│   └── NotificationService.java
└── TeamFlowNotificationServiceApplication.java
```

## Запуск

Микросервис запускается на порту:

```text id="x2h7qk"
8081
```

Запуск через Gradle:

```bash id="r9c4ve"
./gradlew bootRun
```

Для работы Kafka используется:

```text id="m5n2kd"
localhost:9092
```

Тесты:

```bash id="v8x3pa"
./gradlew test
```

## Связь с TeamFlow

`TeamFlow Notification Service` является отдельным сервисом системы **TeamFlow**.

Основное приложение отвечает за бизнес-логику, а обработка уведомлений вынесена в отдельный микросервис. Взаимодействие между сервисами происходит асинхронно через Apache Kafka.

Такой подход позволяет независимо развивать и масштабировать функциональность уведомлений.
