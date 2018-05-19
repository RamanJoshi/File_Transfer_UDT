package com.test.file;

import com.barchart.udt.TestSocketFile;

public class TestFile {

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		final TestSocketFile testSocket = new TestSocketFile();
		try {
			testSocket.fileTransfer();
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
