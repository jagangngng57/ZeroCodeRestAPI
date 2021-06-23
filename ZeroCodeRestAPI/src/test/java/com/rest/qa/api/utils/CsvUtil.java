package com.rest.qa.api.utils;


import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvUtil {


	public static <T> List<T> read(Class<T> clazz, InputStream stream) throws IOException {
		CsvMapper mapper = new CsvMapper();
		mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
		CsvSchema schema = mapper.schemaFor(clazz).withHeader();
		ObjectReader reader = mapper.readerFor(clazz).with(schema);
		return reader.<T>readValues(stream).readAll();
	}

	public static List<Object> readDataFromCsv(InputStream stream,String testID) throws IOException {
		List<Object> objectList = new ArrayList<>();
		List<HashMap> data = read(HashMap.class,stream);
		System.out.println("testID: "+testID);
		System.out.println("data: "+data);
		for(HashMap map:data){
			if(!testID.isEmpty()){
				if(testID.equals(map.get("testID"))){
					objectList.add(convertWithStream(map));
				}
			} else {
				objectList.add(convertWithStream(map));
			}
		}
		System.out.println("returning data: "+objectList);
		return objectList;
	}

	public static String convertWithStream(Map<?, ?> map) {
		String mapAsString = map.keySet().stream()
				.map(key -> key + "=" + map.get(key))
				.collect(Collectors.joining(", ", "{", "}"));
		return mapAsString+"";
	}

	@Test
	public void name() throws IOException {
		List<Object> output = readDataFromCsv(getClass().getClassLoader().getResourceAsStream("data/counter.csv"),"A001");
		System.out.println("Output: "+output);
		for (Object o :
				output) {
			System.out.println("Object: "+o);

		}
		System.out.println("Output: "+output.size());
	}

}