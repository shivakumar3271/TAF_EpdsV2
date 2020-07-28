package com.tests.mdxjson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.beans.mdxjson.RPAJsonTransaction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class DummyJsonValidation extends MdxJsonTestBaseClass {

	public ArrayList<String> validateDummyJson(RPAJsonTransaction dummyJson) {

		LinkedHashMap<String, String> jsonMap = new LinkedHashMap<>();
		ArrayList<String> extraNodesList = new ArrayList<String>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.valueToTree(dummyJson);

		addKeys("", jsonNode, jsonMap, new ArrayList<>());
		Set<String> keySet = jsonMap.keySet();
		String value;
		for (String key : keySet) {
			value = jsonMap.get(key).toString();
			if (!value.equalsIgnoreCase(TRUE))
				extraNodesList.add(key + " : " + value);
		}
		return (extraNodesList);
	}

	private void addKeys(String currentPath, JsonNode jsonNode, LinkedHashMap<String, String> map,
			List<Integer> suffix) {

		if (jsonNode.isObject()) {
			ObjectNode objectNode = (ObjectNode) jsonNode;
			Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
			String pathPrefix = currentPath.isEmpty() ? "" : currentPath + "-";

			while (iter.hasNext()) {
				Map.Entry<String, JsonNode> entry = iter.next();
				addKeys(pathPrefix + entry.getKey(), entry.getValue(), map, suffix);
			}
		} else if (jsonNode.isArray()) {
			ArrayNode arrayNode = (ArrayNode) jsonNode;

			for (int i = 0; i < arrayNode.size(); i++) {
				suffix.add(i + 1);
				addKeys(currentPath + "-" + (i + 1), arrayNode.get(i), map, suffix);

				if (i + 1 < arrayNode.size()) {
					suffix.remove(suffix.size() - 1);
				}
			}
		} else if (jsonNode.isValueNode()) {
			if (currentPath.contains("-")) {
				suffix = new ArrayList<>();
			}

			ValueNode valueNode = (ValueNode) jsonNode;
			map.put(currentPath, valueNode.asText());
		}
	}
}
