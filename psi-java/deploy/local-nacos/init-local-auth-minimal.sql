USE `zo_psi`;
SET NAMES utf8mb4;

-- Minimal auth/bootstrap data for local startup.
-- The official init scripts create the schema, menus, and admin user,
-- but they do not seed role/func_perm rows required by oauth2/gateway.

INSERT INTO `role` (`id`, `name`, `data`, `root`, `auth`, `keyword`)
VALUES (
  '0',
  '超级管理员',
  'local bootstrap role',
  '[{"fun":[{"id":"ALL"}]}]',
  '{}',
  'SUPER_ADMIN'
)
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `data` = VALUES(`data`),
  `root` = VALUES(`root`),
  `auth` = VALUES(`auth`),
  `keyword` = VALUES(`keyword`);

INSERT INTO `func_perm` (`id`, `mid`, `name`, `url`, `sort`, `is_enable`, `remark`)
VALUES (
  'ALL',
  NULL,
  'All APIs',
  '/**',
  0,
  '1',
  'local bootstrap permission'
)
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `url` = VALUES(`url`),
  `sort` = VALUES(`sort`),
  `is_enable` = VALUES(`is_enable`),
  `remark` = VALUES(`remark`);
