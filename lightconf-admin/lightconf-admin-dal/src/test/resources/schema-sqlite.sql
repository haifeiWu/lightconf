-- SQLite 版测试 schema（由 doc/db/light-conf-0.1.1V.sql 转换，仅 DDL，无数据）
-- 转换规则：去反引号 / AUTO_INCREMENT -> INTEGER PRIMARY KEY AUTOINCREMENT / 去 ENGINE、COMMENT / UNIQUE KEY -> UNIQUE

CREATE TABLE light_conf_app (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  uuid TEXT,
  app_name TEXT,
  app_desc TEXT,
  private_key TEXT,
  public_key TEXT,
  is_connected INTEGER DEFAULT 0,
  is_change INTEGER DEFAULT 0,
  is_push_conf INTEGER DEFAULT 0
);

CREATE TABLE light_conf_app_conf (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  app_id TEXT,
  conf_id TEXT
);

CREATE TABLE light_conf_conf (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  app_id INTEGER,
  conf_key TEXT,
  conf_value TEXT,
  conf_desc TEXT,
  UNIQUE (app_id, conf_key)
);

CREATE TABLE light_conf_user (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_name TEXT NOT NULL,
  password TEXT NOT NULL,
  permission INTEGER NOT NULL DEFAULT 0,
  permission_projects TEXT,
  UNIQUE (user_name)
);

CREATE TABLE light_conf_log (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  conf_key TEXT NOT NULL,
  conf_desc TEXT NOT NULL,
  conf_value TEXT,
  opt_time TEXT NOT NULL,
  opt_user TEXT NOT NULL
);

CREATE TABLE light_conf_conf_log (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  log_id INTEGER,
  conf_id INTEGER
);
