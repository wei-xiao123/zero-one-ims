package com.zeroone.star.sysconfig.controller;

import com.zeroone.star.project.vo.JsonVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class LocalUploadController {

    @PostMapping({"/upload", "/user/upload", "/imy/upload"})
    public JsonVO<Map<String, Object>> upload(MultipartFile file) {
        Map<String, Object> data = new LinkedHashMap<>();
        String filename = file == null ? "uploaded-file" : file.getOriginalFilename();
        data.put("name", filename);
        data.put("url", "/uploads/local/" + (filename == null ? "uploaded-file" : filename));
        return JsonVO.success(data);
    }
}
