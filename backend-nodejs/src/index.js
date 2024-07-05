const express = require("express");
const app = express();
const PORT = process.env.PORT || 3000;

require("dotenv").config();
const db = require("./database");

app.use(express.json());

app.get("/", (req, res) => {
  res.send("Hello World");
});

const userRoutes = require("./routes/users");
app.use("/users", userRoutes);

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
