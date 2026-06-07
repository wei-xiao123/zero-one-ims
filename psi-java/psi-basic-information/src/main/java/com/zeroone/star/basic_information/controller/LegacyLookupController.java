package com.zeroone.star.basic_information.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.basic_information.entity.Customer;
import com.zeroone.star.basic_information.entity.Supplier;
import com.zeroone.star.basic_information.entity.Warehouse;
import com.zeroone.star.basic_information.mapper.CustomerMapper;
import com.zeroone.star.basic_information.mapper.SupplierMapper;
import com.zeroone.star.basic_information.mapper.WarehouseMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LegacyLookupController {

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private SupplierMapper supplierMapper;

    @Resource
    private WarehouseMapper warehouseMapper;

    @GetMapping("/customers")
    public Map<String, Object> customers(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) Long page,
                                         @RequestParam(required = false) Long pageIndex,
                                         @RequestParam(required = false) Long pageSize) {
        Page<Customer> result = customerMapper.selectPage(
                new Page<>(resolvePageIndex(page, pageIndex), resolvePageSize(pageSize)),
                likeName(name)
        );
        return legacyPage(result.getRecords(), result.getTotal());
    }

    @GetMapping("/suppliers")
    public Map<String, Object> suppliers(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) Long page,
                                         @RequestParam(required = false) Long pageIndex,
                                         @RequestParam(required = false) Long pageSize) {
        Page<Supplier> result = supplierMapper.selectPage(
                new Page<>(resolvePageIndex(page, pageIndex), resolvePageSize(pageSize)),
                likeName(name)
        );
        return legacyPage(result.getRecords(), result.getTotal());
    }

    @GetMapping("/warehouse/list")
    public Map<String, Object> warehouseList() {
        List<Warehouse> warehouses = warehouseMapper.selectList(new QueryWrapper<Warehouse>().orderByAsc("number", "name"));
        Map<String, Object> response = legacySuccess();
        response.put("data", warehouses);
        return response;
    }

    private QueryWrapper likeName(String name) {
        QueryWrapper wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like("name", name.trim());
        }
        wrapper.orderByAsc("name");
        return wrapper;
    }

    private Map<String, Object> legacyPage(List<?> list, long total) {
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("rows", list);
        data.put("total", total);

        Map<String, Object> response = legacySuccess();
        response.put("data", data);
        return response;
    }

    private Map<String, Object> legacySuccess() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        return response;
    }

    private long resolvePageIndex(Long page, Long pageIndex) {
        Long value = page != null ? page : pageIndex;
        return value == null || value < 1 ? 1 : value;
    }

    private long resolvePageSize(Long pageSize) {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
