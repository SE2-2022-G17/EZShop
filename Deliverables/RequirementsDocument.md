# Requirements Document 
**Authors:**
<br/>
S236053 Andrea    Amato<br/>
S288265 Francesco Blangiardi<br/>
S286645 Matthieu  Brunon<br/>
S287949 Gabriele  Sara<br/>

**Date:** 21/04/2021

**Version:** 1.1

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders

| Stakeholder name  | Description | 
| ----------------- |-----------|
|Owner| The owner/manager of the shop, also administrator. |
|Cashier| Employee who manages sales. |
|Inventory Manager| Employee who manages the goods. |
|Accountant | Employee who manages the funds of the shop. |
|Our team| Developers: For possible updates. |
|Suppliers | Companies that supply shopping goods.|
|Customers | Interact with cashier and buy goods. |
|Point of sale| Safeguard the exchange of money. |

# Context Diagram and interfaces

## Context Diagram

~~~plantuml
@startuml EZshop
:Inventory\nManager: -l- (EZShop)
:Accountant: -r- (EZShop)
:Cashier: -u- (EZShop)
:Product: -d-> (EZShop)
:Owner: -u- (EZShop)
:PoS Device: -- (EZShop)
@enduml
~~~

## Interfaces

| Actor | Logical Interface | Physical Interface  
| :---: |:-----------------:| :-----
| Owner            | GUI #1 | Screen, mouse & keyboard 
|Accountant        | GUI #2 | Screen, mouse & keyboard 
|Inventory Manager | GUI #3 | Screen, mouse & keyboard, barcode reader
| Cashier          | GUI #4 | Cash Register (basic keyboard, screen, barcode reader)
|Product           | Barcode | Barcode reader
|PoS Device| Cash register API | Cash register USB port

# Stories and personas


## Persona 1: Cashier

Waimarie Sørensen, 23, part-time cashier, also studies at the university.

As a cashier, Waimarie is responsible for handling sales. She scans the product and ensures that the transaction is smooth and without errors.

Goals: Wants to handles the sales swiftly without making mistakes.
​

*It's Monday morning.*

*Waimarie opens her working station.*

*She is afraid to make mistakes because she stayed up late last night. If the software that allows her to handle sale was simple, she would have less probability of making mistakes (and pay for it). That way, she would save her money for buying more drinks.*

*She also wants to be quick, because there is a lot of affluence today and she can't keep customers waiting for too long.*

## Persona 2: Inventory manager

Óengus Inx, 45, inventory manager, father of three.

As an inventory manager, Óengus manages the warehouse of the shop so he is responsible for ordering the products for the shop. He also registers delivered supplies in the inventory.

Goals: Wants know what is in the shop so that he can know what products to order without the shop being out of stock or oversupplied.
​

*It's Wednesday afternoon.*

*Óengus has spent the entire morning checking the shelves and the backstore to keep track of what products are in stock and what he needs to order for the next delivery.*

*At the moment Óengus orders the products on his computer, the only information he has is what he has checked earlier. He may forget something or make wrong decision based on partial/outdated data.*

## Persona 3: Accountant

Christian Wolff, 34, accountant, single.

As an accountant, Christian keeps, inspects, and analyses financial accounts of the EZShop.

Goals: Wants to manage the accounting more efficiently.
​

*It's Thursday afternoon.*

*Christian opens his desktop computer at his EZShop's office. He wants to quickly register the receipts and purchase invoices of the day.*

*Everyday, Christian has to register receipts and invoices, while once a month he registers various company costs (salaries etc...).*

*Christian is not interested in staying at the office long. As soon as he is done, he leaves work to go to the shooting range.*

## Persona 4: Owner

Othis McSheppard, 69, owner, single father

As owner, Othis creates, manages and deletes personnel accounts. He also grants and revokes permissions to the users.

Goal: Wants to have an easy way to control the personnel's accounts by giving them access only to the part of the application they need.

*It's monday morning.*

*He just employed the new inventory manager. He logs in on his computer to quickly make the new user account so that the manager can get to work immediately and he can get back to play Candy Crush Saga.*



# Functional and non functional requirements

## Functional Requirements

