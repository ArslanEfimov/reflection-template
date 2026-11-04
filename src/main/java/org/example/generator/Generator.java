package org.example.generator;

import org.example.annotations.Generatable;

import java.lang.reflect.*;
import java.util.*;

public class Generator {

    private final Random random = new Random();
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MAX_TYPE_OCCURRENCES = 3;

    public <T> T generateValueOfType(Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return generateValueOfType(clazz, new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    private  <T> T generateValueOfType(Class<T> clazz, Map<Class<?>, Integer> activeTypeCounts) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object generated;

        if(isPrimitiveOrWrapper(clazz)){
            generated = generatePrimitiveOrWrapper(clazz);
        }
        else if(clazz == String.class){
            generated = generateString();
        }
        else if(clazz.isEnum()){
            generated = generateEnum(clazz);
        }
        else if(clazz.isArray()){
            generated = generateArray(clazz, activeTypeCounts);
        }
        else if (clazz == List.class || clazz == ArrayList.class) {
            generated = generateList(Integer.class, activeTypeCounts);
        }
        else if (clazz == Map.class || clazz == HashMap.class) {
            generated = generateMap(String.class, Integer.class, activeTypeCounts);
        }
        else if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())){
            Class<?> implementation = resolveImplementation(clazz);
            generated = generateValueOfType(implementation, activeTypeCounts);
        }
        else{
            generated = generateUserObject(clazz, activeTypeCounts);
        }

        if(clazz.isPrimitive()){
            return (T) generated;
        }

