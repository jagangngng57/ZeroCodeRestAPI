package org.jsmart.zerocode.core.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.rest.qa.api.utils.CsvUtil;

import static com.rest.qa.api.utils.AtlasConstants.DEFAULT_ENV;
import static com.rest.qa.api.utils.AtlasConstants.ENV_PROPERTY;

public class Parameterized {
    private final List<Object> valueSource;
    private final List<String> csvSource;
    private final Boolean ignoreHeader;
    private final String testID;

    public Parameterized(
            @JsonProperty("valueSource") JsonNode valueSourceJsonNode,
            @JsonProperty("csvSource") JsonNode csvSourceJsonNode,
            @JsonProperty("testID") String testID,
            @JsonProperty("ignoreHeader") Boolean ignoreHeader) {
        this.ignoreHeader = Optional.ofNullable(ignoreHeader).orElse(true);
        this.testID = Optional.ofNullable(testID).orElse("");
        this.valueSource = getValueSourceFrom(valueSourceJsonNode);
        this.csvSource = getCsvSourceFrom(csvSourceJsonNode);
    }

    public List<Object> getValueSource() {
        return valueSource;
    }

    public List<String> getCsvSource() {
        return csvSource;
    }

    private List<String> getCsvSourceFrom(JsonNode csvSourceJsonNode) {
        try {
            if (csvSourceJsonNode.isArray()) {
                return readCsvSourceFromJson(csvSourceJsonNode);

            } else {
                return readCsvSourceFromExternalCsvFile(csvSourceJsonNode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing csvSource", e);
        }
    }


    private List<Object> getValueSourceFrom(JsonNode valueSourceJsonNode) {
        try {
            if (valueSourceJsonNode.isArray()) {
                return readValueSourceFromJson(valueSourceJsonNode);

            } else {
                return readValueSourceFromExternalCsvFile(valueSourceJsonNode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing csvSource", e);
        }
    }

    private List<Object> readValueSourceFromJson(JsonNode csvSourceJsonNode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(new TypeReference<List<String>>() {
        });
        return reader.readValue(csvSourceJsonNode);
    }

    private List<Object> readValueSourceFromExternalCsvFile(JsonNode csvSourceJsonNode) throws IOException {
        String env = System.getProperty(ENV_PROPERTY, DEFAULT_ENV);
        String csvSourceFilePath = csvSourceJsonNode.textValue();
        if (StringUtils.isNotBlank(csvSourceFilePath)) {
            List<Object> csvSourceFileLines = CsvUtil.readDataFromCsv(getClass().getClassLoader().getResourceAsStream(env+"/"+csvSourceFilePath),testID);
//            if (this.ignoreHeader) {
//                return csvSourceFileLines.stream()
//                        .skip(1)
//                        .collect(Collectors.toList());
//            }
            return csvSourceFileLines;
        }
        return Collections.emptyList();
    }

    private List<String> readCsvSourceFromJson(JsonNode csvSourceJsonNode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(new TypeReference<List<String>>() {
        });
        return reader.readValue(csvSourceJsonNode);
    }

    private List<String> readCsvSourceFromExternalCsvFile(JsonNode csvSourceJsonNode) throws IOException {
        String env = System.getProperty("env","stage");
        String csvSourceFilePath = csvSourceJsonNode.textValue();
        if (StringUtils.isNotBlank(csvSourceFilePath)) {
            Path path = Paths.get(env+"/"+csvSourceFilePath);
            List<String> csvSourceFileLines = Files.lines(path)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
            if (this.ignoreHeader) {
                return csvSourceFileLines.stream()
                        .skip(1)
                        .collect(Collectors.toList());
            }
            return csvSourceFileLines;
        }
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "Parameterized{" +
                "valueSource=" + valueSource +
                ", csvSource=" + csvSource +
                '}';
    }

}
