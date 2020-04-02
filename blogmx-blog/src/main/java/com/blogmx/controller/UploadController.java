package com.blogmx.controller;


import com.blogmx.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("saveBlog")
    public ResponseEntity<Void> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("titleName") String titleName,
                                              @RequestParam("subTitle") String subTitle,
                                              @RequestParam("isTop") Boolean isTop,
                                              @RequestParam("isHot") Boolean isHot,
                                              @RequestParam("index") String index,
                                              @RequestParam("watchNum") Long watchNum,
                                              @RequestParam("image") String image,
                                              @RequestParam("date") Long date
    ){
        System.out.println("111");
        int i = uploadService.saveBlog(file, titleName, subTitle, isTop, isHot, index, watchNum, image, date);
        if(i == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();

    }

}

