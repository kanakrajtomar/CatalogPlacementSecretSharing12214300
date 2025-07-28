# Polynomial Secret Finder

This Java project solves the problem of determining the constant term (i.e., the hidden secret) in a polynomial function based on provided evaluation points. It's typically used in secret-sharing mechanisms like Shamir's Secret Sharing.

## 🔍 Problem Statement

Given a polynomial of degree `k-1` and `k` (x, y) points that lie on the polynomial curve, find the constant term `c`, which is the polynomial evaluated at `x = 0`. This constant is the secret.

## 📂 Project Structure

```
Polynomialsecretfinder/
├── PolynomialSecretFinder.java
├── input1.json
├── input2.json
├── README.md
```

## 🔧 Requirements

* Java JDK 17 or later

## 🚀 How to Run

1. **Compile the Program**

   ```bash
   javac PolynomialSecretFinder.java
   ```

2. **Run with a JSON Input File**

   ```bash
   java PolynomialSecretFinder input1.json
   ```

   You can replace `input1.json` with any valid JSON file following the required structure.

## 📘 Input Format

The JSON file should be structured as follows:

```json
{
  "k": 3,
  "points": [
    {"x": 1, "y": 6},
    {"x": 2, "y": 11},
    {"x": 3, "y": 18}
  ]
}
```

* `k`: Number of points
* `points`: An array of objects where each object contains `x` and `y` values of the point

## ✅ Sample Outputs

### 📌 Test Case 1:

Command:

```bash
java PolynomialSecretFinder input1.json
```

**Output:**

```
Secret (constant term c): 3
```

![Test Case 1 Output](https://ik.imagekit.io/eufh68vve/Testcase1.png?updatedAt=1753684591559)

### 📌 Test Case 2:

Command:

```bash
java PolynomialSecretFinder input2.json
```

**Output:**

```
Secret (constant term c): 7
```

![Test Case 2 Output](https://ik.imagekit.io/eufh68vve/testcase2.png?updatedAt=1753684595670)

## 📌 Features

* Reads JSON input using native Java file reading
* Parses data into polynomial points
* Uses matrix elimination to solve for coefficients
* Outputs the secret constant term

## 📁 Author

**Kanak Raj Tomar**

## 📃 License

This project is open-source and free to use for educational and research purposes.
