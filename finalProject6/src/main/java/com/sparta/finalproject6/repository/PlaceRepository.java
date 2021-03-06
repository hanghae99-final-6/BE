package com.sparta.finalproject6.repository;


import com.sparta.finalproject6.model.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place,Long> {
    void deleteAllByPostId(Long postId);
    @EntityGraph(attributePaths = "imgUrl")
    List<Place> findAllByPostId(Long postId);

}
