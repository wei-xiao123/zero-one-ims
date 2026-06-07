package com.zeroone.star.reportmanagement.controller.fundreport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.CashBankStatementDTO;
import com.zeroone.star.project.dto.j5.fundreport.CustomerStatementDetailDTO;
import com.zeroone.star.project.dto.j5.fundreport.CustomerStatementMainDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountPayableStatementDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountReceivableStatementDTO;
import com.zeroone.star.project.dto.j5.fundreport.OtherIncomeExpenditureDetailDTO;
import com.zeroone.star.project.dto.j5.fundreport.ProfitReportDTO;
import com.zeroone.star.project.dto.j5.fundreport.SupplierStatementDetailDTO;
import com.zeroone.star.project.dto.j5.fundreport.SupplierStatementMainDTO;
import com.zeroone.star.project.dto.j5.fundreport.UnitArrearsReportDTO;
import com.zeroone.star.project.query.j5.fundreport.CashBankStatementQuery;
import com.zeroone.star.project.query.j5.fundreport.CustomerStatementQuery;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountPayableStatementQuery;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountReceivableStatementQuery;
import com.zeroone.star.project.query.j5.fundreport.OtherIncomeExpenditureQuery;
import com.zeroone.star.project.query.j5.fundreport.ProfitQuery;
import com.zeroone.star.project.query.j5.fundreport.SupplierStatementQuery;
import com.zeroone.star.project.query.j5.fundreport.UnitReceiptQuery;
import com.zeroone.star.reportmanagement.service.CustomerStatementService;
import com.zeroone.star.reportmanagement.service.DetailedAccountPayableStatementService;
import com.zeroone.star.reportmanagement.service.ICashBankStatementService;
import com.zeroone.star.reportmanagement.service.IDetailedAccountReceivableStatementService;
import com.zeroone.star.reportmanagement.service.OtherIncomeExpenditureDetailService;
import com.zeroone.star.reportmanagement.service.ProfitService;
import com.zeroone.star.reportmanagement.service.SupplierStatementService;
import com.zeroone.star.reportmanagement.service.UnitArrearsReportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crt")
public class LegacyFundReportController {

    @Resource
    private SupplierStatementService supplierStatementService;

    @Resource
    private CustomerStatementService customerStatementService;

    @Resource
    private IDetailedAccountReceivableStatementService receivableStatementService;

    @Resource
    private DetailedAccountPayableStatementService payableStatementService;

    @Resource
    private OtherIncomeExpenditureDetailService otherIncomeExpenditureDetailService;

    @Resource
    private ProfitService profitService;

    @Resource
    private UnitArrearsReportService unitArrearsReportService;

    @Resource
    private ICashBankStatementService cashBankStatementService;