| ID        | Description
| ------------- |-------------
|**FR1** | **HANDLE SALES**
|FR1.1   | Recognize items
|FR1.1.1 | Get input from barcode scanner
|FR1.1.2 | Get input from keyboard as an alternative to barcode scanner
|FR1.1.3 | Retrieve item information based on their ID (barcode)
|FR1.1.4 | Apply discounts if they are valid
|FR1.1.4.1 | Recognise Customer card
|FR1.2   | Display useful information to the cashier and the customer, such as item name, price, total, discounts...
|FR1.3   | Receive payment
|FR1.3.1 | Support payment with credit card
|FR1.3.2 | Support payment in cash
|FR1.3.2.1 | Tell the cashier how much change to give
|FR1.3.3 | Allow hybrid payment (cash + credit card or two separate credit cards)
|FR1.4   | Save transaction in sales history
|FR1.5   | Update Inventory
|FR1.6   | Print receipt
|FR1.7   | Allow the cashier to abort the transaction at any time
|**FR2** | **MANAGE CUSTOMERS**
|FR2.1   | System records Customer Information
|FR2.2   | Register a new customer card at the cash register 
|FR2.3 | Issue new Customer discounts
|FR2.4 | Delete Customer discounts
|**FR3** | **SUPPORT ACCOUNTING**
|FR3.1   | Display product sale statistics
|FR3.1.1 | View product sales over time
|FR3.1.2 | View rate of sale for product
|FR3.2   | Record shop expenses (supplies, employee salaries, etc.)
|FR3.3   | Record shop income
|FR3.4   | Display general shop statistics over time
|**FR4** | **MANAGE INVENTORY**
|FR4.1| Manage Product Reference
|FR4.1.1| Add product reference to Inventory
|FR4.1.2| Remove product reference from Inventory
|FR4.1.3| Edit product information
|FR4.1.4| Display list of references
|FR4.1.4.1| Filter search
|FR4.2  | Register quantity modifications 
|FR4.2.1| Add new items for a Product Reference
|FR4.2.2| Remove items of a Product Reference
|FR4.2.2.1| Remove lost/expired items
|FR4.2.2.2| Remove sold items 
|**FR5**|**MANAGE PERMISSION**
|FR5.1  | Manage personnel
|FR5.1.1| Create new personnel account
|FR5.1.2| Grant permission
|FR5.1.3| Revoke permission
|FR5.1.4| Delete personnel account
|FR5.2  | Authenticate personnel at their work station

## Non Functional Requirements

| ID  | Type | Description  | Refers to FR |
| :-: |:----:| ------------ | -------------|
|NFR1 | efficiency | response time in item scan < 0.5 s | FR1.1 
|NFR2 | usability | time to learn cash register functions < 4-8 hours | FR1<br/>FR2.2|
|NFR3 | usability | time to execute regular accounting operations < 2 hours | FR3 
|NFR4 | portability | application has to be compatible with Windows, MacOS and cash register | all 
|NFR5 | security | storing personal information (name, contact details) has to be compliant with GDPR | FR2.1
|NFR6 | maintainability | possibility to maintain and update application remotely | all
|NFR7 | security | Deploy daily backups | all 
|NFR8 | security | Perform automatic rollback in case of failure | all 
|NFR9 | reliability | Recovery from failure < 5-10 minutes | all
|NFR10 | security | Passwords should never be stored in plaintext, use a salted hash for good practice | 5 
|Domain | localisation | Euro as currency | FR1<br/> FR3


# Use case diagram and use cases

## Use case diagram

~~~plantuml
@startuml

actor Cashier as Cash

actor Accountant as Acc

actor "Inventory Manager" as Inv

actor Owner as Own

usecase "Authenticate" as Au

Cash --> Au
Acc --> Au
Inv --> Au
Own --> Au

@enduml
~~~
~~~plantuml
@startuml
left to right direction

actor Cashier as Cash

actor Accountant as Acc
actor Product as Prd
actor "Inventory Manager" as Inv
actor "PoS Device" as CCS
actor Owner as Own

usecase "Handle Transaction" as HT

usecase "Register Customer" as RC

usecase "Register Transaction" as AI

usecase "Create Discount" as CD
usecase "Delete Discount" as DD
usecase "View Statistics" as VS

