const User = require('../models/User');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

// 注册
exports.register = async (req, res) => {
    try {
        const { username, password, email } = req.body;

        // 检查用户名是否已存在
        const existingUser = await User.findByUsername(username);
        if (existingUser) {
            return res.status(400).json({ message: '用户名已存在' });
        }

        // 密码加密
        const hashedPassword = await bcrypt.hash(password, 10);

        // 创建用户
        await User.create({ username, password: hashedPassword, email });
        res.status(201).json({ message: '用户注册成功' });
    } catch (err) {
        res.status(400).json({ message: err.message });
    }
};

// 登录
exports.login = async (req, res) => {
    try {
        const { username, password } = req.body;

        // 查找用户
        const user = await User.findByUsername(username);
        if (!user) {
            return res.status(400).json({ message: '用户名或密码错误' });
        }

        // 验证密码
        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) {
            return res.status(400).json({ message: '用户名或密码错误' });
        }

        // 生成 JWT
        const token = jwt.sign({ userId: user.id }, 'your-secret-key', { expiresIn: '1h' });
        res.status(200).json({ token });
    } catch (err) {
        res.status(400).json({ message: err.message });
    }
};

// 忘记密码
exports.forgotPassword = async (req, res) => {
    try {
        const { email, newPassword } = req.body;

        // 查找用户
        const user = await User.findByEmail(email);
        if (!user) {
            return res.status(400).json({ message: '邮箱不存在' });
        }

        // 更新密码
        const hashedPassword = await bcrypt.hash(newPassword, 10);
        await User.updatePassword(email, hashedPassword);
        res.status(200).json({ message: '密码重置成功' });
    } catch (err) {
        res.status(400).json({ message: err.message });
    }
};