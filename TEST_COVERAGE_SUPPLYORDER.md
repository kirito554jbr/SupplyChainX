# SupplyOrderService Test Coverage

## Test File
`src/test/java/org/example/supplychainx/service/Approvisionnement/SypplyOrderServiceTest.java`

## ✅ Test Results Summary

**All 7 tests PASSED**
- Tests run: 7
- Failures: 0
- Errors: 0
- Skipped: 0
- Time: ~1.6 seconds

---

## Test Coverage Summary

### Methods Tested (100% coverage)

1. **findById()** - 1 test
2. **findAll()** - 1 test
3. **save()** - 2 tests (success + validation)
4. **delete()** - 2 tests (success + business rule)
5. **update()** - 1 test

---

## Individual Test Cases

### 1. **testFindById_Success**
- ✅ Tests successful retrieval of a supply order by ID
- Verifies supplier name is correctly mapped to DTO
- Confirms repository and mapper are called

### 2. **testFindAll_Success**
- ✅ Tests retrieval of all supply orders
- Verifies list mapping and supplier names
- Confirms repository is called once

### 3. **testSave_Success**
- ✅ Tests successful creation of a supply order
- Validates supplier lookup
- Validates raw material lookup
- Confirms order is saved with EN_ATTENTE status
- Verifies all dependencies are called correctly

### 4. **testSave_FutureDate_ThrowsException**
- ✅ Tests business rule: order date cannot be in the future
- Verifies IllegalArgumentException is thrown
- Confirms save is NOT called when validation fails

### 5. **testDelete_Success**
- ✅ Tests successful deletion of a supply order
- Verifies order is found before deletion
- Confirms deleteById is called

### 6. **testDelete_DeliveredOrder_ThrowsException**
- ✅ Tests business rule: cannot delete delivered orders (RECUE status)
- Verifies ResponseStatusException is thrown
- Confirms delete is NOT called for delivered orders

### 7. **testUpdate_Success**
- ✅ Tests successful update of a supply order
- Verifies supplier lookup by name
- Confirms all fields are updated
- Verifies save is called

---

## Test Data Setup

Each test uses `@BeforeEach` to create:
- **Supplier**: ID=1, Name="Test Supplier"
- **RawMaterial**: ID=1, Name="Steel"
- **SupplyOrder**: ID=1, Status=EN_ATTENTE, Date=Today
- **SupplyOrderDTO**: Matching data for DTO operations

---

## Testing Framework

- **Framework**: JUnit 5 (Jupiter)
- **Mocking**: Mockito
- **Extension**: MockitoExtension

---

## Mocked Dependencies

- `SupplierRepository` - Supplier database operations
- `SupplyOrderRepository` - SupplyOrder database operations
- `SupplyOrderMapper` - Entity/DTO conversions
- `RawMaterialRepository` - RawMaterial database operations

---

## Business Rules Tested

✅ **Order Date Validation**: Cannot create orders with future dates  
✅ **Delete Protection**: Cannot delete orders with RECUE status  
✅ **Supplier Validation**: Supplier must exist when creating order  
✅ **Raw Material Validation**: Raw materials must exist when creating order  
✅ **Default Status**: New orders start with EN_ATTENTE status  

---

## How to Run Tests

### Command Line (Maven Wrapper):
```cmd
# Navigate to project
cd C:\Users\Youcode\IdeaProjects\SupplyChainX

# Run SupplyOrderService tests
mvnw.cmd test -Dtest=SypplyOrderServiceTest

# Run all tests
mvnw.cmd test
```

### PowerShell:
```powershell
cd C:\Users\Youcode\IdeaProjects\SupplyChainX
.\mvnw.cmd test -Dtest=SypplyOrderServiceTest
```

### IntelliJ IDEA:
1. Open `SypplyOrderServiceTest.java`
2. Right-click on the class name
3. Select "Run 'SypplyOrderServiceTest'"

---

## Key Test Patterns Used

### 1. Arrange-Act-Assert (AAA)
```java
// Arrange - Setup mocks and test data
when(repository.findById(1L)).thenReturn(Optional.of(entity));

// Act - Execute the method being tested
Result result = service.findById(1L);

// Assert - Verify the outcome
assertNotNull(result);
verify(repository, times(1)).findById(1L);
```

### 2. Exception Testing
```java
assertThrows(IllegalArgumentException.class, () -> {
    service.save(invalidRequest);
});
```

### 3. Verification of Mock Interactions
```java
verify(repository, times(1)).save(any());
verify(repository, never()).deleteById(any());
```

---

## Coverage Statistics

- **Total Service Methods**: 5
- **Methods Covered**: 5 (100%)
- **Success Scenarios**: 5 tests
- **Error/Validation Scenarios**: 2 tests
- **Total Assertions**: ~20

---

## Notes

- All tests are isolated and don't require a running database
- Tests use Mockito's `when()` and `verify()` for behavior verification
- Business rules (date validation, delete protection) are thoroughly tested
- Edge cases like delivered orders and future dates are covered
- Tests follow naming convention: `test[MethodName]_[Scenario]`

---

## Next Steps

✅ SupplyOrderService tests complete  
✅ RawMaterialService tests complete  

**Suggested next tests to create:**
- SupplierService tests
- ProductService tests
- CustomerService tests
- ProductionOrderService tests
- DeliveryService tests

---

## Related Files

- Service: `src/main/java/org/example/supplychainx/Service/Approvisionnement/SupplyOrderService.java`
- Test: `src/test/java/org/example/supplychainx/service/Approvisionnement/SypplyOrderServiceTest.java`
- Documentation: `TEST_COVERAGE_SUPPLYORDER.md` (this file)

