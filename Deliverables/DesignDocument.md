# Design Document 


Authors: <br/>
S236053 Andrea    Amato<br/>
S288265 Francesco Blangiardi<br/>
S286645 Matthieu  Brunon<br/>
S287949 Gabriele  Sara<br/>

Date: 22/05/2021

Version: 1.1


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

## Architectural style
stand alone, 3 tier struccture (presentation, logic, data), MVC

## Package Diagram

~~~plantuml
@startuml

package EzShop
package EzShopModel
package GUI
package EzShopException
package EzShopData

EzShop <|-d- EzShopModel
EzShop <|-d- EzShopException
EzShop <|-d- GUI
EzShop <|-d- EzShopData


@enduml
~~~

# Low level design

## Class Diagram
## Package EzShopModel
~~~plantuml
@startuml EZShopDesign

class EZShop{
    
    --
    +createUser()
    +deleteUser()
    +getAllUsers()
    +getUser()
    +updateUserRights()
    ..
    +login()
    +logout()
    ..
    +createProductType()
    +updateProduct()
    +deleteProductType()
    +getAllProductTypes()
    +getProductTypeByBarCode()
    +getProductTypeByDescription()
    ..
    +updateQuantity()
    +updatePosition()
    +issueReorder()
    +payOrderFor()
    +payOrder()
    +recordOrderArrival()
    +getAllOrders()
    ..
    +defineCustomer()
    +modifyCustomer()
    +deleteCustomer()
    +getCustomer()
    +getAllCustomers()
    +createCard()
    +attachCardToCustomer()
    +modifyPointsOnCard()
    ..
    +startSaleTransaction()
    +addProductToSale()
    +deleteProductFromSale()
    +applyDiscountRateToProduct()
    +applyDiscountRateToSale()
    +computePointsForSale()
    +endSaleTransaction()
    +deleteSaleTransaction()
    +getSaleTransaction()
    +startReturnTransaction()
    +returnProduct()
    +endReturnTransaction()
    +deleteReturnTransaction()
    ..
    +receiveCashPayment()
    +receiveCreditCardPayment()
    +returnCashPayment()
    +returnCreditCardPayment()
    ..
    +recordBalanceUpdate()
    +getCreditsAndDebits()
    +computeBalance()
}

class User {
    -Integer id
    -String username
    -String password
    -String role
    --
    +checkRole()
    +getCSV()
}

class ProductType {
    -Integer id
    -String description
    -String productCode
    -double pricePerUnit
    -String note
    -String position
    -Integer quantity
    --
    +updateQuantity()
    +validateProductCode()
    +getCSV()
}

class Product {
	-String RFID
	-Integer productTypeId
    -Integer transactionId
    -Integer returnId
    --
    +validateRFID()
    +getCSV()
}


class Position {
    String position
    --
    +isValid()
    +getCSV()
}

class Order {
    -Integer orderId
    -String status
    -String productCode
    -int quantity
    -double pricePerUnit
    -Integer balanceId
    --
    +getCSV()
}

class Customer {
    -Integer id
    -String customerName
    -String customerCard
    -Integer points
    
    --
    +attachCard()
    +getCSV()
}

class SaleTransaction {
    -Integer id
    -Integer ticketN
    -Double rawprice
    -int state
    -double discountRate
    -Integer BalanceOperationId
    -Map<Integer, TicketEntry> t_entries
    -Integer entryCounter
    --
    +getEntryId(barCode)
    +clearEntries()
    +loadEntry(TicketEntry) (only for persistency support)
    +compressEntries()
    +checkState()
    +containsEntry(barCode)
    +addEntry(product, amount)
    +updateEntry(barCode, amount)
    +updateEntry(barCode, discount)
    +getEntry(barCode)
    +computePoints()
    +applyReturn(returns)
    +undoReturn(returns)
    +getCSV()
}

class ReturnTransaction{
    -Integer id
    -Integer saleId
    -Double rawprice
    -double discountRate
    -Map<Integer, ReturnEntry> r_entries
    -Integer entryCounter
    -Integer state
    -Integer BalanceOperationId
    -boolean commit
    --
    +getEntryId(barCode)
    +clearEntries()
    +loadEntry(ReturnEntry) (only for persistency support)
    +checkState()
    +containsEntry(barCode)
    +addEntry(product, amount, discount)
    +updateEntry(barCode, amount)
    +getCSV()    
}

class TicketEntry{
    -Integer id
    -String barCode
    -String description
    -Integer amount
    -Double pricePerUnit
    -Double discount
    -Integer id
    -Integer saleId
    --
    getCSV()
}

