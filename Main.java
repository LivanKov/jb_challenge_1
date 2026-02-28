import java.util.*;

public class Main {

public static PhpType inferTypeFromDoc(PhpVariable variable) {
    PhpDocBlock docBlock = variable.getDocBlock();
    if (docBlock == null) return TypeFactory.createType("mixed");

    List<DocTag> varTags = docBlock.getTagsByName("var");
    if (varTags.isEmpty()) return TypeFactory.createType("mixed");

    String variableName = variable.getName();
    String resolvedTypeString = null;

    for (DocTag tag : varTags) {
        String value = tag.getValue().trim();
        String[] tokens = value.split("\\s+", 2);

        String typePart;
        String namePart;

        if (tokens[0].startsWith("$")) continue;

        typePart = tokens[0];
        namePart = tokens.length > 1 ? tokens[1].trim() : null;

        if (namePart != null && namePart.startsWith("$")) {
            String tagVarName = namePart.split("\\s+")[0];
            if (!tagVarName.equals(variableName)) continue;
        }

        resolvedTypeString = typePart;
        break;
    }

    if (resolvedTypeString == null) return TypeFactory.createType("mixed");

    String[] typeNames = resolvedTypeString.split("\\|");
    if (typeNames.length == 1) {
        return TypeFactory.createType(typeNames[0]);
    } else {
        List<PhpType> types = new ArrayList<>();
        for (String typeName : typeNames) {
            types.add(TypeFactory.createType(typeName));
        }
        return TypeFactory.createUnionType(types);
    }
}

public static void main(String[] args) {
    Main main = new Main();
    
    System.out.println("Test 1: Standard Type");
    PhpVariable user = new PhpVariable("$user");
    user.setDocBlock(new PhpDocBlock("/** @var User */"));
    PhpType userType = main.inferTypeFromDoc(user);
    System.out.println("Expected: User");
    System.out.println("Actual: " + userType.getTypeName());
    System.out.println("Pass: " + userType.getTypeName().equals("User"));
    System.out.println();
    
    System.out.println("Test 2: Union Type");
    PhpVariable id = new PhpVariable("$id");
    id.setDocBlock(new PhpDocBlock("/** @var string|int */"));
    PhpType idType = main.inferTypeFromDoc(id);
    System.out.println("Expected: string|int");
    System.out.println("Actual: " + idType.getTypeName());
    System.out.println("Is UnionType: " + (idType instanceof UnionType));
    System.out.println("Pass: " + (idType instanceof UnionType && idType.getTypeName().equals("string|int")));
    System.out.println();
    
    System.out.println("Test 3: Named Tag");
    PhpVariable log = new PhpVariable("$log");
    log.setDocBlock(new PhpDocBlock("/** @var Logger $log */"));
    PhpType logType = main.inferTypeFromDoc(log);
    System.out.println("Expected: Logger");
    System.out.println("Actual: " + logType.getTypeName());
    System.out.println("Pass: " + logType.getTypeName().equals("Logger"));
    System.out.println();
    
    System.out.println("Test 4: Name Mismatch");
    PhpVariable guest = new PhpVariable("$guest");
    guest.setDocBlock(new PhpDocBlock("/** @var Admin $adm */"));
    PhpType guestType = main.inferTypeFromDoc(guest);
    System.out.println("Expected: mixed");
    System.out.println("Actual: " + guestType.getTypeName());
    System.out.println("Pass: " + guestType.getTypeName().equals("mixed"));
    System.out.println();
    
    System.out.println("Test 5: Multiple Tags");
    PhpVariable name = new PhpVariable("$name");
    name.setDocBlock(new PhpDocBlock("/**\n * @var int $id\n * @var string $name\n */"));
    PhpType nameType = main.inferTypeFromDoc(name);
    System.out.println("Expected: string");
    System.out.println("Actual: " + nameType.getTypeName());
    System.out.println("Pass: " + nameType.getTypeName().equals("string"));
    System.out.println();
    
    System.out.println("Test 6: Fallback (No DocBlock)");
    PhpVariable noDoc = new PhpVariable("$noDoc");
    PhpType noDocType = main.inferTypeFromDoc(noDoc);
    System.out.println("Expected: mixed");
    System.out.println("Actual: " + noDocType.getTypeName());
    System.out.println("Pass: " + noDocType.getTypeName().equals("mixed"));
    System.out.println();
    
    System.out.println("Test 7: Fallback (No matching tag)");
    PhpVariable notFound = new PhpVariable("$notFound");
    notFound.setDocBlock(new PhpDocBlock("/** @param string $other */"));
    PhpType notFoundType = main.inferTypeFromDoc(notFound);
    System.out.println("Expected: mixed");
    System.out.println("Actual: " + notFoundType.getTypeName());
    System.out.println("Pass: " + notFoundType.getTypeName().equals("mixed"));
}

}