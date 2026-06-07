package com.zeroone.star.basic_information.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.basic_information.entity.Warehouse;
import com.zeroone.star.basic_information.service.IWarehouseService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/c2-sysbase/ware")
@Api(tags = "仓库管理兼容接口")
public class WarehouseController {

    @Resource
    private IWarehouseService warehouseService;

    @GetMapping("/query")
    @ApiOperation("查询仓库列表")
    public JsonVO<PageDTO<Warehouse>> query(WarehouseQuery query) {
        Page<Warehouse> page = new Page<>(query.getPageIndex(), query.getPageSize());
        QueryWrapper<Warehouse> wrapper = new QueryWrapper<>();
        likeIfPresent(wrapper, "name", query.getName());
        likeIfPresent(wrapper, "number", query.getNumber());
        likeIfPresent(wrapper, "contacts", query.getContacts());
        likeIfPresent(wrapper, "tel", query.getTel());
        likeIfPresent(wrapper, "`add`", query.getAdd());
        likeIfPresent(wrapper, "data", query.getData());
        wrapper.orderByAsc("number", "name");
        return JsonVO.success(PageDTO.create(warehouseService.page(page, wrapper)));
    }

    @GetMapping("/detail")
    @ApiOperation("查询仓库详情")
    public JsonVO<Warehouse> detail(@RequestParam String id) {
        return JsonVO.success(warehouseService.getById(id));
    }

    @PostMapping("/add")
    @ApiOperation("新增仓库")
    public JsonVO<String> add(@RequestBody Warehouse warehouse) {
        if (!StringUtils.hasText(warehouse.getId())) {
            warehouse.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (warehouseService.save(warehouse)) {
            return JsonVO.success(warehouse.getId());
        }
        return JsonVO.fail(null);
    }

    @PutMapping("/update")
    @ApiOperation("修改仓库")
    public JsonVO<String> update(@RequestBody Warehouse warehouse) {
        if (warehouseService.updateById(warehouse)) {
            return JsonVO.success(warehouse.getId());
        }
        return JsonVO.fail(null);
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除仓库")
    public JsonVO<List<String>> remove(@RequestBody(required = false) Object body) {
        List<String> ids = extractIds(body);
        if (ids.isEmpty()) {
            return JsonVO.fail(ids);
        }
        if (warehouseService.removeByIds(ids)) {
            return JsonVO.success(ids);
        }
        return JsonVO.fail(ids);
    }

    private void likeIfPresent(QueryWrapper<Warehouse> wrapper, String column, String value) {
        if (StringUtils.hasText(value)) {
            wrapper.like(column, value.trim());
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> extractIds(Object body) {
        LinkedHashSet<String> ids = new LinkedHashSet<>();
        if (body instanceof List) {
            ((List<?>) body).forEach(item -> addId(ids, item));
        } else if (body instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) body;
            addId(ids, map.get("id"));
            Object nestedParams = map.get("params");
            if (nestedParams instanceof Map) {
                Object nestedIds = ((Map<String, Object>) nestedParams).get("ids");
                addId(ids, nestedIds);
            }
            addId(ids, map.get("ids"));
        } else {
            addId(ids, body);
        }
        return new ArrayList<>(ids);
    }

    private void addId(LinkedHashSet<String> ids, Object value) {
        if (value instanceof List) {
            ((List<?>) value).forEach(item -> addId(ids, item));
            return;
        }
        if (value != null && StringUtils.hasText(String.valueOf(value))) {
            ids.add(String.valueOf(value));
        }
    }

    public static class WarehouseQuery {
        private Long pageIndex = 1L;
        private Long pageSize = 10L;
        private String name;
        private String number;
        private String contacts;
        private String tel;
        private String add;
        private String data;

        public Long getPageIndex() {
            return pageIndex == null || pageIndex < 1 ? 1 : pageIndex;
        }

        public void setPageIndex(Long pageIndex) {
            this.pageIndex = pageIndex;
        }

        public Long getPageSize() {
            return pageSize == null || pageSize < 1 ? 10 : pageSize;
        }

        public void setPageSize(Long pageSize) {
            this.pageSize = pageSize;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAdd() {
            return add;
        }

        public void setAdd(String add) {
            this.add = add;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
