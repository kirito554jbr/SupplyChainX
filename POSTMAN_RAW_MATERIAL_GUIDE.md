# Guide: Creating Raw Materials via Postman

## Prerequisites
- Docker containers running (app on port 8080)
- Postman installed

## Step-by-Step Guide

### Step 1: Login to Get Access Token

Since the endpoint requires authentication and the role `ADMIN` or `GESTIONNAIRE_APPROVISIONNEMENT`, you need to login first.

**Request:**
```
POST http://localhost:8080/api/auth/login
```

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "email": "admin@test.com",
  "password": "0000"
}
```

**OR for Gestionnaire role:**
```json
{
  "email": "gestionnaire@test.com",
  "password": "0000"
}
```

**Response Example:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB0ZXN0LmNvbSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTYxMjM0NTY3OCwiZXhwIjoxNjEyMzQ2NTc4fQ.xyz...",
  "refreshToken": "abc123...",
  "tokenType": "Bearer",
  "expiresIn": 900000
}
```

**Copy the `accessToken` value** - you'll need it for the next request.

---

### Step 2: Create Raw Material

**Request:**
```
POST http://localhost:8080/api/rawMaterials
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer YOUR_ACCESS_TOKEN_HERE
```

**Body (raw JSON) - Basic Example:**
```json
{
  "name": "Steel Sheet",
  "stock": 100,
  "MinStock": 20,
  "unit": "kg",
  "suppliers": []
}
```

**Body (raw JSON) - With Suppliers (if you have suppliers already):**
```json
{
  "name": "Aluminum Tubes",
  "stock": 500,
  "MinStock": 50,
  "unit": "meters",
  "suppliers": ["Supplier A", "Supplier B"]
}
```

**Field Descriptions:**
- `name` (String, required): Name of the raw material
- `stock` (Integer, required): Current stock quantity
- `MinStock` (Integer, required): Minimum stock threshold
- `unit` (String, required): Unit of measurement (e.g., "kg", "liters", "meters", "pieces")
- `suppliers` (Array of Strings, optional): List of supplier names

**Expected Response (201 Created):**
```json
{
  "idRawMaterial": 1,
  "name": "Steel Sheet",
  "stock": 100,
  "MinStock": 20,
  "unit": "kg",
  "suppliers": []
}
```

---

## Additional Operations

### Get All Raw Materials
```
GET http://localhost:8080/api/rawMaterials
Authorization: Bearer YOUR_ACCESS_TOKEN_HERE
```

### Get Raw Material by ID
```
GET http://localhost:8080/api/rawMaterials/1
Authorization: Bearer YOUR_ACCESS_TOKEN_HERE
```

### Update Raw Material
```
PUT http://localhost:8080/api/rawMaterials/1
Authorization: Bearer YOUR_ACCESS_TOKEN_HERE
Content-Type: application/json

{
  "name": "Steel Sheet - Updated",
  "stock": 150,
  "MinStock": 30,
  "unit": "kg",
  "suppliers": []
}
```

### Delete Raw Material
```
DELETE http://localhost:8080/api/rawMaterials/1
Authorization: Bearer YOUR_ACCESS_TOKEN_HERE
```

### Get Low Stock Materials
```
GET http://localhost:8080/api/rawMaterials/filter/low-stock
Authorization: Bearer YOUR_ACCESS_TOKEN_HERE
```

### Add Supplier to Raw Material
```
POST http://localhost:8080/api/rawMaterials/1/suppliers/1
Authorization: Bearer YOUR_ACCESS_TOKEN_HERE
```

### Remove Supplier from Raw Material
```
DELETE http://localhost:8080/api/rawMaterials/1/suppliers/1
Authorization: Bearer YOUR_ACCESS_TOKEN_HERE
```

---

## Postman Setup Instructions

1. **Create a New Request in Postman**
   - Click "+ New" → "HTTP Request"
   - Name it "Create Raw Material"

2. **Configure the Request**
   - Method: POST
   - URL: `http://localhost:8080/api/rawMaterials`

3. **Add Headers**
   - Go to "Headers" tab
   - Add: `Content-Type` = `application/json`
   - Add: `Authorization` = `Bearer YOUR_TOKEN_HERE`

4. **Add Body**
   - Go to "Body" tab
   - Select "raw"
   - Select "JSON" from dropdown
   - Paste the JSON example

5. **Send Request**
   - Click "Send"
   - Check the response status (should be 201)

---

## Troubleshooting

### Issue: 401 Unauthorized
**Solution:** Make sure you've:
1. Logged in successfully
2. Copied the correct access token
3. Added "Bearer " prefix before the token in Authorization header

### Issue: 403 Forbidden
**Solution:** Use an account with ADMIN or GESTIONNAIRE_APPROVISIONNEMENT role

### Issue: Connection Refused
**Solution:** Check that Docker containers are running:
```bash
docker ps
```
Make sure the `app` container is running on port 8080

### Issue: 500 Internal Server Error
**Solution:** Check that:
1. Database is properly initialized
2. All required fields are provided in the JSON body
3. Data types are correct (integers for stock, string for name)

---

## Quick Test Examples

### Example 1: Wood Material
```json
{
  "name": "Oak Wood Planks",
  "stock": 250,
  "MinStock": 40,
  "unit": "planks",
  "suppliers": []
}
```

### Example 2: Plastic Material
```json
{
  "name": "PVC Pipes",
  "stock": 300,
  "MinStock": 60,
  "unit": "meters",
  "suppliers": []
}
```

### Example 3: Metal Material
```json
{
  "name": "Copper Wire",
  "stock": 1000,
  "MinStock": 200,
  "unit": "meters",
  "suppliers": []
}
```

