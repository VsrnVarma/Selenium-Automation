# Test Data Files
This folder contains Excel files used for data-driven testing.

## 📁 Files Structure

## TestData.xlsx

This file contains test data for different test scenarios.


### 📊 Sheet: LoginData

**Purpose:** Test data for login scenarios

| Column Name | Data Type | Description | Example|  
|-------------|-----------|-------------|---------|  
| username | String | Login username | standard_user |  
| password | String | Login password | secret_sauce |  
| expectedResult | String | Expected outcome (success/failure) | success |

**Sample Data:**
```
| username              | password      | expectedResult |
|-----------------------|---------------|----------------|
| standard_user         | secret_sauce  | success        |
| locked_out_user       | secret_sauce  | failure        |
| problem_user          | secret_sauce  | success        |
| invalid_user          | wrong_pass    | failure        |
```

---

## 📊 Sheet: CheckoutData

**Purpose:** Test data for checkout form validation

| Column Name | Data Type | Description | Example |
|-------------|-----------|-------------|---------|
| firstName | String | Customer first name | John |
| lastName | String | Customer last name | Doe |
| postalCode | String | ZIP/Postal code | 12345 |

**Sample Data:**
```
| firstName | lastName | postalCode |
|-----------|----------|------------|
| John      | Doe      | 12345      |
| Jane      | Smith    | 67890      |
| Mike      | Johnson  | 54321      |
```

---

## 📊 Sheet: ProductData

**Purpose:** Product names for cart testing

| Column Name | Data Type | Description | Example |
|-------------|-----------|-------------|---------|
| productName | String | Product identifier (lowercase with dashes) | sauce-labs-backpack |
| displayName | String | Product name as shown on site | Sauce Labs Backpack |

**Sample Data:**
```
| productName              | displayName              |
|--------------------------|--------------------------|
| sauce-labs-backpack      | Sauce Labs Backpack      |
| sauce-labs-bike-light    | Sauce Labs Bike Light    |
| sauce-labs-bolt-t-shirt  | Sauce Labs Bolt T-Shirt  |
```