usecase "Add Items to Inventory" as AII
usecase "Remove Items from Inventory" as RII
usecase "Add Product Reference to Inventory" as APRI
usecase "Remove Product Reference from Inventory" as RPRI

usecase "Create Personnel Account" as CUA
usecase "Delete Personnel Account" as DUA

Acc --> AI

Acc --> CD
Acc --> DD
Acc --> VS

Inv --> AII
Inv --> RII
Inv --> APRI
Inv --> RPRI

AII --> Prd
RII --> Prd
APRI --> Prd
RPRI --> Prd

Own -->  CUA
Own --> DUA

Cash --> HT

HT --> Prd
HT --> CCS

Cash --> RC


@enduml
~~~

### Use case 1, UC1 - Handle Transaction
| Actors Involved | Cashier</br> PoS Device</br>Product |
| --------------- | ------------- 
| Precondition    | The cashier is logged in.
| Postcondition   | Any successful transaction is registered, inventory and balance are up-to-date.
| Nominal Scenario | The cashier scans the bar codes of products and loyalty card, the application computes the total, payment is successful, the application then updates the inventory and registers the transaction. 
| Variants | a) Transaction is aborted for any reason.</br> b) A bar code is not recognized by the system.

| Scenario 1.1 | The customer pays in cash |
| :----------: | ------------------------- | 
| Precondition | The cash register has enough cash to give change. |
| Postcondition | Transaction is registered, inventory and balance are updated. Customer has paid the required amount. 
| Step #        | Description
| 1 | Cashier scans a bar code. 
| 2 | The application recognizes the bar code, adds it to the list and computes the subtotal. 
|   | Repeat 1 & 2 until every item is scanned. 
| 3 | Cashier selects cash payment. 
| 4 | Cashier puts the cash in the register and confirms.
| 5 | The system records the transaction, updates balance and inventory, prints the receipt.

| Scenario 1.2 | The customer pays with a credit card |
| :----------: | ------------------------- | 
| Precondition | The credit card system is operational. |
| Postcondition | Transaction is registered, inventory and balance are updated. |
| Step #        | Description  |
| 1 | Cashier scans a bar code. |
| 2 | The application recognizes the bar code, adds it to the list and computes the subtotal. |
|   | Repeat 1 & 2 until every item is scanned. |
| 3 | Cashier selects credit card. |
| 4 | The application waits until successful payment is confirmed by the PoS device. |
| 5 | The system records the transaction and updates balance and inventory, prints the receipt.|

| Scenario 1.3 | Hybrid Payment 
| :----------: | -------------- 
| Precondition | The credit card system is operational. 
| Postcondition | Transaction is registered, inventory and balance are updated 
|  Step # | Description 
| 1 | Cashier scans a bar code. 
| 2 | The application recognizes the bar code, adds it to the list and computes the subtotal. 
|   | Repeat 1 & 2 until every item is scanned. 
| 3 | Cashier selects the payment method (card or cash) and the amount to pay. 
| 4a | (Cash) Cashier puts the money in the cash register and confirms. 
| 4b | (Credit card) The application waits until successful payment is confirmed by the PoS device.
| | Repeat 3 & 4 until the customer has payed the required amount.
| 5 | The system records the transaction and updates balance and inventory, prints the receipt.


### Use case 2, UC2 - Register a Customer
| Actors Involved | Cashier |
| :-------------: | ----------------- | 
|  Precondition   | The cashier is logged in.</br>Customer is not already registered. |
|  Postcondition  | Customer informations are registered, customer is associated internally to a card ID (and fidelity card is given to customer) |
| Nominal Scenario | The cashier opens the registration GUI, inputs the customer name, contact details and scans a card to be associated to the customer, which is recorded by the system |
|  Variants     | Customer already owns a fidelty account. |

| Scenario 2.1 | Customer is already in the system.
| :----------: | ------------- 
| Precondition | Customer is already registered. 
| Postcondition | Registration is not performed. 
| Step # | Description 
| 1 | Cashier opens the dedicated GUI. 
| 2 | Cashier inputs customer data. 
| 3 | System recognizes that the contact details are already registered. 
| 4 | System interrupts the registration and notifies the cashier. 


