package com.gyx.superscheduled.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class IncObjectOutputStream extends ObjectOutputStream {
    private File file;

    public IncObjectOutputStream(File file) throws IOException, SecurityException {
        super(new FileOutputStream(file, true));
        this.file = file;
    }

    @Override
    public void writeStreamHeader() throws IOException {
        if (file != null) {
            if (file.length() == 0) {
                //存在文件，但是文件里面内容为空
                super.writeStreamHeader();
            }
        } else {
            //文件不存在
            super.writeStreamHeader();
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
