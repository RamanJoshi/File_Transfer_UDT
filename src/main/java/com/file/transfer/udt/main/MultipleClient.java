package com.file.transfer.udt.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MultipleClient {

	final static int[] portList = { 9000, 9001, 9002, 9003, 9004, 9005, 9006,
			9007, 9008, 9009, 9010, 9011, 9012 };
	final static String ip = "192.168.56.1";
	final static String sourceFolder = "I:\\SourceFile";
	final static String targetFolder = "G:\\TransferFile";
	final static String[] sourceFileList = { "1.mp4", };
	final static String[] targetFileNameList = { "1.mp4" };

	public static void main(final String[] args) {
		final String[] sourceFileList = { "20.mkv", };
		final String[] targetFileNameList = { "20.mkv" };
		/*
		 * final byte[][] byteArrayList = prepareChunksOfFile( sourceFolder +
		 * "\\" + sourceFileList[0], 1024 * 1024 * 100 100 MB );
		 */
		/*
		 * for (int i = 0; i < byteArrayList.length; i++) { new
		 * FileTransferClient(ip, portList[i], sourceFolder + "\\" +
		 * sourceFileList[i], targetFolder + "\\" + targetFileNameList[i],
		 * byteArrayList[i]).start(); }
		 */
		new FileTransferClient(ip, portList[0],
				sourceFolder + "\\" + sourceFileList[0],
				targetFolder + "\\" + targetFileNameList[0]).start();
	}

	public static byte[][] prepareChunksOfFile(final String fileName,
			final int partSize) {
		final File inputFile = new File(fileName);
		FileInputStream inputStream;
		int fileSize = (int) inputFile.length();
		final byte[][] listOfChunks = new byte[fileSize / partSize][];
		int read = 0;
		final int readLength = partSize;
		byte[] byteChunkPart = null;
		try {
			final int count = 0;
			inputStream = new FileInputStream(inputFile);
			while (fileSize > 0) {
				try {
					System.out.println(byteChunkPart);
					System.out.println(readLength);
					byteChunkPart = new byte[readLength];
					System.out.println(byteChunkPart);
				} catch (final Exception ex) {
					System.out.println("ex" + ex);
				}
				read = inputStream.read(byteChunkPart, 0, readLength);
				fileSize -= read;
				assert (read == byteChunkPart.length);

				// listOfChunks[count++] = byteChunkPart;
				new FileTransferClient(ip, portList[0],
						sourceFolder + "\\" + sourceFileList[0],
						targetFolder + "\\" + targetFileNameList[0],
						byteChunkPart, read).start();
				byteChunkPart = null;
				// listOfChunks.add(byteObjects);

			}
			inputStream.close();
		} catch (final IOException exception) {
			exception.printStackTrace();
		}
		return listOfChunks;
	}

}
