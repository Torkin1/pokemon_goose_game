package it.walle.pokemongoosegame.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MethodGetter {
	
	private static final Logger logger = Logger.getLogger(MethodGetter.class.getName());
	
	private MethodGetter() {}

	public static Method getGetter(String attrName, Class<?> objClass) throws NoSuchGetterException {
		try{
			return getGetterOrSetter("get", attrName, objClass);
		}
		catch(NoSuchMethodException e) {
			try {
				return getGetterOrSetter("is", attrName, objClass);
			} catch (NoSuchMethodException eNested) {
				throw new NoSuchGetterException(attrName);
			}
		}
	}
	
	public static Method getSetter(String attrName, Class<?> objClass) throws NoSuchSetterException {
		try{
			return getGetterOrSetter("set", attrName, objClass);
		}
		catch(NoSuchMethodException e) {
			throw new NoSuchSetterException(attrName);
		}
	}

	public static Method getMethodByRoot(String root, Class<?> objClass) throws NoSuchMethodException {
		return getGetterOrSetter(root, "", objClass);

	}
	
	private static Method getGetterOrSetter(String getOrSet, String attrName, Class<?> objClass) throws NoSuchMethodException {
		
		Method[] methods = objClass.getMethods();
		String setAttrName = "Searching method " + getOrSet + " for: " + attrName;
		logger.log(Level.INFO, setAttrName);
		for (int j = 0; j < methods.length; j ++) {
			if ((methods[j].getName().contains(getOrSet)
					&& methods[j].getName().substring(3).compareTo(attrName.substring(0, 1).toUpperCase() + attrName.substring(1)) == 0
					&& !methods[j].isSynthetic()) || (methods[j].getName().contains(getOrSet)
							&& methods[j].getName().substring(2).compareTo(attrName.substring(0, 1).toUpperCase() + attrName.substring(1)) == 0
							&& !methods[j].isSynthetic())
					) {
				return methods[j];
			}
		}
		throw new NoSuchMethodException();
	}
}