### Use case 3, UC3 - Create Discount for Customers
| Actors Involved | Accountant |
| :-------------: | ---------- | 
| Precondition | There are customers registered into the application.</br>The accountant is logged in.|
| Post condition | A set of customer cards will be associated to a discount on a set of products |
| Nominal Scenario | The accountant opens the dedicated GUI, inputs discount informations and the system associates the discount to the selected cards. |
| Variants |


| Scenario 3.1 | Nominal case 
| :----------: | ------------ 
| Postcondition | A set of customer cards will be associated to a discount on a set of products 
| Step # | Description 
| 1 | The accountant opens the dedicated GUI. 
| 2 | The accountant selects the set of Customers (can be all, long time customers, new customers, long gone customers...) 
| 3 | The accountant selects the product(s) for which the discount applies (by category, by brand or by product id) 
| 4 | The accountant selects expiration date 
| 5 | The system associates the discount to the corresponding user cards.

### Use case 4, UC4 - Delete Discount 
| Actors Involved | Accountant |
| :-------------: | ---------- | 
| Precondition | The accountant is logged in.</br>There is an active discount. | 
| Postcondition | The selected discount are no longer usable. |
|  Nominal Scenario | The accountant opens the dedicated GUI, the system displays all the active discounts, the owner selects the ones they want to delete and confirms |
| Variants |

| Scenario 4.1 | Nominal case 
| :----------: | ------------ 
| Postcondition | The discount is no longer usable
| Step # | Description 
| 1 | The system displays existing discounts and the accountant selects at least one.
| 2 | The acountant hits the delete button and it's deleted.

### Use case 5, UC5 - Register transaction 
| Actors Involved | Accountant
| :-------------: | ----------
| Precondition    | A transaction has to be recorded
| Postcondition   | The balance is updated with respect to the transaction
| Nominal Scenario | The accountant opens the GUI, selects the type of transaction (expense or income) and the amount, inputs possible additional informations (motive, recipient... etc.), the system stores the transaction and updates the balance.

| Scenario 5.1 | Register Expense
| :----------: | ------------ 
| Precondition    | An expense has to be recorded
| Postcondition | Balance is updated
| Step # | Description 
| 1 | The accountant opens GUI
| 2 | The accountant selects the Expenses section
| 3 | The accountant clicks on the new expense button
| 4 | The accountant inputs expense informations (description, type, amount, date)
| 5 | The accountant clicks on add button
| 6 | The expense is added to the system and balance is updated

| Scenario 5.2 | Register Income
| :----------: | ------------ 
| Precondition    | An income has to be recorded
| Postcondition | Balance is updated
| Step # | Description 
| 1 | The accountant opens GUI
| 2 | The accountant selects the Income section
| 3 | The accountant clicks on the new income button
| 4 | The accountant inputs income information (description, type, amount, date)
| 5 | The accountant clicks on add button
| 6 | The income is added to the system and balance is updated


### Use case 6, UC6 - View Statistics/history
| Actors Involved | Accountant 
| :-------------: | ---------- 
| Precondition | The accountant is logged in. 
| Post condition | Desired statistic is displayed. |
| Nominal Scenario | The accountant opens the GUI, selects the desired statistics, inputs additional informations, and the system displays the desired information.

| Scenario 6.1 | Display Balance history |
| :----------: | ----------------------- | 
| Precondition | The system has kept track of the balance over some time |
| Post condition | A graph with the desired information is displayed |
| Step # | Description  |
| 1 | The accountant opens the dedicated GUI and selects Balance History |
| 2 | the accountant inserts the time window for which to compute the graph |
| 3 | the system computes the statistics and displays the corresponding graph |

| Scenario 6.2 | Display Expense History |
| :----------: | ----------------------- | 
| Precondition | There are expenses stored in the system |
| Postcondition | A graph with the desired information is displayed |
| Step # | Description  |
| 1 | The accountant opens the dedicated GUI and selects Expense History |
| 2 | The accountant inserts the time window for which to compute the graph|
| 3 | The accountant inserts the combination(s) of expense type he wants to see in the graph |
| 4 | The system computes the statistics and displays the corresponding graphs |

