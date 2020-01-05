package jg.cs.runtime.alloc.disc;

import java.io.File;
import java.io.RandomAccessFile;

public class Main {
  
  public static void main(String[] args) throws Exception {
    File file = new File("OP.ST");
    file.createNewFile();
    file.deleteOnExit();
    
    DiskArray array = new DiskArray(file, Long.BYTES);
    System.out.println(array.maxIndex());
    
    array.set(0, 10);
    System.out.println("---outer: size="+array.getByteSize()+" | "+array.maxIndex());
    array.set(5, 33);
    
    for (int i = 0; i <= array.maxIndex(); i++) {
      System.out.println("index="+i+" | "+array.get(i));
    }
    
    
    System.out.println("i = 3 | "+array.get(3));
    array.set(3, -60);
    System.out.println("i = 3 | "+array.get(3));
    
    for (int i = 0; i <= array.maxIndex(); i++) {
      System.out.println("index="+i+" | "+array.get(i));
    }
    
    array.get(10);
    
    //readRand(array.getFile());
  }
  
  public static void readRand(RandomAccessFile accessFile) throws Exception{
    System.out.println("---------------------");
    accessFile.seek(0);
    for (int i = 0 ; i < accessFile.length(); i += 8) {
      System.out.println("  index: "+accessFile.getFilePointer());
      System.out.println("["+i+"] "+accessFile.readLong());
    }
    System.out.println("---------------------");
  }
}
