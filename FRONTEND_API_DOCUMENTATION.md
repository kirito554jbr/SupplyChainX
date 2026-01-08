# SupplyChainX Backend API Configuration for Frontend

## Base Configuration

### API Base URL
```
Development: http://localhost:8080
Production: [YOUR_PRODUCTION_URL]
```

### CORS Configuration
The backend accepts requests from:
- **Allowed Origin:** `http://localhost:4200` (Angular default)
- **Allowed Methods:** GET, POST, PUT, DELETE, OPTIONS
- **Credentials:** Allowed

---

## Authentication & Authorization

### Authentication Flow

#### 1. User Registration (Public - No Auth Required)
```http
POST /api/users/register
Content-Type: application/json

Request Body:
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "password": "string",
  "role": "ADMIN" | "GESTIONNAIRE_APPROVISIONNEMENT" | "RESPONSABLE_ACHATS" | 
         "SUPERVISEUR_LOGISTIQUE" | "CHEF_PRODUCTION" | "PLANIFICATEUR" | 
         "SUPERVISEUR_PRODUCTION" | "GESTIONNAIRE_COMMERCIAL" | 
         "RESPONSABLE_LOGISTIQUE" | "SUPERVISEUR_LIVRAISONS"
}

Response (201 Created):
{
  "idUser": number,
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "role": "string",
  "enabled": boolean
}
```

#### 2. User Login
```http
POST /api/auth/login
Content-Type: application/json

Request Body:
{
  "email": "string",
  "password": "string"
}

Response (200 OK):
{
  "accessToken": "string (JWT)",
  "refreshToken": "string (JWT)",
  "email": "string",
  "role": "string"
}
```

**Token Details:**
- **Access Token Expiration:** 15 minutes (900000 ms)
- **Refresh Token Expiration:** 7 days (604800000 ms)

#### 3. Token Refresh
```http
POST /api/auth/refresh
Content-Type: application/json

Request Body:
{
  "refreshToken": "string"
}

Response (200 OK):
{
  "accessToken": "string (new JWT)",
  "refreshToken": "string (new JWT)"
}
```

#### 4. Logout
```http
POST /api/auth/logout
Authorization: Bearer {accessToken}

Response (200 OK):
"User logged out successfully"
```

#### 5. Token Validation
```http
GET /api/auth/validate
Authorization: Bearer {accessToken}

Response (200 OK):
{
  "valid": boolean,
  "email": "string",
  "role": "string"
}
```

---

## API Endpoints Reference

### 🔐 Authentication Required
For all protected endpoints, include the Authorization header:
```
Authorization: Bearer {accessToken}
```

---

## 👥 User Management

### Get All Users
```http
GET /api/users
Authorization: Bearer {token}
Required Roles: ADMIN, GESTIONNAIRE_APPROVISIONNEMENT, RESPONSABLE_ACHATS, 
                SUPERVISEUR_LOGISTIQUE, CHEF_PRODUCTION, PLANIFICATEUR, 
                SUPERVISEUR_PRODUCTION, GESTIONNAIRE_COMMERCIAL, 
                RESPONSABLE_LOGISTIQUE, SUPERVISEUR_LIVRAISONS

Response: User[]
```

### Get User by ID
```http
GET /api/users/{id}
Authorization: Bearer {token}
Required Roles: Multiple roles (see above)

Response: User
```

### Get User by Email
```http
GET /api/users/email?email={email}
Authorization: Bearer {token}
Required Roles: Multiple roles

Response: User
```

### Create User (Admin Only)
```http
POST /api/users
Authorization: Bearer {token}
Required Role: ADMIN

Request Body: Same as registration
Response (201): User
```

### Update User
```http
PUT /api/users/{id}
Authorization: Bearer {token}
Required Roles: Multiple roles

Request Body: User data
Response: User
```

### Delete User
```http
DELETE /api/users/{id}
Authorization: Bearer {token}
Required Role: ADMIN

Response (204): No Content
```

---

## 📦 Raw Materials

