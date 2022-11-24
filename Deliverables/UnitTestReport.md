# Unit Testing Documentation

Authors:<br/>
S236053 Andrea    Amato<br/>
S288265 Francesco Blangiardi<br/>
S286645 Matthieu  Brunon<br/>
S287949 Gabriele  Sara<br/>


Date: 18/05/21

Version: 1.0

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

## Notes
<br> Class TicketEntryImpl and ReturnEntryImpl will only be tested in whitebox since they're only comprised of setter and getter methods




## **Class *UserImpl***

<br> All test cases are in class TestUserImpl

 ### **Class *UserImpl* - method *checkRole***
**Criteria for method *checkRole*:**
	

 - values of roles 
 - value of internal state





**Predicates for method *checkRole*:**

| Criteria | Predicate |
| -------- | --------- |
|   validity of String r : roles       | all valid          |
||some null|
|values of roles | one match in internal role|
||no match in internal role|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Validity of roles |value of roles |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|valid|one match with internal role|valid|<br>user.setRole("Cashier")<br>...<br>T1("Cashier","asd","sbrambinator")<br>->return true|testCheckRole|
|''|no match with internal role|valid|<br>user.setRole("Cashier")<br>...<br>T2("Administrator", "MemeMaster")<br>->return false|testCheckRole|
|some null|*|invalid|<br> T3(null,null)<br>->error|testCheckRole|






## **Class *PositionImpl***

<br> All test cases are in class TestPositionImpl

 ### **Class *PositionImpl* - method *isValid***
**Criteria for method *isValid*:**
	

 - value of position 





**Predicates for method *isValid*:**

| Criteria | Predicate |
| -------- | --------- |
|   validity of String p : position       | valid          |
||null|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Validity of position |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|valid|valid|<br>T1("1-a-1")<br>->return true|isValid|
|null|invalid|<br> T2(null)<br>->return false|isValid|







 ## **Class *SaleTransactionImpl***

<br>All test cases are in class TestSaleTransactionImpl

### method SetEntries
**Criteria for method *setEntries*:**
	

 - validity of List<TicketEntry> l
 - validity of the TicketEntry objects e





**Predicates for method *setEntries*:**

| Criteria | Predicate |
| -------- | --------- |
|   Validity of l       | Valid          |
||null|
|validity of TicketEntries e| valid|
||some are null|





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Validity of l |validity of e |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|valid|valid|valid|<br>T1(l1={e1,e2}) <br>T2(l2={e3,e4}) <br> ->lists setted to l1 and then resetted to l2 succesfully, return|TestValidSetList|
|''|some are null|invalid|<br>l3 = {null, e1...}<br>T3(l3)<br>->error|testSomeNullSetList|
|null|*|invalid|<br>T6(null)<br>->error|testNullSetList||

### method compressEntries

**Criteria for method *compressEntries*:**
	

 - internal state of the Sale's entries





**Predicates for method *compressEntries*:**

| Criteria | Predicate |
| -------- | --------- |
|   state of the Sale's entries | some barCodes have multiple entries |
||each batCode has only one entry|

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| State of entries |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|duplicated barCodes|valid|<br>setEntries(l1={e1,e1})<br>T1()<br> ->return n_collapsed_entries|testPerformedCompressEntries|
|one entry/barCode|valid|<br>l3 = {e1,e2,e3}<br>T3()<br>->return 0|testNotPerformedCompressEntries|



### method checkState
**Criteria for method *checkState*:**
	

 - values of states 
 - value of internal state





**Predicates for method *checkState*:**

| Criteria | Predicate |
| -------- | --------- |
|value of parameters "states" | one match with internal state|
||no match with internal state|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Values of states parameters |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|one match with internal state|valid|<br>sale.setState(1)<br>...<br>T1(0,1,2)<br>->return true|testTrueCheckState|
|no match with internal state|valid|<br>sale.setState(1)<br>...<br>T1(0,2)<br>->return false|testFalseCheckState|

### method containsEntry
**Criteria for method *containsEntry*:**
	

 - barCode present





**Predicates for method *containsEntry*:**

