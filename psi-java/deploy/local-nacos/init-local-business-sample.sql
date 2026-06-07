USE `zo_psi`;
SET NAMES utf8mb4;

-- Local business sample data, adapted from the provided SQL files.
-- It intentionally avoids auth/menu records that are already seeded elsewhere.

INSERT INTO `frame` (`id`, `pid`, `name`, `sort`, `data`)
VALUES
  ('f001', NULL, '总部', 1, '集团总部'),
  ('f002', 'f001', '华东分部', 2, '负责华东地区业务')
ON DUPLICATE KEY UPDATE
  `pid` = VALUES(`pid`),
  `name` = VALUES(`name`),
  `sort` = VALUES(`sort`),
  `data` = VALUES(`data`);

INSERT INTO `account` (`id`, `name`, `number`, `frame`, `time`, `initial`, `balance`, `data`)
VALUES
  ('acc001', '招商银行基本户', '1234567890', 'f001', '2025-01-01', 1000000.0000, 1000000.0000, '公司主账户'),
  ('acc002', '支付宝', 'pay@example.com', 'f001', '2025-01-01', 50000.0000, 50000.0000, '电商收入')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `number` = VALUES(`number`),
  `frame` = VALUES(`frame`),
  `time` = VALUES(`time`),
  `initial` = VALUES(`initial`),
  `balance` = VALUES(`balance`),
  `data` = VALUES(`data`);

INSERT INTO `people` (`id`, `name`, `py`, `number`, `frame`, `sex`, `tel`, `add`, `card`, `data`, `more`)
VALUES
  ('p001', '王五', 'wangwu', 'YG001', 'f001', 1, '13500135000', '总部A栋', '110110199001011234', '采购经理', '{}'),
  ('p002', '赵六', 'zhaoliu', 'YG002', 'f002', 0, '13600136000', '华东B栋', '120120199202022345', '销售代表', '{}')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `py` = VALUES(`py`),
  `number` = VALUES(`number`),
  `frame` = VALUES(`frame`),
  `sex` = VALUES(`sex`),
  `tel` = VALUES(`tel`),
  `add` = VALUES(`add`),
  `card` = VALUES(`card`),
  `data` = VALUES(`data`),
  `more` = VALUES(`more`);

INSERT INTO `customer` (`id`, `name`, `py`, `number`, `frame`, `user`, `category`, `grade`, `bank`, `account`, `tax`, `data`, `contacts`, `balance`, `more`)
VALUES
  ('cust001', '客户甲', 'kehujia', 'KH001', 'f001', 'u002', '零售', 'A', '建设银行', '8888888888', 'TAX888', '零售客户', '[]', 0.0000, '{}'),
  ('cust002', '客户乙', 'kehuyi', 'KH002', 'f002', 'u002', '批发', 'B', '中国银行', '9999999999', 'TAX999', '批发客户', '[]', 0.0000, '{}')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `py` = VALUES(`py`),
  `number` = VALUES(`number`),
  `frame` = VALUES(`frame`),
  `user` = VALUES(`user`),
  `category` = VALUES(`category`),
  `grade` = VALUES(`grade`),
  `bank` = VALUES(`bank`),
  `account` = VALUES(`account`),
  `tax` = VALUES(`tax`),
  `data` = VALUES(`data`),
  `contacts` = VALUES(`contacts`),
  `balance` = VALUES(`balance`),
  `more` = VALUES(`more`);

INSERT INTO `warehouse` (`id`, `name`, `number`, `frame`, `contacts`, `tel`, `add`, `data`)
VALUES
  ('w001', '主仓库', 'CK001', 'f001', '仓管A', '13100131000', '总部A区', '主仓库'),
  ('w002', '华东仓', 'CK002', 'f002', '仓管B', '13200132000', '华东B区', '分仓')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `number` = VALUES(`number`),
  `frame` = VALUES(`frame`),
  `contacts` = VALUES(`contacts`),
  `tel` = VALUES(`tel`),
  `add` = VALUES(`add`),
  `data` = VALUES(`data`);

INSERT INTO `category` (`id`, `pid`, `name`, `sort`, `data`)
VALUES
  ('cat001', NULL, '电子产品', 1, '消费电子'),
  ('cat002', 'cat001', '手机', 2, '智能手机')
ON DUPLICATE KEY UPDATE
  `pid` = VALUES(`pid`),
  `name` = VALUES(`name`),
  `sort` = VALUES(`sort`),
  `data` = VALUES(`data`);

