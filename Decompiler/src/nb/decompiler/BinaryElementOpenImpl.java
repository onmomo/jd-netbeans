package nb.decompiler;

import com.sun.source.tree.CompilationUnitTree;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.ClasspathInfo.PathKind;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.ModificationResult;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.WorkingCopy;
import org.netbeans.modules.java.BinaryElementOpen;
import org.netbeans.modules.java.source.indexing.JavaIndex;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * Open the decompiled .java file for a .class file.
 */
@ServiceProvider(service = BinaryElementOpen.class)
public class BinaryElementOpenImpl extends org.netbeans.modules.java.classfile.BinaryElementOpenImpl implements BinaryElementOpen {

    private static final String DISABLE_ERRORS = "disable-java-errors"; //NOI18N
    private static final Logger LOG = Logger.getLogger(BinaryElementOpenImpl.class.getName());
    private final JavaDecompilerRefreshSupportService javaDecompilerRefreshSupportService;
    private final JavaDecompilerService javaDecompilerService;

    public BinaryElementOpenImpl() {
        javaDecompilerRefreshSupportService = Lookup.getDefault().lookup(JavaDecompilerRefreshSupportService.class);
        javaDecompilerService = Lookup.getDefault().lookup(JavaDecompilerService.class);
    }

    @Override
    public boolean open(ClasspathInfo cpInfo, final ElementHandle<? extends Element> toOpen, final AtomicBoolean cancel) {
        if (isPluginActivated()) {
            final ClassPath classPath = cpInfo.getClassPath(PathKind.COMPILE);
            if (classPath != null) {
                final FileObject fileObject = classPath.findResource(toOpen.getQualifiedName().replace('.', '/') + ".class");
                if (fileObject != null) {
                    final String decompiledClass = javaDecompilerService.decompile(fileObject);
                    if (!decompiledClass.isEmpty()) {
                        try {
                            FileObject decompiledRootFolder = FileUtil.createMemoryFileSystem().getRoot().createFolder("decompiledClasses");
                            final FileObject createData = decompiledRootFolder.createData(toOpen.getBinaryName(), "java");
                            final OutputStream outputstream = createData.getOutputStream();

                            try {
                                FileUtil.copy(new ByteArrayInputStream("".getBytes("UTF-8")), outputstream); //NOI18N
                                outputstream.write(decompiledClass.getBytes());
                                createData.setAttribute(DISABLE_ERRORS, true);
                            } finally {
                                outputstream.close();
                            }

                            final DataObject dataObject = DataObject.find(createData);
                            final OpenCookie openCookie = (OpenCookie) dataObject.getLookup().lookup(OpenCookie.class);

                            if (openCookie != null) {
                                openCookie.open();
                                javaDecompilerRefreshSupportService.markDecompiled(fileObject);
                                return true;
                            } else {
                                LOG.warning("Couldn't open decompiled file in editor");
                                return false;
                            }
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }

            return false;
        } else {
            // Well.. there must be an other to do this than extend from org.netbeans.modules.java.classfile.BinaryElementOpenImpl...
            return super.open(cpInfo, toOpen, cancel);
        }
    }

    private boolean isPluginActivated() {
        return true;
    }
}