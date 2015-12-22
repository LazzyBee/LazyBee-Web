package com.born2go.lazzybeemobile.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

@ExportPackage("")
@Export("faceBookAPI")
public class FaceBookAPI implements Exportable {
	public void OnloadFaceBook() {
		LazzyBeeMobile.loadFaceBook();
	}
}
