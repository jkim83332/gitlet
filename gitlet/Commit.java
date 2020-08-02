package gitlet;


import java.io.Serializable;
import java.util.List;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * commit class for Gitlet, the tiny stupid version-control system.
 *
 * @author Jeongsu Kim
 */
public class Commit implements Serializable {
    /**
     * blobs list.
     */
    private List<Blob> _blobs;
    /**
     * message.
     */
    private String _message;
    /**
     * parent.
     */
    private String _parent;
    /**
     * parent.
     */
    private String _parent2;
    /**
     * hashcode.
     */
    private String _hashCode;
    /**
     * bytetime.
     */
    private Date _btimestamp;
    /**
     * timestamp.
     */
    private String _timestamp;
    /**
     * mergeid.
     */
    private String mergeid = "";
    /**
     * blobstring.
     */
    private String blobstring = "";

    /**
     * staging area.
     */
    private LinkedHashMap<String, Blob> _staging = new LinkedHashMap<>();
    /**
     * commit blobs.
     */
    private LinkedHashMap<String, Blob> _cblob = new LinkedHashMap<>();

    /**
     * temptemp.
     */
    private LinkedHashMap<String, Blob> temptemp = new LinkedHashMap<>();

    /**
     * initial constructor.
     */
    Commit() {
        _message = "initial commit";
        _btimestamp = new Date(0);
        _parent = "";
        _timestamp = _btimestamp.toString().replace(" PST", "");
        _timestamp = _timestamp + " -0800";
        _hashCode = Utils.sha1(new Object[]{_parent + _message + blobstring});
    }

    /**
     * arguments instructor.
     *
     * @param message   meessage
     * @param timestamp timestamp
     * @param parent    parent
     * @param blobs     blobs
     * @param staging   staging
     * @param pblobs    pblobs
     * @param removed   removed
     */
    Commit(String message, Date timestamp, String parent, List<Blob> blobs,
           LinkedHashMap<String, Blob> staging,
           LinkedHashMap<String, Blob> pblobs,
           List<String> removed) {
        _message = message;
        _timestamp = timestamp.toString().replace(" PST", "");
        _timestamp = _timestamp + " -0800";
        _parent = parent;
        _blobs = blobs;
        _staging = staging;
        _cblob.putAll(pblobs);

        for (int z = 0; z < removed.size(); z++) {
            if (_cblob.containsKey(removed.get(z))) {
                _cblob.remove(removed.get(z));
            }
        }


        if (_blobs.size() > 0) {
            for (int i = 0; i < blobs.size(); i++) {
                if (_staging.containsKey(_blobs.get(i).hCode())) {
                    blobstring = blobstring + blobs.get(i).hCode();
                }
            }
        }

        temptemp.putAll(staging);
        Set<String> compare1 = temptemp.keySet();
        for (String k : compare1) {
            Blob value = staging.get(k);
            for (int i = 0; i < _blobs.size(); i++) {
                final boolean isA = _blobs.get(i).hCode().equals(value.hCode());
                final boolean isB = !_cblob.containsValue(value);


                if (isA && isB) {
                    _cblob.put(value.name(), _blobs.get(i));
                    staging.remove(_blobs.get(i).hCode());
                }

                if (isA) {
                    staging.remove(_blobs.get(i).hCode());
                }
            }

        }

        while (removed.size() > 0) {
            removed.remove(0);
        }
        _hashCode = Utils.sha1(new Object[]{_parent + _message + blobstring});
    }

    /**
     * arguments instructor.
     *
     * @param message   meessage
     * @param timestamp timestamp
     * @param parent    parent
     * @param parent2   parent2
     * @param blobs     blobs
     * @param staging   staging
     * @param pblobs    pblobs
     * @param removed   removed
     */
    Commit(String message, Date timestamp, String parent, String parent2, List<Blob> blobs, LinkedHashMap<String, Blob> staging,
           LinkedHashMap<String, Blob> pblobs,
           List<String> removed) {
        _message = message;
        _timestamp = timestamp.toString().replace(" PST", "");
        _timestamp = _timestamp + " -0800";
        _parent = parent;
        _parent2 = parent2;
        _blobs = blobs;
        _staging = staging;
        _cblob.putAll(pblobs);


        mergeid = parent.substring(0, 7) + " " + parent2.substring(0, 7);


        for (int z = 0; z < removed.size(); z++) {
            if (_cblob.containsKey(removed.get(z))) {
                _cblob.remove(removed.get(z));
            }
        }


        if (_blobs.size() > 0) {
            for (int i = 0; i < blobs.size(); i++) {
                if (_staging.containsKey(_blobs.get(i).hCode())) {
                    blobstring = blobstring + blobs.get(i).hCode();
                }
            }
        }

        temptemp.putAll(staging);
        Set<String> compare1 = temptemp.keySet();
        for (String k : compare1) {
            Blob value = staging.get(k);
            for (int i = 0; i < _blobs.size(); i++) {
                if (_blobs.get(i).hCode().equals(value.hCode())) {
                    if (!_cblob.containsValue(value)) {
                        _cblob.put(value.name(), _blobs.get(i));
                        staging.remove(_blobs.get(i).hCode());
                    }
                    staging.remove(_blobs.get(i).hCode());
                }
            }

        }
        for (int i = 0; i < removed.size(); i++) {
            removed.remove(i);
        }
        _hashCode = Utils.sha1(new Object[]{_parent
                + _parent2 + _message + blobstring});
    }


    /**
     * return hash.
     */
    public String hash() {
        return _hashCode;
    }

    /**
     * return message.
     */
    public String message() {
        return _message;
    }

    /**
     * return timestamp.
     */
    public String timestamp() {
        return _timestamp;
    }

    /**
     * return parent.
     */
    public String parent() {
        return _parent;
    }

    /**
     * return parent.
     */
    public String parent2() {
        return _parent2;
    }

    /**
     * return  cblobs.
     */
    public LinkedHashMap<String, Blob> cblob() {
        return _cblob;
    }

    /**
     * return  mergeid.
     */
    public String mergeid() {
        return mergeid;
    }

    /**
     * return  mergecheck.
     */
    public boolean mergecheck() {
        return !mergeid.equals("");
    }


}
