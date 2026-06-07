package com.zeroone.star.report.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.report.ProductStockBalanceDTO;
import com.zeroone.star.project.dto.j8.report.WarehouseCellDTO;
import com.zeroone.star.project.dto.j8.report.wss_KazamataNeri.StockSummaryReportDTO;
import com.zeroone.star.project.query.j8.report.ProductStockBalanceQuery;
import com.zeroone.star.project.query.j8.report.StockDetailReportQuery;
import com.zeroone.star.project.query.j8.report.StockSummaryReportQuery;
import com.zeroone.star.project.vo.j8.report.StockDetailReportVO;
import com.zeroone.star.report.service.StockBalanceReportService;
import com.zeroone.star.report.service.StockDetailReportService;
import com.zeroone.star.report.service.StockSummaryReportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wrf")
public class LegacyWarehouseReportController {

    @Resource
    private StockBalanceReportService stockBalanceReportService;

    @Resource
    private StockDetailReportService stockDetailReportService;

    @Resource
    private StockSummaryReportService stockSummaryReportService;

    @PostMapping("/wbs")
    public Map<String, Object> stockBalance(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            ProductStockBalanceQuery query = new ProductStockBalanceQuery();
            fillPage(query, params);
            query.setProductName(asString(params.get("goods")));
            query.setWarehouseId(asStringList(params.get("warehouse")));
            query.setQueryDate(asLocalDate(params.get("time")));

            PageDTO<ProductStockBalanceDTO> page = stockBalanceReportService.getProductStocksByCondition(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            Map<String, String> columns = new LinkedHashMap<>();
            if (page.getRows() != null) {
                for (ProductStockBalanceDTO item : page.getRows()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("goodsData", goodsData(item.getProductName(), item.getProductCode(), item.getSpecification()));
                    row.put("unit", item.getUnit());
                    if (item.getCells() != null) {
                        for (WarehouseCellDTO cell : item.getCells()) {
                            String warehouseId = cell.getWarehouseId();
                            if (warehouseId == null || warehouseId.trim().isEmpty()) {
                                warehouseId = cell.getWarehouseName();
                            }
                            if (warehouseId == null || warehouseId.trim().isEmpty()) {
                                continue;
                            }
                            columns.put(warehouseId, cell.getWarehouseName());
                            row.put("wb_" + warehouseId, amount(cell.getCost(), cell.getQty(), cell.getAmount()));
                        }
                    }
                    row.put("balance", amount(item.getAvgCost(), item.getSumQty(), item.getSumAmount()));
                    rows.add(row);
                }
            }

            List<Map<String, Object>> legacyColumns = new ArrayList<>();
            for (Map.Entry<String, String> entry : columns.entrySet()) {
                Map<String, Object> column = new LinkedHashMap<>();
                column.put("id", entry.getKey());
                column.put("name", entry.getValue() == null ? entry.getKey() : entry.getValue());
                legacyColumns.add(column);
            }
            return success(rows, total(page), legacyColumns);
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/wds")
    public Map<String, Object> stockDetail(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            StockDetailReportQuery query = new StockDetailReportQuery();
            fillPage(query, params);
            query.setGoods(asString(params.get("goods")));
            query.setWarehouse(asStringList(params.get("warehouse")));
            query.setMold(asStringList(params.get("mold")));
            query.setStartTime(asLocalDate(params.get("startTime")));
            query.setEndTime(asLocalDate(params.get("endTime")));

            PageDTO<StockDetailReportVO> page = stockDetailReportService.listStockDetail(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            if (page.getRows() != null) {
                for (StockDetailReportVO item : page.getRows()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("extension", nameMap("type", firstNonBlank(item.getTypeName(), item.getType())));
                    row.put("sourceData", sourceData(item.getNumber(), item.getTime() == null ? null : item.getTime().toString()));
                    row.put("current", nameMap("name", item.getPartnerName()));
                    row.put("goodsData", goodsData(item.getGoodsName(), item.getGoodsId(), item.getAttr()));
                    row.put("unit", item.getUnit());
                    row.put("warehouseData", nameMap("name", item.getWarehouseName()));
                    row.put("in", amount(item.getInUct(), item.getInNums(), item.getInBct()));
                    row.put("out", amount(item.getOutUct(), item.getOutNums(), item.getOutBct()));
                    row.put("balance", amount(item.getBalUct(), item.getBalUns(), item.getBalBct()));
                    rows.add(row);
                }
            }
            return success(rows, total(page), null);
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/wss")
    public Map<String, Object> stockSummary(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            StockSummaryReportQuery query = new StockSummaryReportQuery();
            fillPage(query, params);
            query.setGoods(asString(params.get("goods")));
            query.setWarehouse(asStringList(params.get("warehouse")));
            query.setStartTime(asLocalDate(params.get("startTime")));
            query.setEndTime(asLocalDate(params.get("endTime")));

            PageDTO<StockSummaryReportDTO> page = stockSummaryReportService.listGoodsReceiptAndDispatchSummary(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            if (page.getRows() != null) {
                for (StockSummaryReportDTO item : page.getRows()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("goodsData", goodsData(item.getName(), item.getNumber(), item.getSpec()));
                    row.put("unit", item.getUnit());
                    row.put("warehouseData", nameMap("name", item.getWarehouse()));
                    row.put("state", amount(item.getRoomPrice(), item.getRoomNums(), item.getRoomTotal()));
                    row.put("buy", amount(item.getBuyPrice(), item.getBuyNums(), item.getBuyTotal()));
                    row.put("bre", amount(item.getBrePrice(), item.getBreNums(), item.getBreTotal()));
                    row.put("sell", amount(item.getSellPrice(), item.getSellNums(), item.getSellTotal()));
                    row.put("sre", amount(item.getSrePrice(), item.getSreNums(), item.getSreTotal()));
                    row.put("swapOut", amount(item.getSwapOutPrice(), item.getSwapOutNums(), item.getSwapOutTotal()));
                    row.put("swapEnter", amount(item.getSwapInPrice(), item.getSwapInNums(), item.getSwapInTotal()));
                    row.put("entry", amount(item.getEntryPrice(), item.getEntryNums(), item.getEntryTotal()));
                    row.put("extry", amount(item.getExtryPrice(), item.getExtryNums(), item.getExtryTotal()));
                    row.put("balance", amount(item.getSumPrice(), item.getSumNums(), item.getSumTotal()));
                    rows.add(row);
                }
            }
            return success(rows, total(page), null);
        } catch (Exception e) {
            return error(e);
        }
    }

    private Map<String, Object> success(List<Map<String, Object>> rows, long total, List<Map<String, Object>> columns) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("state", "success");
        result.put("info", rows);
        result.put("count", total);
        if (columns != null) {
            result.put("column", columns);
        }
        return result;
    }

    private Map<String, Object> error(Exception e) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("state", "error");
        result.put("info", e.getMessage() == null ? "warehouse report query failed" : e.getMessage());
        result.put("count", 0);
        return result;
    }

    private Map<String, Object> safeBody(Map<String, Object> body) {
        return body == null ? new LinkedHashMap<>() : body;
    }

    private void fillPage(com.zeroone.star.project.query.PageQuery query, Map<String, Object> params) {
        query.setPageIndex(asLong(params.get("page"), 1L));
        query.setPageSize(asLong(params.get("limit"), 30L));
    }

    private long asLong(Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String asString(Object value) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        return text.isEmpty() ? null : text;
    }

    private List<String> asStringList(Object value) {
        List<String> values = new ArrayList<>();
        if (value == null) {
            return values;
        }
        if (value instanceof Iterable) {
            for (Object item : (Iterable<?>) value) {
                addString(values, item);
            }
            return values;
        }
        addString(values, value);
        return values;
    }

    private void addString(List<String> values, Object value) {
        String text = asString(value);
        if (text != null && !"null".equalsIgnoreCase(text)) {
            values.add(text);
        }
    }

    private LocalDate asLocalDate(Object value) {
        String text = asString(value);
        if (text == null) {
            return null;
        }
        if (text.length() > 10) {
            text = text.substring(0, 10);
        }
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private Map<String, Object> goodsData(String name, String number, String spec) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", name);
        data.put("number", number);
        data.put("spce", spec);
        return data;
    }

    private Map<String, Object> sourceData(String number, String time) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("number", number);
        data.put("time", time);
        data.put("frameData", nameMap("name", ""));
        return data;
    }

    private Map<String, Object> nameMap(String key, Object value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(key, value);
        return data;
    }

    private Map<String, Object> amount(BigDecimal uct, BigDecimal uns, BigDecimal bct) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("uct", uct == null ? BigDecimal.ZERO : uct);
        data.put("uns", uns == null ? BigDecimal.ZERO : uns);
        data.put("bct", bct == null ? BigDecimal.ZERO : bct);
        return data;
    }

    private String firstNonBlank(String first, String second) {
        return asString(first) == null ? second : first;
    }

    private long total(PageDTO<?> page) {
        return page == null || page.getTotal() == null ? 0L : page.getTotal();
    }
}
