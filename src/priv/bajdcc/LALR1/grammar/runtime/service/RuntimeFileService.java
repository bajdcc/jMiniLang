package priv.bajdcc.LALR1.grammar.runtime.service;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 【运行时】运行时文件服务
 *
 * @author bajdcc
 */
public class RuntimeFileService implements IRuntimeFileService {

    private static final int OFFSET_FILE = 20000;
    private static final int MAX_FILE = 1000;
    private static Logger logger = Logger.getLogger("file");
    private RuntimeService service;
    private FileStruct arrFiles[];
    private Set<Integer> setFileId;
    private Map<String, Integer> mapFileNames;
    private int cyclePtr = 0;

    public RuntimeFileService(RuntimeService service) {
        this.service = service;
        this.arrFiles = new FileStruct[MAX_FILE];
        this.setFileId = new HashSet<>();
        this.mapFileNames = new HashMap<>();
    }

    private int encodeHandle(int handle) {
        return handle + OFFSET_FILE;
    }

    private int decodeHandle(int handle) {
        return handle - OFFSET_FILE;
    }

    @Override
    public int create(String name, int mode, String encoding) {
        if (setFileId.size() >= MAX_FILE) {
            return -1;
        }
        if (mapFileNames.containsKey(name)) {
            return -1;
        }
        int handle;
        FileStruct fs = new FileStruct(name, mode, encoding);
        if (fs.status == FileStatus.ERROR) {
            return -1;
        }
        for (; ; ) {
            if (arrFiles[cyclePtr] == null) {
                handle = cyclePtr;
                setFileId.add(cyclePtr);
                mapFileNames.put(name, cyclePtr);
                arrFiles[cyclePtr++] = fs;
                if (cyclePtr >= MAX_FILE) {
                    cyclePtr -= MAX_FILE;
                }
                break;
            }
            cyclePtr++;
            if (cyclePtr >= MAX_FILE) {
                cyclePtr -= MAX_FILE;
            }
        }
        logger.debug("File #" + handle + " '" + name + "' created");
        return encodeHandle(handle);
    }

    @Override
    public boolean destroy(int handle) {
        handle = decodeHandle(handle);
        if (!setFileId.contains(handle)) {
            return false;
        }
        logger.debug("File #" + handle + " '" + arrFiles[handle].name + "' destroyed");
        mapFileNames.remove(arrFiles[handle].name);
        arrFiles[handle].destroy();
        arrFiles[handle] = null;
        setFileId.remove(handle);
        return true;
    }

    @Override
    public int read(int handle) {
        handle = decodeHandle(handle);
        if (!setFileId.contains(handle)) {
            return -1;
        }
        return arrFiles[handle].read();
    }

    @Override
    public String readString(int handle) {
        handle = decodeHandle(handle);
        if (!setFileId.contains(handle)) {
            return null;
        }
        return arrFiles[handle].readString();
    }

    @Override
    public boolean write(int handle, char ch) {
        handle = decodeHandle(handle);
        return setFileId.contains(handle) && arrFiles[handle].write(ch);
    }

    @Override
    public boolean writeString(int handle, String str) {
        handle = decodeHandle(handle);
        return setFileId.contains(handle) && arrFiles[handle].writeString(str);
    }

    enum FileStatus {
        ERROR,
        READING,
        WRITING,
    }

    class FileStruct {
        public String name;
        public FileStatus status;
        private int mode;
        private String encoding;
        private BufferedReader reader;
        private BufferedWriter writer;

        FileStruct(String filename, int mode, String encoding) {
            this.name = filename;
            this.mode = mode;
            this.encoding = encoding;
            this.status = FileStatus.ERROR;
            if (mode == 1) { // read
                try {
                    FileInputStream fis = new FileInputStream(filename);
                    InputStreamReader isr = new InputStreamReader(fis, encoding);
                    reader = new BufferedReader(isr);
                    this.status = FileStatus.READING;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ((mode & 2) != 0) { // write
                try {
                    // mode:2=truncate mode:3=append
                    FileOutputStream fos = new FileOutputStream(filename, (mode & 1) != 0);
                    OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
                    writer = new BufferedWriter(osw);
                    this.status = FileStatus.WRITING;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        int read() {
            if (status != FileStatus.READING) {
                return -1;
            }
            try {
                return reader.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        public String readString() {
            if (status != FileStatus.READING) {
                return null;
            }
            try {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        boolean write(char ch) {
            if (status != FileStatus.WRITING) {
                return false;
            }
            try {
                writer.write(ch);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean writeString(String str) {
            if (status != FileStatus.WRITING) {
                return false;
            }
            try {
                writer.write(str);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        public void destroy() {
            try {
                if (status == FileStatus.READING) {
                    reader.close();
                } else if (status == FileStatus.WRITING) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
