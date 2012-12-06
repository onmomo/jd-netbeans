
package nb.decompiler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.lang.model.element.Element;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.ClasspathInfo.PathKind;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.ui.ElementOpen;
import org.netbeans.modules.java.BinaryElementOpen;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 * Open the decompiled .java file for a .class file.
 */
@ServiceProvider(service = BinaryElementOpen.class)
public class BinaryElementOpenImpl extends org.netbeans.modules.java.classfile.BinaryElementOpenImpl implements BinaryElementOpen {
    
    private final JavaDecompilerRefreshSupportService javaDecompilerRefreshSupportService;

    public BinaryElementOpenImpl() {
        javaDecompilerRefreshSupportService = new JavaDecompilerRefreshSupportService();
    }

    @Override
    public boolean open(ClasspathInfo cpInfo, final ElementHandle<? extends Element> toOpen, final AtomicBoolean cancel) {
        if (isPluginActivated()) {
            final ClassPath classPath = cpInfo.getClassPath(PathKind.COMPILE);
            if (classPath != null) {
                final FileObject fileObject = classPath.findResource(toOpen.getQualifiedName().replace('.', '/') + ".class");
                if (fileObject != null) {
                    final String decompiledClass = JavaDecompilerService.getInstance().decompile(fileObject);
                    if (!decompiledClass.isEmpty()) {
                        try {
                            final FileSystem fs = FileUtil.createMemoryFileSystem();
                            final FileObject createData = fs.getRoot().createData(toOpen.getQualifiedName(), "java");
                            final OutputStream outputstream = createData.getOutputStream();
                            outputstream.write(decompiledClass.getBytes());
                            outputstream.close();
                            javaDecompilerRefreshSupportService.markDecompiled(fileObject);
                            return ElementOpen.open(createData, toOpen);
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
            
            return false;
        } else {
            return super.open(cpInfo, toOpen, cancel);
        }
    }

    private boolean isPluginActivated() {
        return true;
    }
}