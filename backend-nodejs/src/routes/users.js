const express = require('express');
const router = express.Router();
const db = require('../database');

router.get('/', async (req, res) => {
  const [rows] = await db.query('SELECT * FROM users');
  res.json(rows);
});

module.exports = router;
