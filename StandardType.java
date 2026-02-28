class StandardType extends PhpType {
    private final String typeName;

    public StandardType(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
}
