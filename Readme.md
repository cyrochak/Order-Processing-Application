
# **Real-Time Order Processing System**

A real-time order processing system built with **Java**, **Spring Boot**, **Apache Kafka**, and **MongoDB**. This project demonstrates how to handle asynchronous order processing using Kafka as the messaging platform and MongoDB for data persistence.

---

## **Features**
- Real-time processing of orders with Kafka producers and consumers.
- MongoDB as a NoSQL database for storing order details.
- Kafka topics for order management and event-driven communication.
- Scalable and reliable architecture using distributed systems.

---

## **Tech Stack**
- **Programming Language:** Java  
- **Frameworks:** Spring Boot, Spring Data MongoDB  
- **Messaging System:** Apache Kafka  
- **Database:** MongoDB  
- **Build Tool:** Maven  

---

## **Getting Started**

Follow the steps below to set up and run the project.

### **1. Prerequisites**
- **Java 7** or higher (Tested on Java 17) ([Download Java](https://adoptium.net/))  
- **Apache Kafka** (Tested on Kafka 2.8.0) ([Download Kafka](https://kafka.apache.org/downloads))  
- **MongoDB** ([Download MongoDB](https://www.mongodb.com/try/download/community))  
- **Maven** ([Download Maven](https://maven.apache.org/download.cgi))  
- **Lombok Plugin** for your IDE (if not already configured).  

---

### **2. Project Setup**
   * **Clone the Repository:**  
      ```bash
      git clone https://github.com/your-username/real-time-order-processing.git
      cd real-time-order-processing
      ```

   * **Install Dependencies:**  
      Run the following Maven command to download and install all dependencies:  
      ```bash
      mvn clean install
      ```

   * **Configure MongoDB:**  
      Ensure MongoDB is running on `localhost:27017`. Create a database named `order_processing`.  

   * **Start Kafka and Zookeeper:**  
      - Navigate to your Kafka installation directory.  
      - **Start Zookeeper:**  
        ```bash
        bin/zookeeper-server-start.sh config/zookeeper.properties
        ```
      - **Start Kafka:**  
        ```bash
        bin/kafka-server-start.sh config/server.properties
        ```

   * **Update Application Properties (if needed):**  
      If MongoDB or Kafka is running on a different host/port, update `application.properties` in the `src/main/resources/` directory:  
      ```properties
      spring.data.mongodb.uri=mongodb://localhost:27017/order_processing
      spring.kafka.bootstrap-servers=localhost:9092
      ```

---

### **3. Running the Application**
   * Run the following Maven command to start the application:  
      ```bash
      mvn spring-boot:run
      ```

   * Use tools like **Postman** or **cURL** to test the REST endpoints:  
      - **POST** request to `http://localhost:8080/orders`:  
        ```json
        {
          "id": "123",
          "product": "Phone",
          "quantity": 2,
          "price": 999.99,
          "status": "NEW"
        }
        ```

      - The order will be sent to the Kafka topic, processed, and saved in MongoDB.  

---

## **Project Structure**

```plaintext
src
├── main
│   ├── java
│   │   └── com
│   │       └── cyroc
│   │           └── orderprocessing
│   │               ├── OrderProcessingApplication.java
│   │               ├── model
│   │               │   └── Order.java
│   │               ├── controller
│   │               │   └── OrderController.java
│   │               ├── kafka
│   │               │   ├── KafkaProducer.java
│   │               │   └── KafkaConsumer.java
│   │               └── service
│   │                   └── OrderService.java
└── resources
    ├── application.properties
```

---

## **Still need work to do**
- **Error Handling:** Add proper error handling mechanisms for Kafka and MongoDB failures.  
- **Order Status Updates:** Implement status transitions (e.g., "Processing", "Completed").  
- **Scalability Enhancements:** Optimize Kafka configurations for better throughput.  
- **Unit Testing:** Add unit and integration tests for key components.

---
