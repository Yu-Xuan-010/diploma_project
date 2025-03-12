const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const authRoutes = require('./routes/auth');

const app = express();

// 中间件
app.use(cors());
app.use(bodyParser.json());

// 路由
app.use('/api/auth', authRoutes);
app.use('/api/auth', require('./routes/auth'));

module.exports = app;