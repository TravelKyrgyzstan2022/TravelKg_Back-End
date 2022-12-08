package com.example.benomad.service.akinator;

import com.example.benomad.entity.AkinatorQuestion;

import java.util.List;


public class AkinatorQueryBuilder {

    private static final String endQuery = " JOIN place_attributes p2 ON p.id = p2.place_id " +
            "JOIN place_ratings r ON p.id = r.place_id GROUP BY r.place_id"; // prolly wont work fixme

    public static String build(List<AkinatorQuestion> questions, String answers){
        if(answers.length() != questions.size()){
            throw new RuntimeException();
        }
        StringBuilder query = new StringBuilder("SELECT * FROM places p WHERE ");
        answers = answers.toLowerCase();
        for(int i = 0; i < answers.length(); i++){
            if(answers.charAt(i) == '1'){
                query.append(questions.get(i).getFirstOptionQuery());
            }else if(answers.charAt(i) == '2'){
                query.append(questions.get(i).getSecondOptionQuery());
            }else if(answers.charAt(i) == 'n'){
                query.append(questions.get(i).getNeutralOptionQuery());
            }else{
                throw new RuntimeException();
            }

            if(i == answers.length() - 1){
                query.append(";");
            }else{
                query.append(" AND ");
            }
        }
        query.append(endQuery);
        return query.toString();
    }
}