class ReturnEntry{
    -Integer returnId

    --
    getCSV()
}

class CreditCardManager {
    
    --
    +validateCard()
    +addCard()
    +removeCard()
    +withdrawFromCard()
    +refundCard()
}

class Balance {
    -Map<Integer, BalanceOperation> operations
    -Double amount
    --
    +updateAmount()
    +recordBalanceOperation()
    +getBalanceOperations()
    +getLastId()
}
class BalanceOperation {
    -double amount
    -int type
    -String date
    -Integer id
    --
    +getCSV()
}

class PersistencyManager{
    +store()
    +delete()
    +update()
    +reset()
    +getFile()
}

EZShop -- "*" User
EZShop -- "*" ProductType
EZShop -- "*" Order
EZShop -- "*" Customer
EZShop -- "*" SaleTransaction
EZShop -- ReturnTransaction
EZShop -- "1" Balance
EZShop -- "1" CreditCardManager
EZShop -- "*" Position
EZShop -- "1" PersistencyManager

SaleTransaction -- ReturnTransaction
Position -- ProductType
Balance -l- "*" BalanceOperation
ProductType -- "*" Order
EZShop -- "*" Product
Order -- Balance

ProductType "*" -- "*" SaleTransaction
SaleTransaction -l- "*" TicketEntry
ReturnTransaction -l- "*" ReturnEntry
ReturnEntry -|> TicketEntry

SaleTransaction -- BalanceOperation
ReturnTransaction -l- BalanceOperation

note left of EZShop
    the EZShop class contains the following data structures that implement the relationships with other classes:
    -CreditCardManager CMG (1 to 1 for CreditCardManager)
    -Balance balance (1 to 1 for Balance)
    -Map<Integer, Customer> customers (1 to many for Customer)
    -Map<String, Integer> customerCards (1 to many for CustomerCard)
    -Map<Integer, ProductType> products (1 to many for ProductType)
    -Map<Integer, User> users (1 to many for User)
    -Map<Integer, Order> orders (1 to many for Order)
    -Map<Integer, SaleTransaction> sales (1 to many for SaleTransaction)
    -Map<Integer, ReturnTransaction> returns (1 to many for ReturnTransaction)
    -List<Position> positions (1 to many for Position)
    
    The classes whose instances will be persistent are:
    -Customer
    -CustomerCard
    -Position
    -Order
    -SaleTransaction
    -ReturnTransaction
    -User
    -BalanceOperation
end note

@enduml
~~~







# Verification traceability matrix


|      | EZShop | User | ProductType | Position | Order | Customer | CustomerCard | SaleTransaction | ReturnTransaction | BalanceOperation | Balance | CreditCardManager |
|------|--------|:----:|-------------|----------|-------|----------|--------------|-----------------|-------------------|------------------|---------|-------------------|
| FR 1 | X      |   X  |             |          |       |          |              |                 |                   |                  |         |                   |
| FR 3 | X      |      | X           | X        |       |          |              |                 |                   |                  |         |                   |
| FR 4 | X      |      | X           | X        | X     |          |              |                 |                   |                  |         |                   |
| FR 5 | X      |      |             |          |       | X        | X            |                 |                   |                  |         |                   |
| FR 6 | X      |      | X           |          |       |          | X            | X               | X                 | X                | X       | X                 |
| FR 7 | X      |      |             |          |       |          |              | X               | X                 | X                | X       | X                 |
| FR 8 | X      |      |             |          |       |          |              |                 |                   | X                | X       |                   |





# Verification sequence diagrams 

### Scenario 1-1: Create product type X
~~~plantuml
@startuml SD1-1
participant "ShopManager\Administrator" as act
participant EZShop
participant ProductType
participant Position
act -> EZShop : 1: selectDescription
act -> EZShop : 2: selecProductCode
act -> EZShop : 3: selectPricePerUnit
act -> EZShop : 4: selectNotes
act -> EZShop : 5: selectPosition
EZShop -> EZShop : 6: createProductType()
EZShop -> ProductType : 7: getId()
EZShop -> Position : 8: checkProduct()
EZShop -> EZShop : 9: updatePosition()
EZShop -> ProductType: 10: setPosition()
EZShop --> act : 11: successful message 
@enduml
~~~
### Scenario 1-2: Modify product type location
~~~plantuml
@startuml SD1-2
participant "Shop Manager\Administrator" as actor
participant EZShop
participant ProductType
participant Position
actor -> EZShop : 1: inputProductId
actor -> EZShop : 2: inputNewPosition
EZShop -> ProductType : 3: getId()
EZShop -> Position : 4: checkPosition()
EZShop -> EZShop : 5: updatePosition()
EZShop -> ProductType: 6: setPosition()
EZShop --> actor : 7: successful message 
@enduml
~~~

