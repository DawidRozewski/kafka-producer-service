# 📡 Kafka Producer Service

## 📌 Overview
This service is responsible for **publishing messages to Apache Kafka**.  
It provides an API to send messages, which are then produced to a specific Kafka topic for further processing by consumers.

Kafka enables **asynchronous, event-driven communication** between microservices, improving scalability and decoupling components.  
This service integrates with **Spring Kafka** for easy interaction with Kafka brokers.

---

## 🏗️ About This Project
This project is designed as a **skeleton for microservices architecture**, providing a foundation for:
- **Learning event-driven microservices patterns**
- **Testing Kafka producers and consumers**
- **Experimenting with message reliability and monitoring**
- **Exploring how to secure microservices endpoints using Keycloak and OAuth2**

The focus is on demonstrating how microservices interact rather than delivering a full production-ready system.

---

## 🛠️ Tech Stack
- **Java 21**
- **Spring Boot 3.4.3**
- **Spring Kafka**
- **Spring Security + OAuth2 + Keycloak**
- **Maven 3.14.0**
- **Docker**
- **@EmbeddedKafka (for integration testing)**

## 🛠️ Infrastructure Stack (Docker)
- **Keycloak**
- **Kafka**
- **Kafka UI**
- **Prometheus**
- **Grafana**
- **MongoDB**
---

## 🚀 Running Kafka Producer Service
To run this service, make sure you have first **set up the environment** by following the instructions in the **[Config Server README](https://github.com/DawidRozewski/config-server)**.

Once the required services are running, start Kafka Producer Service with:

```bash
mvn spring-boot:run
```

---

## 🔑 Keycloak Setup (Manual Configuration)
To encourage users to explore Keycloak, this project does not include a pre-configured setup.  
Follow these steps to configure authentication manually:

### **1️⃣ Login to Keycloak Admin Console**
- Open a browser and navigate to: [http://localhost:8085/admin](http://localhost:8085/admin)
- Login using default credentials (`admin` / `admin`).
- Ensure you are working in the **master realm** (default setup).

### **2️⃣ Create a New Client**
- Navigate to **Clients** → **Create**.
- Set **Client ID** to `${your-client-id}`.
- Turn on **Client authentication** and **Authorization**.
- Configure **Root URL**: `http://localhost:8082`
- Save and generate a client secret.

### **3️⃣ Create Roles within the Client**
- Navigate to **Clients** → **${your-client-id}** → **Roles**.
- Add two roles: `admin` and `user`.

### **4️⃣ Create a User and Assign Roles**
- Navigate to **Users** → **Add User**.
- Set the username and enable the user.
- Set a password in the **Credentials** tab.
- Navigate to **Users** → Select the user → **Role Mappings**.
- Assign the previously created roles (`admin` or `user`) under **Client Roles**.

### **5️⃣ Retrieve JWT Token**
Authenticate using Keycloak’s token endpoint:

```bash
curl -X POST "http://localhost:8085/realms/master/protocol/openid-connect/token" \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "client_id=${YOUR_CLIENT_ID}" \
-d "client_secret=${YOUR_CLIENT_SECRET}" \
-d "username=${user}" \
-d "password=${password}" \
-d "grant_type=password"
```

Copy the `access_token` and **use it in API requests** if needed.

---

## 📋 Microservices Interactions

The following services interact with this Kafka producer:

1. **[Kafka Consumer Service](https://github.com/DawidRozewski/kafka-consumer-service)** - Consumes messages from Kafka.
2. **[API Gateway Service](https://github.com/DawidRozewski/api-gateway-service)** - Routes requests and provides authentication.
3. **[Eureka Server](https://github.com/DawidRozewski/eureka-server)** - Service discovery.
4. **[Config Server](https://github.com/DawidRozewski/config-server)** - Provides a configuration for this project.

---

## 🔄 Refactoring Suggestions

Looking to enhance this project? Here are some key areas for improvement:

1. **Ensure Reliable Message Delivery**
   - Implement transactional message sending to guarantee message consistency.
   - Use Kafka’s idempotent producer feature to avoid duplicate messages.

2. **Enhance Error Handling with a Dead-Letter Queue (DLQ)**
   - Redirect failed messages to a DLQ topic for later analysis.
   - Implement a **retry mechanism** before moving messages to DLQ.

3. **Implement Security Best Practices**
   - Enable authentication and encryption for Kafka communication.
   - Restrict topic access using Kafka ACLs.

4. **Ensure all exceptions are handled properly**
   - Currently, exception handling might not cover all edge cases.

5. **What if Kafka is unavailable?**
   - If Kafka is down, messages could be lost, or the producer might fail silently.
   - Implement a **Circuit Breaker Pattern** (e.g., using Resilience4j) to detect failures and temporarily block requests until Kafka is back.
---

📢 **This project is a microservices skeleton meant for learning, experimentation, and testing new ideas.**  
Feel free to modify and extend it to fit your needs! 🚀