| Scenario 6.3 | Display Income History |
| :----------: | ---------------------- | 
| Precondition | There are incomes stored in the system |
| Postcondition | A graph with the desired information is displayed |
| Step # | Description  |
| 1 | The accountant opens the dedicated GUI and selects Expense History |
| 2 | the accountant inserts the time window for which to compute the graph|
| 3 | the system computes the statistics and displays the corresponding graphs |

| Scenario 6.4 | Display Product Sales |
| :----------: | --------------------- | 
| Precondition | There are restock expenses and transactions stored in the system |
| Post condition | A graph with the desired information is displayed |
| Step # | Description |
| 1 | The accountant opens the dedicated GUI and selects Product Sales |
| 2 | the accountant inserts the time window for which to compute the graph|
| 3 | the accountant inserts the id of the product for which he wants to see the statistics |
| 4 | the system computes the statistics and displays the graphs corresponding to the number of sold items, number of bought items, number of expired items, revenue and restocking expenses for the selected product |

### Use case 7, UC7 - Add Product References 
| Actors Involved | Inventory Manager</br>Product
| :-------------: | ----------------- 
| Precondition | The inventory manager is logged in.
| Postcondition | The product reference is added to the Inventory.
| Nominal Scenario | The inventory manager opens the application, the actor scans one item of the product with bar code scanner, inputs product informations , the system adds the created reference to the Inventory with the specified quantity. |
| Variants | The ID is already in the inventory. |

| Scenario 7.1 | Product ID not in the list of references |
| :----------: | ---------------- | 
| Precondition | The product ID is not already in the list of references |
| Post condition | The created product reference is added to the Inventory |
| Step # | Description  |
| 1 | The system displays the list of references of the Inventory. |
| 2 | The inventory manager scans one item of the product with the BAR code reader (or types the ID with keyboard) |
| 3 | The system checks that the ID is not already in the list of references |
| 4 | The inventory manager types the product information (name, category, brand, supplier, price...) |
| 5 | The system creates an entry in the Inventory for the product with the specified quantity. |

| Scenario 7.2 | Product ID already in the list of references |
| :----------: | ------------- |
| Precondition | The product ID is already in the list of references |
| Postcondition | Nothing changes. |
| Step # | Description |
| 1 | The application displays the list of references of the Inventory. |
| 2 | The inventory manager scans one item of the product with the BAR code reader (or types the ID with keyboard) |
| 3 | The application checks that the ID is not already in the list of references |
| 4 | The application sends a pop-up message to notify the inventory manager. |

### Use case 8, UC8 - Remove Product Reference from Inventory

| Actors Involved        | Inventory Manager</br>Product |
| :--------------------: | ----------------- | 
| Precondition | The inventory manager is logged in. |
| Post condition | The product reference is removed from the Inventory. |
| Nominal Scenario | The inventory manager opens the application, the actor inputs the id of the product to be removed, the system removes the corresponding product reference from the Inventory. |
| Variants | a) The product ID is not in the list of references.</br> b) Quantity of product in inventory is not zero. |

| Scenario 8.1 | Nominal scenario |
| :----------: | ---------------- | 
| Precondition | The product ID is in the list of references and the corresponding quantity of items is zero. |
| Post condition | The product reference is removed from the Inventory |
| Step # | Description  |
| 1 | The system displays the list of Product References of the Inventory |
| 2 | The inventory manager scans one item of the product with the BAR code reader (or types the ID with keyboard) or selects one product ID in the catalog |
| 3 | The system checks that the product reference of the selected product has zero quantity in the inventory. |
| 4 | The system removes the product from the list of references and notifies succesful removal. |

| Scenario 8.2 | Some items are still in inventory |
| :----------: | ---------------- |
| Precondition | The product ID is in the list of references and the corresponding quantity of items is greater than zero |
| Post condition | Nothing changes. |
| Step # | Description  |
| 1 | The system displays the list of Product References of the Inventory |
| 2 | The inventory manager scans one item of the product with the BAR code reader (or types the ID with keyboard) or selects one product ID in the list of references |
| 3 | The system checks that the product reference of the selected product has zero quantity in the inventory. |
| 4 | The application sends a pop-up message to notify the inventory manager. |