### Scenario 2-1: Create user and define rights
~~~plantuml
@startuml SD2-1
participant Administrator
participant EZShop
participant User
Administrator -> EZShop : 1: inputUsername
Administrator -> EZShop : 2: inputPassword
Administrator -> EZShop : 3: selectRole
EZShop -> EZShop : 4: createUser()
EZShop -> User : 5: setId()
EZShop -> Administrator : 6: successful message
@enduml
~~~

### Scenario 2-3: Modify user rights
~~~plantuml
@startuml SD2-3
participant Administrator
participant EZShop
participant User
Administrator -> EZShop : 1: selectUser
Administrator -> EZShop : 2: selectRole
EZShop -> User : 3: getId()
EZShop -> EZShop : 4: updateUserRights()
EZShop -> User : 5: setRole()
EZShop -> Administrator : 6: successful message
@enduml
~~~

### Scenario 3-1: Order of product type X issued
~~~plantuml
@startuml

participant "Administrator/ShopManager" as act
participant EZShop
participant Order

act -> EZShop : 1: input ProductCode
act -> EZShop : 2: input PricePerUnit
act -> EZShop : 3: input quantity

EZShop -> EZShop : 4: issueOrder() 
EZShop -> Order : 5: Order()
Order -> EZShop : 6: return new Order()


EZShop -> act : 7 : successful message
@enduml
~~~

### Scenario 4-1: Create customer record
~~~plantuml
@startuml

participant "Administrator/ShopManager/Cashier" as act

participant EZShop
participant Customer

act -> EZShop : 1: input customerName
EZShop -> EZShop : 2: defineCustomer()
EZShop -> Customer : 3: Customer()
Customer -> EZShop : 4: return new Customer




EZShop -> act : 5: successful message

@enduml
~~~

### Scenario 4-2: Attach Loyalty card to customer record
~~~plantuml
@startuml

participant "Administrator/ShopManager/Cashier" as act
participant EZShop
participant Customer
participant CustomerCard


act -> EZShop : 1: input customer Id
EZShop -> EZShop : 2: createCard()
EZShop -> CustomerCard : 3 : CustomerCard()
CustomerCard -> EZShop : 4 : return new Card 
EZShop -> EZShop : 5 : attachCardToCustomer()
EZShop -> Customer : 6 : attachCard()
Customer -> CustomerCard : 7 : attachCustomer()
Customer -> EZShop : 8 : return true
EZShop -> act: 9 : successful message

@enduml
~~~

### Scenario 5-1: Login
~~~plantuml
@startuml

participant "Administrator/ShopManager/Cashier" as act
participant EZShop
participant User

act -> EZShop : 1 input user name
act -> EZShop : 2 input user password
EZShop -> EZShop : 3 : login()
EZShop -> User : 4 : checkPassword()
User -> EZShop : 6 : return true
EZShop -> act : 7 : successful message
 


@enduml
~~~
### Scenario 6-2: Sale of product type X with product discount
~~~plantuml
@startuml SD6-2
participant "Cashier" as act
participant EZShop as ez
participant SaleTransaction as ST
participant ProductType as PT
participant TicketEntry as TE
act -> ez : 1: input start
ez -> ez : 2: startSaleTransaction()
ez -> ST : 3: SaleTransaction()
ST -> ez : 4: return new Transaction
act -> ez : 5: inputProductCode
act -> ez : : inputqQuantity
ez -> ez : 8: addProductToSale()
ez -> PT : 6: getQuantity() (for checking)
PT -> ez : 7: return Integer
ez -> ST : : addEntry()
ST -> TE : :TicketEntry()
TE -> ST : : return new Entry
ST -> ez : : return Entry object 
act -> ez : 9 : input discountRate
act -> ez : 10: input product for discount
ez -> ez :  11: applyDiscountRatetoProduct()
ez -> ST : 12: updateEntry()
ST -> TE : : setDiscountRate()
ST -> ez : 13: return true
act -> ez : 14: input confirmation
ez -> ez : 15: endSaleTransaction()
ez -> ST : : setState()
ez -> ST : 16: getEntries()
ez -> PT : 17: updateQuantity() (loop)
ez -> ez : 18: receiveCreditCardPayment() (see 7-1) 
ez -> act : 19: successful message
@enduml
~~~

