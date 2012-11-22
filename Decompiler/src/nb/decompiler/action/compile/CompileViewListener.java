package nb.decompiler.action.compile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Source",
id = "nb.decompiler.action.compile.CompileViewListener")
@ActionRegistration(
    iconBase = "nb/decompiler/action/compile/jd-src.png",
displayName = "#CTL_CompileViewListener")
@ActionReference(path = "Toolbars/Build", position = -120)
@Messages("CTL_CompileViewListener=Compile View")
public final class CompileViewListener implements ActionListener {

    private final DataObject context;

    public CompileViewListener(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
