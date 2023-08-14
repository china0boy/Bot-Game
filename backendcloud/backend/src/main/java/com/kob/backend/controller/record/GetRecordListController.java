package com.kob.backend.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetRecordListController {
    @Autowired
    private GetRecordListService getRecordListService;

    @GetMapping("/api/record/getlist/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        int pageSize = Integer.parseInt(data.get("pageSize"));
        pageSize = Math.min(pageSize, 30);
        return getRecordListService.getList(page,  pageSize);
    }
}
