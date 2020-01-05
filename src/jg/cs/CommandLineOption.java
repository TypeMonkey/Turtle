package jg.cs;

import java.util.Map;

/**
 * Holds command line options that dictate
 * runtime behavior
 * @author Jose
 */
public class CommandLineOption {

  /**
   * Command line options
   * @author Jose
   */
  public enum CLOption{
    DISK_HEAP,
    DISK_OP_STACK,
    DISK_F_STACK,
    IR_OUTPUT;
  }
  
  private final long maxHeapSize;
  private final Map<CLOption, String> options;
  private final String sourceFile;
  
  /**
   * Constructs a CommandLineOption
   * @param maxHeapSize - the max heap size, in bytes
   * @param options - the Options and their arguments
   * @param sourceFile - the .t file to execute
   */
  public CommandLineOption(long maxHeapSize, Map<CLOption, String> options, String sourceFile) {
    this.maxHeapSize = maxHeapSize;
    this.options = options;
    this.sourceFile = sourceFile;
  }
  
  /**
   * Retrieves the String argument associated with this Option
   * @param option - the Option to use
   * @return the String argument associated with this Option, or null if the Option is not mapped
   */
  public String getValue(CLOption option) {
    return options.get(option);
  }

  /**
   * The max size of the heap, in bytes
   * @return The max size of the heap, in bytes
   */
  public long getMaxHeapSize() {
    return maxHeapSize;
  }

  /**
   * The source file to execute
   * @return The source file to execute
   */
  public String getSourceFile() {
    return sourceFile;
  }
  
  /**
   * A map containing the mapping of Option to their arguments
   * @return map containing the mapping of Option to their arguments
   */
  public Map<CLOption, String> getOptions() {
    return options;
  }
}
