module.exports = {
    HOST: "localhost",
    USER: "root",
    PASSWORD: "329839",  // 请根据你的实际数据库密码修改
    DB: "diploma_project",
    dialect: "mysql",
    pool: {
      max: 5,
      min: 0,
      acquire: 30000,
      idle: 10000
    }
  };