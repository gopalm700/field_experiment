# Field Statistics

A Restful API that provides field conditions insights. API is able to handle
two use cases.

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
```
./mvnw clean install
./mvnw spring-boot:run  
```


# Improvement Scope:
1. Add persistence layer.
2. Add Security Header.
3. Add custom logging.
