# Tool Rental Application

## Overview

This application is a robust tool rental system designed to manage the rental process for a variety of tools. It handles complex pricing rules, including different charges for weekdays, weekends, and holidays, as well as applying discounts.

## How to Run the Project

### Building the Application

**Build the application:**
   ```bash
   ./gradlew build
   ```

### Testing the Application

**Run unit tests:**
   ```bash
   ./gradlew test
   ```

### Running the Application

**Run the application locally:**
   ```bash
   ./gradlew bootRun
   ```

**Using Docker Compose:**
   ```bash
   docker-compose up --build
   ```

   This command builds the Docker image for the application and starts all services specified in `docker-compose.yml`, including the PostgreSQL database.

## Application Architecture

### MVC Pattern

This application follows the Model-View-Controller (MVC) architecture pattern, which separates the application into three interconnected components:

- **Model**: Represents the data and business logic (e.g., Tool, ToolType, RentalAgreement)
- **View**: Handles the presentation layer (in this case, it's the API responses)
- **Controller**: Manages the flow between Model and View (e.g., RentalController)

This separation helps manage complexity, facilitates focused testing, and improves maintainability.

### Service Layer

We've implemented a service layer (e.g., RentalService) to encapsulate business logic. This approach:
- Keeps controllers thin and focused on request/response handling
- Allows for reuse of business logic across different parts of the application
- Facilitates easier unit testing of business logic

### Repository Pattern

We use Spring Data JPA repositories to abstract the data layer. This decision:
- Simplifies database operations
- Allows for easy switching between different database implementations
- Provides a consistent interface for data access

### Database

- **PostgreSQL**: Used for production. Configured in the `.env` or the `docker-compose.yml` file for future e2e tests.
- **H2 Database**: Used for testing purposes. This in-memory database ensures quick-running tests without the need for complex database setup.

### Testing

Testing strategy I would follow:

- **Unit Tests**: Fast, focused tests for individual components (e.g., RentalServiceTest) and verifying business logic fast
- **Integration Tests**: Test the interaction between components (TODO)
- **E2E Tests**: Simulate real user scenarios, testing the entire system, thin as most brittle

Testing is crucial for verifying the logic and establishing a contract within the code. Our emphasis on unit tests ensures that we can run tests frequently without the overhead of external dependencies.

### Docker Integration

We use Docker and Docker Compose to:
- Ensure consistency between development and production environments
- Simplify the setup process for new developers
- Facilitate easy deployment and scaling in cloud environments

### Validation

- **Service Layer Validation**: We implement thorough validation in the service layer (e.g., checking for valid rental day count and discount percentage)
- **HTTP Validation**: While out of scope for this project, in a production scenario, validating inputs at the HTTP layer would be crucial for maintaining data integrity and security
- **HTTP Validation**: TODO

### Error Handling

We've implemented custom exceptions (e.g., InvalidRentalDayCountException) to provide clear, specific error messages. This approach:
- Improves debugging and troubleshooting
- Enhances the API's usability by providing meaningful feedback to clients

## Design Decisions and Rationale

1. **Separation of Concerns**: By adhering to the MVC pattern and introducing a service layer, creating a modular, maintainable codebase where each component has a clear, single responsibility.

2. **Immutable Data Models**: We've designed our data models (e.g., Tool, ToolType) to be immutable where possible. This decision enhances thread safety and simplifies reasoning about the application's state.

3. **Use of BigDecimal for Financial Calculations**: To ensure precision in financial calculations, we use BigDecimal instead of floating-point types.

4. **Comprehensive Testing Suite**: Our extensive test coverage, including unit, and E2E tests, ensures the reliability and correctness of the application.

5. **Docker Integration**: By containerizing our application and its dependencies, we ensure consistency across different environments and simplify the development and deployment processes.

6. **Flexible Holiday Handling**: Instead of hardcoding holidays, we've implemented a flexible system that allows for easy addition and modification of holidays directly into the databse.

## Future Enhancements

1. Implement authentication and authorization
2. Add a caching layer to improve performance
3. Implement a frontend user interface
4. Expand the API to include more operations (e.g., tool inventory management)
5. Implement more advanced reporting features