| Criteria | Predicate |
| -------- | --------- |
|   barCodePresent      | yes          |
|          |no          |




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| barCode present |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|yes|valid|<br>String code = "this may be a valid code,null or any string"<br>setEntries(l1={e1={..code..}})<br>...<br>T1(code) <br>->return true|testTrueCheckState|
|no|valid|<br>String code = "this may be a valid code, null or any string"<br>setEntries(l2={not_code})<br>...<br>T2(code) <br>->return false|testFalseCheckState|

### method addEntry
**Criteria for method *addEntry*:**
	

 - barCode present
 - validity of prod
 - sign of amount





**Predicates for method *addEntry*:**

| Criteria | Predicate |
| -------- | --------- |
|   barCodePresent      | yes          |
|          |no          |
| validity of prod | valid |
||null|
|sign of amount| (minInt, 0]|
||(0, masInt)|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
| sign of amount         |   0, 1             |




**Combination of predicates**:


| barCode present|validity of prod |sign of amount|Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|yes|* | *|invalid|<br>setEntries(l1={e1={p1.getBarCode()}})<br>...<br>T1(p1,10)<br>->return null|testAddEntry|
|*| null| *|invalid|<br>T2(null, 10)<br>->error|testAddEntry|
|*| * |(minInt, 0]|invalid|<br>T3(p1, -1)<br>->return null|testAddEntry|
|no|valid|(0,maxInt)|valid|<br>T4(p1,9999)<br>->return new Entry, state updated|testAddEntry|

### method updateEntry
**Criteria for method *updateEntry(String, Integer)*:**
	

 - barCode present
 - negative final amount




**Predicates for method *updateEntry*:**

| Criteria | Predicate |
| -------- | --------- |
|   barCodePresent      | yes          |
|          |no          |
| negative final amount | yes|
||no|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |             |




**Combination of predicates**:


| barCode present|negative final amount|Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|no|* |invalid|<br>setEntries(l1={e1={"a string"}})<br>...<br>T1("another string",10)<br>->error|testUpdateEntry|
|*| yes | invalid|<br>addEntry(p1, 10)<br>...<br>T2(p1.barCode, -100)<br>->return null|testUpdateEntry|
|yes|no|valid|<br>setEntries(l1={e1={"a string"}})<br>...<br>T1("a string",10)<br>.>return updatedEntry, state is updated|testUpdateEntry|

**Criteria for method *updateEntry(String, Double)*:**
	

 - barCode present
 - value of discount




**Predicates for method *updateEntry*:**

| Criteria | Predicate |
| -------- | --------- |
|   barCodePresent      | yes          |
|          |no          |
| value of discount | inside [0,1)|
||outside [0,1)|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|     value of discount     |   0, 0.9999          |




**Combination of predicates**:


| barCode present|value of discount|Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|no|* |invalid|<br>setEntries(l1={e1={"a string"}})<br>...<br>T1("another string",10)<br>->error|testUpdateEntryDouble|
|*| outside [0,1) | invalid|<br>addEntry(p1, 10)<br>...<br>T2(p1.barCode, 1.5)<br>->return null|testUpdateEntryDouble|
|yes|no|valid|<br>setEntries(l1={e1={"a string"}})<br>...<br>T1("a string",10)<br>.>return updatedEntry, state is updated|testUpdateEntryDouble|


### method getEntry
**Criteria for method *getEntry*:**
	

 - barCode present





**Predicates for method *getEntry*:**

| Criteria | Predicate |
| -------- | --------- |
|   barCode Present      | yes          |
|          |no          |




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| barCode present |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|yes|valid|<br>String code = "this may be a valid code,null or any string"<br>setEntries(l1={e1={..code..}})<br>...<br>T1(code) <br>->return TicketEntry|testGetEntry|
|no|valid|<br>String code = "this may be a valid code, null or any string"<br>setEntries(l2={not_code})<br>...<br>T2(code) <br>->return null|testGetEntry|

### method computePoints
**Criteria for method *computePoints*:**
	

 - total price





**Predicates for method *computePoints*:**

| Criteria | Predicate |
| -------- | --------- |
|total price| >= 10*n  |
|| < 10*n |


**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|    total price      |    10 * n - 0.0001, 10 * n             |
|          |                 |



