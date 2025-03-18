package org.repin.dto;

import lombok.Data;

import java.util.List;

@Data
public class GenericTableDataDto<T> {
    private List<String> headers;
    private List<T> data;

    public GenericTableDataDto(List<String> headers, List<T> data) {
        this.headers = headers;
        this.data = data;
    }
}
