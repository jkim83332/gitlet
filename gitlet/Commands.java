package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Formatter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;


/**
 * commands class for Gitlet, the tiny stupid version-control system.
 *
 * @author Jeongsu Kim
 */
public class Commands implements Serializable {
    /**
     * bytecontents.
     */
    private File _gitlet = new File(".gitlet");

    /**
     * commit.
     */
    private Commit _commit;
    /**
     * blobs list.
     */
    private List<Blob> _blobs = new ArrayList<>();
    /**
     * pblobs.
     */
    private LinkedHashMap<String, Blob> _pblobs = new LinkedHashMap<>();
    /**
     * cblos.
     */
    private LinkedHashMap<String, Blob> _cblobs = new LinkedHashMap<>();
    /**
     * cblos.
     */
    private LinkedHashMap<String, Blob> _tempblobs = new LinkedHashMap<>();
    /**
     * commits.
     */
    private LinkedHashMap<String, Commit> commits = new LinkedHashMap<>();
    /**
     * removed.
     */
    private List<String> removed = new ArrayList<>();
    /**
     * branch.
     */
    private LinkedHashMap<String, String> branch = new LinkedHashMap<>();
    /**
     * staging.
     */
    private LinkedHashMap<String, Blob> staging = new LinkedHashMap<>();
    /**
     * staging.
     */
    private List<String> stagingname = new ArrayList<>();
    /**
     * keyvalue.
     */
    private List<String> keyvalue = new ArrayList<>();

    /**
     * bclists.
     */
    private List<Commit> bclists = new ArrayList<>();

    /**
     * message.
     */
    private String _message;
    /**
     * count.
     */
    private int _count = 1;
    /**
     * id.
     */
    private String _id;
    /**
     * current.
     */
    private String _current;
    /**
     * operands.
     */
    private String[] _operands;
    /**
     * command.
     */
    private String _command;
    /**
     * operand.
     */
    private String _operand;

    /**
     * givenb.
     */
    private List<String> givenb = new ArrayList<>();

    /**
     * currb.
     */
    private List<String> currenb = new ArrayList<>();
    /**
     * currdir.
     */
    private List<String> currdir = new ArrayList<>();
    /**
     * copydir.
     */
    private List<String> copydir = new ArrayList<>();
    /**
     * untracked.
     */
    private List<String> untracked = new ArrayList<>();
    /**
     * modi.
     */
    private List<String> mmodifications = new ArrayList<>();
    /**
     * modi.
     */
    private String s1;
    /**
     * modi.
     */
    private String s2;
    /**
     * modi.
     */
    private String split;
    /**
     * modi.
     */
    private LinkedHashMap<String, Blob> ctemp = new LinkedHashMap<>();
    /**
     * modi.
     */
    private LinkedHashMap<String, Blob> gtemp = new LinkedHashMap<>();
    /**
     * modi.
     */
    private LinkedHashMap<String, Blob> stemp = new LinkedHashMap<>();
    /**
     * modi.
     */
    private boolean _mergecheck = false;
    /**
     * modi.
     */
    private LinkedHashMap<String, Blob> currblobs = new LinkedHashMap<>();
    /**
     * modi.
     */
    private LinkedHashMap<String, Blob> givenblobs = new LinkedHashMap<>();
    /**
     * modi.
     */
    private List<String> cmergeb = new ArrayList<>();
    /**
     * modi.
     */
    private List<String> gmergeb = new ArrayList<>();
    /**
     * modi.
     */
    private List<String> fastest = new LinkedList<>();
    /**
     * modi.
     */
    private LinkedHashMap<String, Blob> splitblobs = new LinkedHashMap<>();

    /**
     * one argument constructor.
     *
     * @param args args
     */
    Commands(String[] args) {
        _command = args[0];
        _operands = new String[args.length - 1];
        for (int i = 0; i < _operands.length; i++) {
            _operands[i] = args[i + 1];
        }

        File gitlet = new File(".gitlet/gitlet");


        if (gitlet.exists() && _command.equals("init")) {
            Utils.message("A Gitlet version-control "
                    + "system already exists in the current directory.");
            System.exit(0);
        }
        if (!gitlet.exists() && !_command.equals("init")) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (gitlet.exists()) {
            Commands obj;
            try {
                ObjectInputStream in =
                        new ObjectInputStream(new FileInputStream(gitlet));
                obj = (Commands) in.readObject();
                in.close();
                makecopy(obj);
            } catch (IOException | ClassNotFoundException exception) {
                System.out.println(exception);
            }
        }

    }

