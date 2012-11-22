package nb.decompiler.action.decompile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.text.Document;
import nb.decompiler.JavaDecompilerService;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.Task;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Source",
id = "nb.decompiler.action.decompile.DecompileViewListener")
@ActionRegistration(
    iconBase = "nb/decompiler/action/decompile/jd.png",
displayName = "#CTL_DecompileViewListener")
@ActionReference(path = "Toolbars/Build", position = -20)
@Messages("CTL_DecompileViewListener=Decompile View")
public final class DecompileViewListener implements ActionListener {

    private final DataObject context;
    private final JavaDecompilerService javaDecompilerService;
    private ClassPath classpath;

    public DecompileViewListener(DataObject context) {
        this.context = context;
        javaDecompilerService = JavaDecompilerService.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        context.getNodeDelegate();
        final FileObject fileObject = context.getPrimaryFile();
        final JavaSource javaSource = JavaSource.forFileObject(fileObject); 
        classpath = javaSource.getClasspathInfo().getClassPath(ClasspathInfo.PathKind.COMPILE);
        
        if (javaSource == null) {
            StatusDisplayer.getDefault().setStatusText("Not a Java file: " + fileObject.getPath());
        } else {
            try {
                javaSource.runUserActionTask(new Task<CompilationController>() {

                    @Override
                    public void run(final CompilationController compilationController) throws Exception {
                        compilationController.toPhase(Phase.ELEMENTS_RESOLVED);
                        
                        ClassPath classPath = ClassPath.getClassPath(fileObject, ClassPath.COMPILE);
                        FileObject[] roots = classPath.getRoots();
                        for (FileObject fileObject1 : roots) {
                            System.out.println(fileObject1.getPath());
                        }
                        Document document = compilationController.getDocument();
                        if (document != null) {
                            StatusDisplayer.getDefault().setStatusText("Hurray, the Java file is open!");
                            String decompile = javaDecompilerService.decompile(fileObject);
                            System.out.println(decompile);
                        } else {
                            StatusDisplayer.getDefault().setStatusText("The Java file is closed!");
                            String decompile = javaDecompilerService.decompile(fileObject);
                            System.out.println(decompile);
                        }
                    }
                }, true);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
