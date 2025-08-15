# 🔗 BeloShort — URL Shortener with Analytics & QR

A full-stack URL shortener that lets users create short links, **track clicks**, and **generate QR codes**.  
Secure redirects with **JWT auth**, a clean **React + Tailwind** UI, and **Dockerized** deployment.

- **Backend:** Java 21, Spring Boot (Web, Security, JPA), JWT, **MySQL**
- **Frontend:** React + Vite + Tailwind CSS
- **Infra:** Docker, Docker Compose

---

## ✨ Features
- Shorten & redirect links (`/<shortCode>`)
- JWT login/register + protected APIs
- Click analytics per short URL
- QR code generation for each short link
- Docker Compose for quick deploy

---

## 🗂 Project Structure
```
shortlink/
├─ backend/           # Spring Boot app
│  ├─ src/...
│  ├─ pom.xml
│  └─ .env            # backend environment vars
├─ frontend/          # React + Vite + Tailwind
│  ├─ src/...
│  ├─ package.json
│  └─ .env            # frontend environment vars
├─ docker-compose.yml
└─ README.md
```

---

## ⚙️ Environment Variables

### `backend/.env`
```env
# Database (MySQL in Docker Compose uses service name 'db')
DATABASE_URL=jdbc:mysql://db:3306/shortLinkDB?useSSL=false&serverTimezone=UTC
DATABASE_USERNAME=shortlink_user
DATABASE_PASSWORD=change_me
DATABASE_DIALECT=org.hibernate.dialect.MySQLDialect

# Security
JWT_SECRET=please-change-to-a-long-random-string
JWT_EXP_MINUTES=60

# CORS / Frontend
FRONTEND_URL=http://localhost:5173

# Server
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

> Map them in Spring (example `application.properties`):
```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
spring.jpa.properties.hibernate.dialect=${DATABASE_DIALECT}
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=never

server.port=${SERVER_PORT:8080}
app.cors.allowed-origins=${FRONTEND_URL}
```

### `frontend/.env`
```env
VITE_API_URL=http://localhost:8080
VITE_APP_NAME=BeloShort
```

### `.env` (repo root, for Compose)
```env
MYSQL_DATABASE=shortLinkDB
MYSQL_USER=shortlink_user
MYSQL_PASSWORD=change_me
MYSQL_ROOT_PASSWORD=rootpass

BACKEND_PORT=8080
FRONTEND_PORT=5173
```

---

## 🐳 Run with Docker Compose (recommended)

**docker-compose.yml** at repo root:
```yaml
version: "3.9"

services:
  db:
    image: mysql:8.3
    container_name: shortlink-mysql
    env_file: .env
    environment:
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
    command: >
      --default-authentication-plugin=caching_sha2_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_unicode_ci
    ports:
      - "3306:3306"
    volumes:
      - mysqldata:/var/lib/mysql
    healthcheck:
      test: ["CMD-SHELL", "mysqladmin ping -h localhost -u${MYSQL_USER} -p${MYSQL_PASSWORD} --silent"]
      interval: 10s
      timeout: 5s
      retries: 10
    networks: [appnet]

  backend:
    build: ./backend
    container_name: shortlink-backend
    env_file: ./backend/.env
    depends_on:
      db:
        condition: service_healthy
    environment:
      # Ensure Spring points to the DB in Compose
      DATABASE_URL: jdbc:mysql://db:3306/${MYSQL_DATABASE}?useSSL=false&serverTimezone=UTC
      DATABASE_USERNAME: ${MYSQL_USER}
      DATABASE_PASSWORD: ${MYSQL_PASSWORD}
    ports:
      - "${BACKEND_PORT}:8080"
    networks: [appnet]

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: shortlink-frontend
    env_file: ./frontend/.env
    environment:
      VITE_API_URL: "http://backend:8080"
    ports:
      - "${FRONTEND_PORT}:5173"
    depends_on: [backend]
    networks: [appnet]

networks:
  appnet:

volumes:
  mysqldata:
```

**Run:**
```bash
# from repo root
docker compose up --build
```

- Frontend → http://localhost:5173  
- API → http://localhost:8080

---

## 🧪 Run Locally (without Docker)

### 1) Start MySQL
Create DB and user (adjust passwords as needed):
```bash
mysql -uroot -p -e "CREATE DATABASE shortLinkDB;"
mysql -uroot -p -e "CREATE USER 'shortlink_user'@'%' IDENTIFIED BY 'change_me';"
mysql -uroot -p -e "GRANT ALL PRIVILEGES ON shortLinkDB.* TO 'shortlink_user'@'%'; FLUSH PRIVILEGES;"
```

### 2) Backend (Spring Boot)
```bash
cd backend
# Ensure backend/.env points to local MySQL:
# DATABASE_URL=jdbc:mysql://localhost:3306/shortLinkDB?useSSL=false&serverTimezone=UTC
./mvnw -q clean package
java -jar target/*.jar
# or for dev: ./mvnw spring-boot:run
```

### 3) Frontend (Vite + Tailwind)
```bash
cd frontend
npm i           # or pnpm i / yarn
npm run dev     # or pnpm dev / yarn dev
# production build:
# npm run build && npm run preview
```

Open http://localhost:5173 (make sure `VITE_API_URL=http://localhost:8080`).

---

## 🔐 API Overview

**Auth**
- `POST /api/auth/register` → `{ username, password }`
- `POST /api/auth/login` → `{ token }` (JWT)  
Use `Authorization: Bearer <token>` for protected endpoints.

**Links**
- `POST /api/urls/shorten` → `{ originalUrl }` → `{ shortCode, shortUrl, qrUrl }`
- `GET /{shortCode}` → HTTP redirect (public)
- `GET /api/urls/analytics/{shortCode}` → click stats (JWT)

### Curl examples

```bash
# 1) Register
curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"andrei","password":"S3cret!Pass"}'

# 2) Login (capture token)
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"andrei","password":"S3cret!Pass"}' | jq -r .token)

# 3) Shorten a URL
curl -s -X POST http://localhost:8080/api/urls/shorten \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"originalUrl":"https://example.com"}'

# 4) Follow a short link in the browser:
# http://localhost:8080/<shortCode>

# 5) Analytics for a short code
curl -s http://localhost:8080/api/urls/analytics/<shortCode> \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🧰 Tips & Production Notes
- For prod, prefer `spring.jpa.hibernate.ddl-auto=validate` + Flyway migrations.
- Keep `JWT_SECRET` long and random; rotate periodically.
- Restrict CORS with `FRONTEND_URL` to your real domain (e.g., `https://beloshort.link`).
- If serving behind a reverse proxy, terminate TLS at the proxy and route:
  - `/` → frontend, `/api` → backend.
