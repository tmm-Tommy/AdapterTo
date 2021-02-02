package entity;

import com.intellij.psi.xml.XmlTag;

/**
 * @Author Tommy
 * 2021/1/25
 */
public class Element {
    String name;
    String value;
    XmlTag tag;

    public Element(String name, String value, XmlTag tag) {
        this.name = name;
        this.value = value;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public XmlTag getTag() {
        return tag;
    }

    public void setTag(XmlTag tag) {
        this.tag = tag;
    }
}
