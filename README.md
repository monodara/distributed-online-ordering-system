# DSFinalProject -- Sigma


## Report and Video

- For the full project report, click [here](Additional information/report/ProjectReport-Sigma.docx.pdf).
- A demonstration video is [here](https://www.youtube.com/watch?v=4oymc5YMuq8).


## A brief summary of what your code does

Three components make up the Sigma system: a single broker, a client application, and an expandable number of eateries (include Seafood, SunSalads,TwoStarPizza and ThaiStyle).
To coordinate communication between client instances and the restaurant of the client's choice is the broker's function.
Restaurants are able to send menu and pricing details to the active broker application (i.e., companies that already offer a service outside the Sigma system).
Customers can ask the broker for a list of restaurants and choose one or more from it if they want to have food from one of these establishments.
After making a choice, the client application enables the user to put together and send an order to one or more restaurants.


## System Architecture & Operation

![image](Additional information/photo/System_Architecture.png)


## Instructions on how to compile and run your code

- First git clone with HTTPS.
- Run mvn clean to remove the generated target files from the root directory.
```
mvn clean
```
- Compile and package the project itself to a local repository.
```
mvn install -pl core
mvn compile
```
- Run broker in a separate window.
```
mvn exec:java -pl broker
```
- In new windows running each restaurant.
```
mvn exec:java -pl seafood
mvn exec:java -pl sunsalads
mvn exec:java -pl thaistyle
mvn exec:java -pl twostarpizza
```
- Run the client, also can define the consumer name, 
  then show all restaurants name, consumer can choose one of them or some to order the menu.
```
mvn -q exec:java -pl client
mvn -q exec:java -pl client -Dexec.args="<Customized consumer name>"
```
- 0 to exit the order service, enter 1-3 to access the next selection.
  - 0: Cancel order / Return to main menu
  - 1: Finish order and proceed to payment
  - 2: Add item to order
  - 3: Remove item from order
- Enter 2 to display the menu and prices offered by the restaurant, 
  enter the corresponding menu number to add the order and calculate the cost to be paid.
- Afterwards, you have the opportunity to choose to pay the bill or choose another restaurant's menu at the same time.
- Once the order is submitted, the window of the running broker will show how much the Sigma platform will charge for this order.
- Then the window of the restaurant where the order is placed will show the amount of the order and the dishes.

