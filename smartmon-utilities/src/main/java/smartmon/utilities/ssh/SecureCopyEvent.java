package smartmon.utilities.ssh;

public interface SecureCopyEvent {
  void copyProgress(SecureCopyParameters options, String source, String target,
                    long sourceSize, long count, boolean ended);
}
