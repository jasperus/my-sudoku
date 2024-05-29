package sudoku.entity.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Converter
@RequiredArgsConstructor
@Slf4j
public class BoardConverter3D implements AttributeConverter<String[][][], String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(String[][][] board) {
        String boardJson = null;
        try {
            return objectMapper.writeValueAsString(board);
        } catch (JsonProcessingException e) {
            log.error("JSON writing error");
        }
        return boardJson;
    }

    @Override
    public String[][][] convertToEntityAttribute(String boardJson) {
        String[][][] board = new String[9][9][9];
        try {
            board = objectMapper.readValue(boardJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("JSON reading error", e);
        }
        return board;
    }
}