**Combination of predicates**:

total price|Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|>= 10*n|valid|<br>setEntries(l1)<br>...<br>T1() <br>->return n|testComputePoints|
|< 10*n|valid|<br>setEntries(l1)<br>...<br>T1() <br>->return n-1|testComputePoints|


### method applyReturn
**Criteria for method *applyReturn*:**
	
 - all barcodes present
 - validity of List<TicketEntry> returned
 - validity of all entries in returned
 - all final amounts positive


**Predicates for method *applyReturn*:**

| Criteria | Predicate |
| -------- | --------- |
|all barcodes present| yes|
||no|
|validity of returned | valid|
||null|
|validity of entries | all valid|
||some null|
||some have negative amount|
| all final amounts positive |yes|
||no|

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:

|all barcodes present|validity of returned|validity of entries|all final amounts positive|Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|-------|
|no | * | * | * | invalid | <br>setEntries(l1)<br>l1.addSomething()<br>...<br>T1(l1)<br>->error|testInvalidApplyReturn|
|* | * | * | no |invalid|<br>e1 = {barcode, 20}<br>addEntry(e1.barcode,10)<br>addEntry(...)<br>...<br>T2(l2={e1})<br>->return false|testInvalidApplyReturn|
|*| *| some null| *|invalid|<br>T3(l1={null,e1})<br>->error|testInvalidApplyReturn|
|*| *| some with negative amount| *|invalid|<br>e1 ={..amount : -5..}<br>T2b(l1={e1,e2})<br>->return false|testInvalidApplyReturn|
|*| null| * | * |invalid|<br>T4(null)<br>->return false|testInvalidApplyReturn|
|yes | valid | all valid| yes | valid |<br>setEntries(l1)<br>l1.removeSomething()<br>...<br>T5(l1)<br>->return true|testValidApplyReturn|

### method undoReturn
**Criteria for method *undoReturn*:**
	

 - validity of List<TicketEntry> returned
 - validity of all entries in returned
 - all final amounts positive

**Predicates for method *undoReturn*:**

| Criteria | Predicate |
| -------- | --------- |
|validity of returned | valid|
||null|
|validity of entries | all valid|
||some null|
||some with negative amount|


**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:

validity of returned|validity of entries|Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| null | * |invalid|<br>T1(null)<br>->error|testInvalidUndoReturn|
| *| some null|invalid|<br>T2(l1={null,e1})<br>->error|testInvalidUndoReturn|
| *| some with negative amount|invalid|<br>e1 = {..amount: -5...}<br>T2b(l1={e1,e2})<br>->return false|testInvalidUndoReturn|
|valid | all valid | valid|<br>setEntries(l1)<br>l1.removeSomething()<br>applyReturn(l1)<br>...<br>T3(l1)<br>->return true|testValidUndoReturn|

## **Class *ReturnTransactionImpl***
<br> All test cases are in class TestReturnTransactionImpl
<br>This class is very similar to SaleTransactionImpl
<br>BB test was executed accordingly, WB will still achieve > 90% coverage

### method addEntry
**Criteria for method *addEntry*:**
	

 - barCode present
 - validity of prod
 - sign of amount
 - value of discount




**Predicates for method *addEntry*:**

| Criteria | Predicate |
| -------- | --------- |
|   barCodePresent      | yes          |
|          |no          |
| validity of prod | valid |
||null|
|sign of amount| (minInt, 0]|
||(0, maxInt)|
|value of discount | inside [0,1)|
||outside [0,1)|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
| sign of amount         |   0, 1             |
|value of discount | 0, 0.9999|




**Combination of predicates**:


| barCode present|validity of prod |sign of amount|value of discount|Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|-------|
|yes|* | *| *|invalid|<br>setEntries(l1={e1={p1.getBarCode()}})<br>...<br>T1(p1,10,0.1)<br>->return null|testAddEntry|
|*| null| *| *|invalid|<br>T2(null, 10,0.5)<br>->error|testAddEntry|
|*| * |(minInt, 0]| *|invalid|<br>T3(p1, -1,0.4)<br>->return null|testAddEntry|
| *| * | * | outside [0,1) | invalid|<br>T4(p1,10,-0.6)<br>->return null|testAddEntru|
|no|valid|(0,maxInt)|inside [0,1)|valid|<br>T4(p1,9999,0.7)<br>->return new Entry, state updated|testAddEntry|