    /**
     * makecopy.
     *
     * @param commands commands
     */
    public void makecopy(Commands commands) {

        _commit = commands._commit;
        _blobs = commands._blobs;
        commits = commands.commits;
        branch = commands.branch;
        staging = commands.staging;
        keyvalue = commands.keyvalue;
        _message = commands._message;
        _count = commands._count;
        _id = commands._id;
        _current = commands._current;
        removed = commands.removed;
    }

    /**
     * one command executor.
     */
    public void exeCommnads() {
        _gitlet.mkdir();
        if (_command.equals("init")) {
            nooperand();
            init();

        } else if (_command.equals("add")) {
            oneoperand();
            add(_operands[0]);

        } else if (_command.equals("commit")) {
            oneoperand();
            commit(_operands[0]);

        } else if (_command.equals("rm")) {
            oneoperand();
            rm(_operands[0]);
        } else if (_command.equals("log")) {
            nooperand();
            log();
        } else if (_command.equals("global-log")) {
            nooperand();
            glog();
        } else if (_command.equals("find")) {
            oneoperand();
            find(_operands[0]);
        } else if (_command.equals("status")) {
            nooperand();
            status();
        } else if (_command.equals("checkout")) {
            if (_operands.length > 3) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            } else {
                checkout(_operands);
            }
        } else if (_command.equals("branch")) {
            oneoperand();
            branch(_operands[0]);
        } else if (_command.equals("rm-branch")) {
            oneoperand();
            rmbranch(_operands[0]);
        } else if (_command.equals("reset")) {
            oneoperand();
            reset(_operands[0]);
        } else if (_command.equals("merge")) {
            oneoperand();
            merge(_operands[0]);
        } else {
            if (commits.size() == 0) {
                Utils.message("Not in an initialized Gitlet directory.");
                System.exit(0);
            }
            Utils.message("No command with that name exists.");
            System.exit(0);
        }
    }

    /**
     * nooperand helper.
     */
    private void nooperand() {
        if (_operands.length > 0) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        }
    }

    /**
     * oneoperand helper.
     */
    private void oneoperand() {
        if (!(_operands.length == 1)) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        }
    }

    /**
     * init.
     */
    private void init() {
        commits = new LinkedHashMap<>();
        branch = new LinkedHashMap<>();
        staging = new LinkedHashMap<>();
        _commit = new Commit();
        keyvalue.add(_commit.hash());
        commits.put(_commit.hash(), _commit);
        _id = _commit.hash();
        branch.put("master", _id);
        _current = "master";
    }

    /**
     * add.
     *
     * @param operand operand
     */
    private void add(String operand) {
        _operand = operand;
        File file = new File(operand);
        if (!file.exists()) {
            Utils.message("File does not exist.");
            System.exit(0);
        }
        Blob temp = new Blob(operand);
        _cblobs = _commit.cblob();
        boolean check = false;
        for (int i = 0; i < removed.size(); i++) {
            if (temp.name().equals(removed.get(i))) {
                removed.remove(i);
            }
        }
        if (_cblobs.containsKey(temp.name())) {
            if (_cblobs.get(temp.name()).hCode().equals(temp.hCode())) {
                if (staging.containsKey(temp.hCode())) {
                    staging.remove(temp.hashCode());
                }
                return;
            }
        }
        Set<String> compare1 = staging.keySet();
        Iterator<String> iter = compare1.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Blob value = staging.get(key);
            if (value.name().equals(temp.name())) {
                staging.replace(temp.hCode(), temp);
                if (_blobs.size() == 0) {
                    _blobs.add(temp);
                } else {
                    for (int i = 0; i < _blobs.size(); i++) {
                        if (_blobs.get(i).hCode().equals(temp.hCode())) {
                            check = true;
                        }
                    }
                    if (!check) {
                        _blobs.add(temp);
                    }
                }
                return;
            }
        }
        staging.put(temp.hCode(), temp);
        if (_blobs.size() == 0) {
            _blobs.add(temp);
        } else {
            for (int i = 0; i < _blobs.size(); i++) {
                if (_blobs.get(i).hCode().equals(temp.hCode())) {
                    check = true;
                }
            }
            if (!check) {
                _blobs.add(temp);
            }
        }
    }

    /**
     * commit.
     *
     * @param message message
     */
    private void commit(String message) {
        _message = message;
        if (_message.equals("")) {
            Utils.message("Please enter a commit message.");
            System.exit(0);
        }
        _pblobs.putAll(commits.get(_commit.hash()).cblob());
        for (int i = 0; i < removed.size(); i++) {
            if (_pblobs.containsKey(removed.get(i))) {
                _pblobs.remove(removed.get(i));
            }
        }
        if (staging.size() > 0 || removed.size() != 0) {
            _commit = new Commit(_message, new Date(),
                    _commit.hash(), _blobs, staging, _pblobs, removed);
            keyvalue.add(_commit.hash());
            commits.put(_commit.hash(), _commit);
            _id = _commit.hash();
            branch.replace(_current, _id);
        } else {
            Utils.message("No changes added to the commit.");
            System.exit(0);
        }
    }

    /**
     * remove.
     *
     * @param operand operand
     */
    private void rm(String operand) {
        File file1 = new File(operand);
        _cblobs = _commit.cblob();
        if (!file1.exists()) {
            if (_cblobs.containsKey(operand)) {
                removed.add(operand);
                return;
            }
        }
        Blob temp = new Blob(operand);
        if (!staging.containsKey(temp.hCode())
                && !_cblobs.containsKey(temp.name())) {
            Utils.message("No reason to remove the file.");
            System.exit(0);
        }

        if (staging.containsKey(temp.hCode())) {
            staging.remove(temp.hCode());
        }


        if (_cblobs.containsKey(temp.name())) {
            if (_cblobs.get(temp.name()).hCode().equals(temp.hCode())) {
                removed.add(temp.name());
                File file = new File(temp.name());
                Utils.restrictedDelete(file);
            }
        }
    }

    /**
     * log.
     */
    private void log() {
        loghelper(_commit.hash());
        for (int i = bclists.size() - 1; i >= 0; i--) {
            System.out.println("===");
            System.out.println("commit "
                    + bclists.get(i).hash());

            if (bclists.get(i).mergecheck()) {
                System.out.println("Merge: "
                        + bclists.get(i).mergeid());
            }
            System.out.println("Date: "
                    + bclists.get(i).timestamp());
            System.out.println(bclists.get(i).message());
            System.out.println();
        }
    }

    /**
     * log helper.
     *
     * @param commit commit
     */
    private void loghelper(String commit) {
        if (commit.equals("")) {
            return;
        }
        loghelper(commits.get(commit).parent());
        bclists.add(commits.get(commit));
    }

    /**
     * global log.
     */
    private void glog() {

        Set<String> compare1 = commits.keySet();
        Iterator<String> iter = compare1.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println("===");
            System.out.println("commit " + commits.get(key).hash());

            if (commits.get(key).mergecheck()) {
                System.out.println("Merge: "
                        + commits.get(key).mergeid());
            }
            System.out.println("Date: " + commits.get(key).timestamp());
            System.out.println(commits.get(key).message());
            System.out.println();
        }
    }

    /**
     * find.
     *
     * @param message message
     */
    private void find(String message) {
        _message = message;
        boolean result = false;
        Set<String> compare1 = commits.keySet();
        Iterator<String> iter = compare1.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (commits.get(key).message().equals(_message)) {
                System.out.println(commits.get(key).hash());
                result = true;
            }
        }
        if (!result) {
            Utils.message("Found no commit with that message.");
            System.exit(0);

        }
    }

    /**
     * status.
     */
    private void status() {
        _cblobs = _commit.cblob();
        System.out.println("=== Branches ===");
        Set<String> compare1 = branch.keySet();

        String[] myArray = new String[compare1.size()];
        compare1.toArray(myArray);
        shelper(myArray);
        System.out.println();

        System.out.println("=== Staged Files ===");
        Set<String> compare2 = staging.keySet();
        String[] myArray2 = new String[compare2.size()];
        compare2.toArray(myArray2);
        for (int i = 0; i < staging.size(); i++) {
            stagingname.add(staging.get(myArray2[i]).name());
        }
        for (int j = 0; j < stagingname.size(); j++) {
            myArray2[j] = stagingname.get(j);
        }
        shelper(myArray2);
        System.out.println();


        System.out.println("=== Removed Files ===");
        String[] array = new String[removed.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = removed.get(i);
        }
        shelper(array);
        System.out.println();

        setuntracked();
        System.out.println("=== Modifications Not Staged For Commit ===");
        String[] array3 = new String[mmodifications.size()];
        for (int i = 0; i < array3.length; i++) {
            array3[i] = mmodifications.get(i);
        }
        shelper(array3);

        System.out.println();


        System.out.println("=== Untracked Files ===");

        String[] array4 = new String[untracked.size()];
        for (int i = 0; i < array4.length; i++) {
            array4[i] = untracked.get(i);
        }
        shelper(array4);
        System.out.println();
    }

    /**
     * status helper.
     *
     * @param words words
     */
    private void shelper(String[] words) {
        if (words.length > 0) {
            String temp;
            int result = 0;
            for (int i = 0; i < words.length; i++) {
                for (int j = i + 1; j < words.length; j++) {
                    result = words[i].compareTo(words[j]);
                    if (result > 0) {
                        temp = words[i];
                        words[i] = words[j];
                        words[j] = temp;
                    }
                }
            }
        }

        for (int z = 0; z < words.length; z++) {
            if (words[z].equals(_current)) {
                System.out.println("*" + words[z]);
            } else {
                System.out.println(words[z]);
            }
        }

    }

    /**
     * checkout.
     *
     * @param operand operand
     */
    private void checkout(String[] operand) {
        if (operand.length == 1) {
            String bname = operand[0];
            _tempblobs = _commit.cblob();
            checkouthelper(bname);
        } else if (operand.length == 2) {
            Blob temp = new Blob(operand[1]);
            Blob temp2;
            _cblobs = _commit.cblob();
            if (_cblobs.containsKey(temp.name())) {
                temp2 = _cblobs.get(temp.name());
                File file = new File(temp.name());
                Utils.writeContents(file, temp2.gstringcontents());
            } else {
                Utils.message("File does not exist in that commit.");
                System.exit(0);
            }
        } else if (operand.length == 3) {
            String cid = operand[0];
            if (!operand[1].equals("--")) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            if (cid.length() < 10) {
                Set<String> comset = commits.keySet();
                String shortid;
                for (String x : comset) {
                    if (x.contains(cid)) {
                        cid = x;
                    }
                }
            }
            if (commits.containsKey(cid)) {
                setuntracked();
                if (untracked.size() > 0) {
                    Utils.message("There is an untracked "
                            + "file in the way; delete it or add it first.");
                    System.exit(0);
                }
                File test = new File(operand[2]);
                if (!test.exists()) {
                    Utils.writeContents(test, new Object[]{""});
                }
                Blob temp = new Blob(operand[2]);
                Blob temp2;
                _cblobs = commits.get(cid).cblob();
                if (_cblobs.containsKey(temp.name())) {
                    temp2 = _cblobs.get(temp.name());
                    File file = new File(temp.name());
                    Utils.writeContents(file, temp2.gstringcontents());
                } else {
                    Utils.message("File does not exist in that commit.");
                    System.exit(0);
                }
            } else {
                Utils.message("No commit with that id exists.");
                System.exit(0);
            }
        }
    }

    /**
     * check1helper.
     *
     * @param bname name
     */
    private void checkouthelper(String bname) {
        if (branch.containsKey(bname)) {
            String cid = branch.get(bname);
            if (bname.equals(_current)) {
                Utils.message("No need to checkout the current branch.");
                System.exit(0);
            } else {
                setuntracked();
                if (untracked.size() > 0) {
                    Utils.message("There is an untracked "
                            + "file in the way; "
                            + "delete it or add it first.");
                    System.exit(0);
                }
                if (commits.containsKey(cid)) {
                    _cblobs = commits.get(cid).cblob();
                    Set<String> compare1 = _cblobs.keySet();
                    Iterator<String> iter = compare1.iterator();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        Blob value = _cblobs.get(key);


                        File test = new File(key);

                        if (!test.exists()) {
                            Utils.writeContents(test, new Object[]{""});
                        }
                        Blob temp = new Blob(key);

                        if (key.equals(temp.name())) {
                            Utils.writeContents(test,
                                    value.gstringcontents());
                        }
                    }

                    for (int i = 0; i < _blobs.size(); i++) {
                        if (!_cblobs.containsKey(_blobs.get(i).name())) {
                            File file3 = new File(_blobs.get(i).name());
                            Utils.restrictedDelete(file3);
                        }
                    }
                    staging = new LinkedHashMap<>();
                    _current = bname;
                    _id = cid;
                    _commit = commits.get(cid);
                } else {
                    Utils.message("No commit with that id exists.");
                    System.exit(0);
                }
            }
        } else {
            Utils.message("No such branch exists.");
            System.exit(0);
        }
    }

    /**
     * branch.
     *
     * @param name name
     */
    private void branch(String name) {
        if (branch.containsKey(name)) {
            Utils.message("A branch with that name already exists.");
            System.exit(0);
        } else {
            branch.put(name, _id);
        }
    }

    /**
     * remove branch.
     *
     * @param name name
     */
    private void rmbranch(String name) {
        if (!branch.containsKey(name)) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        }

        if (_current.equals(name)) {
            Utils.message("Cannot remove the current branch.");
            System.exit(0);
        } else {
            branch.remove(name);
        }
    }

    /**
     * reset.
     *
     * @param id id
     */
    private void reset(String id) {

        if (!commits.containsKey(id)) {
            Utils.message("No commit with that id exists.");
            System.exit(0);
        } else {
            _cblobs = commits.get(id).cblob();
            Set<String> compare1 = _cblobs.keySet();
            Iterator<String> iter = compare1.iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                String[] scheckout = {id, "--", key};
                checkout(scheckout);
            }
            for (int i = 0; i < _blobs.size(); i++) {
                if (!_cblobs.containsKey(_blobs.get(i).name())) {
                    File file3 = new File(_blobs.get(i).name());
                    Utils.restrictedDelete(file3);
                }
            }

            branch.replace(_current, commits.get(id).hash());
            staging = new LinkedHashMap<>();
            _commit = commits.get(id);

        }
    }

    /**
     * untracked.
     */
    public void setuntracked() {

        currdir = Utils.plainFilenamesIn(System.getProperty("user.dir"));
        copydir = new ArrayList<>();
        for (int i = 0; i < currdir.size(); i++) {
            if (!currdir.get(i).equals(".gitignore")
                    && !currdir.get(i).equals("Makefile")
                    && !currdir.get(i).equals("gitlet-design.txt")
                    && !currdir.get(i).equals("proj3.iml")) {
                copydir.add(currdir.get(i));
            }
        }
        _tempblobs = _commit.cblob();
        for (int i = 0; i < copydir.size(); i++) {
            if (_tempblobs.containsKey(copydir.get(i))) {
                Blob temp = new Blob(copydir.get(i));
                if (staging.containsKey(temp.hCode())) {
                    if (!_tempblobs.get(copydir.get(i))
                            .hCode().equals(temp.hCode())) {
                        mmodifications.add(copydir.get(i) + " (modified)");
                    }
                } else {
                    if (!_tempblobs.get(copydir.get(i))
                            .hCode().equals(temp.hCode())) {
                        mmodifications.add(copydir.get(i) + " (modified)");
                    }
                }
            } else {
                Blob temp2 = new Blob(copydir.get(i));
                if (!staging.containsKey(temp2.hCode())) {
                    if (!removed.contains(temp2.hCode())) {
                        untracked.add(copydir.get(i));
                    }

                }
            }
        }

        Set<String> compare1 = _tempblobs.keySet();
        Set<String> setcopydir = new HashSet<>(copydir);

        Set<String> setremoved = new HashSet<>(removed);
        for (String k : compare1) {
            if (!setcopydir.contains(k)) {
                if (!setremoved.contains(k)) {
                    mmodifications.add(k + " (deleted)");
                }
            }
        }

        Set<String> st = staging.keySet();
        for (String k : st) {
            Blob temp = staging.get(k);
            if (!setcopydir.contains(temp.name())) {
                mmodifications.add(temp.name() + " (deleted)");
            }
        }


    }

    /**
     * merge.
     *
     * @param name name
     */

    private void merge(String name) {
        String tempname = name;
        mergehelp1(tempname);
        putallhelp();
        Set<String> splits = splitblobs.keySet();
        for (String s : splits) {
            Blob spfile = splitblobs.get(s);
            if (givenblobs.containsKey(s) && currblobs.containsKey(s)) {
                Blob currfile = currblobs.get(s);
                Blob givefile = givenblobs.get(s);
                if (!spfile.hCode().equals(givefile.hCode())
                        && spfile.hCode().equals(currfile.hCode())) {
                    String[] check = new String[]{s2, "--", s};
                    checkout(check);
                    add(s);
                    removehelper(s);
                } else if (spfile.hCode().equals(givefile.hCode())
                        && !spfile.hCode().equals(currfile.hCode())) {
                    removehelper(s);
                } else if (!spfile.hCode().equals(givefile.hCode())
                        && !spfile.hCode().equals(currfile.hCode())) {
                    if (givefile.hCode().equals(currfile.hCode())) {
                        removehelper(s);
                    } else if (!givefile.hCode().equals(currfile.hCode())) {
                        mergeconflict(s, gtemp);
                        _mergecheck = true;
                        removehelper(s);
                    }
                }
            } else if (!givenblobs.containsKey(s)
                    && !currblobs.containsKey(s)) {
                Set<String> setcopydir = new HashSet<>(copydir);
                if (setcopydir.contains(s)) {
                    removehelper(s);
                }
            } else if (!givenblobs.containsKey(s)
                    && currblobs.containsKey(s)) {
                Blob currfile = currblobs.get(s);
                if (spfile.hCode().equals(currfile.hCode())) {
                    rm(s);
                    removehelper(s);
                } else if (!spfile.hCode().equals(currfile.hCode())) {
                    mergeconflict(s, gtemp);
                    _mergecheck = true;
                    removehelper(s);
                }
            } else if (givenblobs.containsKey(s) && !currblobs.containsKey(s)) {
                Blob givefile = givenblobs.get(s);
                if (spfile.hCode().equals(givefile.hCode())) {
                    removehelper(s);
                } else if (!spfile.hCode().equals(givefile.hCode())) {
                    mergeconflict(s, gtemp);
                    _mergecheck = true;
                    removehelper(s);
                }
            }
        }
        mergehelp2();
        mergecommit(tempname);
    }

    /**
     * putallhelp.
     */
    private void putallhelp() {
        currblobs.putAll(_commit.cblob());
        givenblobs.putAll(commits.get(s2).cblob());
        splitblobs.putAll(commits.get(split).cblob());
        ctemp.putAll(_commit.cblob());
        gtemp.putAll(commits.get(s2).cblob());
        stemp.putAll(commits.get(split).cblob());
    }

    /**
     * mergehelp.
     *
     * @param tempname name
     */
    private void mergehelp1(String tempname) {
        if (!branch.containsKey(tempname)) {
            Utils.message(" A branch with that name does not exist.");
            System.exit(0);
        }
        if (staging.size() > 0 || removed.size() > 0) {
            Utils.message("You have uncommitted changes.");
            System.exit(0);
        }
        if (tempname.equals(_current)) {
            Utils.message("Cannot merge a branch with itself.");
            System.exit(0);
        } else {
            setuntracked();
            if (untracked.size() > 0) {
                Utils.message("There is an untracked"
                        + " file in the way; delete it or add it first.");
                System.exit(0);
            }
        }
        s1 = branch.get(_current);
        s2 = branch.get(tempname);
        findsplit();
        if (split.equals(s2)) {
            Utils.message("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (split.equals(s1)) {
            Utils.message("Current branch fast-forwarded.");
            checkout(new String[]{tempname});
            System.exit(0);
        }
        currdir = Utils.plainFilenamesIn(System.getProperty("user.dir"));
        copydir = new ArrayList<>();
        for (int i = 0; i < currdir.size(); i++) {
            if (!currdir.get(i).equals(".gitignore")
                    && !currdir.get(i).equals("Makefile")
                    && !currdir.get(i).equals("gitlet-design.txt")
                    && !currdir.get(i).equals("proj3.iml")) {
                copydir.add(currdir.get(i));
            }
        }
    }

    /**
     * remove.
     *
     * @param c c
     */
    private void removehelper(String c) {
        ctemp.remove(c);
        gtemp.remove(c);
        stemp.remove(c);
    }

    /**
     * mergecommit.
     *
     * @param tempname name
     */
    private void mergecommit(String tempname) {
        if (_mergecheck) {
            Utils.message("Encountered a merge conflict.");
        }
        _pblobs.putAll(commits.get(_commit.hash()).cblob());
        for (int i = 0; i < removed.size(); i++) {
            if (_pblobs.containsKey(removed.get(i))) {
                _pblobs.remove(removed.get(i));
            }
        }
        String mergemsg = "Merged " + tempname + " into " + _current + ".";
        _commit = new Commit(mergemsg, new Date(),
                _commit.hash(), branch.get(tempname),
                _blobs, staging, _pblobs, removed);
        keyvalue.add(_commit.hash());
        commits.put(_commit.hash(), _commit);
        _id = _commit.hash();
        branch.replace(_current, _id);
    }

    /**
     * help.
     */
    private void mergehelp2() {

        currblobs = new LinkedHashMap<>();
        givenblobs = new LinkedHashMap<>();
        splitblobs = new LinkedHashMap<>();
        currblobs.putAll(ctemp);
        givenblobs.putAll(gtemp);
        splitblobs.putAll(stemp);
        Set<String> current = currblobs.keySet();
        for (String c : current) {
            Blob currfile = currblobs.get(c);
            if (!givenblobs.containsKey(c) && !splitblobs.containsKey(c)) {
                removehelper(c);
            }
            if (givenblobs.containsKey(c) && !splitblobs.containsKey(c)) {
                mergeconflict(c, gtemp);
                _mergecheck = true;
                removehelper(c);
            }
        }
        currblobs = new LinkedHashMap<>();
        givenblobs = new LinkedHashMap<>();
        splitblobs = new LinkedHashMap<>();
        currblobs.putAll(ctemp);
        givenblobs.putAll(gtemp);
        splitblobs.putAll(stemp);
        Set<String> giveng = givenblobs.keySet();
        for (String g : giveng) {
            Blob givefile = givenblobs.get(g);
            if (!currblobs.containsKey(g) && !splitblobs.containsKey(g)) {
                String[] check = new String[]{s2, "--", g};
                checkout(check);
                add(g);
                removehelper(g);
            } else if (currblobs.containsKey(g) && !splitblobs.containsKey(g)) {
                mergeconflict(g, gtemp);
                _mergecheck = true;
                removehelper(g);
            }
        }
    }

    /**
     * mergeconeflic.
     *
     * @param s     s
     * @param given given.
     */
    private void mergeconflict(String s, LinkedHashMap<String, Blob> given) {

        File currentf = new File(s);
        String givencon = "";
        if (given.containsKey(s)) {
            Blob temp1 = given.get(s);
            givencon = temp1.gstringcontents();
        } else {
            givencon = "";
        }


        Formatter result = new Formatter();
        String currcon = "";

        if (currentf.exists()) {
            currcon = Utils.readContentsAsString(currentf);
        }


        result.format("<<<<<<< HEAD\n");
        result.format(currcon);
        result.format("=======\n");
        result.format(givencon);
        result.format(">>>>>>>\n");
        String str = result.toString();
        Utils.writeContents(currentf, str);
        Blob blobtemp = new Blob(s);
        add(s);

    }

    /**
     * cbranch.
     *
     * @param id id
     */
    private void cbranchpath(String id) {
        if (id.equals("")) {
            return;
        }
        cbranchpath(commits.get(id).parent());
        currenb.add(commits.get(id).hash());
    }

    /**
     * gbranchpath.
     *
     * @param id id
     */
    private void gbranchpath(String id) {
        if (id.equals("")) {
            return;
        }
        gbranchpath(commits.get(id).parent());
        givenb.add(commits.get(id).hash());
    }

    /**
     * gbranchpath.
     */
    private void findsplit() {
        cbranchpath(s1);
        gbranchpath(s2);
        cmergebranchpath(s1);
        gmergebranchpath(s2);
        String case1 = findsplit(currenb, givenb);
        String case2 = findsplit(cmergeb, givenb);
        String case3 = findsplit(currenb, gmergeb);
        String case4 = findsplit(cmergeb, gmergeb);
        int case11 = 0;
        int temp = 0;
        int case22 = 0;
        int case33 = 0;
        int case44 = 0;
        case11 = helperss(currenb, case1);
        temp = helperss(givenb, case1);
        case11 = case11 + temp;
        case22 = helperss(cmergeb, case2);
        temp = helperss(givenb, case2);
        case22 = case22 + temp;
        case33 = helperss(currenb, case3);
        temp = helperss(gmergeb, case3);
        case33 = case33 + temp;
        case44 = helperss(cmergeb, case4);
        temp = helperss(gmergeb, case4);
        case44 = case44 + temp;
        int min1 = Math.min(case11, case22);
        int min2 = Math.min(case33, case44);
        int min = Math.min(min1, min2);
        if (min == case11) {
            split = case1;
        } else if (min == case22) {
            split = case2;
        } else if (min == case33) {
            split = case3;

        } else if (min == case44) {
            split = case4;
        }

    }

    /**
     * return gbranchpath.
     *
     * @param xx    xx
     * @param case1 case1
     */
    private int helperss(List<String> xx, String case1) {
        int k = 0;
        if (xx.size() > 0) {
            for (int j = xx.size() - 1; j >= 0; j--) {
                if (xx.get(j).equals(case1)) {
                    return k;
                }
                k++;
            }
        }
        return k;
    }

    /**
     * gbranchpath.
     *
     * @param id id
     */
    private void gmergebranchpath(String id) {
        if (id.equals("")) {
            return;
        }
        if (commits.get(id).mergecheck()) {
            gmergebranchpath(commits.get(id).parent2());
        } else {
            gmergebranchpath(commits.get(id).parent());
        }
        gmergeb.add(commits.get(id).hash());
    }

    /**
     * gbranchpath.
     *
     * @param id id
     */
    private void cmergebranchpath(String id) {
        if (id.equals("")) {
            return;
        }
        if (commits.get(id).mergecheck()) {
            cmergebranchpath(commits.get(id).parent2());
        } else {
            cmergebranchpath(commits.get(id).parent());
        }
        cmergeb.add(commits.get(id).hash());
    }

    /**
     * return findsplit.
     *
     * @param x x
     * @param y y
     */
    private String findsplit(List<String> x, List<String> y) {
        for (int i = x.size() - 1; i >= 0; i--) {
            for (int j = y.size() - 1; j >= 0; j--) {
                if (x.get(i).equals(y.get(j))) {
                    return x.get(i);
                }
            }
        }
        return "";
    }

    /**
     * return findsplit.
     *
     * @param id id
     * @param x  x
     * @param y  y
     * @param z  z
     */
    private int fast(String id, String x, String y, String z) {
        if (id.equals("")) {
            return 0;
        }
        int xx = 0;
        cbranchpath(commits.get(id).parent());
        fastest.add(id);
        for (int i = 0; i < fastest.size(); i++) {
            if (fastest.get(i).equals(x)
                    || fastest.get(i).equals(y) || fastest.get(i).equals(z)) {
                xx++;
            }
        }
        return xx;
    }
}



