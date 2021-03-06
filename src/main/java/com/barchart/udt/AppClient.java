/**
 * Copyright (C) 2009-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.udt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.barchart.udt.ccc.UDPBlast;
import com.barchart.udt.net.NetSocketUDT;

public class AppClient {

	static boolean finished = false;
	private static final long start = System.currentTimeMillis();
	private static int count = 0;
	private static final String sourceFile = "H:\\New movie\\Avatar 3d (2009) [1080p] [HSBS] [3d]\\Avatar 3d (2009) HSBS BrRip x264 - YFIY.mp4";
	private static final String targetFile = "Avatar 3d (2009) HSBS BrRip x264 - YFIY123.mp4";
	private final static double maxBW = 3000 * 0.93;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) {

		final String host = "192.168.0.108";
		final int port = 9000;
		final int size = 10000;
		final byte[] data = new byte[size];
		final Future<Boolean> monResult = null;

		/*
		 * if (args.length != 2) {
		 * System.out.println("usage: appclient server_host server_port");
		 * return; }
		 */

		// host = args[0];
		// port = Integer.parseInt(args[1]);

		try {

			final NetSocketUDT socket = new NetSocketUDT();

			socket.socketUDT().setOption(OptionUDT.UDT_CC,
					new FactoryUDT<UDPBlast>(UDPBlast.class));
			socket.socketUDT().setOption(OptionUDT.UDT_SNDTIMEO, 60000);
			socket.connect(new InetSocketAddress(host, port));

			final Object obj = socket.socketUDT().getOption(OptionUDT.UDT_CC);
			final UDPBlast objCCC = (UDPBlast) obj;
			objCCC.setRate((int) maxBW);

			System.out.println("Connected");
			final OutputStream os = socket.getOutputStream();

			// Start the monitor background task
			/*
			 * monResult = Executors.newSingleThreadExecutor() .submit(new
			 * Callable<Boolean>() {
			 * 
			 * @Override public Boolean call() { return
			 * monitor(socket.socketUDT()); } });
			 */
			final File f = new File(sourceFile);
			final FileInputStream is = new FileInputStream(f);
			time();
			os.write((targetFile + "\n" + f.length() + "\n").getBytes("UTF-8"));
			System.out.println("DONE WITH COPY!!");
			// Thread.sleep(220 * 1000);
			// IOUtils.copy(is, os);
			copy(is, os);
			/*
			 * for (int i = 0; i < 1000000; i++) { os.write(data); }
			 */

			finished = true;
			if (monResult != null)
				monResult.get();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final ExecutionException e) {
			e.printStackTrace();
		}

	}

	public static boolean monitor(final SocketUDT socket) {

		System.out.println(
				"SendRate(Mb/s)\tRTT(ms)\tCWnd\tPktSndPeriod(us)\tRecvACK\tRecvNAK");

		try {

			while (!finished) {

				Thread.sleep(1000);

				socket.updateMonitor(false);

				System.out.printf(
						"%.2f\t\t" + "%.2f\t" + "%d\t" + "%.2f\t\t\t" + "%d\t"
								+ "%d\n",
						socket.monitor().mbpsSendRate, socket.monitor().msRTT,
						socket.monitor().pktCongestionWindow,
						socket.monitor().usPktSndPeriod,
						socket.monitor().pktRecvACK,
						socket.monitor().pktRecvNAK);
			}

			return true;

		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static long copy(final InputStream input, final OutputStream output)
			throws IOException {

		final int DEFAULT_BUFFER_SIZE = 1024 * 4;
		final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		final long end = System.currentTimeMillis();
		System.out.println("TOTAL TIME: " + (end - start) / 1000 + " seconds");
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
