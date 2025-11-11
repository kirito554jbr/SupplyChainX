# How to Run Tests - SupplyChainX

## ✅ Test Results Summary

**RawMaterialServiceTest**: ✅ All 18 tests PASSED  
- Tests run: 18
- Failures: 0
- Errors: 0
- Skipped: 0
- Time: ~1.7 seconds

---

## 🚀 Running Tests - Multiple Options

### Option 1: Using Maven Wrapper (Recommended - Works Without Maven Installation)

The project includes `mvnw.cmd` (Windows) and `mvnw` (Linux/Mac), so you don't need Maven installed.

#### Run specific test class:
```cmd
mvnw.cmd test -Dtest=RawMaterialServiceTest
```

#### Run all tests:
```cmd
mvnw.cmd test
```

#### Run a specific test method:
```cmd
mvnw.cmd test -Dtest=RawMaterialServiceTest#testSaveRawMaterial_Success
```

#### Run tests with detailed output:
```cmd
mvnw.cmd test -Dtest=RawMaterialServiceTest -X
```

---

### Option 2: Using IntelliJ IDEA (Easiest)

1. **Run entire test class:**
   - Open `RawMaterialServiceTest.java`
   - Right-click on the class name
   - Select "Run 'RawMaterialServiceTest'"
   - Or use keyboard shortcut: `Ctrl + Shift + F10`

2. **Run single test method:**
   - Click the green play icon ▶️ next to the test method
   - Or right-click on the method and select "Run"

3. **Run with coverage:**
   - Right-click on test class
   - Select "Run 'RawMaterialServiceTest' with Coverage"
   - Or use: `Ctrl + Shift + F10` then `Ctrl + Alt + F6`

4. **Re-run last test:**
   - Use keyboard shortcut: `Shift + F10`

5. **Debug tests:**
   - Right-click and select "Debug 'RawMaterialServiceTest'"
   - Or use: `Shift + F9`

---

### Option 3: Using Maven (If Maven is Installed)

#### Run specific test:
```cmd
mvn test -Dtest=RawMaterialServiceTest
```

#### Run all tests:
```cmd
mvn test
```

#### Run tests and generate coverage report:
```cmd
mvn clean test jacoco:report
```
Report will be in: `target/site/jacoco/index.html`

---

### Option 4: Using PowerShell

Since you're on Windows with PowerShell:

```powershell
# Navigate to project directory
cd C:\Users\Youcode\IdeaProjects\SupplyChainX

# Run tests using Maven wrapper
.\mvnw.cmd test -Dtest=RawMaterialServiceTest

# Run all tests
.\mvnw.cmd test

# Clean and run tests
.\mvnw.cmd clean test
```

---

## 📊 Understanding Test Output

### Successful Test Run:
```
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Failed Test Run:
```
[INFO] Tests run: 18, Failures: 2, Errors: 0, Skipped: 0
[ERROR] BUILD FAILURE
```

---

## 🔍 Running Tests for Other Services

### Pattern for running specific test classes:
```cmd
mvnw.cmd test -Dtest=SupplierServiceTest
mvnw.cmd test -Dtest=ProductServiceTest
mvnw.cmd test -Dtest=CustomerServiceTest
```

### Run multiple test classes:
```cmd
mvnw.cmd test -Dtest=RawMaterialServiceTest,SupplierServiceTest
```

### Run tests matching a pattern:
```cmd
mvnw.cmd test -Dtest=*ServiceTest
mvnw.cmd test -Dtest=Approvisionnement.*Test
```

---

## 🐛 Troubleshooting

### Issue: "mvnw.cmd is not recognized"
**Solution:** Make sure you're in the project root directory:
```cmd
cd C:\Users\Youcode\IdeaProjects\SupplyChainX
```

### Issue: Tests fail with database connection errors
**Solution:** The unit tests use mocks, so they shouldn't connect to DB. If you see DB errors:
1. Check that `@ExtendWith(MockitoExtension.class)` is present
2. Verify all repositories are `@Mock` annotated
3. Make sure you're not running integration tests by mistake

### Issue: "No tests found"
**Solution:** 
1. Rebuild the project: `mvnw.cmd clean compile`
2. Verify test class is in `src/test/java/` directory
3. Check test methods are public and annotated with `@Test`

---

## 📈 Test Coverage with JaCoCo

### Add JaCoCo to pom.xml (if not already present):
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Generate coverage report:
```cmd
mvnw.cmd clean test jacoco:report
```

### View coverage report:
Open in browser: `target/site/jacoco/index.html`

---

## 🎯 Quick Reference Commands

```cmd
# Most common commands (run from project root)

# Run RawMaterialServiceTest
mvnw.cmd test -Dtest=RawMaterialServiceTest

# Run all tests
mvnw.cmd test

# Run tests and skip compilation if already compiled
mvnw.cmd surefire:test -Dtest=RawMaterialServiceTest

# Run in quiet mode (less output)
mvnw.cmd -q test -Dtest=RawMaterialServiceTest

# Run with stack traces on failure
mvnw.cmd test -Dtest=RawMaterialServiceTest -e

# Clean, compile, and test
mvnw.cmd clean test
```

---

## ✨ Best Practices

1. **Always run tests before committing code**
   ```cmd
   mvnw.cmd test
   ```

2. **Run specific tests during development**
   ```cmd
   mvnw.cmd test -Dtest=RawMaterialServiceTest
   ```

3. **Check coverage periodically**
   ```cmd
   mvnw.cmd clean test jacoco:report
   ```

4. **Use IntelliJ for quick iterations during development**
   - Faster than command line
   - Better debugging capabilities
   - Visual test results

5. **Run all tests before creating a pull request**
   ```cmd
   mvnw.cmd clean test
   ```

---

## 📝 Test Naming Convention

Our tests follow this pattern:
- `test[MethodName]_[Scenario]`
- Example: `testSaveRawMaterial_Success`
- Example: `testFindByIdRawMaterial_NotFound`

This makes it easy to understand what each test does at a glance.

---

## ���� Next Steps

1. **Run the tests** using any method above
2. **Check the test results** in the console or IDE
3. **Review test coverage** using JaCoCo reports
4. **Create more tests** for other services following the same pattern
5. **Integrate tests** into CI/CD pipeline (GitHub Actions, Jenkins, etc.)

---

## 📞 Need Help?

- Check IntelliJ's "Run" panel for detailed test results
- Look at `target/surefire-reports/` for detailed test reports
- Use `-X` flag for debug output: `mvnw.cmd test -X -Dtest=RawMaterialServiceTest`

