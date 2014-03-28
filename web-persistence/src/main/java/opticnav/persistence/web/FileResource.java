package opticnav.persistence.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.activation.MimeType;

public class FileResource extends Resource {
    private File file;

    public FileResource(MimeType mimeType, File file) {
        super(mimeType);
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }
}
