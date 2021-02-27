-- 登录 MySQL 服务器
mysql -hlocalhost -uroot -proot

-- 创建数据库 cloud_coupon_data
CREATE DATABASE IF NOT EXISTS cloud_coupon_data;

-- 登录 MySQL 服务器, 并进入到 cloud_coupon_data 数据库中
mysql -hlocalhost -uroot -proot -Dcloud_coupon_data
