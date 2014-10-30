package opticnav.daemon.persistence;

import opticnav.daemon.protocol.PassCode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * TODO: Alternative persistence method, perhaps don't accept entire maps for storage
 * Right now, there isn't any protection against corrupt file-writes.
 * To illustrate the problem: if the program crashes in the middle of a file-write,
 * the original contents of that file will be lost.
 */
public class FilePersistence implements Persistence {
    private static final XLogger LOG = XLoggerFactory.getXLogger(FilePersistence.class);
    
    private final File ardListFile, indexesFile;
    private int index_ardID;

    public FilePersistence(File ardListFile, File indexesFile)
            throws IOException {
        this.ardListFile = ardListFile;
        this.indexesFile = indexesFile;

        if (this.indexesFile.exists()) {
            try (Scanner in = new Scanner(indexesFile)) {
                this.index_ardID = in.nextInt();
            } catch (NoSuchElementException e) {
                throw new IOException("Could not read index file - malformed data");
            }
            LOG.info("Read index values: " + this.index_ardID);
        } else {
            // Files don't exist - set indices to default
            LOG.info("Index file doesn't exist - default indices");
            this.index_ardID = 1;
        }
    }

    @Override
    public void persistARDList(Iterator<Map.Entry<Integer, PassCode>> list)
            throws IOException {
        LOG.info("Overwriting ARD List file");
        // Overwrite any existing file
        final FileWriter writer = new FileWriter(this.ardListFile, false);
        try (PrintWriter out = new PrintWriter(writer)) {
            while (list.hasNext()) {
                final Map.Entry<Integer, PassCode> entry = list.next();
                final int id = entry.getKey();
                final PassCode passCode = entry.getValue();
                out.printf("%d,%s\n", id, passCode.getString());
            }
        }
    }

    @Override
    public void readARDList(Map<Integer, PassCode> list)
            throws IOException {
        if (!this.ardListFile.exists()) {
            LOG.info("No ARD List file - empty");
            return;
        }
        LOG.info("Reading ARD List file");

        try (Scanner in = new Scanner(this.ardListFile)) {
            while (in.hasNextLine()) {
                final String line = in.nextLine();
                final String[] s = line.split(",");
                if (s.length != 2) {
                    throw new IOException("Malformed line: " + line);
                }

                final int id;
                final PassCode passCode;

                try {
                    id = Integer.parseInt(s[0]);
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid ID: " + s[0]);
                }
                if (!PassCode.isStringCodeValid(s[1])) {
                    throw new IOException("Invalid passCode: " + s[1]);
                }

                passCode = new PassCode(s[1]);

                list.put(id, passCode);
            }
        }
    }

    @Override
    public int nextARDID() throws IOException {
        final int id = index_ardID++;
        flushIndexes();
        return id;
    }

    @Override
    public void close() throws IOException {
    }

    private void flushIndexes() throws IOException {
        final String content = Integer.toString(index_ardID);
        LOG.info("Flushing indexes file: " + content);
        
        // Overwrite any existing file
        final FileWriter writer = new FileWriter(this.indexesFile, false);
        try (PrintWriter out = new PrintWriter(writer)) {
            out.println(content);
        }
    }
}
