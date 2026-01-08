# SupplyChainX - Angular Frontend API Documentation

## 🚀 Quick Start

**Backend URL:** `http://localhost:8080`  
**Authentication:** Currently **DISABLED** for development  
**CORS:** Enabled for `http://localhost:4200`

---

## Table of Contents
1. [Getting Started](#getting-started)
2. [API Base URL](#api-base-url)
3. [HTTP Client Setup](#http-client-setup)
4. [API Endpoints](#api-endpoints)
5. [Data Models](#data-models)
6. [Angular Services Examples](#angular-services-examples)
7. [Common Patterns](#common-patterns)

---

## Getting Started

### Prerequisites
- Spring Boot backend running on `http://localhost:8080`
- Angular app on `http://localhost:4200` (or update CORS in `application.properties`)
- No authentication required (currently disabled)

### Angular HTTP Client Setup

```typescript
// app.config.ts or app.module.ts
import { provideHttpClient } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(),
    // ... other providers
  ]
};
```

---

## API Base URL

```typescript
// environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};

// environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://your-production-domain.com/api'
};
```

---

## HTTP Client Setup

### Basic Service Template

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BaseService {
  protected apiUrl = environment.apiUrl;

  constructor(protected http: HttpClient) {}
}
```

---

## API Endpoints

### 1. Users API (`/api/users`)

#### Get All Users
```typescript
GET /api/users
Response: User[]
```

#### Get User by ID
```typescript
GET /api/users/{id}
Response: User
```

#### Create User
```typescript
POST /api/users
Body: {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  role: string; // ADMIN, GESTIONNAIRE_APPROVISIONNEMENT, etc.
}
Response: User
```

#### Update User
```typescript
PUT /api/users/{id}
Body: {
  firstName: string;
  lastName: string;
  email: string;
  role: string;
}
Response: User
```

#### Delete User
```typescript
DELETE /api/users/{id}
Response: 204 No Content
```

---

### 2. Raw Materials API (`/api/rawMaterials`)

#### Get All Raw Materials
```typescript
GET /api/rawMaterials
Response: RawMaterial[]
```

#### Get Raw Material by ID
```typescript
GET /api/rawMaterials/{id}
Response: RawMaterial
```

#### Get Low Stock Items
```typescript
GET /api/rawMaterials/filter/low-stock
Response: RawMaterial[]
```

#### Create Raw Material
```typescript
POST /api/rawMaterials
Body: {
  name: string;
  quantity: number;
  unit: string; // "kg", "liter", "unit", etc.
  minThreshold: number;
}
Response: RawMaterial
```

#### Update Raw Material
```typescript
PUT /api/rawMaterials/{id}
Body: {
  name: string;
  quantity: number;
  unit: string;
  minThreshold: number;
}
Response: RawMaterial
```

#### Delete Raw Material
```typescript
DELETE /api/rawMaterials/{id}
Response: 204 No Content
```

---

### 3. Suppliers API (`/api/suppliers`)

#### Get All Suppliers
```typescript
GET /api/suppliers
Response: Supplier[]
```

#### Get Supplier by ID
```typescript
GET /api/suppliers/{id}
Response: Supplier
```

#### Create Supplier
```typescript
POST /api/suppliers
Body: {
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
}
Response: Supplier
```

#### Update Supplier
```typescript
PUT /api/suppliers/{id}
Body: {
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
}
Response: Supplier
```

#### Delete Supplier
```typescript
DELETE /api/suppliers/{id}
Response: 204 No Content
```

---

### 4. Supply Orders API (`/api/supply-orders`)

#### Get All Supply Orders
```typescript
GET /api/supply-orders
Response: SupplyOrder[]
```

#### Get Supply Order by ID
```typescript
GET /api/supply-orders/{id}
Response: SupplyOrder
```

#### Create Supply Order
```typescript
POST /api/supply-orders
Body: {
  supplierId: number;
  rawMaterialId: number;
  quantity: number;
  orderDate: string; // ISO date format
  expectedDeliveryDate: string; // ISO date format
  status: string; // "PENDING", "CONFIRMED", "DELIVERED", "CANCELLED"
}
Response: SupplyOrder
```

#### Update Supply Order
```typescript
PUT /api/supply-orders/{id}
Body: {
  quantity: number;
  expectedDeliveryDate: string;
  status: string;
}
Response: SupplyOrder
```

#### Delete Supply Order
```typescript
DELETE /api/supply-orders/{id}
Response: 204 No Content
```

---

### 5. Customers API (`/api/customers`)

#### Get All Customers
```typescript
GET /api/customers
Response: Customer[]
```

#### Get Customer by ID
```typescript
GET /api/customers/{id}
Response: Customer
```

#### Get Customer by Name
```typescript
GET /api/customers/by-name/{name}
Response: Customer
```

#### Create Customer
```typescript
POST /api/customers
Body: {
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
}
Response: Customer
```

#### Update Customer
```typescript
PUT /api/customers/{id}
Body: {
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
}
Response: Customer
```

#### Delete Customer
```typescript
DELETE /api/customers/{id}
Response: 204 No Content
```

---

### 6. Products API (`/api/products`)

#### Get All Products
```typescript
GET /api/products
Response: Product[]
```

#### Get Product by ID
```typescript
GET /api/products/{id}
Response: Product
```

#### Create Product
```typescript
POST /api/products
Body: {
  name: string;
  description: string;
  price: number;
  quantity: number;
  category: string;
}
Response: Product
```

#### Update Product
```typescript
PUT /api/products/{id}
Body: {
  name: string;
  description: string;
  price: number;
  quantity: number;
  category: string;
}
Response: Product
```

#### Delete Product
```typescript
DELETE /api/products/{id}
Response: 204 No Content
```

---

### 7. Bill of Materials (BOM) API (`/api/boms`)

#### Get All BOMs
```typescript
GET /api/boms
Response: BOM[]
```

#### Get BOM by ID
```typescript
GET /api/boms/{id}
Response: BOM
```

#### Create BOM
```typescript
POST /api/boms
Body: {
  productId: number;
  rawMaterialId: number;
  quantityRequired: number;
}
Response: BOM
```

#### Update BOM
```typescript
PUT /api/boms/{id}
Body: {
  quantityRequired: number;
}
Response: BOM
```

#### Delete BOM
```typescript
DELETE /api/boms/{id}
Response: 204 No Content
```

---

### 8. Production Orders API (`/api/productionOrders`)

#### Get All Production Orders
```typescript
GET /api/productionOrders
Response: ProductionOrder[]
```

#### Get Production Order by ID
```typescript
GET /api/productionOrders/{id}
Response: ProductionOrder
```

#### Create Production Order
```typescript
POST /api/productionOrders
Body: {
  productId: number;
  quantityOrdered: number;
  startDate: string; // ISO date format
  expectedEndDate: string; // ISO date format
  status: string; // "PLANNED", "IN_PROGRESS", "COMPLETED", "CANCELLED"
}
Response: ProductionOrder
```

#### Update Production Order
```typescript
PUT /api/productionOrders/{id}
Body: {
  quantityOrdered: number;
  startDate: string;
  expectedEndDate: string;
  status: string;
}
Response: ProductionOrder
```

#### Delete Production Order
```typescript
DELETE /api/productionOrders/{id}
Response: 204 No Content
```

---

### 9. Orders API (`/api/orders`)

#### Get All Orders
```typescript
GET /api/orders
Response: Order[]
```

#### Get Order by ID
```typescript
GET /api/orders/{id}
Response: Order
```

#### Create Order
```typescript
POST /api/orders
Body: {
  customerId: number;
  productId: number;
  quantity: number;
  orderDate: string; // ISO date format
  status: string; // "PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"
}
Response: Order
```

#### Update Order
```typescript
PUT /api/orders/{id}
Body: {
  quantity: number;
  status: string;
}
Response: Order
```

#### Delete Order
```typescript
DELETE /api/orders/{id}
Response: 204 No Content
```

---

### 10. Deliveries API (`/api/deliveries`)

#### Get All Deliveries
```typescript
GET /api/deliveries
Response: Delivery[]
```

#### Get Delivery by ID
```typescript
GET /api/deliveries/{id}
Response: Delivery
```

#### Create Delivery
```typescript
POST /api/deliveries
Body: {
  orderId: number;
  deliveryDate: string; // ISO date format
  deliveryAddress: string;
  status: string; // "SCHEDULED", "IN_TRANSIT", "DELIVERED", "FAILED"
}
Response: Delivery
```

#### Update Delivery
```typescript
PUT /api/deliveries/{id}
Body: {
  deliveryDate: string;
  deliveryAddress: string;
  status: string;
}
Response: Delivery
```

#### Delete Delivery
```typescript
DELETE /api/deliveries/{id}
Response: 204 No Content
```

---

## Data Models

### User Model
```typescript
interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: UserRole;
  createdAt: string;
  updatedAt: string;
}

enum UserRole {
  ADMIN = 'ADMIN',
  GESTIONNAIRE_APPROVISIONNEMENT = 'GESTIONNAIRE_APPROVISIONNEMENT',
  RESPONSABLE_ACHATS = 'RESPONSABLE_ACHATS',
  SUPERVISEUR_LOGISTIQUE = 'SUPERVISEUR_LOGISTIQUE',
  CHEF_PRODUCTION = 'CHEF_PRODUCTION',
  PLANIFICATEUR = 'PLANIFICATEUR',
  SUPERVISEUR_PRODUCTION = 'SUPERVISEUR_PRODUCTION',
  GESTIONNAIRE_COMMERCIAL = 'GESTIONNAIRE_COMMERCIAL',
  RESPONSABLE_LOGISTIQUE = 'RESPONSABLE_LOGISTIQUE',
  SUPERVISEUR_LIVRAISONS = 'SUPERVISEUR_LIVRAISONS'
}
```

### RawMaterial Model
```typescript
interface RawMaterial {
  id: number;
  name: string;
  quantity: number;
  unit: string;
  minThreshold: number;
  createdAt: string;
  updatedAt: string;
}
```

### Supplier Model
```typescript
interface Supplier {
  id: number;
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
  createdAt: string;
  updatedAt: string;
}
```

### Product Model
```typescript
interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  category: string;
  createdAt: string;
  updatedAt: string;
}
```

### Customer Model
```typescript
interface Customer {
  id: number;
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
  createdAt: string;
  updatedAt: string;
}
```

---

## Angular Services Examples

### 1. Raw Materials Service

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface RawMaterial {
  id: number;
  name: string;
  quantity: number;
  unit: string;
  minThreshold: number;
}

@Injectable({
  providedIn: 'root'
})
export class RawMaterialService {
  private apiUrl = `${environment.apiUrl}/rawMaterials`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<RawMaterial[]> {
    return this.http.get<RawMaterial[]>(this.apiUrl);
  }

  getById(id: number): Observable<RawMaterial> {
    return this.http.get<RawMaterial>(`${this.apiUrl}/${id}`);
  }

  getLowStock(): Observable<RawMaterial[]> {
    return this.http.get<RawMaterial[]>(`${this.apiUrl}/filter/low-stock`);
  }

  create(material: Partial<RawMaterial>): Observable<RawMaterial> {
    return this.http.post<RawMaterial>(this.apiUrl, material);
  }

  update(id: number, material: Partial<RawMaterial>): Observable<RawMaterial> {
    return this.http.put<RawMaterial>(`${this.apiUrl}/${id}`, material);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

### 2. Suppliers Service

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface Supplier {
  id: number;
  name: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
}

@Injectable({
  providedIn: 'root'
})
export class SupplierService {
  private apiUrl = `${environment.apiUrl}/suppliers`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(this.apiUrl);
  }

  getById(id: number): Observable<Supplier> {
    return this.http.get<Supplier>(`${this.apiUrl}/${id}`);
  }

  create(supplier: Partial<Supplier>): Observable<Supplier> {
    return this.http.post<Supplier>(this.apiUrl, supplier);
  }

  update(id: number, supplier: Partial<Supplier>): Observable<Supplier> {
    return this.http.put<Supplier>(`${this.apiUrl}/${id}`, supplier);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

### 3. Products Service

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  category: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  create(product: Partial<Product>): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  update(id: number, product: Partial<Product>): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

### 4. Users Service

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  create(user: Partial<User> & { password: string }): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  update(id: number, user: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, user);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

---

## Common Patterns

### Component Usage Example

```typescript
import { Component, OnInit } from '@angular/core';
import { RawMaterialService, RawMaterial } from '../services/raw-material.service';

@Component({
  selector: 'app-raw-materials',
  templateUrl: './raw-materials.component.html'
})
export class RawMaterialsComponent implements OnInit {
  materials: RawMaterial[] = [];
  lowStockMaterials: RawMaterial[] = [];
  loading = false;
  error: string | null = null;

  constructor(private materialService: RawMaterialService) {}

  ngOnInit() {
    this.loadMaterials();
    this.loadLowStockMaterials();
  }

  loadMaterials() {
    this.loading = true;
    this.materialService.getAll().subscribe({
      next: (data) => {
        this.materials = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load materials';
        this.loading = false;
        console.error(err);
      }
    });
  }

  loadLowStockMaterials() {
    this.materialService.getLowStock().subscribe({
      next: (data) => {
        this.lowStockMaterials = data;
      },
      error: (err) => {
        console.error('Failed to load low stock materials', err);
      }
    });
  }

  createMaterial(material: Partial<RawMaterial>) {
    this.materialService.create(material).subscribe({
      next: (created) => {
        this.materials.push(created);
        alert('Material created successfully!');
      },
      error: (err) => {
        alert('Failed to create material');
        console.error(err);
      }
    });
  }

  updateMaterial(id: number, material: Partial<RawMaterial>) {
    this.materialService.update(id, material).subscribe({
      next: (updated) => {
        const index = this.materials.findIndex(m => m.id === id);
        if (index !== -1) {
          this.materials[index] = updated;
        }
        alert('Material updated successfully!');
      },
      error: (err) => {
        alert('Failed to update material');
        console.error(err);
      }
    });
  }

  deleteMaterial(id: number) {
    if (confirm('Are you sure you want to delete this material?')) {
      this.materialService.delete(id).subscribe({
        next: () => {
          this.materials = this.materials.filter(m => m.id !== id);
          alert('Material deleted successfully!');
        },
        error: (err) => {
          alert('Failed to delete material');
          console.error(err);
        }
      });
    }
  }
}
```

### Error Handling Interceptor (Optional)

```typescript
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An error occurred';
        
        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // Server-side error
          errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
        }
        
        console.error(errorMessage);
        return throwError(() => error);
      })
    );
  }
}
```

---

## Testing with Postman

### Import Collection
Import `SupplyChainX-Postman-Collection.json` into Postman for ready-to-use API calls.

### Sample Requests

#### Create Raw Material
```bash
POST http://localhost:8080/api/rawMaterials
Content-Type: application/json

