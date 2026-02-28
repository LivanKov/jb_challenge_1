import java.util.*;

/*
* Describe the entirety of a docblock using this class as follows
*/

public class PhpDocBlock {
    private String content;

    public PhpDocBlock(String content) {
        this.content = content;
    }

    public List<DocTag> getTagsByName(String tagName) {
        List<DocTag> tags = new ArrayList<>();
        String[] lines = content.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("/**") || line.startsWith("*/") || line.startsWith("*")) {
                line = line.replaceFirst("^\\*\\s?", ""); // Remove leading '*'
                if (line.startsWith("@" + tagName)) {
                    String value = line.substring(tagName.length() + 1).trim();
                    tags.add(new DocTag(value));
                }
            }
        }
        return tags;
    }
}