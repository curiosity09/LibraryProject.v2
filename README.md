# SecondLibProject

A small library management application.

- **Backend** — Spring Boot 3 (Spring MVC + Spring Data JPA + Spring Security), exposing a
  JSON REST API under `/api`.
- **Frontend** — a React 18 single page application (Vite + TypeScript + Bootstrap 5) that talks
  to the backend exclusively over that REST API.

There is no server side templating: every screen is rendered by the SPA, and the server only
serves JSON plus the compiled static assets.

## Tech stack

| Layer    | Technology                                              |
| -------- | ------------------------------------------------------- |
| Backend  | Java 17, Spring Boot 3.3, Spring Security, Spring Data JPA |
| Database | PostgreSQL (prod) / H2 in-memory (test)                 |
| Frontend | React 18, TypeScript 5, Vite 5, React Router 6, Bootstrap 5 |

## Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 18+ and npm (only needed to build the SPA)

## Running the application

### 1. Build the frontend

The SPA is built into the Spring Boot static resources, so the backend can serve it directly:

```bash
cd frontend
npm install
npm run build      # outputs to ../src/main/resources/static
```

### 2. Start the backend

The default `prod` profile expects PostgreSQL. Provide the credentials via environment variables
(see `src/main/resources/application-prod.yml`):

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your-password"
mvn spring-boot:run
```

For a quick local run without a database, use the `test` profile, which starts an in-memory H2
database seeded with demo accounts:

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=test"
```

The application then listens on <http://localhost:8080>.

Demo accounts (`test` profile):

| Username | Password | Role      |
| -------- | -------- | --------- |
| `user`   | `pass`   | user      |
| `lib`    | `pass`   | librarian |
| `admin`  | `pass`   | admin     |

### Frontend development (hot reload)

Run the backend on `:8080` as described above, then start the Vite dev server:

```bash
cd frontend
npm run dev
```

Open <http://localhost:5173>. The dev server proxies every `/api` request to `http://localhost:8080`,
so the browser sees a single origin and the session cookie works transparently.

## Authentication

Authentication uses the HTTP session. The SPA logs in through `POST /api/auth/login` and the
server returns the `JSESSIONID` cookie, which is sent back automatically on every subsequent
request (the SPA always calls the API with `credentials: 'include'`).

| Method | Path                 | Description                          |
| ------ | -------------------- | ------------------------------------ |
| POST   | `/api/auth/login`    | Log in with `{ username, password }` |
| POST   | `/api/auth/register` | Register a new `user` account        |
| POST   | `/api/auth/logout`   | Invalidate the session               |
| GET    | `/api/auth/me`       | Current account (401 when anonymous) |

Authorities are plain strings derived from the account discriminator: `user`, `librarian`, `admin`.
`/api/admin/**` requires `admin`, `/api/librarian/**` requires `librarian` or `admin`, and every
other `/api/**` endpoint requires an authenticated session.

## REST API overview

| Area      | Endpoints                                                                              |
| --------- | -------------------------------------------------------------------------------------- |
| Books     | `GET /api/books`, `GET /api/books/{id}`, `GET /api/books/by-author/{id}`, `POST/PUT` (staff) |
| Authors   | `GET /api/authors`, `GET /api/authors/all`, `POST /api/authors` (staff)                |
| Genres    | `GET/POST /api/genres` (POST staff)                                                    |
| Sections  | `GET/POST /api/sections` (POST staff)                                                  |
| Orders    | `GET /api/orders` (staff), `/api/orders/my`, `/api/orders/user/{username}` (staff), `POST /api/orders`, `DELETE /api/orders/{id}` (staff) |
| Account   | `PUT /api/account/me`                                                                  |
| Admin     | `/api/admin/users`, `/admins`, `/librarians`, `/debtors`, `POST /users/{u}/block`, `DELETE /users/{id}`, `POST /employees` |
| Files     | `GET /api/files`, `POST /api/files`, `GET/DELETE /api/files/{filename}` (staff)        |

Paged endpoints return `{ content, page, size, totalPages, hasNext, hasPrevious }`.
Errors are returned as `{ status, message, timestamp, errors }`.

## Project layout

```
src/main/java/by/tms/
  model/        domain entities, DTOs, mappers, DAOs, services
  web/config/   Spring Security configuration
  web/rest/     REST controllers, request/response DTOs, exception handlers
  web/          SPA deep-link forward controller

frontend/       React + Vite single page application
  src/api/      typed REST client
  src/contexts/ auth and shopping-cart providers
  src/pages/    user, librarian and admin screens
  src/i18n/     Russian / English translations
```

## Notes

- The shopping cart lives in the browser (`localStorage`) and is sent to the server only when an
  order is created (`POST /api/orders` with `{ bookIds, rentalPeriod }`).
- Uploaded files are handled by `FilesStorageService` and exposed through `/api/files`.
