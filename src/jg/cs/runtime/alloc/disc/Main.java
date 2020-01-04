package jg.cs.runtime.alloc.disc;

import java.io.File;
import java.io.RandomAccessFile;

public class Main {
  
  public static void main(String[] args) throws Exception {
    File file = new File("OP.ST");
    file.createNewFile();
    file.deleteOnExit();
    
    RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
    accessFile.setLength(0);
    
    System.out.println(accessFile.getFilePointer());
    accessFile.writeLong(10);
    System.out.println(accessFile.getFilePointer()); 
    accessFile.writeLong(6);
    System.out.println(accessFile.getFilePointer());
    
    readRand(accessFile);
    
    accessFile.seek(8);
    accessFile.writeLong(7);
    System.out.println(accessFile.getFilePointer());
    
    accessFile.seek(8);
    System.out.println("read: "+accessFile.readLong());
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