### Get All Raw Materials
```http
GET /api/rawMaterials
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, PLANIFICATEUR, GESTIONNAIRE_APPROVISIONNEMENT

Response: RawMaterial[]
```

### Get Raw Material by ID
```http
GET /api/rawMaterials/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, PLANIFICATEUR, GESTIONNAIRE_APPROVISIONNEMENT

Response: RawMaterial
```

### Get Low Stock Raw Materials
```http
GET /api/rawMaterials/filter/low-stock
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE

Response: RawMaterial[]
```

### Create Raw Material
```http
POST /api/rawMaterials
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, PLANIFICATEUR, GESTIONNAIRE_APPROVISIONNEMENT

Request Body: RawMaterial data
Response (201): RawMaterial
```

### Update Raw Material
```http
PUT /api/rawMaterials/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, PLANIFICATEUR, GESTIONNAIRE_APPROVISIONNEMENT

Request Body: RawMaterial data
Response: RawMaterial
```

### Delete Raw Material
```http
DELETE /api/rawMaterials/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, PLANIFICATEUR, GESTIONNAIRE_APPROVISIONNEMENT

Response (204): No Content
```

### Link Raw Material to Supplier
```http
POST /api/rawMaterials/{materialId}/suppliers/{supplierId}
Authorization: Bearer {token}
Required Roles: ADMIN, GESTIONNAIRE_APPROVISIONNEMENT

Response: Success message
```

---

## 🏭 Suppliers

### Get All Suppliers
```http
GET /api/suppliers
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, GESTIONNAIRE_APPROVISIONNEMENT

Response: Supplier[]
```

### Get Supplier by ID
```http
GET /api/suppliers/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, GESTIONNAIRE_APPROVISIONNEMENT

Response: Supplier
```

### Create Supplier
```http
POST /api/suppliers
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, GESTIONNAIRE_APPROVISIONNEMENT

Request Body: Supplier data
Response (201): Supplier
```

### Update Supplier
```http
PUT /api/suppliers/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, GESTIONNAIRE_APPROVISIONNEMENT

Request Body: Supplier data
Response: Supplier
```

### Delete Supplier
```http
DELETE /api/suppliers/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, GESTIONNAIRE_APPROVISIONNEMENT

Response (204): No Content
```

---

## 📋 Supply Orders

### Get All Supply Orders
```http
GET /api/supply-orders
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, RESPONSABLE_ACHATS

Response: SupplyOrder[]
```

### Get Supply Order by ID
```http
GET /api/supply-orders/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, RESPONSABLE_ACHATS

Response: SupplyOrder
```

### Create Supply Order
```http
POST /api/supply-orders
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, RESPONSABLE_ACHATS

Request Body: SupplyOrder data
Response (201): SupplyOrder
```

### Update Supply Order
```http
PUT /api/supply-orders/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, RESPONSABLE_ACHATS

Request Body: SupplyOrder data
Response: SupplyOrder
```

### Delete Supply Order
```http
DELETE /api/supply-orders/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LOGISTIQUE, RESPONSABLE_ACHATS

Response (204): No Content
```

---

## 👤 Customers

### Get All Customers
```http
GET /api/customers
Authorization: Bearer {token}
Required Roles: ADMIN, GESTIONNAIRE_COMMERCIAL

Response: Customer[]
```

### Get Customer by ID
```http
GET /api/customers/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, GESTIONNAIRE_COMMERCIAL

Response: Customer
```

### Get Customer by Name
```http
GET /api/customers/by-name/{name}
Authorization: Bearer {token}
Required Roles: ADMIN, GESTIONNAIRE_COMMERCIAL

Response: Customer
```

### Create Customer
```http
POST /api/customers
Authorization: Bearer {token}
Required Roles: ADMIN, GESTIONNAIRE_COMMERCIAL

Request Body: Customer data
Response (201): Customer
```

### Update Customer
```http
PUT /api/customers/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, GESTIONNAIRE_COMMERCIAL

Request Body: Customer data
Response: Customer
```

