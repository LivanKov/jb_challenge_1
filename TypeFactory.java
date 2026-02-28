import java.util.*;

class TypeFactory {
    public static PhpType createType(String typeName) {
        return new StandardType(typeName);
    }

    public static UnionType createUnionType(List<PhpType> types) {
        return new UnionType(types);
    }
}