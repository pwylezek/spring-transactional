# Repository for transactional mechanism in Spring comparison

## How to run:
1. `docker-compose up -d` in order to run postgres db
2. Run app via starting `Application.main()`
3. Import `Skillup Transactional.postman_collection.json` to Postman
4. Save patient using POST v1 or v2 endpoints
5. Adjust GET endpoint `localhost:8080/v1/patients/311f51f4-2164-4f3a-949f-b473236c46c0/external-results?count=5` path using saved patient id (simply replace uuid with obtained value from save endpoint)
6. Run performance tests using postman for GET external-results endpoint