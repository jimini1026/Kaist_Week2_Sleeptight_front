const express = require("express");
const router = express.Router();
const db = require("../database");

router.get("/", async (req, res) => {
  const [rows] = await db.query("SELECT * FROM userlogin");
  res.json(rows);
});

router.get("/sleepInfo/:id", async (req, res) => {
  const userId = req.params.id;

  const [rows] = await db.query(
    "SELECT * FROM userSleepInfo WHERE user_id = ?",
    [userId]
  );
  res.json(rows);
});

module.exports = router;
