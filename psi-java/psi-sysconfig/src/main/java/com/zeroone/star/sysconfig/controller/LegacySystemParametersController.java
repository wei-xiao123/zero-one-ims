package com.zeroone.star.sysconfig.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/c1-systemparameters")
public class LegacySystemParametersController {

    @GetMapping({
            "/user/query-all",
            "/role/query-all",
            "/personnel/query",
            "/number-type/query-all",
            "/periods/query",
            "/log/query"
    })
    public JsonVO<PageDTO<Map<String, Object>>> emptyPage() {
        PageDTO<Map<String, Object>> page = new PageDTO<>();
        page.setPageIndex(1L);
        page.setPageSize(10L);
        page.setTotal(0L);
        page.setPages(0L);
        page.setRows(new ArrayList<>());
        return JsonVO.success(page);
    }

    @GetMapping({
            "/organization/query-by-list",
            "/organization/tree",
            "/role/list",
            "/role/permission"
    })
    public JsonVO<ArrayList<Object>> emptyList() {
        return JsonVO.success(new ArrayList<>());
    }

    @GetMapping({
            "/user/query-one",
            "/role/detail",
            "/personnel/detail",
            "/organization/detail",
            "/sys/get-msg"
    })
    public JsonVO<Map<String, Object>> emptyDetail() {
        return JsonVO.success(new LinkedHashMap<>());
    }

    @PostMapping({
            "/user/add-user",
            "/role/add-role",
            "/personnel/add",
            "/personnel/import",
            "/organization/add",
            "/number-type/get-number",
            "/periods/save",
            "/log/logs"
    })
    public JsonVO<Boolean> addOrSave(@RequestBody(required = false) Object body) {
        return JsonVO.success(Boolean.TRUE);
    }

    @PutMapping({
            "/user/mod-user",
            "/role/modify-role",
            "/personnel/modify-per",
            "/organization/update",
            "/sys/put-msg"
    })
    public JsonVO<Boolean> update(@RequestBody(required = false) Object body) {
        return JsonVO.success(Boolean.TRUE);
    }

    @DeleteMapping({
            "/user/del-user",
            "/role/delete-role",
            "/personnel/delete",
            "/organization/delete",
            "/number-type/remove-number",
            "/periods/delete",
            "/log/logs"
    })
    public JsonVO<Boolean> delete(@RequestBody(required = false) Object body) {
        return JsonVO.success(Boolean.TRUE);
    }
}
