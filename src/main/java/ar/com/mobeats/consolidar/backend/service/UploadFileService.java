package ar.com.mobeats.consolidar.backend.service;

import java.io.InputStream;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.util.FileUtil;

@Service
public class UploadFileService {

    @Autowired
    private FileUtil fileUtil;

    public String saveFile(Attachment attachment, String directory) throws Exception {
        InputStream is = attachment.getDataHandler().getInputStream();
        String fileName = attachment.getContentDisposition().getParameter("filename");
        return fileUtil.saveToFile(directory, fileName, is);
    }

    public String saveImage(Attachment attachment, String dir, Long fileName) throws Exception {
    	InputStream is = attachment.getDataHandler().getInputStream();
    	fileUtil.saveToFile(dir, fileName.toString(), is);
    	return fileName.toString();
    }
}
