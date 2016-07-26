package net.richardsprojects.nlptest.triples;

public class CBSObject {

	private String objectName;
	private String objectPlural;
	
	public CBSObject(String name, String plural) {
		objectName = name;
		objectPlural = plural;
	}

	public String getObjectName() {
		return objectName;
	}

	public String getObjectPlural() {
		return objectPlural;
	}
}