### Delete Customer
```http
DELETE /api/customers/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, GESTIONNAIRE_COMMERCIAL

Response (204): No Content
```

---

## 🚚 Deliveries

### Get All Deliveries
```http
GET /api/deliveries
Authorization: Bearer {token}
Required Roles: ADMIN, RESPONSABLE_LOGISTIQUE, SUPERVISEUR_LIVRAISONS

Response: Delivery[]
```

### Get Delivery by ID
```http
GET /api/deliveries/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, RESPONSABLE_LOGISTIQUE, SUPERVISEUR_LIVRAISONS

Response: Delivery
```

### Create Delivery
```http
POST /api/deliveries
Authorization: Bearer {token}
Required Roles: ADMIN, RESPONSABLE_LOGISTIQUE, SUPERVISEUR_LIVRAISONS

Request Body: Delivery data
Response (201): Delivery
```

### Update Delivery
```http
PUT /api/deliveries/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, RESPONSABLE_LOGISTIQUE, SUPERVISEUR_LIVRAISONS

Request Body: Delivery data
Response: Delivery
```

### Delete Delivery
```http
DELETE /api/deliveries/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, RESPONSABLE_LOGISTIQUE, SUPERVISEUR_LIVRAISONS

Response (204): No Content
```

---

## 📦 Orders

### Get All Orders
```http
GET /api/orders
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LIVRAISONS, GESTIONNAIRE_COMMERCIAL

Response: Order[]
```

### Get Order by ID
```http
GET /api/orders/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LIVRAISONS, GESTIONNAIRE_COMMERCIAL

Response: Order
```

### Create Order
```http
POST /api/orders
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LIVRAISONS, GESTIONNAIRE_COMMERCIAL

Request Body: Order data
Response (201): Order
```

### Update Order
```http
PUT /api/orders/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LIVRAISONS, GESTIONNAIRE_COMMERCIAL

Request Body: Order data
Response: Order
```

### Delete Order
```http
DELETE /api/orders/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_LIVRAISONS, GESTIONNAIRE_COMMERCIAL

Response (204): No Content
```

---

## 🏭 Products

### Get All Products
```http
GET /api/products
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION, PLANIFICATEUR

Response: Product[]
```

### Get Product by ID
```http
GET /api/products/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION, PLANIFICATEUR

Response: Product
```

### Create Product
```http
POST /api/products
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION, PLANIFICATEUR

Request Body: Product data
Response (201): Product
```

### Update Product
```http
PUT /api/products/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION, PLANIFICATEUR

Request Body: Product data
Response: Product
```

### Delete Product
```http
DELETE /api/products/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION, PLANIFICATEUR

Response (204): No Content
```

---

## 📋 Bill of Materials (BOM)

### Get All BOMs
```http
GET /api/boms
Authorization: Bearer {token}
Required Roles: ADMIN, CHEF_PRODUCTION, SUPERVISEUR_PRODUCTION, PLANIFICATEUR

Response: BOM[]
```

### Get BOM by ID
```http
GET /api/boms/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, CHEF_PRODUCTION, SUPERVISEUR_PRODUCTION, PLANIFICATEUR

Response: BOM
```

### Create BOM
```http
POST /api/boms
Authorization: Bearer {token}
Required Roles: ADMIN, CHEF_PRODUCTION, SUPERVISEUR_PRODUCTION, PLANIFICATEUR

Request Body: BOM data
Response (201): BOM
```

### Update BOM
```http
PUT /api/boms/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, CHEF_PRODUCTION, SUPERVISEUR_PRODUCTION, PLANIFICATEUR

Request Body: BOM data
Response: BOM
```

### Delete BOM
```http
DELETE /api/boms/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, CHEF_PRODUCTION, SUPERVISEUR_PRODUCTION, PLANIFICATEUR

Response (204): No Content
```

---

## 🏭 Production Orders

### Get All Production Orders
```http
GET /api/productionOrders
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION

Response: ProductionOrder[]
```

