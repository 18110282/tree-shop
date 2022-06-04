package com.treeshop.service;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import java.nio.file.Path;

public interface CommonService {
    void processFile(Path uploadPath, MultipartFile multipartFile, String fileName) throws IOException;

    String getSiteUrl(HttpServletRequest request);
}

