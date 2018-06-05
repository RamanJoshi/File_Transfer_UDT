package com.file.transfer.udt.main;

import java.io.InputStream;
import java.io.OutputStream;

public class TestServer {

	public void doCopy(final InputStream input, final OutputStream output) {
		final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 100;// 1024 * 4;
		final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		final long sequence = 0;
		final int n = 0;
		// while (-1 != (n = input.read(buffer))) {
		// while (sequence)
		// output.write(buffer, 0, n);
		// if (count == length) {
		// break;
		// }
		// log.info("Bytes written: "+count);
		// }
	}

}