### Get Production Order by ID
```http
GET /api/productionOrders/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION

Response: ProductionOrder
```

### Create Production Order
```http
POST /api/productionOrders
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION

Request Body: ProductionOrder data
Response (201): ProductionOrder
```

### Update Production Order
```http
PUT /api/productionOrders/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION

Request Body: ProductionOrder data
Response: ProductionOrder
```

### Delete Production Order
```http
DELETE /api/productionOrders/{id}
Authorization: Bearer {token}
Required Roles: ADMIN, SUPERVISEUR_PRODUCTION, CHEF_PRODUCTION

Response (204): No Content
```

---

## 🔑 User Roles Reference

| Role | Code | Access Level |
|------|------|--------------|
| Administrator | `ADMIN` | Full access to all endpoints |
| Supply Manager | `GESTIONNAIRE_APPROVISIONNEMENT` | Raw materials, suppliers, users |
| Purchasing Manager | `RESPONSABLE_ACHATS` | Supply orders, users |
| Logistics Supervisor | `SUPERVISEUR_LOGISTIQUE` | Raw materials, suppliers, supply orders, users |
| Production Manager | `CHEF_PRODUCTION` | Products, BOMs, production orders, users |
| Planner | `PLANIFICATEUR` | Raw materials, products, BOMs, users |
| Production Supervisor | `SUPERVISEUR_PRODUCTION` | Products, BOMs, production orders, users |
| Sales Manager | `GESTIONNAIRE_COMMERCIAL` | Customers, orders, users |
| Logistics Manager | `RESPONSABLE_LOGISTIQUE` | Deliveries, users |
| Delivery Supervisor | `SUPERVISEUR_LIVRAISONS` | Deliveries, orders, users |

---

## 🛠️ Frontend Implementation Guide

### 1. HTTP Interceptor (Angular Example)

```typescript
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, filter, take, switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Add token to request if available
    const token = this.authService.getAccessToken();
    if (token) {
      request = this.addToken(request, token);
    }

    return next.handle(request).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return this.handle401Error(request, next);
        }
        return throwError(error);
      })
    );
  }

  private addToken(request: HttpRequest<any>, token: string) {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      const refreshToken = this.authService.getRefreshToken();
      if (refreshToken) {
        return this.authService.refreshToken(refreshToken).pipe(
          switchMap((tokens: any) => {
            this.isRefreshing = false;
            this.refreshTokenSubject.next(tokens.accessToken);
            return next.handle(this.addToken(request, tokens.accessToken));
          }),
          catchError((err) => {
            this.isRefreshing = false;
            this.authService.logout();
            return throwError(err);
          })
        );
      }
    }

    return this.refreshTokenSubject.pipe(
      filter(token => token != null),
      take(1),
      switchMap(token => next.handle(this.addToken(request, token)))
    );
  }
}
```