### Use case 9, UC9 - Add items to Inventory
| Actors Involved | Inventory Manager</br>Product |
| :-------------: | ----------------- |
| Precondition | The inventory manager is logged in. |
| Postcondition | The quantity of items of the selected product is updated. |
| Nominal Scenario | The inventory manager opens the dedicated GUI, selects the product to be restocked, types the quantity and the inventory is updated |
| Variants | The product ID is not in the Inventory. |

| Scenario 9.1 | Nominal scenario |
| ------------ | -------------- |
| Precondition | The incoming products have a Product Reference in the Inventory. |
| Post condition | The Product Reference is updated with the additional quantity. |
| Step # | Description |
| 1 | The inventory manager scans one item of the product with the BAR code reader (or types the ID with keyboard) or selects one product ID in the list of references |
| 2 | The system looks for the product id in the list of references. |
| 3 | The inventory manager types the quantity he's received. |
| 4 | The system adds the additional quantity to the inventory. |

### Use case 10, UC10 - Remove items from the Inventory

| Actors Involved | Inventory Manager</br>Product |
| :-------------: | ----------------- |
| Precondition | The inventory manager is logged in.</br>The product has a Product Reference in the Inventory. |  
| Post condition | The inventory is updated. |
| Nominal Scenario | The inventory manager opens the application, selects the product to be removed, types the quantity and the system subtracts it from the quantity of the corresponding Product Reference. |
| Variants | a) the item is not present in the list of references</br> b) the current quantity in the Product Reference is less than the quantity to be removed |


| Scenario 10.1 | Nominal scenario |
| :-----------: | ---------------- |
| Precondition | The product ID is in the list of references and the quantity of stored items is greater than the quantity to be removed. |
| Postcondition | The selected quantity is removed from the Product Reference of the selected product. |
| Step#        | Description  |
| 1 | The inventory manager scans one item of the product with the BAR code reader (or types the ID with keyboard) or selects one product ID from the list of references |
| 2 | The system looks for the product id in the list of references of the Inventory. |
| 3 | The inventory manager types the quantity to remove and the reason for removal (expired, damaged, missing...) |
| 4 | The system checks that the quantity in the product reference is greater than the quantity to be removed.
| 5 | The system subtracts the quantity from quantity in the Product Reference. |


### Use case 11, UC11 - Create Personnel Account

| Actors Involved | Owner/manager
| :-------------: | --------------------- 
| Precondition | The owner is logged in.
| Post condition | A user of the selected category is created.
| Nominal Scenario | The owner opens the application, inputs user informations (name, phone number, etc.) and the system registers the user.

| Scenario 11.1 |  |
| :-----------: |-|
|  Precondition | The owner is logged in. |
|  Postcondition | A user of the selected category is created. |
| Step # | Description |
| 1 | The application displays the list of users and their category |
| 2 | The owner selects the desired category, types name and password of the user and other informations |
| 3 | The software creates a new user.
| 4 | The owner communicates the account credentials to the user.
| 5 | System hashes the password and stores the hash. |

### Use case 12, UC12 - Delete User Account

| Actors Involved | Owner/General manager
| :-------------: | --------------------- 
| Precondition | The owner is logged in.
| Post condition | The selected user is removed.
| Nominal Scenario | The owner opens the application, selects the user, confirms and the system deletes it.

| Scenario 12.1 | nominal scenario |
| :-----------: |-|
|  Precondition | The owner is logged in. |
|  Postcondition | A user of the selected category is created. |
| Step # | Description |
| 1 | The application displays the list of users and their category |
| 2 | The owner selects the user in the list. |
| 3 | The system asks the owner to type their master password for confirmation.
| 4 | The user is removed, their credentials are now invalid.|

### Use case 13, UC13 - Authenticate personnel at their workstation

| Actors Involved | Owner</br>Inventory manager</br>Cashier</br>Accountant |
| :-------------: | --------------------- | 
| Precondition | Personnel is not logged in. Personnel know their password.
| Post condition | Personnel is logged in and may access the appropriate interface for their tasks. |
| Nominal Scenario | Personnel types their ID and correct password, the system checks these credentials and grants access to the appropriate interface. |
| Variants | Personnel have forgotten their password. |

