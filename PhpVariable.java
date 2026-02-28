public class PhpVariable {
    private String name;
    private PhpDocBlock docBlock;

    public PhpVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PhpDocBlock getDocBlock() {
        return docBlock;
    }

    public void setDocBlock(PhpDocBlock docBlock) {
        this.docBlock = docBlock;
    }
}