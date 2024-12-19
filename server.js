const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const port = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// MySQL
const db = mysql.createConnection({
    host: 'localhost',       // MySQL Server address
    user: 'root',            // MySQL account
    password: '2304',        // MySQL password
    database: 'restaurant'   // database name
});

// Connect MySQL
db.connect((err) => {
    if (err) {
        console.error('MySQL connected unsuccessfully:', err);
    } else {
        console.log('MySQL connected successfully!');
    }
});

// Modify /list route to fetch all data without query parameters
app.get("/list", (req, res) => {
    const query = `SELECT * FROM list`;  // Query to select all rows from the list table (all columns)

    db.query(query, (err, results) => {
        if (err) {
            console.error('Error executing query', err);
            res.status(500).send('Server error');
            return;
        }
        res.json(results);  // Return all results as JSON
    });
});

// Start server
app.listen(port, '0.0.0.0', () => {
    console.log(`Server is running at http://localhost:${port}`);
});
