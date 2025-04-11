# idatt2105-project
The optional project in the subject IDATT2105 Fullstack at NTNU.
The group members are:
Vetle Bjørnøy
Florian Leucht
Jon Bergland

# To run the project
To connect to our NTNU database, you need to be connected to the NTNU-internet. This can be done via the Cisco Secure Client.

You also have to create a .env file with the environmental variables. An example on how it should look are in teh .env.example file

## With Docker
If you have docker running on your PC you can run this command to start the container:
```sh
docker compose up --build
```
*NB!: The build time on first build is quite long (~5 min +)*

To stop the container, click CTRL + C and type this command:
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


## API Docs
The Swagger documentation for API endpoints can be found at:
[Swagger UI Documentation](http://localhost:8080/swagger-ui/index.html#/),
while the application is running.