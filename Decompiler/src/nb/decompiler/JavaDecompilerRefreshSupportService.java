package nb.decompiler;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import org.openide.filesystems.FileObject;

/**
 * This service offers support to refresh the content of decompiled files on configuration change.
 */
public class JavaDecompilerRefreshSupportService {
    private ConcurrentHashMap<WeakReference<FileObject>, WeakReference<FileObject>> decompiledFiles =
            new ConcurrentHashMap<WeakReference<FileObject>, WeakReference<FileObject>>();

    public void markDecompiled(FileObject fileObject) {
        WeakReference<FileObject> weakRef = new WeakReference<FileObject>(fileObject);
        decompiledFiles.put(weakRef, weakRef);
    }


    public void refreshDecompiledFiles() {
//        LaterInvocator.invokeLater(new RefreshDecompiledFilesTask());
    }

//    private class RefreshDecompiledFilesTask implements Runnable {
//        @Override public void run() {
//            FileDocumentManager documentManager = FileDocumentManager.getInstance();
//
//            // need lock ?
//            HashSet<WeakReference<VirtualFile>> weakReferences =
//                    new HashSet<WeakReference<VirtualFile>>(decompiledFiles.keySet());
//            decompiledFiles.clear();
//
//            for (WeakReference<VirtualFile> virtualFileWeakReference : weakReferences) {
//                VirtualFile virtualFile = virtualFileWeakReference.get();
//                if (virtualFile != null) {
//                    System.out.println("contentsChanged on : " + virtualFile.getPresentableUrl());
//                    ((VirtualFileListener) documentManager).contentsChanged(
//                            new VirtualFileEvent(null, virtualFile, virtualFile.getName(), virtualFile.getParent())
//                    );
//                }
//
//            }
//        }
//    }
}
