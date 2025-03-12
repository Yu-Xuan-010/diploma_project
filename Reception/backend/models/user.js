const db = require('../config/db');

class User {
    static async create({ username, password, email }) {
        const [result] = await db.query(
            'INSERT INTO user (username, password, email) VALUES (?, ?, ?)',
            [username, password, email]
        );
        return result.insertId;
    }

    static async findByUsername(username) {
        const [rows] = await db.query('SELECT * FROM user WHERE username = ?', [username]);
        return rows[0];
    }

    static async findByEmail(email) {
        const [rows] = await db.query('SELECT * FROM user WHERE email = ?', [email]);
        return rows[0];
    }

    static async updatePassword(email, newPassword) {
        await db.query('UPDATE user SET password = ? WHERE email = ?', [newPassword, email]);
    }
}

module.exports = User;