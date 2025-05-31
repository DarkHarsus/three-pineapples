# API-Gateway
Этот сервис представляет собой API-Gateway для маршрутизации запросов к бизнес-сервисам.

# Actuator
В приложении пристутсвует Spring Boot Actuator со следующими метриками:
- _localhost:8751/actuator/health_ — проверка состояния приложения
- _localhost:8751/actuator/info_ — информация о приложении
- _localhost:8751/actuator/metrics_ — метрики приложения
- _localhost:8751/actuator/gateway_ — просмотр и управление маршрутами Spring Cloud Gateway