### method updateEntry
**Criteria for method *updateEntry(String, Integer)*:**
	

 - barCode present
 - negative final amount




**Predicates for method *updateEntry*:**

| Criteria | Predicate |
| -------- | --------- |
|   barCodePresent      | yes          |
|          |no          |
| negative final amount | yes|
||no|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |             |




**Combination of predicates**:


| barCode present|negative final amount|Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|no|* |invalid|<br>setEntries(l1={e1={"a string"}})<br>...<br>T1("another string",10)<br>->error|testUpdateEntry|
|*| yes | invalid|<br>addEntry(p1, 10)<br>...<br>T2(p1.barCode, -100)<br>->return null|testUpdateEntry|
|yes|no|valid|<br>setEntries(l1={e1={"a string"}})<br>...<br>T1("a string",10)<br>.>return updatedEntry, state is updated|testUpdateEntry|

### method setEntries

**Criteria for method *setEntries*:**
	

 - validity of List<TicketEntry> l
 - validity of the ReturnEntryImpl objects e





**Predicates for method *setEntries*:**

| Criteria | Predicate |
| -------- | --------- |
|   Validity of l       | Valid          |
||null|
|validity of ReturnEntries e| valid|
||some are null|





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Validity of l |validity of e |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|valid|valid|valid|<br>T1(l1={e1,e2}) <br>T2(l2={e3,e4}) <br> ->lists setted to l1 and then resetted to l2 succesfully, return|TestValidSetList|
|''|some are null|invalid|<br>l3 = {null, e1...}<br>T3(l3)<br>->error|testSomeNullSetList|
|null|*|invalid|<br>T6(null)<br>->error|testNullSetList||



<br/>

# Class *PersistencyManager*



### Method store



**Criteria for method store:**
	

- Validity of storable

- Existence of file

  


**Predicates for method store:**

| Criterion            | Predicate |
| -------------------- | --------- |
| Validity of storable | Valid     |
|                      | null      |
| Existence of file    | Yes       |
|                      | No        |



**Boundaries for method store**:

| Criterion | Boundary values |
| --------- | --------------- |



 **Combination of predicates for method store**

| Validity of storable | Existence of file | Valid/Invalid | Description of the test case                                 | JUnit test case                               |
| -------------------- | ----------------- | ------------- | ------------------------------------------------------------ | --------------------------------------------- |
| null                 | *                 | Valid         | T1(null; false)                                              | TestPersistency<br />testStoreNull            |
| *                    | No                | Valid         | -> return false                                              | TestPersistency<br />testStoreNotExistingFile |
| Valid                | Yes               | Valid         | UserImpl  kanye = new UserImpl("yeezy", "iamagod", "Cashier");<br />store(kanye) -> return true | TestPersistency<br />testStoreSuccessful      |

<br/>

### Method delete

**Criteria for method delete:**
	

- Validity of storable

- Existence of file

  


**Predicates for method delete:**

| Criterion            | Predicate |
| -------------------- | --------- |
| Validity of storable | Valid     |
|                      | null      |
| Existence of file    | Yes       |
|                      | No        |



**Boundaries for method delete**:

| Criterion | Boundary values |
| --------- | --------------- |



 **Combination of predicates for method delete**

| Validity of storable | Existence of file | Valid/Invalid | Description of the test case                                 | JUnit test case                                |
| -------------------- | ----------------- | ------------- | ------------------------------------------------------------ | ---------------------------------------------- |
| null                 | *                 | Valid         | T1(null; false)                                              | TestPersistency<br />testDeleteNull            |
| *                    | No                | Valid         | -> return false                                              | TestPersistency<br />testDeleteNotExistingFile |
| Valid                | Yes               | Valid         | UserImpl  kanye = new UserImpl("yeezy", "iamagod", "Cashier");<br />store(kanye) ;<br />delete(kanye) -> return true; | TestPersistency<br />testDeleteSuccessful      |

<br/>

### Method update

