# Event Feedback Analyzer

## Project Overview

**Event Feedback Analyzer** is a Spring Boot backend application that collects written feedback for events and automatically performs **sentiment analysis** using the Hugging Face model  **cardiffnlp/twitter-roberta-base-sentiment**.

The system analyzes each feedback message as:

- **Positive**
- **Neutral**
- **Negative**

and provides a **sentiment summary per event**.

## Functionality

- Create events  
- Submit feedback for events  
- AI-powered sentiment analysis  
- Retrieve sentiment breakdown per event  
- In-memory H2 database  
- Fully documented API via Swagger  
- Centralized error handling  
- DTO validation on all inputs 

## Setup and Running

### Prerequisites
- Java 21  
- Maven 3.9+  
- Internet connection (model downloads automatically)

### Clone the repository

```bash
git clone https://github.com/Myshk-IN/event-feedback-analyzer.git
cd event-feedback-analyzer
```

### Build the project

```bash
mvn clean install
```

### Run the application

```bash
mvn spring-boot:run
```
Application starts at http://localhost:8080.

### H2 Database

H2 console:
```bash
http://localhost:8080/h2-console
```

Example credentials:
```bash
JDBC URL: jdbc:h2:file:./data/mydb
User: sa
Password: password
```
Specify in src\main\resources\application.properties: spring.datasource.url, spring.datasource.username, spring.datasource.password.

### Hugging Face Inference API:

Specify your Hugging Face API key in src\main\resources\application.properties huggingface.api.key.

## API Documentation (Swagger UI)

Swagger UI is available at:
```bash
http://localhost:8080/swagger-ui.html
```

## API tests with Postman

1. Import the Postman collection located at src\test\postman by dragging it into Postman.
2. Import event_id as a variable.
3. Pick and choose which tests you wish to run. Tests are focused on project's endpoints.
