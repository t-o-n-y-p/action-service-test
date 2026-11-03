## Инструкция по запуску

### Окружение
В папку infrastructure скопировать файл приложения. 
Там же создать файл `.env` и записать в него ключ API в виде

`API_KEY=apiKey`

Выполнить в папке infrastructure команду

`docker compose up -d`

### Тесты
В корневой директории проекта выполнить команду, подставив значения ключа API и URL

`mvn clean test -DapiKey=apiKey -DbaseUrl=http://localhost:8080`

### Отчет
В корневой директории проекта выполнить команду

`mvn allure:serve`
