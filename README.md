## SEP-Server

A Spring Boot 3 (Java 17) backend for managing events, tasks, recruitment requests, and financial requests. Uses H2 database with seed data.

### Requirements

- **Java 17** (e.g., Temurin/OpenJDK 17)
- No need to install Maven — the project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`)

### Quick Start

From the project root (`SEP-Server`):

```bash
# macOS/Linux
./mvnw spring-boot:run

# Windows (PowerShell or CMD)
mvnw.cmd spring-boot:run
```

Then open: `http://localhost:8080`

### Build and Run (JAR)

```bash
# Build
./mvnw clean package

# Run the generated jar
java -jar target/SEP-Server-0.0.1-SNAPSHOT.jar
```

### Run Tests

```bash
./mvnw test
```

### Configuration (defaults)

Key properties from `src/main/resources/application.properties`:

- **Server port**: `8080`
- **Database**: H2 file database at `./data/eventdb`
- **Schema**: `spring.jpa.hibernate.ddl-auto=create` (tables recreated on startup)
- **Seed data**: `src/main/resources/import-dev.sql` loads automatically on startup
- **H2 Console**: enabled at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/eventdb`, username: `sa`, empty password)

### Seeded Sample Data

On startup, the app loads sample rows for Events, Tasks, Recruitment Requests, and Financial Requests from `import-dev.sql`.

### Login and Sample Credentials

The login module and example credentials are documented in `LOGIN_SYSTEM.md`. Common examples:

- PM / pm123
- FM / fm123
- CSO / cso123

Login endpoint:

```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"user": "PM", "pass": "pm123"}'
```

### Useful Endpoints

- H2 Console: `http://localhost:8080/h2-console`
- Login API examples: see `LOGIN_SYSTEM.md`

### Resetting the Database

Because the app uses an H2 file database under `./data` and recreates tables on startup, you can fully reset the database by stopping the app and deleting the files:

```bash
rm -f data/eventdb.mv.db data/eventdb.trace.db
```

They will be recreated with fresh schema and seed data on next startup.

### Troubleshooting

- **Port 8080 already in use**: Stop the conflicting process or run with `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`.
- **Java version error**: Ensure `java -version` reports 17.x. If multiple JDKs are installed, set `JAVA_HOME` to a JDK 17.
- **Schema/data not appearing**: Confirm `spring.jpa.hibernate.ddl-auto=create` and `spring.sql.init.mode=always` are active (default in this repo). Also check the H2 console URL/credentials above.

### Project Tech

- Spring Boot 3.5.x
- Java 17
- Spring Web, Spring Data JPA
- H2 Database
- Lombok (compile-time only)
- Maven Wrapper
