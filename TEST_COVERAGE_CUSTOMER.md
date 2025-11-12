# CustomerService Test Coverage

## Test File
`src/test/java/org/example/supplychainx/service/Livraison/CustomerServiceTest.java`

## ✅ Test Results Summary

**All 10 tests PASSED**
- Tests run: 10
- Failures: 0
- Errors: 0
- Skipped: 0
- Time: ~2.3 seconds

---

## Test Coverage Summary

### Methods Tested (100% coverage)

1. **findById()** - 2 tests (success + not found)
2. **findAll()** - 2 tests (success + empty list)
3. **save()** - 1 test
4. **update()** - 2 tests (success + not found)
5. **deleteById()** - 1 test
6. **findByName()** - 2 tests (success + not found)

---

## Individual Test Cases

### 1. **testFindById_Success**
- ✅ Tests successful retrieval of a customer by ID
- Verifies customer details are correctly mapped to DTO
- Confirms repository and mapper are called

### 2. **testFindById_NotFound**
- ✅ Tests behavior when customer ID doesn't exist
- Verifies null is returned
- Confirms repository lookup is attempted

### 3. **testFindAll_Success**
- ✅ Tests retrieval of all customers
- Verifies list mapping with multiple customers
- Confirms all customer data is correctly converted to DTOs

### 4. **testFindAll_EmptyList**
- ✅ Tests behavior when no customers exist
- Verifies empty list is returned (not null)
- Confirms repository is called

### 5. **testSave_Success**
- ✅ Tests successful creation of a new customer
- Validates DTO to Entity conversion
- Verifies customer is saved and returned as DTO
- Confirms all mapper methods are called

### 6. **testUpdate_Success**
- ✅ Tests successful update of existing customer
- Verifies customer data is updated correctly
- Validates ID is preserved during update
- Confirms save is called with updated data

### 7. **testUpdate_NotFound**
- ✅ Tests update behavior when customer doesn't exist
- Verifies null is returned
- Confirms save is NOT called for non-existent customer

### 8. **testDeleteById_Success**
- ✅ Tests successful deletion of a customer
- Verifies deleteById is called on repository
- Confirms deletion operation completes

### 9. **testFindByName_Success**
- ✅ Tests finding customer by name
- Verifies correct customer is returned
- Confirms name-based lookup works correctly

### 10. **testFindByName_NotFound**
- ✅ Tests name lookup when customer doesn't exist
- Verifies null is returned
- Confirms repository lookup is attempted

---

## Test Data Setup

Each test uses `@BeforeEach` to create:
- **Customer Entity**: ID=1, Name="John Doe", Address="123 Main Street", City="Casablanca"
- **CustomerDTO**: Matching data for DTO operations

---

## Testing Framework

- **Framework**: JUnit 5 (Jupiter)
- **Mocking**: Mockito
- **Extension**: MockitoExtension

---

## Mocked Dependencies

- `CustomerRepository` - Customer database operations
- `CustomerMapper` - Entity/DTO conversions
- `OrderRepository` - Order-related operations (injected dependency)

---

## Business Rules Tested

✅ **Create**: Can create new customers  
✅ **Read**: Can retrieve by ID, all customers, or by name  
✅ **Update**: Can update existing customers, returns null if not found  
✅ **Delete**: Can delete customers  
✅ **Empty Results**: Properly handles empty lists and missing records  

---

## How to Run Tests

### Command Line (Maven Wrapper):
```cmd
# Navigate to project
cd C:\Users\Youcode\IdeaProjects\SupplyChainX

# Run CustomerService tests
mvnw.cmd test -Dtest=CustomerServiceTest

# Run all tests
mvnw.cmd test
```

### PowerShell:
```powershell
cd C:\Users\Youcode\IdeaProjects\SupplyChainX
.\mvnw.cmd test -Dtest=CustomerServiceTest
```

### IntelliJ IDEA:
1. Open `CustomerServiceTest.java`
2. Right-click on the class name
3. Select "Run 'CustomerServiceTest'"

---

## Key Test Patterns Used

### 1. Arrange-Act-Assert (AAA)
```java
// Arrange - Setup mocks and test data
when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
when(customerMapper.toDto(customer)).thenReturn(customerDTO);

// Act - Execute the method being tested
CustomerDTO result = customerService.findById(1L);

// Assert - Verify the outcome
assertNotNull(result);
assertEquals("John Doe", result.getName());
verify(customerRepository, times(1)).findById(1L);
```

### 2. Null Handling Tests
```java
when(customerRepository.findById(999L)).thenReturn(Optional.empty());
CustomerDTO result = customerService.findById(999L);
assertNull(result);
```

### 3. Verification of Mock Interactions
```java
verify(customerRepository, times(1)).save(any());
verify(customerRepository, never()).save(any(Customer.class));
```

---

## Coverage Statistics

- **Total Service Methods**: 6
- **Methods Covered**: 6 (100%)
- **Success Scenarios**: 6 tests
- **Error/Not Found Scenarios**: 4 tests
- **Total Assertions**: ~30

---

## Notes

- All tests are isolated and don't require a running database
- Tests use Mockito's `when()` and `verify()` for behavior verification
- Edge cases like non-existent customers and empty lists are covered
- Tests follow naming convention: `test[MethodName]_[Scenario]`
- Mapper layer is properly mocked to isolate service logic

---

## Test Summary by Category

### ✅ **CRUD Operations**
- Create (save) - 1 test
- Read (findById, findAll, findByName) - 6 tests
- Update - 2 tests
- Delete - 1 test

### ✅ **Error Handling**
- Not found scenarios - 3 tests
- Empty list handling - 1 test

---

## Related Files

- Service: `src/main/java/org/example/supplychainx/Service/Livraison/CustomerService.java`
- Test: `src/test/java/org/example/supplychainx/service/Livraison/CustomerServiceTest.java`
- Model: `src/main/java/org/example/supplychainx/Model/Livraison/Customer.java`
- DTO: `src/main/java/org/example/supplychainx/DTO/Livraison/CustomerDTO.java`
- Documentation: `TEST_COVERAGE_CUSTOMER.md` (this file)

---

## Next Steps

✅ CustomerService tests complete (10 tests)  
✅ RawMaterialService tests complete (18 tests)  
✅ SupplyOrderService tests complete (7 tests)  
✅ SupplierService tests complete (12 tests)  

**Total Unit Tests**: 47 tests - All passing! 🎉

**Suggested next tests to create:**
- ProductService tests
- OrderService tests
- DeliveryService tests
- ProductionOrderService tests
- BOMService tests

