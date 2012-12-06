package jd.ide.intellij.config;


import javax.swing.*;

public class JDPluginComponent {

    public static final String SHOW_METADATA_ATTRIBUTE = "displayMetadata";
    public static final String SHOW_LINE_NUMBERS_ATTRIBUTE = "displayLineNumbers";

    private boolean showLineNumbersEnabled = true;
    private boolean showMetadataEnabled = true;

    public void initComponent() {
    } // nop

    public void disposeComponent() {
    } // nop

    public String getComponentName() {
        return "Java Decompiler plugin";
    }

    public String getDisplayName() {
        return "Java Decompiler";
    }

    public Icon getIcon() {
        return null;
//        return IconLoader.getIcon("main/resources/icons/jd_64.png");
    }

    public String getHelpTopic() {
        return null;
    } // nop

    public void getState() {
//        Element jdConfiguration = new Element(JD_CONFIGURATION_CONFIG_ELEMENT);
//        jdConfiguration.setAttribute(SHOW_LINE_NUMBERS_ATTRIBUTE, String.valueOf(showLineNumbersEnabled));
//        jdConfiguration.setAttribute(SHOW_METADATA_ATTRIBUTE, String.valueOf(showMetadataEnabled));
//        return jdConfiguration;
    }

    public void loadState() {
//        String showLineNumbersStr = jdConfiguration.getAttributeValue(SHOW_LINE_NUMBERS_ATTRIBUTE);
//        if (StringUtils.isNotBlank(showLineNumbersStr)) {
//            showLineNumbersEnabled = Boolean.valueOf(showLineNumbersStr);
//        }
//        String showMetadataStr = jdConfiguration.getAttributeValue(SHOW_METADATA_ATTRIBUTE);
//        if (StringUtils.isNotBlank(showMetadataStr)) {
//            showMetadataEnabled = Boolean.valueOf(showMetadataStr);
//        }
    }

    public JComponent createComponent() {
        return new JComponent() {};
    }

    public boolean isModified() {
        return false;
    }

    public void apply() {
    }

    public void reset() {
    }

    public void disposeUIResources() {
    }

    public boolean isShowLineNumbersEnabled() {
        return showLineNumbersEnabled;
    }

    public void setShowLineNumbersEnabled(boolean showLineNumbersEnabled) {
        this.showLineNumbersEnabled = showLineNumbersEnabled;
    }

    public boolean isShowMetadataEnabled() {
        return showMetadataEnabled;
    }

    public void setShowMetadataEnabled(boolean showMetadataEnabled) {
        this.showMetadataEnabled = showMetadataEnabled;
    }

}
