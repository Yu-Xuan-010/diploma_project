const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");
const db = require("./models");

const app = express();

// 配置 CORS
app.use(cors({
  origin: true, // 允许所有来源访问
  credentials: true,
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization', 'X-Requested-With', 'Accept', 'Origin']
}));

// 添加自定义响应头中间件
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', req.headers.origin);
  res.header('Access-Control-Allow-Credentials', 'true');
  res.header('Access-Control-Allow-Methods', 'GET,HEAD,PUT,PATCH,POST,DELETE,OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization, X-Requested-With, Accept, Origin');
  
  // 处理 OPTIONS 请求
  if (req.method === 'OPTIONS') {
    return res.sendStatus(200);
  }
  next();
});

// 解析 JSON 请求体
app.use(bodyParser.json());

// 解析 URL 编码的请求体
app.use(bodyParser.urlencoded({ extended: true }));

// 同步数据库
db.sequelize.sync()
  .then(() => {
    console.log("数据库同步成功");
  })
  .catch((err) => {
    console.error("数据库同步失败:", err);
  });

// 注册路由
require("./routes/user.routes")(app);

// 简单的路由响应
app.get("/", (req, res) => {
  res.json({ message: "欢迎访问 CMS 接口服务" });
});

// 错误处理中间件
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({
    code: 500,
    message: "服务器内部错误"
  });
});

const PORT = 5000;
app.listen(PORT, '0.0.0.0', () => { // 监听所有网络接口
  console.log(`Server is running on port ${PORT}`);
});