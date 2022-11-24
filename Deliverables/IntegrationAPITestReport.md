# Integration and API Test Documentation

Authors:<br/>
S236053 Andrea    Amato<br/>
S288265 Francesco Blangiardi<br/>
S286645 Matthieu  Brunon<br/>
S287949 Gabriele  Sara<br/>

Date: 26/05/2021

Version: 1.0

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>

~~~plantuml
@startuml

EZShop -d-> CustomerImpl
EZShop -d-> OrderImpl
EZShop -d-> ProductTypeImpl
EZShop -d-> SaleTransactionImpl
EZShop -d-> UserImpl
EZShop -d-> BalanceImpl
EZShop -d-> CreditCardManagerImpl
EZShop -d-> PersistencyManager
EZShop -d-> PositionImpl
EZShop -d-> Storable
EZShop -d-> Dummy
EZShop -d-> ReturnTransactionImpl
EZShop -d-> Product

BalanceImpl -d-> BalanceOperationImpl
ReturnTransactionImpl -d-> ReturnEntryImpl
SaleTransactionImpl -d-> TicketEntryImpl
@enduml
~~~


# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>

For integration we used a bottom up approach.
We first implemented and tested the main basic classes and then we implemented and tested the class EZShop that contains all our APIs.

step1: class UserImpl<br/>
step2: class ProductTypeImpl<br/>
step3: class PositionImpl<br/>
step4: class OrderImpl<br/>
step5: class BalanceOperationImpl<br/>
step6: class BalanceImpl<br/>
step7: class CustomerImpl<br/>
step8: class CreditCardManagerImpl<br/>
step9: class TicketEntryImpl<br/>
step10: class SaleTransactionImpl + TicketEntryImpl<br/>
step11: class ReturnEntryImpl<br/>
step12: class ReturnTransactionImpl + ReturnEntryImpl<br/>
step13: class EZShop + all above classes<br/>
step14: class ProductImpl (after change request)<br/>
step15: class EZShop (new API) + class ProductImpl<br/> 


#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop



## Step 1
| Classes  | JUnit test cases |
|--|--|
|UserImpl|it.polito.ezshop.teamTests<br />TestUserImpl, TestUserImplWhiteBox|

## Step 2
| Classes  | JUnit test cases |
|--|--|
|ProductTypeImpl|it.polito.ezshop.teamTests<br />TestProductTypeImpl, TestProductTypeImplWhiteBox|

## Step 3
| Classes  | JUnit test cases |
|--|--|
|PositionImpl|it.polito.ezshop.teamTests<br />TestPositionImpl, TestPositionImplWhiteBox|

## Step 4
| Classes  | JUnit test cases |
|--|--|
|OrderImpl|it.polito.ezshop.teamTests<br />TestOrderImplWhiteBox|

## Step 5
| Classes  | JUnit test cases |
|--|--|
|balanceOperationImpl|it.polito.ezshop.teamTests<br />TestBalanceOperationImplWhiteBox|

## Step 6
| Classes  | JUnit test cases |
|--|--|
|BalanceImpl|it.polito.ezshop.teamTests<br />TestBalanceImpl, TestBalanceImplWhiteBox|

## Step 7
| Classes  | JUnit test cases |
|--|--|
|CustomerImpl|it.polito.ezshop.teamTests<br />TestCustomerImplWhiteBox|

## Step 8
| Classes  | JUnit test cases |
|--|--|
|CreditCardManagerImpl|it.polito.ezshop.teamTests<br />TestCreditCardManagerImplWhiteBox|

## Step 9
| Classes  | JUnit test cases |
|--|--|
|TicketEntryImpl|it.polito.ezshop.teamTests<br />TestTicketEntryImplWhiteBox|

## Step 10
| Classes  | JUnit test cases |
|--|--|
|SaleTransactionImpl + TicketEntryImpl|it.polito.ezshop.teamTests<br />TestSaleTransactionImpl, TestSaleTransactionWhiteBox|

## Step 11
| Classes  | JUnit test cases |
|--|--|
|ReturnEntryImpl|it.polito.ezshop.teamTests<br />TestReturnEntryImplWhiteBox|

