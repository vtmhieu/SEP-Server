SEP Application - Full Stack Setup Guide
==========================================

This document provides instructions for running both the frontend (Angular) and backend (Spring Boot) parts of the SEP application.

================================================================================
BACKEND (SEP-Server)
================================================================================

Overview:
---------
A Spring Boot 3 (Java 17) backend for managing events, tasks, recruitment requests, and financial requests. Uses H2 database with seed data.

Requirements:
-------------
- Java 17 (e.g., Temurin/OpenJDK 17)
- No need to install Maven — the project includes the Maven Wrapper (mvnw / mvnw.cmd)

Quick Start:
------------
From the project root (SEP-Server):

  macOS/Linux:
    ./mvnw spring-boot:run

  Windows (PowerShell or CMD):
    mvnw.cmd spring-boot:run

The backend server will start at: http://localhost:8080

Build and Run (JAR):
--------------------
  Build:
    ./mvnw clean package

  Run the generated jar:
    java -jar target/SEP-Server-0.0.1-SNAPSHOT.jar

Run Tests:
----------
  ./mvnw test

Configuration (defaults):
-------------------------
Key properties from src/main/resources/application.properties:

  - Server port: 8080
  - Database: H2 file database at ./data/eventdb
  - Schema: spring.jpa.hibernate.ddl-auto=create (tables recreated on startup)
  - Seed data: src/main/resources/import-dev.sql loads automatically on startup
  - H2 Console: enabled at http://localhost:8080/h2-console
    (JDBC URL: jdbc:h2:file:./data/eventdb, username: sa, empty password)

API Base Path:
--------------
  The backend exposes APIs under /api/... endpoints.

Seeded Sample Data:
-------------------
On startup, the app loads sample rows for Events, Tasks, Recruitment Requests, and Financial Requests from import-dev.sql.

Login and Sample Credentials:
------------------------------
Common example credentials:
  - PM / pm123
  - FM / fm123
  - CSO / cso123

Login endpoint example:
  curl -X POST http://localhost:8080/api/login \
    -H "Content-Type: application/json" \
    -d '{"user": "PM", "pass": "pm123"}'

For more details, see LOGIN_SYSTEM.md

Useful Endpoints:
-----------------
  - H2 Console: http://localhost:8080/h2-console
  - Login API: http://localhost:8080/api/login
  - Events API: http://localhost:8080/api/events
  - Tasks API: http://localhost:8080/api/tasks
  - Budgets API: http://localhost:8080/api/budgets
  - Recruitment Requests API: http://localhost:8080/api/recruitment-requests
  - Financial Requests API: http://localhost:8080/api/financial-requests

Resetting the Database:
------------------------
Because the app uses an H2 file database under ./data and recreates tables on startup, you can fully reset the database by stopping the app and deleting the files:

  rm -f data/eventdb.mv.db data/eventdb.trace.db

They will be recreated with fresh schema and seed data on next startup.

Backend Troubleshooting:
-------------------------
  - Port 8080 already in use: Stop the conflicting process or run with:
      ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

  - Java version error: Ensure java -version reports 17.x. If multiple JDKs are installed, set JAVA_HOME to a JDK 17.

  - Schema/data not appearing: Confirm spring.jpa.hibernate.ddl-auto=create and spring.sql.init.mode=always are active (default in this repo). Also check the H2 console URL/credentials above.

Backend Tech Stack:
--------------------
  - Spring Boot 3.5.x
  - Java 17
  - Spring Web, Spring Data JPA
  - H2 Database
  - Lombok (compile-time only)
  - Maven Wrapper


================================================================================
FRONTEND (Angular Application)
================================================================================

Configuration:
--------------
  - API base path: The UI expects APIs under /api/... and uses the Angular dev-server proxy during development (see proxy.conf.json).
  - Port: UI runs on http://localhost:4200/ in dev.

Requirements:
-------------
  - Node.js and npm (or yarn)
  - Angular CLI (install globally with: npm install -g @angular/cli)

Quick Start:
------------
From the frontend project root:

  1. Install dependencies:
     npm install
     (or: yarn install)

  2. Start the development server:
     ng serve
     (or: npm start / yarn start)

