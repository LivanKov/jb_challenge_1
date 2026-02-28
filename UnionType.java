import java.util.*;

class UnionType extends PhpType {
        private final List<PhpType> types;
    
        public UnionType(List<PhpType> types) {
            this.types = types;
        }
    
        public List<PhpType> getTypes() {
            return types;
        }

        @Override
        public String getTypeName() {
            return types.stream()
                    .map(PhpType::getTypeName)
                    .reduce((a, b) -> a + "|" + b)
                    .orElse("");
        }
}