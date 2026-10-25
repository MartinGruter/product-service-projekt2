## Ansvar

Product Service är en mikrotjänst i BLackmarqet systemet som ansvarar för hantering av produkter. Tjänsten inkulderar
endpoints för att hämta, skapa, uppdatera och ta bort produkter. Vissa endpints är skyddade med JWT authentication via
OAuth2 Resource Server.

## Tekniker

- Java 21
- Spring boot v4
- Spring security
- JWT
- Maven
- Postgresql

## Databas

Tjänsten använder Postgresql, hostad hos Neon.

## Miljövariabler

Miljövariabler måste vara satta för att tjänsten ska fungera korrekt.

ALLOWED_ORIGINS - Tillåtna origins för cors, kommaseparera om flera.
DB_URL - JDBC url till postgresql.
DB_USERNAME - Användarnamn till databasen.
DB_PASSWORD - Lösenord till databasen.
JWT_PUBLIC_URL - URL till JWK set för JWT verifiering.

Mappning i `application.properties`:

```
app.cors.allowed-origins=${ALLOWED_ORIGINS}
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt-public-key=${JWT_PUBLIC_URL}
```

## Starta applikationen lokalt

Kräver Java 21 och Maven.

1. Klona projektet
2. Sätt miljövariablerna
3. Starta applikationen:

**MacOS/Linux**

```
./mvnw spring-boot:run
```

**Windows:**

```
mvnw.cmd spring-boot:run
```

Tjänsten är tillgänglig på `http://localhost:8081`.

## Relaterade repositories

1. Gateway - https://github.com/AnwarAsry/gateway-service
2. Auth Service - https://github.com/MartinGruter/auth-server-projekt2
3. Order Service - https://github.com/MartinGruter/order-service-projekt2
4. Email Service - https://github.com/MartinGruter/email-service-projekt2
5. Frontend - https://github.com/MartinGruter/micro-services