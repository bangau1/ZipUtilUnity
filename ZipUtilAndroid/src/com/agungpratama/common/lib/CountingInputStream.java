package com.agungpratama.common.lib;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends FilterInputStream {
    private long totalBytesHasBeenRead = 0;

    public CountingInputStream(InputStream in) {
    	super(in);
    }

    public int getTotalBytesRead() {
    	return (int)totalBytesHasBeenRead;
    }

    @Override
    public int read() throws IOException {
    	int byteValue = super.read();
    	if (byteValue != -1) totalBytesHasBeenRead++;
    	return byteValue;
    }

    @Override
    public int read(byte[] b) throws IOException {
    	int bytesRead = super.read(b);
    	if (bytesRead != -1) totalBytesHasBeenRead+=bytesRead;
    	return bytesRead;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
    	int bytesRead = super.read(b,off,len);
    	if (bytesRead != -1) totalBytesHasBeenRead+=bytesRead;
    	return bytesRead;
    }
}