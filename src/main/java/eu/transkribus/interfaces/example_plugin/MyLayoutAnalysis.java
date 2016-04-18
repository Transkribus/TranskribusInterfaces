package eu.transkribus.interfaces.example_plugin;

import eu.transkribus.interfaces.ILayoutAnalysis;
import eu.transkribus.interfaces.types.Image;

public class MyLayoutAnalysis implements ILayoutAnalysis {

	@Override public String usage() {
		return "myUsage";
	}

	@Override public String getToolName() {
		return "myToolName";
	}

	@Override public String getVersion() {
		return "myVersion";
	}

	@Override public String getProvider() {
		return "myProvider";
	}

	@Override public void process(Image image, String xmlInOut, String[] ids, String[] props) {
		System.out.println("process of MyLayoutAnalysis, xmlInOut = "+xmlInOut);
	}

}


