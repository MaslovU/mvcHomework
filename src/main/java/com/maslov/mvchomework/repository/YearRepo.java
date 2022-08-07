package com.maslov.mvchomework.repository;

import com.maslov.mvchomework.domain.YearOfPublish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YearRepo extends JpaRepository<YearOfPublish, Long> {
    YearOfPublish findByDateOfPublish(String year);
}
