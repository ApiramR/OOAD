package com.example.demo.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ModelMapperUtil {

    public Map<String, Object> mapFieldsToGetters(Object model, String[] fieldNames){
        Map<String, Object> fieldMap = new HashMap<String,Object>();

        // Loop through each field name
        for (String fieldName : fieldNames) {
            // Create getter method name (e.g., "firstName" -> "getFirstName")
            String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            try {
                // Get the getter method using reflection
                Method getterMethod = model.getClass().getMethod(getterMethodName);

                // Invoke the getter method and store the result in the map
                Object value = getterMethod.invoke(model);

                // Put field name and value into the map
                fieldMap.put(fieldName, value);
            } catch (NoSuchMethodException e) {
                // If the getter method doesn't exist, handle it here (optional)
                fieldMap.put(fieldName, "Method not found");
            } catch(Exception e){
                fieldMap.put(fieldName, "Error: " + e.getMessage());    
            }
        }
        return fieldMap;
    }
}