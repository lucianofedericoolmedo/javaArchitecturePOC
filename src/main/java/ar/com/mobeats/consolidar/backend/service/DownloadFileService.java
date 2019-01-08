package ar.com.mobeats.consolidar.backend.service;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.util.FileUtil;

@Service
public class DownloadFileService {

    @Autowired
    private FileUtil fileUtil;

    public StreamingOutput openFile(final String path) {

        return new StreamingOutput() {

            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                byte[] out = fileUtil.openFile(path);
                output.write(out);
            }
        };
    }

}
