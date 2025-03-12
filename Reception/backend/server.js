const express = require('express');
const app = express();
const authRoutes = require('./routes/auth');
const cors = require('cors');


// 中间件
app.use(express.json());

// 路由
app.use('/api/auth', authRoutes);
app.use(cors());
// 启动服务器
const PORT = 5000;
app.listen(PORT, '0.0.0.0', () => {
    console.log(`服务器运行在 http://192.168.151.141:${PORT}`);
});