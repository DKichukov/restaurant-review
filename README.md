# Restaurant Review Application Demo

A Spring Boot-based RESTful API for managing restaurant reviews, built with Elasticsearch for fast search and retrieval. The application supports restaurant creation, review management, photo uploads, and robust error handling.

## Features

- **Restaurant Management:** Create, update, delete, and search restaurants by name, rating, or location.
- **Review System:** Users can post, update, and delete reviews for restaurants, including ratings and photos.
- **Photo Uploads:** Upload and retrieve restaurant and review photos via REST endpoints.
- **Geolocation:** Assigns random London-based geolocation to restaurants for demo purposes.
- **Validation & Error Handling:** Comprehensive validation and custom error responses for all endpoints.
- **Security:** JWT-based authentication for protected endpoints.

## Technologies

- Java 17+
- Spring Boot
- Spring Data Elasticsearch
- Spring Security (JWT)
- Lombok
- Maven
- Docker (optional for deployment)

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Elasticsearch (running locally or via Docker)

### Build & Run

1. **Clone the repository:**
   ```sh
   git clone `https://github.com/DKichukov/restaurant-review.git` https://github.com/DKichukov/restaurant-review.git
   cd restaurant-review
   ```

2. **Start Elasticsearch (Docker example):**
   ```sh
   docker-compose up -d
   ```

3. **Build the project:**
   ```sh
   ./mvnw clean package
   ```

4. **Run the application:**
   ```sh
   ./mvnw spring-boot:run
   ```

### API Endpoints

#### Restaurants

- `POST /api/restaurants` — Create a restaurant
- `GET /api/restaurants` — Search restaurants (by query, rating, location)
- `GET /api/restaurants/{restaurantId}` — Get restaurant details
- `PUT /api/restaurants/{restaurantId}` — Update restaurant
- `DELETE /api/restaurants/{restaurantId}` — Delete restaurant

#### Reviews

- `POST /api/restaurants/{restaurantId}/reviews` — Add review
- `GET /api/restaurants/{restaurantId}/reviews` — List reviews
- `GET /api/restaurants/{restaurantId}/reviews/{reviewId}` — Get review
- `PUT /api/restaurants/{restaurantId}/reviews/{reviewId}` — Update review
- `DELETE /api/restaurants/{restaurantId}/reviews/{reviewId}` — Delete review

#### Photos

- `POST /api/photos` — Upload photo
- `GET /api/photos/{id}` — Retrieve photo

## Configuration

- **Environment Variables:** See `.env` for configurable options.
- **Photo Storage:** Default location is `uploads/` (configurable via `app.storage.location`).