    @PostMapping("/cst")
    public Map<String, Object> supplierStatement(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            SupplierStatementQuery query = new SupplierStatementQuery();
            fillPage(query, params);
            query.setName(firstParam(params, "supplier", "name"));
            query.setBeginTime(asLocalDate(firstParam(params, "startTime", "beginTime")));
            query.setEndTime(asLocalDate(firstParam(params, "endTime")));
            Page<SupplierStatementMainDTO> page = supplierStatementService.listSupplierStatement(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (SupplierStatementMainDTO item : safeList(page.getRecords())) {
                rows.add(statementRow(item.getTableType(), item.getFrame(), formatTime(item.getTime()), item.getNumber(),
                        item.getTotal(), item.getDiscountAmount(), item.getActual(), item.getMoney(), item.getActual2(),
                        item.getData(), supplierDetails(item.getDetails())));
            }
            return success(rows, page.getTotal());
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/cct")
    public Map<String, Object> customerStatement(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            CustomerStatementQuery query = new CustomerStatementQuery();
            fillPage(query, params);
            query.setName(firstParam(params, "customer", "name"));
            query.setBeginTime(asLocalDate(firstParam(params, "startTime", "beginTime")));
            query.setEndTime(asLocalDate(firstParam(params, "endTime")));
            Page<CustomerStatementMainDTO> page = customerStatementService.listCustomerStatement(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (CustomerStatementMainDTO item : safeList(page.getRecords())) {
                rows.add(statementRow(item.getTableType(), item.getFrame(), formatTime(item.getTime()), item.getNumber(),
                        item.getTotal(), item.getDiscountAmount(), item.getActual(), item.getMoney(), item.getActual2(),
                        item.getData(), customerDetails(item.getDetails())));
            }
            return success(rows, page.getTotal());
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/crs")
    public Map<String, Object> receivableDetail(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            DetailedAccountReceivableStatementQuery query = new DetailedAccountReceivableStatementQuery();
            fillPage(query, params);
            query.setCustomer(firstParam(params, "customer", "name"));
            query.setCategory(firstParam(params, "category"));
            query.setStartTime(firstParam(params, "startTime"));
            query.setEndTime(firstParam(params, "endTime"));
            PageDTO<DetailedAccountReceivableStatementDTO> page = receivableStatementService.listDetailedAccountReceivableStatement(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (DetailedAccountReceivableStatementDTO item : safeList(page.getRows())) {
                rows.add(accountDetailRow(item.getCustomer(), item.getType(), item.getFrame(), item.getTime(), item.getNumber(),
                        item.getIncreaseAccountsReceivable(), item.getIncreaseAdvanceReceipts(), item.getBalance(), item.getData()));
            }
            return success(rows, total(page));
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/cps")
    public Map<String, Object> payableDetail(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            DetailedAccountPayableStatementQuery query = new DetailedAccountPayableStatementQuery();
            fillPage(query, params);
            query.setSupplier(firstParam(params, "supplier", "name"));
            query.setSupplierType(firstParam(params, "supplierType", "category"));
            query.setStartTime(firstParam(params, "startTime"));
            query.setEndTime(firstParam(params, "endTime"));
            PageDTO<DetailedAccountPayableStatementDTO> page = payableStatementService.listDetailedAccountPayableStatement(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (DetailedAccountPayableStatementDTO item : safeList(page.getRows())) {
                rows.add(accountDetailRow(item.getSupplier(), item.getDocumentType(), item.getFrame(), item.getDateTime(), item.getDocumentNumber(),
                        item.getAccountsPayableIncrease(), item.getAdvancePaymentIncrease(), item.getAccountsPayableBalance(), item.getRemark()));
            }
            return success(rows, total(page));
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/cos")
    public Map<String, Object> otherIncomeExpenditure(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            OtherIncomeExpenditureQuery query = new OtherIncomeExpenditureQuery();
            fillPage(query, params);
            query.setDocumentType(firstParam(params, "mold", "documentType"));
            query.setDocumentNumber(firstParam(params, "number", "documentNumber"));
            query.setStartTime(firstParam(params, "startTime"));
            query.setEndTime(firstParam(params, "endTime"));
            query.setCategory(firstParam(params, "iet", "category"));
            query.setSettlementAccount(firstParam(params, "account", "settlementAccount"));
            query.setRemark(firstParam(params, "data", "remark"));
            PageDTO<OtherIncomeExpenditureDetailDTO> page = otherIncomeExpenditureDetailService.listOtherIncomeExpenditureDetail(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (OtherIncomeExpenditureDetailDTO item : safeList(page.getRows())) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("name", item.getDocumentType());
                row.put("class", classData(item.getFrame(), item.getDateTime(), item.getDocumentNumber(), item.getSettlementAccount(), item.getRemark()));
                row.put("current", nameMap(item.getCounterparty()));
                row.put("info", infoCategory(item.getCategory()));
                row.put("in", zero(item.getIncome()));
                row.put("out", zero(item.getExpenditure()));
                rows.add(row);
            }
            return success(rows, total(page));
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/cit")
    public Map<String, Object> profit(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            ProfitQuery query = new ProfitQuery();
            fillPage(query, params);
            query.setStartTime(asLocalDate(firstParam(params, "startTime")));
            query.setEndTime(asLocalDate(firstParam(params, "endTime")));
            PageDTO<ProfitReportDTO> page = profitService.listProfitReportForm(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (ProfitReportDTO item : safeList(page.getRows())) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("name", item.getProjectName());
                row.put("money", item.getAmount());
                rows.add(row);
            }
            return success(rows, total(page));
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/cds")
    public Map<String, Object> unitArrears(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            UnitReceiptQuery query = new UnitReceiptQuery();
            fillPage(query, params);
            query.setName(firstParam(params, "name"));
            query.setNumber(firstParam(params, "number"));
            query.setType(firstParam(params, "mold", "type"));
            query.setData(firstParam(params, "data"));
            PageDTO<UnitArrearsReportDTO> page = unitArrearsReportService.query(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (UnitArrearsReportDTO item : safeList(page.getRows())) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("mold", item.getType());
                row.put("name", item.getName());
                row.put("number", item.getNumber());
                row.put("collection", zero(item.getBalanceCol()));
                row.put("payment", zero(item.getBalancePay()));
                row.put("data", item.getData());
                rows.add(row);
            }
            return success(rows, total(page));
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/cbf")
    public Map<String, Object> cashBank(@RequestBody(required = false) Map<String, Object> body) {
        try {
            Map<String, Object> params = safeBody(body);
            CashBankStatementQuery query = new CashBankStatementQuery();
            fillPage(query, params);
            query.setCustomer(firstParam(params, "customer"));
            query.setSupplier(firstParam(params, "supplier"));
            query.setStartTime(firstParam(params, "startTime"));
            query.setEndTime(firstParam(params, "endTime"));
            query.setUser(firstParam(params, "user"));
            query.setAccount(firstParam(params, "account"));
            PageDTO<CashBankStatementDTO> page = cashBankStatementService.listCashBankStatement(query);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (CashBankStatementDTO item : safeList(page.getRows())) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("key", item.getNumber());
                row.put("name", item.getName());
                row.put("extension", typeMap(item.getType()));
                row.put("sourceData", sourceData(item.getFrame(), item.getTime(), item.getNumber(), item.getUser(), item.getData()));
                row.put("current", nameMap(item.getCustomer()));
                row.put("in", zero(item.getIncome()));
                row.put("out", zero(item.getExpend()));
                row.put("balance", zero(item.getBalance()));
                row.put("node", new ArrayList<>());
                rows.add(row);
            }
            return success(rows, total(page));
        } catch (Exception e) {
            return error(e);
        }
    }

    private Map<String, Object> statementRow(String bill, String frame, String time, String number,
                                             BigDecimal total, BigDecimal discount, BigDecimal actual,
                                             BigDecimal money, BigDecimal balance, String data,
                                             List<Map<String, Object>> details) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("key", number);
        row.put("bill", bill);
        row.put("frameData", nameMap(frame));
        row.put("time", time);
        row.put("number", number);
        row.put("total", zero(total));
        row.put("discount", zero(discount));
        row.put("actual", zero(actual));
        row.put("money", zero(money));
        row.put("balance", zero(balance));
        row.put("data", data);
        row.put("node", details);
        return row;
    }

    private List<Map<String, Object>> supplierDetails(List<SupplierStatementDetailDTO> details) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (SupplierStatementDetailDTO detail : safeList(details)) {
            rows.add(statementDetail(detail.getGoods(), detail.getPrice(), detail.getNums()));
        }
        return rows;
    }

    private List<Map<String, Object>> customerDetails(List<CustomerStatementDetailDTO> details) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (CustomerStatementDetailDTO detail : safeList(details)) {
            rows.add(statementDetail(detail.getGoods(), detail.getPrice(), detail.getNums()));
        }
        return rows;
    }

    private Map<String, Object> statementDetail(String goods, BigDecimal price, BigDecimal nums) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("detail", detailData(goods, "", "", price, nums));
        return row;
    }

    private Map<String, Object> detailData(String name, String attr, String unit, BigDecimal price, BigDecimal nums) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", name);
        data.put("attr", attr);
        data.put("unit", unit);
        data.put("price", zero(price));
        data.put("nums", zero(nums));
        data.put("dsc", BigDecimal.ZERO);
        data.put("total", zero(price).multiply(zero(nums)));
        data.put("tat", BigDecimal.ZERO);
        data.put("tpt", BigDecimal.ZERO);
        return data;
    }

    private Map<String, Object> accountDetailRow(String name, String bill, String frame, String time, String number,
                                                 BigDecimal cw, BigDecimal pia, BigDecimal balance, String data) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("key", number);
        row.put("name", name);
        row.put("bill", bill);
        row.put("frameData", nameMap(frame));
        row.put("time", time);
        row.put("number", number);
        row.put("cw", zero(cw));
        row.put("pia", zero(pia));
        row.put("balance", zero(balance));
        row.put("data", data);
        row.put("node", new ArrayList<>());
        return row;
    }

    private Map<String, Object> classData(String frame, String time, String number, String account, String data) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("frameData", nameMap(frame));
        result.put("time", time);
        result.put("number", number);
        result.put("accountData", nameMap(account));
        result.put("data", data);
        return result;
    }

    private Map<String, Object> sourceData(String frame, String time, String number, String user, String data) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("frameData", nameMap(frame));
        result.put("time", time);
        result.put("number", number);
        result.put("userData", nameMap(user));
        result.put("data", data);
        return result;
    }

    private Map<String, Object> infoCategory(String name) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("ietData", nameMap(name));
        return result;
    }

    private Map<String, Object> typeMap(String type) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", type);
        return result;
    }

    private Map<String, Object> nameMap(String name) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        return result;
    }

    private Map<String, Object> success(List<Map<String, Object>> rows, long total) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("state", "success");
        result.put("info", rows);
        result.put("count", total);
        return result;
    }

    private Map<String, Object> error(Exception e) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("state", "error");
        result.put("info", e.getMessage() == null ? "fund report query failed" : e.getMessage());
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

    private String firstParam(Map<String, Object> params, String... keys) {
        for (String key : keys) {
            String value = asString(params.get(key));
            if (value != null) {
                return value;
            }
        }
        return null;
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
        return text.isEmpty() || "null".equalsIgnoreCase(text) ? null : text;
    }

    private LocalDate asLocalDate(String text) {
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

    private String formatTime(LocalDateTime time) {
        return time == null ? null : time.toString();
    }

    private BigDecimal zero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private <T> List<T> safeList(List<T> list) {
        return list == null ? new ArrayList<>() : list;
    }

    private long total(PageDTO<?> page) {
        return page == null || page.getTotal() == null ? 0L : page.getTotal();
    }
}
