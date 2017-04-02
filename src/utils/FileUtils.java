package utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

public class FileUtils {
	/**
	  * Checks if a file is gzipped.
	  * 
	  * @param f
	  * @return
	  */
	 public static boolean isGZipped(File f) {
	  int magic = 0;
	  try {
	   RandomAccessFile raf = new RandomAccessFile(f, "r");
	   magic = raf.read() & 0xff | ((raf.read() << 8) & 0xff00);
	   raf.close();
	  } catch (Throwable e) {
	   e.printStackTrace(System.err);
	  }
	  return magic == GZIPInputStream.GZIP_MAGIC;
	 }
}