{
  "name": "Steel Sheets",
  "quantity": 500,
  "unit": "kg",
  "minThreshold": 100
}
```

#### Get Low Stock Items
```bash
GET http://localhost:8080/api/rawMaterials/filter/low-stock
```

#### Create Supplier
```bash
POST http://localhost:8080/api/suppliers
Content-Type: application/json

{
  "name": "ABC Supplies Ltd",
  "contactPerson": "John Doe",
  "email": "john@abcsupplies.com",
  "phone": "+1234567890",
  "address": "123 Business St, City"
}
```

---

## Important Notes

### Current Development Mode
- 🔓 Authentication is **DISABLED**
- ✅ All endpoints are publicly accessible
- ✅ No JWT tokens required
- ✅ CORS enabled for localhost:4200

### Before Production
- 🔐 Re-enable JWT authentication (see `DISABLE_JWT_GUIDE.md`)
- 🔑 Update `jwt.secret` in `application.properties`
- 🌐 Update CORS origins to production domains
- ✅ Test all authentication flows

---

## Troubleshooting

### CORS Issues
If you get CORS errors, check:
1. Angular app is running on `http://localhost:4200`
2. Backend `application.properties` has correct CORS configuration
3. Browser console for specific CORS error messages

### Connection Refused
If you can't connect to the backend:
1. Verify Spring Boot is running on port 8080
2. Check `server.port` in `application.properties`
3. Verify no firewall blocking the port

### 404 Not Found
- Verify the endpoint path is correct
- Check that the controller is registered
- Ensure Spring Boot application started successfully

---

**Last Updated:** January 7, 2026  
**API Version:** 1.0  
**Backend:** Spring Boot 3.x  
**Frontend:** Angular 17+

