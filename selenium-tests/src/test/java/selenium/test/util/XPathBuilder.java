package selenium.test.util;

public class XPathBuilder {
	private StringBuilder xpath;

	public XPathBuilder() {
		this.xpath = new StringBuilder("//");
	}

	public XPathBuilder element(String element) {
		xpath.append(element);
		return this;
	}

	public XPathBuilder attribute(String attribute, String value) {
		xpath.append("[@").append(attribute).append("='").append(value).append("']");
		return this;
	}

	public XPathBuilder text(String text) {
		xpath.append("[text()='").append(text).append("']");
		return this;
	}

	public XPathBuilder followingSibling(String element) {
		xpath.append("/following-sibling::").append(element);
		return this;
	}

	public XPathBuilder parent(String element) {
		xpath.append("/parent::").append(element);
		return this;
	}

	public XPathBuilder descendant(String element) {
		xpath.append("//").append(element);
		return this;
	}

	public XPathBuilder className(String className) {
		return attribute("class", className);
	}

	public String build() {
		return xpath.toString();
	}
}
