# Drone Application
This is a sample Java / Maven / H2 Database/ Spring Boot (version 2.7.8) application that can be used as a starter for creating Drone Delivery System.
The Drone is capable to cary to device from point to point (The assumption of the destination is not yet developed but it will be developed on the next release).
## How to Run
This application is packaged as a jar which has Tomcat embedded. No Tomcat or JBoss installation is necessary. You run it using the **java -jar** command.
* Clone this repository
* build the project and run the tests by running **mvn clean package**
* Once successfully built, you can run the service by one of these two methods:
```
        java -jar target/drone-1.0-SNAPSHOT.jar
or
        mvn spring-boot:run
```
* The application will run on Port **8080** 
## About the Services
### Register new Drone
To register a new Drone you need to follow the example below. 
```
POST /api/drones
Accept: application/json
Content-Type: application/json
{
"serialNumber" : "{Serial number 100 characters max }",
"model" : "{Lightweight|Middleweight|Cruiserweight|Heavyweight}",
"weight" : {Number},
"batteryCapacity" : {Number}
"state": {IDLE|LOADING|LOADED|DELIVERING|DELIVERED|RETURNING}.
}
RESPONSE: HTTP 201 (Created)
```
#### Constrains 
* **Serial Number** is mandatory and has 100 characters max.
* **Model** is mandatory.
* **Weight** is mandatory and must be between 100 to 500 gr.
* other attributes are optional.
### Load Drone
To load products into drone you need to follow the example below. 
```
PUT /api/drones
Accept: application/json
Content-Type: application/json
[
    {
        "name": "{allowed only letters, numbers, ‘-‘, ‘_’}",
        "code": "{allowed only upper case letters, underscore and numbers}",
        "weight": {number},
        "image": {String}
    }
]
RESPONSE: HTTP 200 (Success)
```
#### Constrains 
* **Name** is mandatory and allowed only letters, numbers, ‘-‘, ‘_’.
* **Code** is mandatory and allowed only upper case letters, underscore and numbers.
* **Weight** is mandatory.
* other attributes are optional.

#### Implementation assumptions
* List of products need to be send and one drone need to serve it. so the sum of the weights of the products need to be equal or less than the available drone otherwise it will respond with no drone available to serve. 
(Future enhancement: multiple drone can serve the List i will used the maximum sub array that can be served for one drone and repeat this process until no items remaining on the list)
* All the product that is send it will be saved into H2 database even it will not delivered but if it will not delivered to keep track wich products are served and which are not.
* Transaction Id will be saved into products and to the drone that will serve the list of products and it will be removed once from the drone once the item is delivered. this help us in the implementation of checking loaded medication items for a given drone.
* Best match drone will be selected from available drone list if this list is empty it will throw an exception RecordNotFound
* For selecting the best drone match I used min heap to select the minimum available drone that can serve this list. 
* The best match drone will be removed from the available drone list. 
* If no drone found the application will throw WeightExceedException that demonstrate that the app cannot serve the weight of these products. 
* Once the application found the best match drone into priority queue workingQueueDrone and there's a scheduler that is run every two second that serve the workingQueue.
* And every 1000 seconds the state of the drone will be changed.  LOADING, LOADED, DELIVERING, DELIVERED, RETURNING, IDLE (Not that every two state will take 1000 seconds for example LOADING|LOADED will take 1000 seconds)
* After the drone will return it will reduce the battery by 20% and i will return back if the battery capacity more than 20% to the available queue list. 
### Check Drone items
```
GET /api/drones/products/{id}
Accept: application/json
Content-Type: application/json
RESPONSE: HTTP 200 (Success)
```
#### Constrains 
* **id** the id of the Drone is mandatory.
### Get Drone Battery
```
GET /api/drones/battery/{id}
Accept: application/json
Content-Type: application/json
RESPONSE: HTTP 200 (Success)
```
#### Constrains 
* **id** the id of the Drone is mandatory.
### Get Available Drone
```
GET /api/drones/available
Accept: application/json
Content-Type: application/json
RESPONSE: HTTP 200 (Success)
```
## Future Enhancement
* MicroService architecture i will use kafka in this scenario because kafka will save the message as long as we need it to be saved. so it will be easily find any request that cannot be delivered for any reason (battery low drones, no available drones)
    * Order Service: Take the order from the client and select which warehouse (warehouse that have ) will be nearest from the client and send this request to the drone service   
    * Drone Service: Drone service take the request and select the best match drone to handle the request and send the request to product service and also handle the drones that need to be recharge.
    * Product Service: product service will load the drones and and mark the drones as loaded. 
* multiple drone can serve the List of products by using maximum sub array that can be served for one drone and repeat this process until no items remaining on the list
* The capacity of consuming  the battery will be calculated based on the destination the drone will be delivered. so some requested will not be delivered because no drones can travel to the target. 
* The logs need more enhanced so by sending RequestId, ServiceId then the log message.
## Last but not least
All of this assumptions will be implemented as soon as possible on the next releases. 
Follow this repo to get updated with the new updates.  
### Questions and Comments: karim.omaya.elfaham@gmail.com