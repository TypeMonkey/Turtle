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
    RAM_HEAP,
    RAM_STACK,
    RAM_OP_STACK,
    NO_HEAP,
    NO_OP_STACK,
    NO_F_STACK,
    IR_OUTPUT;
  }
  
  private final long maxHeapSize;
  private final Map<CLOption, String> options;
  private final String sourceFile;
  private final boolean printMeasure;
  
  /**
   * Constructs a CommandLineOption
   * @param maxHeapSize - the max heap size, in bytes
   * @param options - the Options and their arguments
   * @param sourceFile - the .t file to execute
   * @param printMeasure - whether to print the elapsed execution time after
   */
  public CommandLineOption(long maxHeapSize, Map<CLOption, String> options, String sourceFile, boolean printMeasure) {
    this.maxHeapSize = maxHeapSize;
    this.options = options;
    this.sourceFile = sourceFile;
    this.printMeasure = printMeasure;
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
   * Retrieves the option of whether to print the elapsed execution time.
   * @return the option of whether to print the elapsed execution time.
   */
  public boolean isPrintMeasure() {
    return printMeasure;
  }

  /**
   * A map containing the mapping of Option to their arguments
   * @return map containing the mapping of Option to their arguments
   */
  public Map<CLOption, String> getOptions() {
    return options;
  }
}
