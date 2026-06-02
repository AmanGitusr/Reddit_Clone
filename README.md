# Reddit Clone 🚀

A full-stack social media platform inspired by Reddit, built using **Spring Boot**, **React.js**, and **MySQL**. Users can create communities, publish posts, participate in discussions, and vote on content through a modern and responsive web application.

---

## 📖 Overview

Reddit Clone is a community-driven discussion platform where users can:

* Create an account and log in securely
* Create and browse communities
* Publish posts within communities
* Upvote and downvote content
* Add comments to discussions
* Discover trending and recent posts

The project demonstrates full-stack application development, REST API design, authentication, database modeling, and frontend-backend integration.

---

## ✨ Features

### 🔐 Authentication & Authorization

* User Registration
* User Login
* JWT-based Authentication
* Password Encryption using BCrypt
* Protected API Endpoints

### 👥 Communities

* Create Communities
* Browse Communities
* Community-specific Pages
* Community Post Listings

### 📝 Posts

* Create Posts
* View Post Details
* Edit/Delete Own Posts
* Support for Text and Link Posts

### ⬆️ Voting System

* Upvote Posts
* Downvote Posts
* Vote Count Tracking
* One Vote Per User

### 💬 Comments

* Add Comments
* View Comment Threads
* Community Discussions

### 🔍 Sorting & Discovery

* Latest Posts
* Most Popular Posts
* Community-based Filtering

---

## 🏗️ System Architecture

```text
React Frontend
       │
       ▼
Spring Boot REST API
       │
       ▼
Service Layer
       │
       ▼
Repository Layer (JPA/Hibernate)
       │
       ▼
MySQL Database
```

---

## 🛠️ Tech Stack

### Backend

* Java 17+
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* JWT Authentication
* Maven

### Frontend

* React.js
* React Router
* Axios
* Tailwind CSS

### Database

* MySQL
---

## 📂 Project Structure

```text
reddit-clone/

├── backend/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   ├── dto/
│   ├── security/
│   ├── config/
│   └── resources/
│
├── frontend/
│   ├── components/
│   ├── pages/
│   ├── services/
│   ├── context/
│   ├── hooks/
│   └── assets/
│
└── README.md
```

---

## 🗄️ Database Design

### User

| Field    | Type   |
| -------- | ------ |
| id       | Long   |
| username | String |
| email    | String |
| password | String |

### Community

| Field | Type   |
| ----- | ------ |
| id    | Long   |
| name  | String |
| slug  | String |

### Post

| Field     | Type    |
| --------- | ------- |
| id        | Long    |
| title     | String  |
| content   | Text    |
| imageUrl  | String  |
| voteCount | Integer |

### Comment

| Field   | Type |
| ------- | ---- |
| id      | Long |
| content | Text |

### Vote

| Field | Type              |
| ----- | ----------------- |
| id    | Long              |
| type  | UPVOTE / DOWNVOTE |

---

## ⚙️ Installation & Setup

### Prerequisites

Make sure the following are installed:

* Java 17+
* Maven
* Node.js 18+
* MySQL 8+

---

### Manual Testing Checklist

* User Registration
* User Login
* JWT Generation
* Community Creation
* Post Creation
* Voting Functionality
* Comment System
* Database Persistence

---

## 🔒 Security Features

* JWT Authentication
* BCrypt Password Hashing
* Input Validation
* Role-Based Access Ready
* Protected API Routes
* Secure Database Access
  
---

## 👨‍💻 Author

Developed as a full-stack social media platform project demonstrating:

* Java Backend Development
* REST API Design
* Authentication & Security
* Database Modeling
* React Frontend Development
* Full-Stack Architecture