## Step 12
| Classes  | JUnit test cases |
|--|--|
|ReturnTransactionImpl + ReturnEntryImpl|it.polito.ezshop.teamTests<br />TestReturnTransactionImpl, TestReturbTransactionImplWhiteBox|


## Step 13
| Classes  | JUnit test cases |
|--|--|
|EZShop + all other classes|it.polito.ezshop.integrationTests<br />Initializator, IntegrationTestSuite, TestAccounting, TestAuthentication, TestCustomer, TestOrders, TestPayment, TestProductType, TestReturns, TestSales, TestUserManagement|

## Step 14
| Classes  | JUnit test cases |
|--|--|
|class ProductImpl|it.polito.ezshop.teamTests<br />| testProductImpl, testProductImplWhiteBox |

## Step 14
| Classes  | JUnit test cases |
|--|--|
|class EZShop + class ProductImpl|it.polito.ezshop.integrationTests<br />| testRFID |

# Scenarios


<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UCx.y

| Scenario |  name |
| ------------- |:-------------:|
|  Precondition     |  |
|  Post condition     |   |
| Step#        | Description  |
|  1     |  ... |
|  2     |  ... |

## Scenario UC6.7

| Scenario |  Sale of product type X completed (quantity of sold product changed) |
| ------------- |:-------------:|
|  Precondition     | Cashier C exists and is logged in |
| | Product type X exists and has enough units to complete the sale |
|  Post condition     | Balance += (N-M)*X.unitPrice  |
| | X.quantity -= (N-M) |
| Step#        | Description  |
|  1    |  C starts a new sale transaction |
|  2    |  C reads bar code of X |
|  3    |  C adds N units of X to the sale |
|  4    |  X available quantity is decreased by N |
|  3    |  C removes M units of X from the sale |
|  4    |  X available quantity is increased by M |
|  5    |  C closes the sale transaction |
|  6    |  System asks payment type |
|  7    |  Manage  payment (see UC7) |
|  8    |  Payment successful |
|  9   |  C confirms the sale and prints the sale Ticket |
|  10   |  Balance is updated |

## Scenario UC8.3

| Scenario 8-3 |  Return of product type X cancelled |
| ------------- |:-------------:|
|  Precondition     | Cashier C exists and is logged in |
| | Product type X exists |
| | Sale S exists and has at least N units of X |
|  Post condition     | Balance not changed  |
| | X.quantity not changed |
| Step#        | Description  |
|  1    |  C inserts S.ticketNumber |
|  2    |  return transaction starts|
|  3    |  C reads bar code of X |
|  4    |  C adds N units of X to the return transaction |
|  5    |  C closes the return transaction |
|  6    |  X available quantity is increased by N |
|  7    |  C deletes the transaction |
|  8    |  Return transaction aborted |
|  9   |  Return transaction deleted, revert changes |


# Coverage of Scenarios and FR