INSERT INTO `goods` (`id`, `name`, `py`, `number`, `spec`, `category`, `brand`, `unit`, `buy`, `sell`, `code`, `location`, `stock`, `type`, `data`, `imgs`, `details`, `units`, `strategy`, `serial`, `batch`, `validity`, `protect`, `threshold`, `more`)
VALUES
  ('g001', '智能手机X', 'zhinengshoujix', 'SP001', 'X-1000', 'cat002', '品牌A', '台', 1500.0000, 1999.0000, '1234567890123', 'A01', 20.0000, 0, '旗舰机型', '[]', '详情', '[]', '{}', 1, 1, 1, 365, 30, '{}'),
  ('g002', '蓝牙耳机', 'lanyaerji', 'SP002', 'TWS-8', 'cat001', '品牌B', '个', 200.0000, 399.0000, '9876543210987', 'A02', 50.0000, 0, '降噪耳机', '[]', '详情', '[]', '{}', 0, 0, 0, 0, 0, '{}')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `py` = VALUES(`py`),
  `number` = VALUES(`number`),
  `spec` = VALUES(`spec`),
  `category` = VALUES(`category`),
  `brand` = VALUES(`brand`),
  `unit` = VALUES(`unit`),
  `buy` = VALUES(`buy`),
  `sell` = VALUES(`sell`),
  `code` = VALUES(`code`),
  `location` = VALUES(`location`),
  `stock` = VALUES(`stock`),
  `type` = VALUES(`type`),
  `data` = VALUES(`data`),
  `imgs` = VALUES(`imgs`),
  `details` = VALUES(`details`),
  `units` = VALUES(`units`),
  `strategy` = VALUES(`strategy`),
  `serial` = VALUES(`serial`),
  `batch` = VALUES(`batch`),
  `validity` = VALUES(`validity`),
  `protect` = VALUES(`protect`),
  `threshold` = VALUES(`threshold`),
  `more` = VALUES(`more`);

INSERT INTO `room` (`id`, `warehouse`, `goods`, `attr`, `nums`)
VALUES
  ('room001', 'w001', 'g001', '红色,128G', 50.0000),
  ('room002', 'w001', 'g002', NULL, 100.0000)
ON DUPLICATE KEY UPDATE
  `warehouse` = VALUES(`warehouse`),
  `goods` = VALUES(`goods`),
  `attr` = VALUES(`attr`),
  `nums` = VALUES(`nums`);

INSERT INTO `sor` (`id`, `frame`, `customer`, `time`, `number`, `total`, `actual`, `people`, `arrival`, `logistics`, `file`, `data`, `more`, `examine`, `state`, `user`)
VALUES
  ('sor_local_001', 'f001', 'cust001', NOW(), 'SO-LOCAL-001', 1999.0000, 1999.0000, 'p002', CURDATE(), '{}', '[]', '本地销售订单', '{}', 1, 0, 'u001'),
  ('sor_local_002', 'f002', 'cust002', NOW(), 'SO-LOCAL-002', 399.0000, 399.0000, 'p002', CURDATE(), '{}', '[]', '本地销售订单', '{}', 0, 0, 'u001')
ON DUPLICATE KEY UPDATE
  `frame` = VALUES(`frame`),
  `customer` = VALUES(`customer`),
  `time` = VALUES(`time`),
  `number` = VALUES(`number`),
  `total` = VALUES(`total`),
  `actual` = VALUES(`actual`),
  `people` = VALUES(`people`),
  `arrival` = VALUES(`arrival`),
  `logistics` = VALUES(`logistics`),
  `file` = VALUES(`file`),
  `data` = VALUES(`data`),
  `more` = VALUES(`more`),
  `examine` = VALUES(`examine`),
  `state` = VALUES(`state`),
  `user` = VALUES(`user`);

INSERT INTO `sor_info` (`id`, `pid`, `goods`, `attr`, `unit`, `warehouse`, `price`, `nums`, `discount`, `dsc`, `total`, `tax`, `tat`, `tpt`, `data`, `handle`)
VALUES
  ('sor_info_local_001', 'sor_local_001', 'g001', '红色,128G', '台', 'w001', 1999.0000, 1.0000, 0.00, 0.0000, 1999.0000, 13.00, 259.8700, 2258.8700, '订单明细', 0.0000),
  ('sor_info_local_002', 'sor_local_002', 'g002', NULL, '个', 'w001', 399.0000, 1.0000, 0.00, 0.0000, 399.0000, 6.00, 23.9400, 422.9400, '订单明细', 0.0000)
