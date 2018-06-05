package com.file.transfer.udt.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MultipleServer {
	final static int[] portList = { 9000, 9001, 9002, 9003, 9004, 9005, 9006,
			9007, 9008, 9009, 9010, 9011, 9012, 9013, 9014, 9015, 9016, 9017,
			9018, 9019 };
	final static String ip = "45.64.192.102";

	public static void main(final String[] args) throws FileNotFoundException {
		final List<FileTransferServer> fileTransferServers = new ArrayList<FileTransferServer>();
		final File file = new File("G:\\TransferFile\\1.mp4");
		final OutputStream os = new FileOutputStream(file);
		for (int i = 0; i < portList.length; i++) {
			final FileTransferServer fileTransferServer = new FileTransferServer(
					ip, portList[i], os);
			fileTransferServer.start();
			fileTransferServers.add(fileTransferServer);
		}
		// for (int i = 0; i < fileTransferServers.size(); i++) {
		// try {
		// fileTransferServers.get(i).join();
		// } catch (final InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// final byte[][] bytesObjects = new byte[fileTransferServers.size()][];
		// for (int i = 0; i < fileTransferServers.size(); i++) {
		// bytesObjects[i] = fileTransferServers.get(i).getChunks();
		// }
		// mergeChunksIntoFile("", bytesObjects);
	}

	// public static void mergeChunksIntoFile(final String target_FileName,
	// final byte[][] chunks) {
	// final File ofile = new File(target_FileName);
	// FileOutputStream fos;
	// try {
	// fos = new FileOutputStream(ofile, true);
	// for (int i = 0; i < chunks.length; i++) {
	// System.out.println("chunks-------> " + chunks);
	// fos.write(chunks[i]);
	// fos.flush();
	// }
	// fos.close();
	// fos = null;
	// } catch (final Exception exception) {
	// exception.printStackTrace();
	// }
	// }
}
