package com.bajdcc.LALR1.grammar.runtime.service;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 【运行时】运行时文件服务
 *
 * @author bajdcc
 */
public class RuntimeFileService implements IRuntimeFileService {

	private static final int OFFSET_FILE = 20000;
	private static final int MAX_FILE = 1000;
	private static final String VFS_PREFIX = "$";
	private static final String RESOURCE_PREFIX = "!";
	private static Logger logger = Logger.getLogger("file");
	private RuntimeService service;
	private FileStruct arrFiles[];
	private Set<Integer> setFileId;
	private Map<String, Integer> mapFileNames;
	private int cyclePtr = 0;
	private Map<String, VfsStruct> mapVfs;

	public RuntimeFileService(RuntimeService service) {
		this.service = service;
		this.arrFiles = new FileStruct[MAX_FILE];
		this.setFileId = new HashSet<>();
		this.mapFileNames = new HashMap<>();
		this.mapVfs = new HashMap<>();
	}

	@Override
	public void addVfs(String name, String content) {
		mapVfs.put(VFS_PREFIX + name, new VfsStruct(content.getBytes(UTF_8), true));
	}

	@Override
	public String getVfs(String name) {
		return mapVfs.containsKey(name) ? new String(mapVfs.get(name).data) : null;
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
		FileStruct fs = new FileStruct(name, mode, encoding, mapVfs);
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
		FileStruct fs = arrFiles[handle];
		logger.debug("File #" + handle + " '" + fs.name + "' destroyed");
		if (fs.vfs) {
			mapFileNames.remove(fs.name);
			fs.destroy(mapVfs);
			arrFiles[handle] = null;
			setFileId.remove(handle);
		} else {
			mapFileNames.remove(fs.name);
			fs.destroy();
			arrFiles[handle] = null;
			setFileId.remove(handle);
		}
		return true;
	}

	class VfsStruct {
		public byte[] data;
		public boolean readonly;

		public VfsStruct() {
			data = null;
			readonly = false;
		}

		public VfsStruct(byte[] data) {
			this.data = data;
			readonly = false;
		}

		public VfsStruct(byte[] data, boolean readonly) {
			this.data = data;
			this.readonly = true;
		}
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
		private boolean vfs;
		private ByteArrayOutputStream baos;

		FileStruct(String filename, int mode, String encoding, Map<String, VfsStruct> mapVfs) {
			this.name = filename;
			this.mode = mode;
			this.encoding = encoding;
			this.status = FileStatus.ERROR;
			this.vfs = false;
			if (mode == 1) { // read
				if (filename.startsWith(VFS_PREFIX)) {
					if (mapVfs.containsKey(filename)) {
						try {
							ByteArrayInputStream bais = new ByteArrayInputStream(mapVfs.get(filename).data);
							InputStreamReader isr = new InputStreamReader(bais, encoding);
							reader = new BufferedReader(isr);
							this.status = FileStatus.READING;
							this.vfs = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						InputStream is;
						if (filename.startsWith(RESOURCE_PREFIX)) {
							is = getClass().getResourceAsStream(filename.substring(1));
						} else {
							is = new FileInputStream(filename);
						}
						InputStreamReader isr = new InputStreamReader(is, encoding);
						reader = new BufferedReader(isr);
						this.status = FileStatus.READING;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if ((mode & 2) != 0) { // write
				if (filename.startsWith(VFS_PREFIX)) {
					VfsStruct vs = mapVfs.get(filename);
					if (vs == null || !vs.readonly) {
						try {
							// mode:2=truncate mode:3=append
							baos = new ByteArrayOutputStream();
							if (mapVfs.containsKey(filename) && mode == 3) {
								baos.write(mapVfs.get(filename).data);
							}
							OutputStreamWriter osw = new OutputStreamWriter(baos, encoding);
							writer = new BufferedWriter(osw);
							this.status = FileStatus.WRITING;
							this.vfs = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
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

		public void destroy(Map<String, VfsStruct> mapVfs) {
			if (!vfs)
				return;
			try {
				if (status == FileStatus.READING) {
					reader.close();
				} else if (status == FileStatus.WRITING) {
					writer.flush();
					mapVfs.put(name, new VfsStruct(baos.toByteArray()));
					writer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