ON DUPLICATE KEY UPDATE
  `pid` = VALUES(`pid`),
  `goods` = VALUES(`goods`),
  `attr` = VALUES(`attr`),
  `unit` = VALUES(`unit`),
  `warehouse` = VALUES(`warehouse`),
  `price` = VALUES(`price`),
  `nums` = VALUES(`nums`),
  `discount` = VALUES(`discount`),
  `dsc` = VALUES(`dsc`),
  `total` = VALUES(`total`),
  `tax` = VALUES(`tax`),
  `tat` = VALUES(`tat`),
  `tpt` = VALUES(`tpt`),
  `data` = VALUES(`data`),
  `handle` = VALUES(`handle`);

INSERT INTO `sell` (`id`, `source`, `frame`, `customer`, `time`, `number`, `total`, `actual`, `money`, `cost`, `account`, `people`, `logistics`, `file`, `data`, `more`, `examine`, `nucleus`, `cse`, `invoice`, `check`, `user`)
VALUES
  ('sell_local_001', NULL, 'f001', 'cust001', NOW(), 'XSD-LOCAL-001', 1999.0000, 1999.0000, 0.0000, 50.0000, 'acc001', 'p002', '{}', '[]', '本地销售单', '{}', 1, 0, 0, 0, 0, 'u001'),
  ('sell_local_002', NULL, 'f002', 'cust002', NOW(), 'XSD-LOCAL-002', 399.0000, 399.0000, 0.0000, 0.0000, 'acc002', 'p002', '{}', '[]', '本地销售单', '{}', 0, 0, 3, 0, 0, 'u001')
ON DUPLICATE KEY UPDATE
  `frame` = VALUES(`frame`),
  `customer` = VALUES(`customer`),
  `time` = VALUES(`time`),
  `number` = VALUES(`number`),
  `total` = VALUES(`total`),
  `actual` = VALUES(`actual`),
  `money` = VALUES(`money`),
  `cost` = VALUES(`cost`),
  `account` = VALUES(`account`),
  `people` = VALUES(`people`),
  `logistics` = VALUES(`logistics`),
  `file` = VALUES(`file`),
  `data` = VALUES(`data`),
  `more` = VALUES(`more`),
  `examine` = VALUES(`examine`),
  `nucleus` = VALUES(`nucleus`),
  `cse` = VALUES(`cse`),
  `invoice` = VALUES(`invoice`),
  `check` = VALUES(`check`),
  `user` = VALUES(`user`);

INSERT INTO `sell_info` (`id`, `pid`, `source`, `goods`, `attr`, `unit`, `warehouse`, `batch`, `mfd`, `price`, `nums`, `serial`, `discount`, `dsc`, `total`, `tax`, `tat`, `tpt`, `data`, `retreat`)
VALUES
  ('sell_info_local_001', 'sell_local_001', NULL, 'g001', '红色,128G', '台', 'w001', 'batch001', '2025-10-01', 1999.0000, 1.0000, '[\"SN001\"]', 0.00, 0.0000, 1999.0000, 13.00, 259.8700, 2258.8700, '出库', 0.0000),
  ('sell_info_local_002', 'sell_local_002', NULL, 'g002', NULL, '个', 'w001', 'batch002', '2025-10-02', 399.0000, 1.0000, '[]', 0.00, 0.0000, 399.0000, 6.00, 23.9400, 422.9400, '出库', 0.0000)
ON DUPLICATE KEY UPDATE
  `pid` = VALUES(`pid`),
  `source` = VALUES(`source`),
  `goods` = VALUES(`goods`),
  `attr` = VALUES(`attr`),
  `unit` = VALUES(`unit`),
  `warehouse` = VALUES(`warehouse`),
  `batch` = VALUES(`batch`),
  `mfd` = VALUES(`mfd`),
  `price` = VALUES(`price`),
  `nums` = VALUES(`nums`),
  `serial` = VALUES(`serial`),
  `discount` = VALUES(`discount`),
  `dsc` = VALUES(`dsc`),
  `total` = VALUES(`total`),
  `tax` = VALUES(`tax`),
  `tat` = VALUES(`tat`),
  `tpt` = VALUES(`tpt`),
  `data` = VALUES(`data`),
  `retreat` = VALUES(`retreat`);