### Scenario 7-1: Manage payment by valid credit card
~~~plantuml
@startuml SD7-1
participant "ShopManager\Administrator\Cashier" as act
participant EZShop
participant CreditCardManager
participant SaleTransaction  as ST
participant Balance 
participant BalanceOperation 

act -> EZShop : 1: inputCreditCardNumber
act -> EZShop : 2: input saleTransactionId
EZShop -> CreditCardManager : 2: validateCard()
CreditCardManager -> EZShop : 3: return true
act -> EZShop : 4: inputSalePrice
act -> EZShop : 5: select credit amount option
EZShop -> EZShop : 6: receiveCreditCardPayment()
EZShop -> ST : 7: getAmount()
ST -> EZShop : 8: return Double
EZShop -> CreditCardManager : 9: withdrawFromCard()
CreditCardManager -> EZShop : 10: return true
EZShop -> Balance : 11: addBalanceOperation()
Balance -> BalanceOperation : 12: BalanceOperation()
BalanceOperation -> Balance : 13: return new BalanceOperation()
Balance -> ST : 14: setOperationId()
Balance -> EZShop : 15: return true
EZShop --> act : 16: successful message 

@enduml
~~~

### Scenario 7-4: Manage cash payment
~~~plantuml
@startuml SD7-4
participant "ShopManager\Administrator\Cashier" as act
participant EZShop as ez
participant Balance as bal
participant BalanceOperation as BO
participant SaleTransaction  as ST

act -> ez : 1 : input saleId
ez -> ez : 2 : receiveCashPayment()
ez -> ST : 3 : getAmount()
ST -> ez : 4 : return Double
ez -> bal : 5 : addBalanceOperation()
bal -> BO : 6 : BalanceOperation()
BO -> bal : 7 : return new BalanceOperation
bal -> ST : 8 : setOperationId()
bal -> ez : 9 : return true
ez -> act : 10 : return Double (change)

@enduml
~~~

### Scenario 8-1: Return transaction of product type X completed, credit card

~~~plantuml
@startuml

participant "ShopManager/Administrator/Cashier" as act
participant EZShop as ez
participant ReturnTransaction as RT
participant SaleTransaction as ST
participant ProductType as PT


act -> ez : 1 : inputTransactionId
ez -> ez : 2 : startReturnTransaciton
ez -> RT : 3 : ReturnTransaction()
RT -> ez : 4 : return new ReturnTransaction
act -> ez : 5 : input ProductCode
ez -> ez : 6 : returnProduct()
ez -> PT : 7 : getQuantity()
PT -> ez : 8 : return Integer
ez -> ST : 9 : getQuantityforProduct()
ST -> ez : 10 : return Integer
ez -> RT : 11 : addReturnProduct()

act -> ez : 12 :input confirmation

ez -> ez : 13 : endReturnTransaction()

ez -> RT : 14 : getProductsandQuantities()
ez -> PT : 15 : updateQuantity() (loop)
ez -> ReturnTransaction : 16 : endReturnTransaction()
ez -> ez : 17 : returnCreditCardPayment() (see 10-1)

ez -> act : 18 : return successful message

@enduml
~~~


### Scenario 9-1: List credits and debits
~~~plantuml
@startuml SD9-1
participant "ShopManager\Administrator" as act
participant EZShop
participant Balance
act -> EZShop : 1: selectStartDate
act -> EZShop : 2: selectEndDate
EZShop -> Balance: 3: getCreditsAndDebits()
Balance -> EZShop: 4: return debits and credits list
EZShop --> act : 5: show list 
@enduml
~~~

### Scenario 10-1: Return payment by  credit card
~~~plantuml
@startuml SD10-1
participant "ShopManager\Administrator" as act
participant EZShop as ez
participant CreditCardManager as CCM
participant ReturnTransaction as RT
participant Balance as bal
participant BalanceOperation as BO
act -> ez : 1: inputCreditCardNumber
act -> ez : 2 : input returnId
ez -> ez : 3: returnCreditCardPayment()
ez -> RT : 4 :getAmount()
RT -> ez : 5 : return Double
ez -> CCM: 6 : refundCard() (contains validate())
CCM -> ez :7: return true
ez -> bal :8: addBalanceOperation()
bal -> BO :9: BalanceOperation()
BO -> bal :10: return new BalanceOperation()
bal -> RT : 11: setOperationId()
bal -> ez :12 : return true
ez --> act : 13: successful message 
@enduml
~~~
