package com.blogmx.upload.controller;

import com.blogmx.upload.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @GetMapping("save")
    public ResponseEntity<Void> save(){
        System.out.println("get save");
        return ResponseEntity.ok().build();
    }
    @PostMapping("saveBlog")
    public ResponseEntity<Void> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("titleName") String titleName,
                                              @RequestParam("subTitle") String subTitle,
                                              @RequestParam("isTop") Boolean isTop,
                                              @RequestParam("isHot") Boolean isHot,
                                              @RequestParam("index") String index,
                                              @RequestParam("watchNum") Long watchNum,
                                              @RequestParam("image") String image
                                              ){
        System.out.println("111");
        int i = uploadService.saveBlog(file, titleName, subTitle, isTop, isHot, index, watchNum, image);
        if(i == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();

    }

}

