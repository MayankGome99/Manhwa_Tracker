# 📚 Java MySQL Manhwa Tracker

A console-based Java application built with **JDBC** and **MySQL** that allows users to track, manage, rate, and update their favorite manhwa, manhua, and web novels. 

This project was built as a hands-on backend development exercise to practice CRUD operations (Create, Read, Update, Delete) and database connectivity.

---

## 🚀 Features

* **Add Manhwa:** Save new titles along with chapter counts (supports text symbols like `200+`) and ratings.
* **View List:** Display all saved manhwa neatly formatted with visual star ratings (⭐).
* **Update Chapters:** Easily update your reading progress for any title.
* **Delete Records:** View a numbered list of saved titles and delete entries instantly by typing their index number.
* **Database Persistence:** All data is safely stored and retrieved using a local MySQL database.

---

## 🛠️ Tech Stack

* **Language:** Java (JDK 25)
* **Database:** MySQL & MySQL Workbench
* **Library:** MySQL JDBC Driver (`mysql-connector-j`)
* **IDE:** IntelliJ IDEA

---

## ⚙️ How to Setup and Run

### 1. Database Setup
Open your MySQL Workbench and run the following commands to set up the database and table:

```sql
CREATE DATABASE manhwa_tracker;
USE manhwa_tracker;

CREATE TABLE my_list (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    chapter VARCHAR(50) NOT NULL,
    rating DECIMAL(3,1) NOT NULL
);
