package jd.ide.intellij;

import org.openide.awt.StatusDisplayer;

/**
 * Java Decompiler tool, use native libs to achieve decompilation.
 * <p/>
 * <p>NB will try to load the matching lib in the module's lib directory</p>
 * @see http://bits.netbeans.org/dev/javadoc/org-openide-modules/org/openide/modules/doc-files/api.html#jni
 */
public class JavaDecompiler {
    
//    public static final String JD_NBLIB = "libjd-netbeans";
    public static final String JD_NBLIB = "jd-netbeans";

    public JavaDecompiler() {
        try {
            // NB loads the matching JNI lib from cluster/modules/lib/os_arch/libjd-netbeans.* automatically
            System.loadLibrary(JD_NBLIB);
            StatusDisplayer.getDefault().setStatusText("Hurray, Library loaded!!");
        } catch (Exception e) {
            throw new IllegalStateException("Something went wrong when loading the Java Decompiler native lib", e);
        }
    }

    /**
     * Actual call to the native lib.
     *
     * @param basePath Path to the root of the classpath, either a path to a directory or a path to a jar file.
     * @param internalClassName internal name of the class.
     * @return Decompiled class text.
     */
    public native String decompile(String basePath, String internalClassName);
}
