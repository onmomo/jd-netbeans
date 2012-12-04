package nb.decompiler;

import java.io.File;
import jd.ide.intellij.JavaDecompiler;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 * Java Decompiler Service.
 */
public class JavaDecompilerService {

    private static final JavaDecompilerService instance = new JavaDecompilerService();
    private final JavaDecompiler javaDecompiler;

    private JavaDecompilerService() {
        javaDecompiler = new JavaDecompiler();
    }

    public static synchronized JavaDecompilerService getInstance() {
        return instance;
    }

    public String decompile(FileObject fileObject) {
        try {
            final String internalClassname = fileObject.getPath();
            final FileObject parent = fileObject.getFileSystem().getRoot();
            final File jarFile = FileUtil.archiveOrDirForURL(parent.toURL());
            final FileObject jarFileObject = FileUtil.toFileObject(jarFile);
            if (FileUtil.isArchiveFile(jarFileObject)) {
                final String decompiled = javaDecompiler.decompile(jarFileObject.getPath(), internalClassname);
                 if (validContent(decompiled)) {
                    return decompiled;
                }
            } //else { handle non jar files? }
        } catch (FileStateInvalidException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "";
    }

    private static boolean validContent(String decompiled) {
        return decompiled != null && !decompiled.matches("(?sm)class\\s*\\{\\s*\\}.*");
    }
//    /**
//     * Simple utility class to iterate on possible path arguments
//     * for class files not in standard location inside the project.
//     * <p/>
//     * Produces {@link DecompilerPathArgs} types.
//     */
//    private static class DecompilerPathArgsFinder implements Iterable<DecompilerPathArgs> {
//        private final VirtualFile virtualFile;
//
//        public DecompilerPathArgsFinder(VirtualFile virtualFile) {
//            this.virtualFile = virtualFile;
//        }
//
//        @Override
//        public Iterator<DecompilerPathArgs> iterator() {
//            return new Iterator<DecompilerPathArgs>() {
//                private VirtualFile classPathRoot = virtualFile.getParent();
//
//                @Override
//                public boolean hasNext() {
//                    return classPathRootIsNotRootDirectory();
//                }
//
//                private boolean classPathRootIsNotRootDirectory() {
//                    return classPathRoot != null;
//                }
//
//                @Override
//                public DecompilerPathArgs next() {
//                    classPathRoot = classPathRoot.getParent();
//                    String internalClassName = VfsUtil.getRelativePath(virtualFile, classPathRoot, '/');
//                    return new DecompilerPathArgs(classPathRoot.getPresentableUrl(), internalClassName);
//                }
//
//                @Override
//                public void remove() {
//                    throw new UnsupportedOperationException("why the heck would you want to do that!");
//                }
//            };
//        }
//    }
//    /**
//     * Java Decompiler path arguments.
//     * <p/>
//     * Composed of the <em>base name</em> and the <em>qualified name</em>.
//     */
//    private static class DecompilerPathArgs {
//        private final String basePath;
//        private final String internalClassName;
//
//        public DecompilerPathArgs(String basePath, String internalClassName) {
//            this.basePath = basePath;
//            this.internalClassName = internalClassName;
//        }
//
//        public String getBasePath() {
//            return basePath;
//        }
//
//        public String getInternalClassName() {
//            return internalClassName;
//        }
//    }

    public static String getRelativePath(File file, File ancestor, char separator) {

        int length = 0;
        File parent = file;
        while (true) {
            if (parent == null) {
                return null;
            }
            if (parent.equals(ancestor)) {
                break;
            }
            if (length > 0) {
                length++;
            }
            length += parent.getName().length();
            parent = parent.getParentFile();
        }

        char[] chars = new char[length];
        int index = chars.length;
        parent = file;
        while (true) {
            if (parent.equals(ancestor)) {
                break;
            }
            if (index < length) {
                chars[--index] = separator;
            }
            String name = parent.getName();
            for (int i = name.length() - 1; i >= 0; i--) {
                chars[--index] = name.charAt(i);
            }
            parent = parent.getParentFile();
        }
        return new String(chars);
    }
}
