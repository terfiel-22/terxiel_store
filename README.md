## Simple E-Commerce REST API
This is a production-ready backend application built with Spring Boot and MySQL to handle core digital commerce operations. The system secures sensitive endpoints using Spring Security and stateless JWT token authentication to enforce strict role-based access control for customers and administrators. It manages dynamic product catalogs, processes complex user shopping carts, and handles secure financial transactions through direct integration with the Stripe API payment gateway. The entire architecture is fully containerized using Docker to ensure identical development and production environments, while a continuous integration and continuous deployment (CI/CD) pipeline automates the testing, building, and deployment phases.
## 🚀 Features

* User Authentication: Secure signup, login, and role-based access control (Admin/Customer) using Spring Security and JWT.
* Product Catalog: Full CRUD operations for managing categories and products with image URLs and stock tracking.
* Shopping Cart: Dynamic cart management per user with real-time price calculations and inventory validation.
* Stripe Payments: End-to-end checkout workflow integrated with the Stripe API for handling mock or live charges.
* DevOps Ready: Multi-stage Docker optimization and a fully automated CI/CD pipeline for automated testing and zero-downtime deployment.

## 🛠️ Tech Stack

* Backend: Java / Spring Boot (Spring MVC, Spring Data JPA, Spring Security)
* Database: MySQL
* Authentication: JSON Web Tokens (JWT)
* External API: Stripe SDK
* DevOps: Docker, CI/CD Pipeline

## 📦 Prerequisites
Before running the project locally, ensure you have the following installed:

* [Docker Desktop](https://www.docker.com/products/docker-desktop/)
* [Git](https://git-scm.com/)
* A Stripe account (for your API keys)

## 🔧 Getting Started

   1. Clone the repository:

   ```
   git clone https://github.com/terfiel-22/terxiel_store.git
   cd terxiel_store
   ```
   
   2. Configure environment variables:
   Create a .env file in the root directory and add your credentials:

   ```env
   JWT_SECRET=
   STRIPE_SECRET_KEY=
   STRIPE_WEBHOOK_SECRET_KEY=
   ROOT_PASSWORD=
   DB_NAME=
   DB_USER=
   DB_PASSWORD=
   DB_PORT=
   ```
   
   3. Launch the application with Docker:

   ```
   docker-compose up --build
   ```
   
   The API will be accessible at http://localhost:8080.

## 🧪 CI/CD Pipeline
The project includes an automated CI/CD pipeline configuration file. On every push to the main branch, the pipeline automatically:

   1. Provisions a clean runtime environment.
   2. Runs the entire suite of unit and integration tests.
   3. Builds the production-ready application JAR file.
   4. Builds and pushes the new Docker image to the registry.
   5. Deploys the updated container directly to the cloud environment.


