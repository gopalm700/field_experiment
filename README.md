# Field Statistics

API documentation present in swagger. Which can be access after application is running.

http://localhost:8080/swagger-ui/index.html

How to run application:

Docker:
```
./mvnw clean package
sudo docker build -t field .
sudo docker run -p 8080:8080 field:latest
```

Maven:
./mvnw clean install
./mvnw spring-boot:run  
