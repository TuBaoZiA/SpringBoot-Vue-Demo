package com.ycj.demo.web.admin.controller;

import cn.hutool.core.util.IdUtil;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.CommonService;
import com.ycj.demo.util.OSSUtil;
import com.ycj.demo.web.admin.vo.Drop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @GetMapping("roles")
    public Result rolesDrop(){
        List<Drop> drops = commonService.rolesDrop();
        return Result.success(drops, drops.size());
    }

    @GetMapping("permissions")
    public Result permissionsDrop(){
        List<Drop> drops = commonService.permissionsDrop();
        return Result.success(drops, drops.size());
    }

    @GetMapping("menus")
    public Result menusDrop(@RequestParam(name = "id") String idStr, @RequestParam(name = "keepParent") String keepParent){
        Integer id = "undefined".equals(idStr) ? null : Integer.valueOf(idStr);
        Boolean isKeepParent = "undefined".equals(keepParent) ? null : true;
        List<Drop> drops = commonService.selectNotChildrenMenuById(id, isKeepParent);
        return Result.success(drops, drops.size());
    }

    @PostMapping("upload/avatar")
    public Result upload(@RequestParam("file") MultipartFile file)throws IOException {
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf('.'));

        String objectName = "user-avatar/"+ IdUtil.simpleUUID() + fileType;
        OSSUtil.putObject(objectName, file.getInputStream(), true);
        return Result.success(objectName);
    }
}
