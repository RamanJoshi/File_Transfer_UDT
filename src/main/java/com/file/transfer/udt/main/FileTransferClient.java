package com.file.transfer.udt.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import com.barchart.udt.FactoryUDT;
import com.barchart.udt.OptionUDT;
import com.barchart.udt.ccc.UDPBlast;
import com.barchart.udt.net.NetSocketUDT;

public class FileTransferClient extends Thread {

	static boolean finished = false;
	private static final long start = System.currentTimeMillis();
	private static int count = 0;
	public String sourceFile;
	public String targetFileName;
	private final static double maxBW = 5000 * 0.93;

	public String ip;
	public int port;
	public byte[] chunks;
	private int read;

	public FileTransferClient(final String ip, final int port,
			final String sourceFile, final String targetFileName,
			final byte[] chunks, final int read) {
		this.ip = ip;
		this.port = port;
		this.sourceFile = sourceFile;
		this.targetFileName = targetFileName;
		this.chunks = chunks;
		this.read = read;
	}

	public FileTransferClient(final String ip, final int port,
			final String sourceFile, final String targetFileName) {
		this.ip = ip;
		this.port = port;
		this.sourceFile = sourceFile;
		this.targetFileName = targetFileName;
	}

	@Override
	public void run() {
		try {

			final NetSocketUDT socket = new NetSocketUDT();

			socket.socketUDT().setOption(OptionUDT.UDT_CC,
					new FactoryUDT<UDPBlast>(UDPBlast.class));
			socket.socketUDT().setOption(OptionUDT.UDT_SNDTIMEO, 60000);
			socket.connect(new InetSocketAddress(ip, port));

			final Object obj = socket.socketUDT().getOption(OptionUDT.UDT_CC);
			final UDPBlast objCCC = (UDPBlast) obj;
			objCCC.setRate((int) maxBW);

			System.out.println("Connected");
			final OutputStream os = socket.getOutputStream();

			final File f = new File(sourceFile);
			final FileInputStream is = new FileInputStream(f);
			// time();
			os.write((targetFileName + "\n" + f.length() + "\n")
					.getBytes("UTF-8"));
			copy(is, os);
			// copy(os);
			System.out.println("DONE WITH COPY!!");
			finished = true;
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private long copy(final InputStream input, final OutputStream output)
			throws IOException {

		final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 4;
		final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		final byte[] data = new byte[DEFAULT_BUFFER_SIZE];
		while (-1 != (n = input.read(buffer))) {
			output.write(data, 0, n);
			count += n;
		}
		final long end = System.currentTimeMillis();
		System.out.println("TOTAL TIME: " + (end - start) / 1000
				+ " seconds for file: " + targetFileName);
		return count;
	}

	// private long copy(final InputStream input, final OutputStream output)
	// throws IOException {
	//
	// final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 100;
	// final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
	// int n = 0;
	// final byte sequence = 0;
	// // final byte[] data = new byte[DEFAULT_BUFFER_SIZE + 1];
	// while (-1 != (n = input.read(buffer))) {
	// output.write(buffer, 0, n);
	// count += n;
	// }
	// final long end = System.currentTimeMillis();
	// System.out.println("TOTAL TIME: " + (end - start) / 1000
	// + " seconds for file: " + targetFileName);
	// return count;
	// }

	private long copy(final OutputStream output) throws IOException {

		final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 100;
		// final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		final int n = 0;
		// while (-1 != (n = input.read(buffer))) {
		output.write(chunks, 0, read);
		count += n;
		// }

		final long end = System.currentTimeMillis();
		System.out.println("TOTAL TIME: " + (end - start) / 1000
				+ " seconds for file: " + targetFileName);
		return count;
	}

	private static void time() {
		final TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				final long cur = System.currentTimeMillis();
				final long secs = (cur - start) / 1000;
				System.out.println("TRANSFERRED: " + count / 1024 + " SPEED: "
						+ (count / (1024 * 1024)) / secs + "MB/s");
				System.out.println(
						"Thread name " + Thread.currentThread().getId());
			}
		};
		final Timer t = new Timer();
		t.schedule(tt, 2000, 2000);
	}

}