**Criteria for method update:**
	

- Validity of storable

- Existence of file

  


**Predicates for method update:**

| Criterion            | Predicate |
| -------------------- | --------- |
| Validity of storable | Valid     |
|                      | null      |
| Existence of file    | Yes       |
|                      | No        |



**Boundaries for method update**:

| Criterion | Boundary values |
| --------- | --------------- |



 **Combination of predicates for method update**

| Validity of storable | Existence of file | Valid/Invalid | Description of the test case                                 | JUnit test case                                |
| -------------------- | ----------------- | ------------- | ------------------------------------------------------------ | ---------------------------------------------- |
| null                 | *                 | Valid         | T1(null, false);                                             | TestPersistency<br />testUpdateNull            |
| *                    | No                | Valid         | -> return false                                              | TestPersistency<br />testUpdateNotExistingFile |
| Valid                | Yes               | Valid         | UserImpl  kanye = new UserImpl("yeezy", "iamagod", "Cashier");<br />store(kanye) ;<br />kanye.setPassword("followgod");<br />update(kanye) -> return true; | TestPersistency<br />testUpdateSuccessful      |

<br/>

### Method getFile

**Criteria for method getFile:**
	

- Validity of storable

  


**Predicates for method getFile:**

| Criterion            | Predicate |
| -------------------- | --------- |
| Validity of storable | Valid     |
|                      | null      |
| Existence of file    | Yes       |
|                      | No        |



**Boundaries for method getFile**:

| Criterion | Boundary values |
| --------- | --------------- |



 **Combination of predicates for method getFile**

| Validity of storable | Existence of file | Valid/Invalid | Description of the test case                                 | JUnit test case                             |
| -------------------- | ----------------- | ------------- | ------------------------------------------------------------ | ------------------------------------------- |
| null                 | *                 | Valid         | T1(null; "");                                                | TestPersistency<br />testGetFileNull        |
| *                    | No                | Valid         | => return "";                                                | TestPersistency<br />testGetFileNotExisting |
| Valid                | Yes               | Valid         | UserImpl  kanye = new UserImpl("yeezy", "iamagod", "Cashier");<br />getFile(kanye) -> return "dataCSV/users.csv"; | TestPersistency<br />testGetFileSuccessful  |

<br/>

# Class *BalanceImpl*



### Method recordBalanceOperation

**Criteria for method recordBalanceOperation:**
	

- Validity of balanceOperation

  


**Predicates for method recordBalanceOperation:**

| Criterion                    | Predicate |
| ---------------------------- | --------- |
| Validity of balanceOperation | Valid     |
|                              | null      |



**Boundaries for method recordBalanceOperation**:

| Criterion | Boundary values |
| --------- | --------------- |



 **Combination of predicates for method recordBalanceOperation**

| Validity of balanceOperation | Valid/Invalid | Description of the test case                                 | JUnit test case                                     |
| ---------------------------- | ------------- | ------------------------------------------------------------ | --------------------------------------------------- |
| null                         | Valid         | T1(null) -> return;                                          | TestBalanceImpl<br />testRecordBalanceOperationNull |
| Valid                        | Valid         | BalanceOperationImpl bo = new BalanceOperationImpl(1, "18/05/21", 50.0, CREDIT);<br />recordBalanceOperation(bo) -> return; <br />//put new balance operation | TestBalanceImpl<br />testRecordBalanceSuccessful    |

<br/>



# Class *ProductTypeImpl*

### Method validateProductCode

**Criteria for method validateProductCode:**
	

- Validity of productCode

- Length of productCode

- Wrong barcode

  

**Predicates for method validateProductCode:**

| Criterion               | Predicate            |
| ----------------------- | -------------------- |
| Validity of productCode | Valid                |
|                         | null                 |
| Length of productCode   | [12, 14]             |
|                         | [0, 12) U (14, +inf) |
| Wrong barcode           | Yes                  |
|                         | No                   |



**Boundaries for method validateProductCode**:

| Criterion             | Boundary values |
| --------------------- | --------------- |
| Length of productCode | 11,12,14,15     |



 **Combination of predicates for method validateProductCode**

