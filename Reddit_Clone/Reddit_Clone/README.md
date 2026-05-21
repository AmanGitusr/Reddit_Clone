# Reddit Clone Full-Stack MVP

A runnable Reddit-style social media application with a Spring Boot backend, React frontend, and MySQL connectivity.

## Stack

- Backend: Java 17, Spring Boot, Spring Security, JWT, Spring Data JPA, Maven
- Frontend: React, Vite, Tailwind CSS
- Database: MySQL 8
- Tests: Spring Boot integration tests with H2

## Project Structure

```text
backend/   Spring Boot REST API
frontend/  React + Vite client
```

## Database Setup

Open MySQL as an admin user and run:

```sql
CREATE DATABASE IF NOT EXISTS reddit_clone;
CREATE USER IF NOT EXISTS 'reddit_user'@'localhost' IDENTIFIED BY 'reddit_password';
GRANT ALL PRIVILEGES ON reddit_clone.* TO 'reddit_user'@'localhost';
FLUSH PRIVILEGES;
```

Hibernate creates and updates local development tables automatically through `spring.jpa.hibernate.ddl-auto=update`.

## Backend Configuration

Copy the example file and adjust values if needed:

```powershell
Copy-Item backend\.env.example backend\.env
```

Spring Boot reads environment variables directly. In PowerShell, set them before starting the server:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/reddit_clone?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME="reddit_user"
$env:DB_PASSWORD="reddit_password"
$env:JWT_SECRET="change-this-to-a-long-random-secret-with-at-least-32-characters"
$env:JWT_EXPIRATION_MS="86400000"
$env:FRONTEND_ORIGIN="http://localhost:5173"
```

## Frontend Configuration

Copy the example file:

```powershell
Copy-Item frontend\.env.example frontend\.env
```

Default value:

```text
VITE_API_URL=http://localhost:8080/api
```

## Install Dependencies

Backend dependencies are resolved by Maven:

```powershell
cd backend
mvn test
```

Frontend dependencies:

```powershell
cd frontend
npm.cmd install
```

Use `npm.cmd` on this Windows machine because PowerShell script execution blocks `npm.ps1`.

## Run The Application

Start the backend:

```powershell
cd backend
mvn spring-boot:run
```

Start the frontend in another terminal:

```powershell
cd frontend
npm.cmd run dev
```

Open:

```text
http://localhost:5173
```

## Test Commands

Backend:

```powershell
cd backend
mvn test
```

Frontend:

```powershell
cd frontend
npm.cmd run build
npm.cmd run lint
```

## Main API Endpoints

- `POST /api/auth/signup`
- `POST /api/auth/login`
- `GET /api/communities`
- `POST /api/communities`
- `GET /api/communities/{slug}`
- `GET /api/communities/{slug}/posts?sort=latest|popular`
- `GET /api/posts?sort=latest|popular`
- `POST /api/posts`
- `GET /api/posts/{id}`
- `GET /api/posts/{id}/comments`
- `POST /api/posts/{id}/comments`
- `POST /api/votes`

## Manual Verification Flow

1. Register a user.
2. Create a community.
3. Create a text, image URL, or link post.
4. Open the post detail page.
5. Upvote and downvote the post.
6. Add a comment.
7. Switch between latest and popular sorting.

## Notes

- Image posts store external image URLs only; binary upload is intentionally outside this MVP.
- JWTs are stored in browser local storage for local development simplicity.
- Use a long random `JWT_SECRET` for any deployed environment.
