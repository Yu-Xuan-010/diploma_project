const mysql = require('mysql2');

const pool = mysql.createPool({
    host: 'localhost',
    user: 'root', // 替换为你的 MySQL 用户名
    password: '329839', // 替换为你的 MySQL 密码
    database: 'diploma_project', // 替换为你的数据库名称
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0,
});

module.exports = pool.promise();