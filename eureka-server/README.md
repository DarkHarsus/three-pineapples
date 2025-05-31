# Eureka Server
Этот сервис представляет собой eureka-server для регистрации остальных микросервисов.

# Actuator
В приложении пристутсвует Spring Boot Actuator со следующими метриками:
- _localhost:8750/actuator/health_ — проверка состояния приложения
- _localhost:8750/actuator/info_ — информация о приложении
- _localhost:8750/actuator/metrics_ — метрики приложения