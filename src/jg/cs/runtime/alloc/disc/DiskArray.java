package jg.cs.runtime.alloc.disc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class DiskArray {
  
  private final long dataSize;
  
  private final RandomAccessFile array;
  
  public DiskArray(File storage,long dataSize) throws IOException {
    this.dataSize = dataSize;
    array = new RandomAccessFile(storage, "rwd");
    array.setLength(0);
  }

  public Long get(long index) throws IOException {
    if (index > maxIndex() || index < 0) {
      throw new IllegalArgumentException("Invalid index: "+index);
    }
    array.seek(index * dataSize);
    return array.readLong();
  }
  
  private void growArray(long desiredFarthestIndex) throws IOException {    
    long currentIndex = maxIndex() == -1 ? 0 : maxIndex() + dataSize ;
    //System.out.println("GROWING: MAX INDEX PRIOR: "+currentIndex+" | SEEKING AT: "+(currentIndex * dataSize));
    array.seek(currentIndex * dataSize);

    while (currentIndex < desiredFarthestIndex) {
      array.write(ByteBuffer.allocate((int) dataSize).putLong(0).array());
      currentIndex ++;
    }
    //System.out.println("GROWING: MAX INDEX NOW: "+currentIndex);
  }
  
  public void set(long index, long value) throws IOException {
    //System.out.println("---INSERTING val="+value+" at index="+index);
    
    array.seek(index * dataSize);
    array.writeLong(value);
  }
  
  public long maxIndex() throws IOException {
    return (getByteSize() / dataSize) - 1;
  }
  
  public long getByteSize() throws IOException {
    return array.length();
  }
  
  public RandomAccessFile getFile(){
    return array;
  }
}
