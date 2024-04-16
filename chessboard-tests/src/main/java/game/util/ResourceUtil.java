package game.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ResourceUtil {

	private ResourceUtil() {
	}
	
	public static OutputStream openCompressedBase64OutputStream(Path path) throws IOException {
		OutputStream fos = new FileOutputStream(path.toFile());
	    
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    
	    GZIPOutputStream gzipOut = new GZIPOutputStream(bos);
	    
	    OutputStream base64Out = Base64.getEncoder().wrap(gzipOut);

	    return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                base64Out.write(b);
            }

            @Override
            public void close() throws IOException {
                try {
                    base64Out.close();
                } catch (IOException e) {
                    try {
                        gzipOut.close();
                    } finally {
                        bos.close();
                        fos.close();
                    }
                    throw e;
                }
            }

            @Override
            public void flush() throws IOException {
                base64Out.flush();
            }
        };
	}
	
	 public static InputStream openDecompressedBase64InputStream(Path path) throws IOException {
	        InputStream fis = new FileInputStream(path.toFile());
	        InputStream bis = new BufferedInputStream(fis);
	        InputStream base64In = Base64.getDecoder().wrap(bis);
	        GZIPInputStream gzipIn = new GZIPInputStream(base64In);

	        return new InputStream() {
	            @Override
	            public int read() throws IOException {
	                return gzipIn.read();
	            }

	            @Override
	            public int read(byte[] b, int off, int len) throws IOException {
	                return gzipIn.read(b, off, len);
	            }

	            @Override
	            public void close() throws IOException {
	                try {
	                    gzipIn.close();
	                } catch (IOException e) {
	                    bis.close();
	                    fis.close();
	                    throw e;
	                }
	            }
	        };
	    }
}
