package com.born2go.lazzybee.client.export;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.born2go.lazzybee.client.LazzyBee;

@ExportPackage("GWTExport")
@Export
public class LazzyBeeExport implements Exportable {

	public void handleClientLoad() {
		LazzyBee.loginControl.onGoogleJSLoad();
	}

}
