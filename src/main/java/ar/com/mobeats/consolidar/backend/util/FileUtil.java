package ar.com.mobeats.consolidar.backend.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FileUtil {

    private static final Logger LOGGER = Logger.getLogger(FileUtil.class);

    public static byte[] openFile(final String path) {
        Path p = FileSystems.getDefault().getPath(path);
        byte[] fileData;
        try {
            fileData = Files.readAllBytes(p);
        } catch (IOException e) {
            return null;
        }
        return fileData;
    }

    public String saveToFile(String absolutePath, String fileName, InputStream is) {

        String file = getCompleteFileName(absolutePath, fileName);

        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);

            IOUtils.copy(is, outputStream);

        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("Ocurrio un error al intentar guardar el archivo {0}", file), e);
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;
    }
    
    public static byte[] openTemporaryFile(final String fileName) {
        FileSystem fileSystem = FileSystems.getDefault();
        byte[] fileData;
        
        try {
            String tmpFolder = System.getProperty("java.io.tmpdir");
            String completeFileName = getCompleteFileName(tmpFolder, fileName);
            Path p = fileSystem.getPath(completeFileName);
            fileData = Files.readAllBytes(p);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileData;
    }

    private static String getCompleteFileName(String absolutePath, String fileName) {
        return absolutePath + File.separator + fileName;
    }

}
