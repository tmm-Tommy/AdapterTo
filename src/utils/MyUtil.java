package utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.awt.RelativePoint;
import entity.Element;

import java.util.ArrayList;

/**
 * @Author Tommy
 * 2021/1/25
 */
public class MyUtil {
    public static ArrayList<Element> getIDsFromLayout(final PsiFile file, final ArrayList<Element> elements) {
        file.accept(new XmlRecursiveElementVisitor() {

            @Override
            public void visitElement(final PsiElement element) {
                super.visitElement(element);
                //解析XML标签
                if (element instanceof XmlTag) {
                    XmlTag tag = (XmlTag) element;
                    //解析include标签
                    if (tag.getName().equalsIgnoreCase("include")) {
                        XmlAttribute layout = tag.getAttribute("layout", null);

                        if (layout != null) {
                            Project project = file.getProject();
//                            PsiFile include = findLayoutResource(file, project, getLayoutName(layout.getValue()));
                            PsiFile include = null;
                            PsiFile[] mPsiFiles = FilenameIndex.getFilesByName(project, getLayoutName(layout.getValue())+".xml", GlobalSearchScope.allScope(project));
                            if (mPsiFiles.length>0){
                                include = mPsiFiles[0];
                            }

                            if (include != null) {
                                getIDsFromLayout(include, elements);

                                return;
                            }
                        }
                    }

                    // get element ID
                    XmlAttribute id = tag.getAttribute("android:id", null);
                    if (id == null) {
                        return; // missing android:id attribute
                    }
                    String value = id.getValue();
                    if (value == null) {
                        return; // empty value
                    }

                    // check if there is defined custom class
                    String name = tag.getName();
                    XmlAttribute clazz = tag.getAttribute("class", null);
                    if (clazz != null) {
                        name = clazz.getValue();
                    }

                    try {
                        entity.Element e = new entity.Element(name, value, tag);
                        elements.add(e);
                    } catch (IllegalArgumentException e) {
                        // TODO log
                    }
                }
            }
        });


        return elements;
    }

    public static String getLayoutName(String layout) {
        if (layout == null || !layout.startsWith("@") || !layout.contains("/")) {
            return null; // it's not layout identifier
        }

        String[] parts = layout.split("/");
        if (parts.length != 2) {
            return null; // not enough parts
        }

        return parts[1];
    }
    public static void showNotification(Project project, MessageType type, String text) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(text, type, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }
    public static String formatMyFileName(String XMLFileName){
        StringBuilder s = new StringBuilder();
        //大写开头的驼峰格式化-因为是类
        boolean isLog = true;
        for (int i = 0; i < XMLFileName.length(); i++) {
            if (XMLFileName.charAt(i)=='_'){
                isLog = true;
                continue;
            }
            if (isLog&&XMLFileName.charAt(i)>='a'&&XMLFileName.charAt(i)<='z'){
                s.append((char) (XMLFileName.charAt(i)-32));
                isLog = false;
            }else {
                s.append(XMLFileName.charAt(i));
            }
        }
        return  s.toString();
    }
}
