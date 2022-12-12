package com.example.benomad.service.akinator;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.AkinatorQuestion;
import com.example.benomad.entity.Place;
import com.example.benomad.mapper.PlaceMapper;
import com.example.benomad.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

//coming soon :)
@RequiredArgsConstructor
public class AkinatorServiceImpl implements AkinatorService{

    private final PlaceMapper placeMapper;
    private final PlaceRepository placeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PlaceDTO> getResults(String answers) {
        return null;
    }

    public List<PlaceDTO> getHardcodedResults(){
        List<AkinatorQuestion> questions = getHardcodedQuestions();
        String answers = getHardcodedAnswers();
        String query = AkinatorQueryBuilder.build(questions, answers);
        Query nativeQuery = entityManager.createQuery(query, Place.class);
        List<Place> places = (List<Place>) nativeQuery.getResultList();
        return placeMapper.entityListToDtoList(places);
    }

    private String getHardcodedAnswers(){
        return "n1221";
    }

    private List<AkinatorQuestion> getHardcodedQuestions(){
        List<AkinatorQuestion> list = new ArrayList<>();
        list.add(
                AkinatorQuestion.builder()
                        .question("Do you prefer active rest?")
                        .firstOptionText("Yes")
                        .secondOptionText("No")
                        .firstOptionQuery("p.place_type IN ('HIKING', 'LAKE', 'GORGE', 'FOREST', 'SPORT_CENTER')")
                        .secondOptionQuery("p.place_type NOT IN ('HIKING', 'LAKE', 'GORGE', 'FOREST', 'SPORT_CENTER')")
                        .build()
        );
        list.add(
                AkinatorQuestion.builder()
                        .question("Do you prefer places where there are quite a lot of people (crowded)?")
                        .firstOptionText("Yes")
                        .secondOptionText("No")
                        .firstOptionQuery("p.is_crowded = true")
                        .secondOptionQuery("p.is_crowded = false")
                        .build()
        );
        list.add(
                AkinatorQuestion.builder()
                        .question("Are you planning to travel with children?")
                        .firstOptionText("Yes")
                        .secondOptionText("No")
                        .firstOptionQuery("p.is_only_adults = true")
                        .secondOptionQuery("p.is_only_adults = false")
                        .build()
        );
        list.add(
                AkinatorQuestion.builder()
                        .question("What is the approximate budget for the place? (excluding hotels)")
                        .firstOptionText("Less than $1000")
                        .secondOptionText("More than $1000")
                        .firstOptionQuery("p.approximate_price < 1000")
                        .secondOptionQuery("")
                        .build()
        );
        list.add(
                AkinatorQuestion.builder()
                        .question("What type of vacation do you prefer: In nature" +
                                " or in city")
                        .firstOptionText("In nature")
                        .secondOptionText("Urban")
                        .firstOptionQuery("p.place_type IN ('NATURE')") //fixme
                        .secondOptionQuery("p.place_type NOT IN ('NATURE')") //fixme
                        .build()
        );
        return list;
    }
}
