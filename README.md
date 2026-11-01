# PharmaLocator

**Project Name:** PharmaLocator
**Student:** ISHIMWE Emmanuel
**Student ID:** 26424
**Course:** INSY 8322 – Web Technology and Internet
**University:** Adventist University of Central Africa (AUCA)

## Technologies

- Java 25 (LTS)
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Database Setup

1. Install and start PostgreSQL.
2. Create a database named `pharma_locator`.
3. Set the `DB_PASSWORD` environment variable to the password for your PostgreSQL user. The application reads it through `spring.datasource.password=${DB_PASSWORD}`; do not commit your password to the repository.

## How to Run

Install Java 25 (LTS), then run the Spring Boot application from the project root:

```bash
export DB_PASSWORD="your-postgres-password"
./mvnw spring-boot:run
```

On Windows PowerShell, set the variable and run:

```powershell
$env:DB_PASSWORD = "your-postgres-password"
.\mvnw.cmd spring-boot:run
```

The API starts on `http://localhost:8082`

## Testing

Import `pharma-locator-midterm.postman_collection.json` into Postman.

## API Endpoints

### Qualification
- POST /api/qualifications/save
- GET /api/qualifications/all
- GET /api/qualifications/{id}
- PUT /api/qualifications/update/{id}
- DELETE /api/qualifications/delete/{id}
- GET /api/qualifications/unused

### Pharmacy
- POST /api/pharmacies/save
- GET /api/pharmacies/all
- GET /api/pharmacies/{id}
- PUT /api/pharmacies/update/{id}
- DELETE /api/pharmacies/delete/{id}
- GET /api/pharmacies/free

### Pharmacist
- POST /api/pharmacists/save
- GET /api/pharmacists/all
- GET /api/pharmacists/{id}
- PUT /api/pharmacists/update/{id}
- DELETE /api/pharmacists/delete/{id}
- GET /api/pharmacists/by-qualification?name=
- PUT /api/pharmacists/{pharmacistId}/qualifications/{qualificationId}
- PATCH /api/pharmacists/{pharmacistId}/pharmacy?licenseNumber=

### Customer
- POST /api/customers/save
- GET /api/customers/all
- GET /api/customers/{id}
- PUT /api/customers/update/{id}
- DELETE /api/customers/delete/{id}
- GET /api/customers/by-district?district=
- GET /api/customers/of-pharmacist/{pharmacistId}

### Drug Request
- POST /api/drug-requests/save
- GET /api/drug-requests/all
- GET /api/drug-requests/{id}
- PUT /api/drug-requests/update/{id}
- DELETE /api/drug-requests/delete/{id}
- PATCH /api/drug-requests/{id}/status?status=
- GET /api/drug-requests/by-status?status=
- GET /api/drug-requests/between?start=&end=
- GET /api/drug-requests/stats/by-status
- PATCH /api/drug-requests/cancel-day?pharmacistId=&date=
- GET /api/drug-requests/page?page=0&size=5&sort=requestDate,desc

### Sale Line
- POST /api/sale-lines/save?drugRequestId=
- GET /api/sale-lines/all
- GET /api/sale-lines/{id}
- GET /api/sale-lines/customer/{customerCode}
- DELETE /api/sale-lines/delete/{id}

### Dashboard
- GET /api/dashboard/summary
