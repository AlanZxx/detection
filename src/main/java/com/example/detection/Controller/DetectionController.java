package com.example.detection.Controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DetectionController {
    public final static String IMG_PATH_PREFIX= "static/upload/imgs";

    @RequestMapping(value = "/hello")
//    @GetMapping("/test")
    public String Hello(){
        return "hello";
    }



    @RequestMapping("/uploadfile")
    @ResponseBody
    public Map<String, Object> uploadApk(@RequestParam("file")MultipartFile file) {
        //得到根目录文件

        Map<String, Object> resultMap = new HashMap<>();
        try {
            String filename = file.getOriginalFilename();
            File fileDir = DetectionController.getAbsoluteFilePath();
            File newFile = new File(fileDir.getAbsolutePath()+File.separator+filename);
            System.out.println(newFile);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(newFile));
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
            resultMap.put("msg", "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            resultMap.put("msg", "上传失败");
        }
        return resultMap;
    }

    @RequestMapping("download")
    public ResponseEntity download() throws IOException {
        FileSystemResource file = new FileSystemResource("1.jpg");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment;filename = 123.jpg");
//        在响应头中添加这个，设置下载文件默认的名称
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

    private static File getAbsoluteFilePath(){
        File filepath = new File(IMG_PATH_PREFIX);
        if(!filepath.exists()){
            filepath.mkdirs();
        }
        return filepath;
    }
}