<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) |
| ----------- | ------------------------------- | ----------- |
| 1-1      | FR3.1 | it.polito.ezshop.integrationTests.TestProductType<br />testCreateProductTypeInvalidDescription, testCreateProductTypeInvalidBarCode, testCreateProductTypeInvalidPrice, testCreateProductTypeInvalidUser, testCreateProductTypeExistingBarCode, testCreateProductTypeSuccessful |
| 1-2      | FR3.1 | it.polito.ezshop.integrationTests.TestProductType<br />testUpdatePositionInvalidUser, testUpdatePositionInvalidProductId, testUpdatePositionInvalidPosition, testUpdatePositionSuccessful |
| 1-3      | FR3.1 | it.polito.ezshop.integrationTests.TestProductType<br />testUpdateProductInvalidId, testUpdateProductInvalidDescription, testUpdateProductInvalidBarCode, testUpdateProductInvalidPrice, testUpdateProductInvalidUser, testUpdateProductUnexistingId, testUpdateProductExistingBarCode, testUpdateProductSuccessful |
| 2-1     |  FR1.1  | it.polito.ezshop.integrationTests.TestUserManagement<br/>testCreateUserInvalidUsername, testCreateUserInvalidPassword, testCreateUserInvalidRole, testCreateUserExistingUsername, testCreateUserSuccessful |
| 2-2     |  FR1.2  | it.polito.ezshop.integrationTests.TestUserManagement<br/>testDeleteUserInvalidUserId, testDeleteUserUnauthorized, testDeleteUserSuccessful |
| 2-3     |  FR1.5  | it.polito.ezshop.integrationTests.TestUserManagement<br/>testUpdateUserRightsInvalidUserId, testUpdateUserRightsUnauthorized, testUpdateUserRightsInvalidRole, testUpdateUserRightsSuccessful |
| 3-1     |  FR4.3  | it.polito.ezshop.integrationTests.Orders<br/>testIssueOrderUnauthorized, testIssueOrderInvalidProductCode, testIssueOrderInvalidQuantity, testIssueOrderInvalidPricePerUnit, testIssueOrderSuccessful |
| 3-2     |  FR4.5  | it.polito.ezshop.integrationTests.Orders<br/>testPayOrderUnauthorized, testPayOrderInvalidOrderId, testPayOrderSuccessful |
| 3-3     |  FR4.6  | it.polito.ezshop.integrationTests.Orders<br/>testRecordOrderArrivalUnauthorized, testRecordOrderArrivalInvalidOrderId, testRecordOrderArrivalInvalidPosition, testRecordOrderArrivalSuccessful |
| 4-1 | FR5.1 | it.polito.ezshop.integrationTests.TestCustomer<br />testUnauthorizedCustomerCreation,  testInvalidCustomerNameCreation, testUniqueCustomerCreation, testNotUniqueCustomerCreation |
| 4-2 | FR5.6 | it.polito.ezshop.integrationTests.TestCustomer<br />testUnauthorizedAttachCard, testInvalidCustomerAttachCard, testInvalidCardAttachCard, testInexistantCustomerAttachCard, testInexistantCardAttachCard, testAlreadyAssignedCardAttachCard, testAttachCard |
| 4-3 | FR5.6 | it.polito.ezshop.integrationTests.TestCustomer<br />testCompleteModification |
| 4-4 | FR5.1 | it.polito.ezshop.integrationTests.TestCustomer<br />testUnauthorizedCustomerModification, testInvalidIdCustomerModification, testInvalidNameCustomerModification, testInvalidCardCustomerModification, testInexistantCustomerModification, testCompleteModification |
| 5-1      | FR1 | it.polito.ezshop.integrationTests.TestAuthentication <br />testEmptyNullUsername, testEmptyNullPassword, testWrongCredentials, testCorrectCredentials |
| 5-2 | FR1 | it.polito.ezshop.integrationTests.TestAuthentication<br />testNoUserLogged, testUserLogged |
| 6-1 | FR6.1, FR6.2,FR6.10,FR7 |-- it.polito.ezshop.integrationTests.TestSales --<br/>TestStartSaleTransaction,  TestAddProductToSaleSuccessful, TestEndSaleTransactionSuccessful<br/>-- it.polito.ezshop.integrationTests.TestPayment --<br/>testCashPayment, testCreditCardPayment|
| 6-2 | FR6.1, FR6.2,FR6.5,FR6.10,FR7 |-- it.polito.ezshop.integrationTests.TestSales --<br/>TestStartSaleTransaction, TestAddProductToSaleSuccessful, TestApplyDiscountRateToProductSuccessful, TestEndSaleTransactionSuccessful<br/>-- it.polito.ezshop.integrationTests.TestPayment --<br/>testCashPayment, testCreditCardPayment|
| 6-3 | FR6.1, FR6.2,FR6.4,FR6.10,FR7 |-- it.polito.ezshop.integrationTests.TestSales --<br/>TestStartSaleTransaction, TestAddProductToSaleSuccessful, TestApplyDiscountRateToSaleSuccessful, TestEndSaleTransactionSuccessful<br/>-- it.polito.ezshop.integrationTests.TestPayment --<br/>testCashPayment, testCreditCardPayment|
| 6-4 | FR6.1, FR6.2, FR6.6,FR6.10,FR7 |-- it.polito.ezshop.integrationTests.TestSales --<br/>TestStartSaleTransaction,  TestAddProductToSaleSuccessful, TestEndSaleTransactionSuccessful, TestComputePointsForSaleSuccessful<br/>-- it.polito.ezshop.integrationTests.TestPayment --<br/>testCashPayment, testCreditCardPayment<br />-- it.polito.ezshop.integrationTests.TestCustomer --<br /> testAddPoints|
| 6-5 | FR6.1, FR6.2,FR6.10,FR6.11 |-- it.polito.ezshop.integrationTests.TestSales --<br/>TestStartSaleTransaction,  TestAddProductToSaleSuccessful, TestEndSaleTransactionSuccessful, TestDeleteSaleTransactionSuccessful|
| 6-6 | FR6.1, FR6.2,FR6.10,FR7.1 |-- it.polito.ezshop.integrationTests.TestSales --<br/>TestStartSaleTransaction,  TestAddProductToSaleSuccessful, TestEndSaleTransactionSuccessful<br/>-- it.polito.ezshop.integrationTests.TestPayment --<br/>testCashPayment|
| 6-7 | FR6.1, FR6.2,FR6.3,FR6.10,FR7 |-- it.polito.ezshop.integrationTests.TestSales --<br/>TestStartSaleTransaction,  TestAddProductToSaleSuccessful, TestDeleteProductFromSaleSuccessful, TestEndSaleTransactionSuccessful<br/>-- it.polito.ezshop.integrationTests.TestPayment --<br/>testCashPayment, testCreditCardPayment|
| 7-1 | FR7.2 | it.polito.ezshop.integrationTests.TestPayment<br/>testCreditCardPayment |
| 7-2 | FR7.2 | it.polito.ezshop.integrationTests.TestPayment<br/>testInvalidCreditCardPayment |
| 7-3 | FR7.2 | it.polito.ezshop.integrationTests.TestPayment<br/>testNotEnoughFundsCreditCardPayment |
| 7-4 | FR7.1 | it.polito.ezshop.integrationTests.TestPayment<br/>testCashPayment |
| 8-1 | FR6.12 -- FR6.15, FR 7.2 |-- it.polito.ezshop.integrationTests.TestSales --<br />TestStartReturnTransactionSuccessful, TestReturnProductSuccessful, TestEndReturnTransactionSuccessful<br/>-- it.polito.ezshop.integrationTests.TestPayment --<br/>testCreditCardPayment|
| 8-2 | FR6.12 -- FR6.15, FR 7.1 |-- it.polito.ezshop.integrationTests.TestSales --<br />TestStartReturnTransactionSuccessful, TestReturnProductSuccessful, TestEndReturnTransactionSuccessful<br/>-- it.polito.ezshop.integrationTests.TestPayment --<br/>testCashPayment|
| 8-3 | FR6.12 -- FR6.15 |-- it.polito.ezshop.integrationTests.TestSales --<br />TestStartReturnTransactionSuccessful, TestReturnProductSuccessful, TestEndReturnTransactionSuccessful, TestDeleteReturnTransactionSuccessful|
| 9-1 | FR8.3 |it.polito.ezshop.integrationTests.TestAccounting <br />testGetCreditsAndDebitsInvalidUser, testGetCreditsAndDebitsSuccessful|
| 10-1 | FR7.4 | it.polito.ezshop.integrationTests.TestPayment <br /> testCreditCardPayment |
| 10-2 | FR7.3 | it.polito.ezshop.integrationTests.TestPayment <br /> testCashPayment  |




# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name                                                    |
| -------------------------- | ------------------------------------------------------------ |
| NFR2                       | it.polito.ezshop.integrationTests.*                          |
| NFR4                       | it.polito.ezshop.integrationTests.TestProductType<br />testCreateProductTypeInvalidBarCode |
| NFR5                       | -- it.polito.ezshop.teamTests.TestCreditCardManagerImplWhiteBox --<br />testLuhn |