The frontend will be available at: http://localhost:4200/

Note: The frontend uses a proxy configuration (proxy.conf.json) to forward API requests to the backend running on http://localhost:8080 during development.

How to use (App Overview):
---------------------------
  - Login: Access the app and authenticate via the Login page (/login).

  - Home: Landing page linking to key features.

  - Events:
      * View events: Events list page.
      * Create/edit events: Create Event form.

  - Tasks:
      * Create tasks for an event via Create Task.
      * View and update tasks in Task List.

  - Budgets:
      * Add and review budget items via the Add Budget dialog and View Budget.

  - Recruitment Requests:
      * Submit and track requests via Recruitment Request form/list.

  - Financial Requests:
      * Submit and track requests via Financial Request form/list.


================================================================================
RUNNING THE FULL STACK APPLICATION
================================================================================

Step-by-Step Setup:
-------------------

1. Start the Backend:
   ------------------
   Navigate to the SEP-Server directory (where this readme.txt is located):
   
     cd /path/to/SEP-Server
     
   Start the Spring Boot server:
   
     macOS/Linux:
       ./mvnw spring-boot:run
     
     Windows:
       mvnw.cmd spring-boot:run
   
   Wait until you see a message like: "Started SepServerApplication in X.XXX seconds"
   The backend should now be running at http://localhost:8080

2. Start the Frontend:
   -------------------
   In a new terminal window, navigate to the frontend project directory:
   
     cd /path/to/frontend-project
   
   Install dependencies (if not already done):
   
     npm install
   
   Start the Angular development server:
   
     ng serve
     (or: npm start)
   
   Wait until you see: "Application bundle generation complete" and "Compiled successfully"
   The frontend should now be running at http://localhost:4200

3. Access the Application:
   -----------------------
   Open your web browser and navigate to:
   
     http://localhost:4200
   
   You should see the application interface. Start by logging in using one of the sample credentials mentioned above (e.g., PM / pm123).


Verification:
-------------
  - Backend running: Visit http://localhost:8080/api/login (or check http://localhost:8080/h2-console)
  - Frontend running: Visit http://localhost:4200
  - Full integration: Login at http://localhost:4200/login and verify you can access features


Important Notes:
----------------
  - The backend must be running before you can successfully use the frontend, as the frontend makes API calls to the backend.
  - The frontend uses a proxy configuration during development, so API calls to /api/... from the frontend are automatically forwarded to http://localhost:8080.
  - Make sure both servers are running on their respective ports (backend: 8080, frontend: 4200).
  - If you need to change ports, ensure the proxy configuration in the frontend (proxy.conf.json) matches the backend port.


================================================================================
TROUBLESHOOTING
================================================================================

Common Issues:
--------------

1. Backend won't start:
   - Check if port 8080 is already in use
   - Verify Java 17 is installed: java -version
   - Check if the database files in ./data are corrupted (delete and restart)

2. Frontend won't start:
   - Verify Node.js and npm are installed: node --version, npm --version
   - Install Angular CLI if missing: npm install -g @angular/cli
   - Clear node_modules and reinstall: rm -rf node_modules && npm install

3. API calls failing from frontend:
   - Ensure backend is running on http://localhost:8080
   - Check proxy.conf.json configuration in frontend project
   - Verify CORS settings if running without proxy

4. Login not working:
   - Check backend logs for errors
   - Verify sample credentials are correct (see LOGIN_SYSTEM.md for details)
   - Ensure database seed data loaded correctly


================================================================================
PROJECT STRUCTURE
================================================================================

Backend (SEP-Server):
  - src/main/java/group25/sep/server/: Main application code
  - src/main/resources/: Configuration files and seed data
  - data/: H2 database files (created at runtime)
  - pom.xml: Maven dependencies
  - mvnw, mvnw.cmd: Maven wrapper scripts

Frontend (Angular):
  - src/app/: Main Angular application code
  - proxy.conf.json: Proxy configuration for API calls
  - angular.json: Angular project configuration
  - package.json: Node.js dependencies


================================================================================
END OF README
================================================================================

