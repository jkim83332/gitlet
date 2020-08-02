package gitlet;

import java.io.File;

/**
 * Driver class for Gitlet, the tiny stupid version-control system.
 *
 * @author Jeongsu Kim
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND> ....
     */
    public static void main(String... args) {
        if (args.length > 0) {
            final Commands comm = new Commands(args);
            comm.exeCommnads();
            Utils.writeContents(new File(".gitlet/gitlet"),
                    Utils.serialize(comm));
            return;
        }
        Utils.message("Please enter a command.");
        System.exit(0);

    }
}
