# Transaction Service - Transaction Processor
## Requirements
- Java 21
- Maven 3.9+
- PostgreSQL

## Database configuration

Create a PostgreSQL database named `transactions_core`,
Default DB settings:
- URL: jdbc:postgresql://localhost:5432/transactions_core
- username: postgres
- password: postgres
Override with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.

Execute the schema.sql file in the postgres database to create required tables and sample data.

## API

GET `/api/v1/accounts/{accountId}/balance`

Example:

    {
        "accountId": 2,
        "accountNumber": "**C002",
        "balance": 500.00,
        "currency": "INR",
        "status": "ACTIVE"
    }

GET `/api/v1/accounts/{accountId}/transactions`

Example:

    {
    "content": [
        {
            "transactionId": 3,
            "reference": "06211cea-0ca1-4291-a62c-ccc72cf3aba4",
            "transactionType": "DEPOSIT",
            "entryType": "CREDIT",
            "amount": 1500.00,
            "balanceAfter": 3500.00,
            "createdAt": "2026-09-17T23:07:33.04157"
        },
        {
            "transactionId": 2,
            "reference": "df2f8299-5f7d-4e8f-b115-0121e5dbec59",
            "transactionType": "DEPOSIT",
            "entryType": "CREDIT",
            "amount": 500.00,
            "balanceAfter": 2000.00,
            "createdAt": "2026-09-17T23:05:53.908243"
        }
    ],
        "page": 0,
        "size": 10,
        "totalElements": 4,
        "totalPages": 1
    }

POST `/api/v1/transactions/deposit`

Example:

request:  

    {
        "amount": 1500.00,
        "accountId":1
    }

response:

    {
        "transactionId": 3,
        "transactionType": "DEPOSIT",
        "amount": 1500.00,
        "status": "COMPLETED",
        "reference": "06211cea-0ca1-4291-a62c-ccc72cf3aba4",
        "createdAt": "2026-09-17T23:07:33.0395759"
    }

POST `/api/v1/transactions/withdraw`

Example:

request:

    {
        "amount": 200.00,
        "accountId":1
    }

response:

    {
        "transactionId": 4,
        "transactionType": "WITHDRAWAL",
        "amount": 200.00,
        "status": "COMPLETED",
        "reference": "95aebdab-2532-4748-b6a7-e2ea45adc3b8",
        "createdAt": "2026-09-17T23:20:11.1697007"
    }

POST `/api/v1/transactions/transfer`

Example:

request:

    {
        "amount": 200.00,
        "fromAccountId":1,
        "toAccountId":2
    }

response:

    {
        "transactionId": 5,
        "transactionType": "TRANSFER",
        "amount": 200.00,
        "status": "COMPLETED",
        "reference": "da413e4e-0061-4a41-b161-4a25545a3a2f",
        "createdAt": "2026-09-17T23:27:12.0253118"
    }  



## Build and Run Junit

    mvn clean install
    java -jar transaction-core-1.0.0.jar

    I have also enabled swagger UI , we can access the API Docs by http://localhost:8080/swagger-ui/index.html

The unit test runs without Spring or PostgreSQL.
