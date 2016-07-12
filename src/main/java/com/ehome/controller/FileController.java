package com.ehome.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ehome.bean.FilesBean;
import com.ouyang.util.file.FileUtil;
import com.ouyang.util.file.model.Files;

@RestController
@RequestMapping("/files")
public class FileController {
    private static final Logger logger = Logger.getLogger(FileController.class);
    @Value("${system.static.dir}")
	private String staticDir;
    @ResponseBody
    @RequestMapping(value="",method=RequestMethod.GET)
    public List<FilesBean> getFiles() {
    	Files files = FileUtil.getFiles(staticDir.replace("file:", ""));
    	List<FilesBean> filesBeans = new ArrayList<FilesBean>();
    	modifiFiles(files,filesBeans);
    	return filesBeans;
    }
	private void modifiFiles(Files files,List<FilesBean> filesBeans) {
		FilesBean filesBean = new FilesBean();
		if(files.getName().contains(".js") 
				|| files.getName().contains(".html") 
				|| files.getName().contains(".git") 
				|| files.getName().contains("index.md") 
				|| files.getName().contains(".jpg") 
				|| files.getName().contains(".docx") 
				|| files.getName().contains(".png") 
				|| files.getName().contains(".xls") 
				|| files.getName().contains(".xlsx") 
				|| files.getName().contains(".css") 
				|| files.getName().contains(".pdf")){
			return;
		}
		filesBean.setName(files.getName().replace(".md", ""));
		filesBean.setType(files.getType());
		if(files.getType() == Files.TYPE_FILE){
			String path = files.getPath().replace("\\", "/");
			filesBean.setUrl(path.replace(staticDir.replace("file:", ""), ""));
		}
		filesBeans.add(filesBean);
    	if(files.getType() == Files.TYPE_DIR){
    		List<Files> allFiles = files.getFiles();
    		for (Files temp : allFiles) {
				modifiFiles(temp,filesBeans);
			}
    	}
	}
} 