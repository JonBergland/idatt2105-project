# idatt2105-project
The optinal project in the subject IDATT2105 Fullstack at NTNU

# To run the project

## Docker
If you have docker running on your PC you can run this command to start the container:
```sh
docker compose up --build
```
*NB!: The build time on first compose is quite long (~5 min) 

To stop it, click CTRL + C and type this command:
```sh
docker compose down
```

## Without Docker
### Frontend
To run the frontend use these commands:
```sh
npm install
npm run dev
```

### Backend
To run the backend use these commands:
```sh
mvn clean install
mvn spring-boot:run
```

## Tests
To check the coverage on the frontend:
### Frontend
To run the tests in the frontend use this command:
```sh
npm run test:unit
```

For test coverage, run:
```sh
npm run test:coverage
```

### Backend
For tests run: 
### Frontend
To run the frontend use these commands:
```sh
mvn clean test
```

For test coverage, run this after running the tests:
```sh
mvn jacoco:report
```
The report can be found in target/site/jacoco/index.html and can be opened in a browser