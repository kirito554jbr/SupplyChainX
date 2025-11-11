# RawMaterial Service Test Coverage

## Test File
`src/test/java/org/example/supplychainx/service/Approvisionnement/RawMaterialServiceTest.java`

## Test Coverage Summary

### ✅ Complete Test Suite Created

All methods in `RawMateialService` are now fully tested with comprehensive unit tests.

---

## Methods Tested

### 1. **findByIdRawMaterial()**
- ✅ `testFindByIdRawMaterial_Success` - Tests successful retrieval by ID
- ✅ `testFindByIdRawMaterial_NotFound` - Tests when material doesn't exist

### 2. **findAllRawMaterials()**
- ✅ `testFindAllRawMaterials_Success` - Tests retrieval of all materials
- ✅ `testFindAllRawMaterials_EmptyList` - Tests when repository is empty

### 3. **saveRawMaterial()**
- ✅ `testSaveRawMaterial_Success` - Tests successful save with suppliers
- ✅ `testSaveRawMaterial_SupplierNotFound` - Tests exception when supplier doesn't exist

### 4. **deleteRawMaterial()**
- ✅ `testDeleteRawMaterial_Success` - Tests successful deletion

### 5. **updateRawMaterial()**
- ✅ `testUpdateRawMaterial_Success` - Tests successful update with all fields

### 6. **deleteOneSupplierFromRawMaterial()**
- ✅ `testDeleteOneSupplierFromRawMaterial_Success` - Tests removing one supplier
- ✅ `testDeleteOneSupplierFromRawMaterial_MaterialNotFound` - Tests when material doesn't exist

### 7. **addSupplierToRawMaterial()**
- ✅ `testAddSupplierToRawMaterial_Success` - Tests adding supplier to existing material
- ✅ `testAddSupplierToRawMaterial_WithNullSupplierList` - Tests adding supplier when list is null
- ✅ `testAddSupplierToRawMaterial_MaterialNotFound` - Tests when material doesn't exist

### 8. **findByName()**
- ✅ `testFindByName_Success` - Tests finding material by name

### 9. **findEntityById()**
- ✅ `testFindEntityById_Success` - Tests successful entity retrieval
- ✅ `testFindEntityById_NotFound` - Tests exception when not found

### 10. **getRawMaterialsWithLowStock()**
- ✅ `testGetRawMaterialsWithLowStock_Success` - Tests filtering low stock materials
- ✅ `testGetRawMaterialsWithLowStock_NoLowStockMaterials` - Tests when no low stock exists

---

## Test Statistics

- **Total Tests**: 18
- **Methods Covered**: 10/10 (100%)
- **Test Types**:
  - Success scenarios: 11 tests
  - Error/Edge cases: 7 tests

---

## Testing Framework

- **Framework**: JUnit 5 (Jupiter)
- **Mocking**: Mockito
- **Extension**: MockitoExtension

---

## Mocked Dependencies

- `RawMaterialRepository` - Database operations
- `RawMaterialMapper` - DTO/Entity conversions
- `SupplierService` - Supplier operations
- `SupplierMapper` - Supplier DTO/Entity conversions

---

## How to Run Tests

```bash
# Run all tests in the class
mvn test -Dtest=RawMaterialServiceTest

# Run a specific test
mvn test -Dtest=RawMaterialServiceTest#testSaveRawMaterial_Success

# Run all tests with coverage
mvn clean test jacoco:report
```

---

## Test Data Setup

Each test uses a `@BeforeEach` setup method that creates:
- A `Supplier` entity with ID=1, name="Test Supplier"
- A `SupplierDTO` with matching data
- A `RawMaterial` entity with ID=1, name="Steel", stock=100, minStock=50
- A `RawMaterialDTO` with matching data

---

## Key Assertions Verified

✅ Null safety  
✅ Correct data mapping  
✅ Exception handling  
✅ Repository method invocations  
✅ Mapper method invocations  
✅ Business logic (e.g., low stock filtering)  
✅ List operations (filtering, adding, removing)  

---

## Notes

- All tests use Mockito's `when()` and `verify()` for behavior verification
- Tests are isolated and don't depend on database state
- Edge cases like null values and empty lists are covered
- Exception scenarios include proper assertion of error messages

