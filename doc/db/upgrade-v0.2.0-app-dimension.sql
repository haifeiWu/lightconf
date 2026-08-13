/*
LIGHTCONF - v0.2.0 升级脚本：conf 增加 app 维度
=================================================
背景：v0.1.x 中 light_conf_conf.conf_key 全局唯一，不同应用无法拥有同名 key。
本脚本将维度升级为 (app_id, conf_key) 唯一，并对存量数据做回填与清理。

适用：已有 v0.1.x 数据库升级到 v0.2.0 时执行。
注意：执行前请先备份数据库。
*/

USE `light-conf`;

-- 1) conf 表增加 app_id 列（放在 id 之后）
ALTER TABLE `light_conf_conf` ADD COLUMN `app_id` int(11) DEFAULT NULL COMMENT '所属应用id' AFTER `id`;

-- 2) 从 app_conf 关联表回填 app_id（同一 conf_id 属于多个 app 时保留最小 app_id，其余由步骤3去重）
UPDATE `light_conf_conf` c
INNER JOIN (
    SELECT MIN(CAST(app_id AS UNSIGNED)) AS app_id, conf_id
    FROM `light_conf_app_conf`
    WHERE conf_id REGEXP '^[0-9]+$'
    GROUP BY conf_id
) ac ON ac.conf_id = c.id
SET c.app_id = ac.app_id;

-- 3) 清理无归属的配置（不在 app_conf 关联表中的历史数据，删除）
DELETE FROM `light_conf_conf` WHERE `app_id` IS NULL;

-- 4) 清理同一 app 下重复的 conf_key（保留 id 最小的一条，其余删除）
DELETE c FROM `light_conf_conf` c
LEFT JOIN (
    SELECT MIN(id) AS keep_id
    FROM `light_conf_conf`
    GROUP BY `app_id`, `conf_key`
) k ON k.keep_id = c.id
WHERE k.keep_id IS NULL;

-- 5) 增加 (app_id, conf_key) 唯一索引
ALTER TABLE `light_conf_conf` ADD UNIQUE KEY `uk_app_conf_key` (`app_id`, `conf_key`);

-- 6) 应用表补充缺失列（v0.1.1V.sql 建库脚本曾缺失这两列）
ALTER TABLE `light_conf_app` ADD COLUMN `is_change` tinyint(1) DEFAULT '0' COMMENT '配置是否更新过';
ALTER TABLE `light_conf_app` ADD COLUMN `is_push_conf` tinyint(1) DEFAULT '0' COMMENT '配置是否上传';

-- 7) 用户表唯一索引（v0.1.1V.sql 该 ALTER 缺失）
ALTER TABLE `light_conf_user` ADD UNIQUE KEY `uk_user_name` (`user_name`);
