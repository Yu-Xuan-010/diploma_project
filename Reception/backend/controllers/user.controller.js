const db = require("../models");
const User = db.user;
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");

// 用户注册
exports.register = async (req, res) => {
  try {
    // 检查用户名是否已存在
    const existingUser = await User.findOne({
      where: {
        username: req.body.username
      }
    });

    if (existingUser) {
      return res.status(400).json({
        code: 400,
        message: "用户名已存在"
      });
    }

    // 检查邮箱是否已存在
    const existingEmail = await User.findOne({
      where: {
        email: req.body.email
      }
    });

    if (existingEmail) {
      return res.status(400).json({
        code: 400,
        message: "邮箱已被注册"
      });
    }

    // 创建新用户
    const hashedPassword = await bcrypt.hash(req.body.password, 10);
    const user = await User.create({
      username: req.body.username,
      email: req.body.email,
      password: hashedPassword
    });

    res.status(200).json({
      code: 200,
      message: "注册成功",
      data: {
        id: user.id,
        username: user.username,
        email: user.email
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({
      code: 500,
      message: "注册失败，请稍后重试"
    });
  }
};

// 用户登录
exports.login = async (req, res) => {
    console.log('Login attempt:', req.body);
    try {
      const user = await User.findOne({
        where: {
          username: req.body.username
        }
      });
  
      if (!user) {
        console.log('User not found:', req.body.username);
        return res.status(404).json({
          code: 404,
          message: "用户不存在"
        });
      }
  
      const validPassword = await bcrypt.compare(req.body.password, user.password);
      if (!validPassword) {
        console.log('Invalid password for user:', req.body.username);
        return res.status(401).json({
          code: 401,
          message: "密码错误"
        });
      }

    // 更新最后登录时间
    await user.update({
        lastLoginTime: new Date()
      });

    // 生成 JWT token
    const token = jwt.sign(
        { id: user.id, username: user.username },
        "your-jwt-secret",
        { expiresIn: "24h" }
      );
  
      console.log('Login successful for user:', user.username);
      res.status(200).json({
        code: 200,
        message: "登录成功",
        data: {
          token,
          user: {
            id: user.id,
            username: user.username,
            email: user.email,
            avatar: user.avatar
          }
        }
      });
    } catch (error) {
      console.error('Login error:', error);
      res.status(500).json({
        code: 500,
        message: "登录失败，请稍后重试"
      });
    }
  };

// 获取用户信息
exports.getUserInfo = async (req, res) => {
  try {
    const user = await User.findByPk(req.userId, {
      attributes: { exclude: ['password'] }
    });

    if (!user) {
      return res.status(404).json({
        code: 404,
        message: "用户不存在"
      });
    }

    res.status(200).json({
      code: 200,
      data: user
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({
      code: 500,
      message: "获取用户信息失败"
    });
  }
}; 