### 2. Auth Service (Angular Example)

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api';
  private currentUserSubject: BehaviorSubject<any>;
  public currentUser: Observable<any>;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.currentUserSubject = new BehaviorSubject<any>(
      JSON.parse(localStorage.getItem('currentUser') || 'null')
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue() {
    return this.currentUserSubject.value;
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.API_URL}/users/register`, userData);
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.API_URL}/auth/login`, { email, password })
      .pipe(
        tap((response: any) => {
          if (response.accessToken) {
            const user = {
              email: response.email,
              role: response.role,
              accessToken: response.accessToken,
              refreshToken: response.refreshToken
            };
            localStorage.setItem('currentUser', JSON.stringify(user));
            this.currentUserSubject.next(user);
          }
        })
      );
  }

  logout(): Observable<any> {
    return this.http.post(`${this.API_URL}/auth/logout`, {})
      .pipe(
        tap(() => {
          localStorage.removeItem('currentUser');
          this.currentUserSubject.next(null);
          this.router.navigate(['/login']);
        })
      );
  }

  refreshToken(refreshToken: string): Observable<any> {
    return this.http.post(`${this.API_URL}/auth/refresh`, { refreshToken })
      .pipe(
        tap((response: any) => {
          if (response.accessToken) {
            const user = this.currentUserValue;
            user.accessToken = response.accessToken;
            user.refreshToken = response.refreshToken;
            localStorage.setItem('currentUser', JSON.stringify(user));
            this.currentUserSubject.next(user);
          }
        })
      );
  }

  getAccessToken(): string | null {
    const user = this.currentUserValue;
    return user ? user.accessToken : null;
  }

  getRefreshToken(): string | null {
    const user = this.currentUserValue;
    return user ? user.refreshToken : null;
  }

  validateToken(): Observable<any> {
    return this.http.get(`${this.API_URL}/auth/validate`);
  }

  hasRole(role: string): boolean {
    const user = this.currentUserValue;
    return user && user.role === role;
  }

  hasAnyRole(roles: string[]): boolean {
    const user = this.currentUserValue;
    return user && roles.includes(user.role);
  }
}
```

### 3. Route Guard (Angular Example)

```typescript
import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const currentUser = this.authService.currentUserValue;
    
    if (currentUser) {
      // Check if route requires specific roles
      if (route.data['roles'] && !this.authService.hasAnyRole(route.data['roles'])) {
        this.router.navigate(['/unauthorized']);
        return false;
      }
      return true;
    }

    // Not logged in, redirect to login with return url
    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
}
```

### 4. Environment Configuration

```typescript
// environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  tokenExpirationTime: 900000, // 15 minutes
  refreshTokenExpirationTime: 604800000 // 7 days
};

// environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://your-production-url.com/api',
  tokenExpirationTime: 900000,
  refreshTokenExpirationTime: 604800000
};
```

---

## 📝 Error Handling

### Common HTTP Status Codes

| Status Code | Meaning | Action |
|-------------|---------|--------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 204 | No Content | Deletion successful |
| 400 | Bad Request | Invalid request data |
| 401 | Unauthorized | Token missing, invalid, or expired |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Duplicate resource (e.g., email already exists) |
| 500 | Internal Server Error | Server error |

### Error Response Format

```json
{
  "timestamp": "2026-01-06T13:24:59.897194292",
  "status": 401,
  "error": "Unauthorized",
  "message": "User not authenticated",
  "path": "/api/users"
}
```

---

## 🧪 Testing with Default Users

The backend creates 4 default test users on startup:

| Email | Password | Role |
|-------|----------|------|
| admin@test.com | 0000 | ADMIN |
| gestionnaire@test.com | 0000 | GESTIONNAIRE_APPROVISIONNEMENT |
| production@test.com | 0000 | CHEF_PRODUCTION |
| logistique@test.com | 0000 | RESPONSABLE_LOGISTIQUE |

---

## 📚 Additional Resources

### Swagger UI Documentation
When the backend is running, access interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI JSON
```
http://localhost:8080/v3/api-docs
```

---

## 🔒 Security Best Practices

1. **Store tokens securely**
   - Use HttpOnly cookies for production (if possible)
   - Or use localStorage with XSS protection
   - Never expose tokens in URL parameters

2. **Implement token refresh logic**
   - Automatically refresh tokens before expiration
   - Handle 401 errors by attempting token refresh
   - Logout user if refresh fails

3. **Validate roles on frontend**
   - Hide/disable UI elements based on user roles
   - Always validate on backend (frontend validation is for UX only)

4. **Handle CORS properly**
   - Backend is configured for `http://localhost:4200`
   - Update for production domain

5. **Implement request timeout**
   - Set reasonable timeout for HTTP requests
   - Show loading indicators
   - Handle network errors gracefully

---

## 📞 Support & Contact

For backend issues or questions:
- Check application logs: `docker-compose logs app`
- Verify database: `docker exec supplychainx-db mysql -uroot -proot -e "USE supplyChainX; SELECT * FROM users;"`
- Review documentation files in project root

---

**Last Updated:** January 6, 2026
**Backend Version:** 0.0.1-SNAPSHOT
**API Version:** v1