        return clazz.cast(generated);
    }

    private Object generatePrimitiveOrWrapper(Class<?> clazz){
        if(clazz == int.class || clazz == Integer.class){
            return random.nextInt(1000);
        }
        else if(clazz == long.class || clazz == Long.class){
            return random.nextLong(1000);
        }
        else if(clazz == float.class || clazz == Float.class){
            return random.nextFloat(1000);
        }
        else if(clazz == double.class || clazz == Double.class){
            return random.nextDouble(1000);
        }
        else if(clazz == boolean.class || clazz == Boolean.class){
            return random.nextBoolean();
        }
        else if(clazz == byte.class || clazz == Byte.class){
            return (byte) random.nextInt(-128, 128);
        }
        else if(clazz == short.class || clazz == Short.class){
            return (short) random.nextInt(-32768, 32768);
        }
        else if (clazz == char.class || clazz == Character.class){
            return CHARS.charAt(random.nextInt(CHARS.length()));
        }

        throw new IllegalArgumentException("Unsupported primitive or wrapper: " + clazz.getName());
    }

    private String generateString() {
        int length = random.nextInt(1, 21);
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            builder.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        return builder.toString();
    }

    private Object generateUserObject(Class<?> clazz, Map<Class<?>, Integer> activeTypeCounts) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if(!clazz.isAnnotationPresent(Generatable.class)){
            throw new IllegalArgumentException("Cannot generate an object of the class " + clazz.getName() + ": missing @Generatable annotation");
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        if(constructors.length == 0){
            throw new IllegalArgumentException("No constructors found for " + clazz.getName());
        }
        int previousCount = activeTypeCounts.getOrDefault(clazz, 0);

        if(previousCount >= MAX_TYPE_OCCURRENCES){
            return null;
        }

        activeTypeCounts.put(clazz, previousCount + 1);

        try {
            Constructor<?> constructor = constructors[random.nextInt(constructors.length)];
            return constructor.newInstance(generateArguments(constructor, activeTypeCounts));
        }
        finally {
            if(previousCount == 0){
                activeTypeCounts.remove(clazz);
            }
            else{
                activeTypeCounts.put(clazz, previousCount);
            }
        }

    }

    private Object generateEnum(Class<?> clazz){
        Object[] enumConstants = clazz.getEnumConstants();
        if(enumConstants.length == 0)
            throw new IllegalStateException("The enumeration " + clazz.getName() + " has no values to choose from");
        return enumConstants[random.nextInt(enumConstants.length)];
    }

    private Object generateArray(Class<?> clazz, Map<Class<?>, Integer> activeTypeCounts) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> componentType = clazz.getComponentType();
        int arrayLength  = random.nextInt(1, 6);

        Object array = Array.newInstance(componentType, arrayLength);

        for (int i = 0; i < arrayLength; i++) {
            Object element = generateValueOfType(componentType, activeTypeCounts);
            Array.set(array, i, element);
        }
        return array;
    }
    private List<?> generateList(Class<?> elementType, Map<Class<?>, Integer> activeTypeCounts) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        int size = random.nextInt(1, 6);
        List<Object> items = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            items.add(generateValueOfType(elementType, activeTypeCounts));
        }

        return items;
    }

    private Map<?, ?> generateMap(Class<?> keyType, Class<?> valueType, Map<Class<?>, Integer> activeTypeCounts) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        int attempts = random.nextInt(1, 6);
        Map<Object, Object> map = new HashMap<>();

        for (int i = 0; i < attempts; i++) {
            Object key = generateValueOfType(keyType, activeTypeCounts);
            Object value = generateValueOfType(valueType, activeTypeCounts);

            map.put(key, value);
        }

        return map;
    }

    private Object[] generateArguments(Constructor<?> constructor, Map<Class<?>, Integer> activeTypeCounts) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Type[] parameterTypes = constructor.getGenericParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            arguments[i] = generateArgument(parameterTypes[i], activeTypeCounts);
        }

        return arguments;
    }

    private Object generateArgument(Type type, Map<Class<?>, Integer> activeTypeCounts) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (type instanceof Class<?> clazz) {
            return generateValueOfType(clazz, activeTypeCounts);
        }

        if (type instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();

            if (rawType == List.class || rawType == ArrayList.class) {
                Type elementType = parameterizedType.getActualTypeArguments()[0];

                if (elementType instanceof Class<?> elementClass) {
                    return generateList(elementClass, activeTypeCounts);
                }

                throw new IllegalArgumentException("Unsupported list element type: " + elementType.getTypeName());
            }
            if (rawType == Map.class || rawType == HashMap.class) {
                Type[] typeArguments = parameterizedType.getActualTypeArguments();

                Type keyType = typeArguments[0];
                Type valueType = typeArguments[1];

                if (keyType instanceof Class<?> keyClass && valueType instanceof Class<?> valueClass) {
                    return generateMap(keyClass, valueClass, activeTypeCounts);
                }

                throw new IllegalArgumentException("Unsupported map key or value type: " + parameterizedType.getTypeName());
            }
        }

        throw new IllegalArgumentException("Unsupported constructor parameter type: " + type.getTypeName());
    }

    private Class<?> resolveImplementation(Class<?> clazz) {
        Generatable annotation = clazz.getAnnotation(Generatable.class);

        if (annotation == null) {
            throw new IllegalArgumentException("Cannot resolve implementation for " + clazz.getName() + ": missing @Generatable annotation");
        }

        Class<?>[] implementations = annotation.implementations();

        if (implementations.length == 0) {
            throw new IllegalArgumentException("No implementations specified for " + clazz.getName());
        }

        for (Class<?> candidate : implementations) {
            validateImplementation(clazz, candidate);
        }

        return implementations[random.nextInt(implementations.length)];
    }

    private void validateImplementation(
            Class<?> requestedType,
            Class<?> candidate
    ) {
        if (!requestedType.isAssignableFrom(candidate)) {
            throw new IllegalArgumentException(candidate.getName() + " is not compatible with " + requestedType.getName());
        }

        if (candidate.isInterface() || Modifier.isAbstract(candidate.getModifiers())) {
            throw new IllegalArgumentException("Implementation must be a concrete class: " + candidate.getName());
        }
    }

    private static final Set<Class<?>> WRAPPER_TYPES = Set.of(
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Boolean.class,
            Character.class
    );

    private boolean isPrimitiveOrWrapper(Class<?> clazz){
        return clazz != void.class && (clazz.isPrimitive() || WRAPPER_TYPES.contains(clazz));
    }
}