| Validity of productCode | Length of productCode | Wrong barcode | Valid/Invalid | Description of the test case                                 | JUnit test case                                             |
| ----------------------- | --------------------- | ------------- | ------------- | ------------------------------------------------------------ | ----------------------------------------------------------- |
| null                    | *                     | *             | Valid         | T1(null;  false)                                             | TestProductTypeImpl <br />testValidateProductCodeNull       |
| *                       | [0, 12) U (14, +inf)  | *             | Valid         | T2("80040248"; false);<br />T2b1("80570140500"; false);<br />T2b2("80570140500356": false); | TestProductTypeImpl <br />testValidateProductCodeLength     |
| *                       | *                     | Yes           | Valid         | T3("1234567891011"; false);                                  | TestProductTypeImpl <br />testValidateProductCodeWrong      |
| Valid                   | [12, 14]              | No            | Valid         | T4("8057014050035"; true);                                   | TestProductTypeImpl <br />testValidateProductCodeSuccessful |

<br/>

# Class *ProductImpl*

### Method validateRFID

**Criteria for method validateRFID:**
	

- Validity of RFID

- Length of RFID

- Wrong RFID

  

**Predicates for method validateRFID:**

| Criterion        | Predicate            |
| ---------------- | -------------------- |
| Validity of RFID | Valid                |
|                  | null                 |
| Length of RFID   | [10]                 |
|                  | [0, 10) U (10, +inf) |
| Wrong RFID       | Yes                  |
|                  | No                   |

**Boundaries for method validateRFID**:

| Criterion      | Boundary values |
| -------------- | --------------- |
| Length of RFID | 9, 11           |

 **Combination of predicates for method validateRFID**

| Validity of RFID | Length of RFID       | Wrong RFID | Valid/Invalid | Description of the test case                               | JUnit test case                        |
| ---------------- | -------------------- | ---------- | ------------- | ---------------------------------------------------------- | -------------------------------------- |
| null             | *                    | *          | Valid         | T1(null;  false)                                           | TestProductImpl <br />testValidateRFID |
| *                | [0, 10) U (10, +inf) | *          | Valid         | T2("00001"; false);<br />T2b1("00000000001"; false);<br /> | TestProductImpl <br />testValidateRFID |
| *                | *                    | Yes        | Valid         | T3("000000000a"; false);                                   | TestProductImpl <br />testValidateRFID |
| Valid            | [10]                 | No         | Valid         | T4("0000000001"; true);                                    | TestProductImpl <br />testValidateRFID |




# White Box Unit Tests

### Test cases definition

    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|BalanceImpl|testBalanceImplSetGet|
||testBalanceImplUpdate|
||testGetLastId|
|BalanceOperationImpl| testBalanceOperationSetGet  |
||testBalanceOperationImplCSV|
|ProductImpl|testConstructors|
||testSettersGetters|
||testGetCSV|
|CreditCardManagerImpl|testLuhn
||testWithdraw
||testRefund
|CustomerCardImpl|testCustomerCardGetSet
||testCustomerCardCSV
|CustomerImpl|testCustomerGetSet
||testCustomerCSV
|OrderImpl|testOrderImplSetGet|
||testOrderImplCSV|
|PositionImpl|testPositionImplSetGet|
||testPositionImplCSV|
|ProductTypeImpl|testProductTypeImplSetGet|
||testProductTypeImplCSV|
|SaleTransactionImpl| testSaleTransactionImplSetGet|
||testSaleTransactionImplCSV|
||testApplyReturnLoop|
|ReturnTransactionImpl| testReturnTransactionImplSetGet|
||testReturnTransactionImplSetCSV|
|TicketEntryImpl|testTicketEntryImplSetGet|
||testTicketEntryImplCSV|
|ReturnEntryImpl|testReturnEntryImplSetGet|
||testReturnEntryImplCSV|
|UserImpl|testUserImplSetGet|
||testUserImplCSV|

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >

![alt text](coverage.png)

### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|SaleTransactionImpl|231-241|0|testApplyReturnLoop|
|||1|testApplyReturnLoop|
|||3|testApplyReturnLoop|
|||invalid 1|testApplyReturnLoop|
|||invalid 2|testApplyReturnLoop