| Scenario 13.1 | nominal scenario |
| :-----------: |-|
| Precondition | Personnel is not logged in. Personnel know their password.
| Post condition | Personnel is logged in and may access the appropriate interface for their tasks. |
| Step # | Description |
| 1 | The Actor opens the application |
| 2 | The system displays the login screen |
| 3 | The Actor inputs their username and password
| 4 | The system checks correctness of the credentials|
| 5 | The system gives the Actor access to their account's functionalities | 

# Glossary
 
~~~plantuml
@startuml

PersonnelAccount : usercode
PersonnelAccount : password
PersonnelAccount : name
PersonnelAccount : surname
PersonnelAccount : category
PersonnelAccount : phone number

CustomerCard : id
CustomerCard : phone number
CustomerCard : name
CustomerCard : surname

ProductReference : id
ProductReference : quantity

ProductDescriptor : id
ProductDescriptor : name
ProductDescriptor : category
ProductDescriptor : brand
ProductDescriptor : supplier
ProductDescriptor : price

Balance : total amount

Expense : type
Expense : amount
Expense : date
Expense : description

Income : type
Income : amount
Income : date
Income : description

Discount : id
Discount : percentage
Discount : expiration date

Sale : payment method
Sale : date
Sale : receipt id


EzShop "1...*" --  "1" PersonnelAccount


EzShop "1" -d- "1" Inventory
EzShop "1" -d- "1" Balance
Inventory "0...*" -d- "1" ProductReference : contains
EzShop "0...*" -d- "1" CustomerCard : issues

ProductReference "1" -d- "1" ProductDescriptor : is described by

ProductReference -[hidden]l- Expense
Expense -[hidden]l- Income
CustomerCard -[hidden]- Income


Balance "0...*" -d- "1" Income : lists
Balance "0...*" -d- "1" Expense : lists

Sale -u-|> Income

Sale "1...*" -d- "0...*" ProductDescriptor : involve

Discount "1..*" -u- "0...*" CustomerCard : is granted
CustomerCard "0...*" -d- "0...1" Sale
Discount "1...*" -d- "0...*" ProductDescriptor: refers to

note top of PersonnelAccount
    Represents an employee
    Category can be Owner, Cashier,
     Accountant and Inventory manager
    each category corresponds to 
    a set of functionalities and permissions.
    The application is delivered with an 
    already initialized account of category "Owner"
end note

note right of ProductReference
    Represents a product stored in 
    the shop's inventory. Quantity represents
    the number of physiscally stored items of
    the product. The id is the BAR code of the
    product  
end note

note top of Inventory
    list of Product  References
end note

note left of Discount
    a discount on a set of products
    available for a set of customer cards
    has a unique id
end note

note top of Balance
    registry of incomes and expenses
    also takes track of the shop's funds
end note

note bottom of Expense
    type can be "salary", "restock" etc.
end note

@enduml
~~~

# System Design

~~~plantuml
@startuml
Software "3" -d- Computer
Software "*" -d- "Cash Register"

Computer "0...1" -d- "0...1" "BAR Code Reader"
"Cash Register" "1" -d- "0...1" "BAR Code Reader"

"Receipt Printer" -u-o "Cash Register"

note top of Computer
    Owner, Accountant and
    Inventory Manager have their
    personal workstation. It can
    be their personal computer.The Inventory 
    Manager is also provided with a
    Bar code reader 
end note

note top of "Cash Register"
    Point of interaction of the cashier.
    It contains a screen, a keyboard, a printer,
    can interact with a PoS (external, which furthermore
    interacts with the credit card system) and a BAR code 
    reader.
    See SAM4S SAP-630 for a possible model (Android)
end note
 
note right of "BAR Code Reader"
    used to scan either products or customer cards. It is a simple device that reads
    a BAR code and outputs an id
end note

@enduml
~~~


# Deployment Diagram 

~~~plantuml
@startuml deploymentDiagramEZshop

    node "Company server" as cs 
    node "Inventory PC" as ipc 
    node "Accounting PC" as apc
    node "Cash register" as cr

    artifact Application {
        artifact "Accounting"
        artifact "Inventory management"
        artifact "Customer management"
        artifact "Personnel management"    
        artifact "Sale management"
    }  

cr -- cs 
cs -- ipc
apc -- cs : intranet
cs -- Application

@enduml
~~~

