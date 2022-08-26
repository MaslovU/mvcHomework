package com.maslov.mvchomework.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
@Builder
public class BookModel {
    private String name;
    private List<String> authors;
    private String year;
    private String genre;
    private List<String> listOfComments;
}
