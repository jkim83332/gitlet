package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * contents class for Gitlet, the tiny stupid version-control system.
 *
 * @author Jeongsu Kim
 */
public class Blob implements Serializable {
    /**
     * bytecontents.
     */
    private byte[] _bytecontents;
    /**
     * stringcontents.
     */
    private String _stringcontents;
    /**
     * name.
     */
    private String _name;
    /**
     * hashcode.
     */
    private String _hashCode;
    /**
     * object.
     */
    private List<Object> _object = new ArrayList<>();

    /**
     * contructor.
     */
    Blob() {
    }

    /**
     * one argument constructor.
     *
     * @param save save
     */

    Blob(String save) {
        File file = new File(save);
        _name = file.getName();
        _stringcontents = Utils.readContentsAsString(file);
        _bytecontents = Utils.readContents(file);
        _object.add(_name);
        _object.add(_bytecontents);
        _object.add(_stringcontents);
        _hashCode = Utils.sha1(_object);
    }

    /**
     * return Hashcode.
     */
    public String hCode() {
        return _hashCode;
    }

    /**
     * return name.
     */
    public String name() {
        return _name;
    }

    /**
     * return stringcontents.
     */
    public String gstringcontents() {
        return _stringcontents;
    }

}
