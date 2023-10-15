# VTB

Запуск: 
1. ``docker-compose up``
2. ``./gradlew bootRun``
3. ``swagger-ui:`` http://localhost:8080/swagger-ui.html
4. ``x-root-secret``: vtbGoodBank
5.  После первого запуска спарсятся данные об отделениях и банкоматах, после чего необходимо поменять переменные окружения parse.atm и parse.office на значения false
