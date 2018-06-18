package generated.CAEXFile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class ExternalInterface {

    @XmlAttribute(name = "Name")
    protected String name;
    @XmlAttribute(name = "RefBaseClassPath")
    protected String refBaseClassPath;
    @XmlAttribute(name = "ID")
    protected String id;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getRefBaseClassPath() {
	return refBaseClassPath;
    }

    public void setRefBaseClassPath(String refBaseClassPath) {
	this.refBaseClassPath = refBaseClassPath;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

}