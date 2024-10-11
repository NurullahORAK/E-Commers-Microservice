# E-Commerce Microservices Project

This is a small e-commerce platform developed using a **microservices architecture**, which ensures scalability and flexibility by decoupling services. The project includes several key components, including service discovery, routing, and inter-service communication.

### Key Features:
- **Product Management:** Add, update, and retrieve product details.
- **Order Service:** Place and manage customer orders.
- **Inventory Service:** Manage stock and inventory updates.
- **Customer Service:** Handle customer data and authentication.
- **Service Discovery:** Managed with **Eureka** for registering and discovering microservices.
- **API Gateway:** Implemented using **Spring Cloud Gateway** to handle routing between microservices.
- **Inter-service Communication:** Achieved with **RestTemplate** for calling and integrating services.

### Technologies Used:
- **Java & Spring Boot** (for microservices)
- **Spring Cloud Eureka** (for service discovery)
- **Spring Cloud Gateway** (for API Gateway)
- **RestTemplate** (for communication between microservices)
- **H2 Database** (in-memory database for demo purposes)
- **Docker** (optional, for containerizing the services)

### How to Run:
1. Clone the repository.
2. Start Eureka Server.
3. Run all microservices (Product, Order, Inventory, Customer).
4. Launch the Gateway for routing requests.
5. Use Postman or another tool to interact with the API.

### Future Improvements:
- Add load balancing with **Ribbon** or **Spring Cloud LoadBalancer**.
- Implement **Feign Client** for a more declarative approach to inter-service communication.
- Integrate a real database like **MySQL** for production use.

This project showcases the power of microservices architecture in building scalable and modular applications, specifically in the e-commerce domain.
