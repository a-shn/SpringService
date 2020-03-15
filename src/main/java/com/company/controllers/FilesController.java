package com.company.controllers;

import com.company.services.FileUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.annotation.MultipartConfig;

@Controller
@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
        maxFileSize = 10485760L, // 10 MB
        maxRequestSize = 20971520L // 20 MB
)
public class FilesController {
    @Autowired
    @Qualifier("fileUploader")
    private FileUploader fileUploader;

    @RequestMapping(value = "/uploadfiles", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getUploadPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadfile");
        return modelAndView;
    }

    @RequestMapping(value = "/uploadfiles", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        System.out.println("im here");
        fileUploader.uploadAndSaveToDb(multipartFile);
        return null;
    }

    // localhost:8080/files/123809183093qsdas09df8af.jpeg

//    @RequestMapping(value = "/getfile/{file-name:.+}", method = RequestMethod.GET)
//    public ModelAndView getFile(@PathVariable("file-name") String fileName) {
//        // TODO: найти на диске
//        // TODO: отдать пользователю
//        return null;
//    }
}