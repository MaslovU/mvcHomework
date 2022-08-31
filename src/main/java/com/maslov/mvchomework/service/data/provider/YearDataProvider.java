package com.maslov.mvchomework.service.data.provider;

import com.maslov.mvchomework.domain.YearOfPublish;
import com.maslov.mvchomework.repository.YearRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class YearDataProvider {

    private final YearRepo yearRepo;

    public YearOfPublish create(YearOfPublish yearOfPublish) {
        return yearRepo.save(yearOfPublish);
    }

    public YearOfPublish findByDate(String year) {
        return yearRepo.findByDateOfPublish(year);
    }
}
