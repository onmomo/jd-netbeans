package nb.decompiler;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * This service offers support to refresh the content of decompiled files on configuration change.
 */
@ServiceProvider(service=JavaDecompilerRefreshSupportService.class)
public class JavaDecompilerRefreshSupportService {
    private ConcurrentHashMap<WeakReference<FileObject>, WeakReference<FileObject>> decompiledFiles =
            new ConcurrentHashMap<WeakReference<FileObject>, WeakReference<FileObject>>();

    public void markDecompiled(FileObject fileObject) {
        WeakReference<FileObject> weakRef = new WeakReference<FileObject>(fileObject);
        decompiledFiles.put(weakRef, weakRef);
    }

}
