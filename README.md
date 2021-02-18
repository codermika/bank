# Banking

The banking application is a simple example Spring boot application.

It allows you to get the bank statement. You can also deposit and withdraw money from the account.

## Running environment

You should have:

* The maven tool (the application has tested with Apache Maven 3.6.3)
* The Java 11 JRE (the application has tested with openjdk version "11.0.10" 2021-01-19)
* The curl tool (or similar tool to send requests to the service)

## Running unit tests and starting application

Run the unit tests and start the service with the command:

	$ mvn clean test spring-boot:run

## Using application

The application contains already the initial data when it is started. 

The internal application database contains only one account `FI123`. The application supports only the operations which are mentioned below. 

### Get bank statement

To get the bank statement use the command:

	$ curl -v localhost:8080/api/accounts/FI123/statement

The response could contain the line like this:

	$ [{"date":"2006-07-29T12:34:00","amount":111.11,"balance":222.22}]

### Deposit money

To deposit amount into the account use the command:

	$ curl -X POST localhost:8080/api/accounts/FI123/deposit -H 'Content-type:application/json' -d '{"amount": 333.33}'

Application doesn't respond anything (only the HTTP 200 response code if everything goes fine).

### Withdraw money

To withdraw amount from the account use the command:

	$ curl -X POST localhost:8080/api/accounts/FI123/withdraw -H 'Content-type:application/json' -d '{"amount": 333.33}'

Application doesn't respond anything (only the HTTP 200 response code if everything goes fine).

### Error situations

If the error occurs, there are not implemented meaningful error messages currently. You get only the technical error message, for example:

	$ {"timestamp":"2021-02-15T05:04:37.424+00:00","status":500,"error":"Internal Server Error","message":"","path":"/api/accounts/FI1234/withdraw"}
	
Please look the application log for the more information.

Possible error situations are:

* the amount is null or <= 0 in the deposit or withdraw operations
* there are not enough balance in the account in the withdraw operation
* the api url syntax is wrong or the JSON format is wrong