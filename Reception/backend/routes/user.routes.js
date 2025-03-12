module.exports = app => {
    const users = require("../controllers/user.controller.js");
    const router = require("express").Router();
  
    // 处理 OPTIONS 请求
    router.options('*', (req, res) => {
      res.sendStatus(200);
    });
  
    // 注册新用户
    router.post("/auth/register", users.register);
  
    // 用户登录
    router.post("/auth/login", users.login);
  
    // 获取用户信息
    router.get("/user/profile", users.getUserInfo);
  
    app.use('/api', router);
  };