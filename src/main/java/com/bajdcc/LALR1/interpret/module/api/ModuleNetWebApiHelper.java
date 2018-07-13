package com.bajdcc.LALR1.interpret.module.api;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 实用方法
 *
 * @author bajdcc
 */
public class ModuleNetWebApiHelper {

	public static Object toJsonObject(RuntimeObject obj) {
		if (obj == null)
			return null;
		switch (obj.getType()) {
			case kNull:
			case kFunc:
			case kNan:
				return null;
			case kPtr:
			case kObject:
			case kChar:
			case kString:
			case kBool:
				return obj.getObj();
			case kInt:
				return ((BigInteger) obj.getObj()).longValue();
			case kReal:
				return ((BigDecimal) obj.getObj()).doubleValue();
			case kArray:
				return ((RuntimeArray) obj.getObj()).getArray().stream()
						.map(ModuleNetWebApiHelper::toJsonObject).collect(Collectors.toList());
			case kMap:
				return ((RuntimeMap) obj.getObj()).getMap().entrySet().stream()
						.filter(entry -> entry.getValue().getType() != RuntimeObjectType.kNull)
						.collect(Collectors.toMap(Map.Entry::getKey, entry -> toJsonObject(entry.getValue())));
		}
		return null;
	}
}
