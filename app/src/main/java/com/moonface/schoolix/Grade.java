package com.moonface.schoolix;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Grade {
    private String title;
    private Subject subject;
    private Date date;
    private String type;
    private Float weight;
    private Grading grading;
    private Float grade;
    public enum Grading {
        ONE_TO_HUNDRED, ONE_TO_TEN, A_TO_F
    }
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    Grade(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getGrade() {
        return grade;
    }

    public Grading getGrading() {
        return grading;
    }

    public void setGradeInPercents(Float grade) throws IllegalArgumentException{
        if(grade >= 0 && grade <=100) {
            this.grading = Grading.ONE_TO_HUNDRED;
            this.grade = grade;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setGradeInOneToTen(Float grade) throws IllegalArgumentException{
        if(grade >= 0 && grade <=10) {
            this.grading = Grading.ONE_TO_TEN;
            this.grade = grade;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setGradeInAToF(int grade) throws IllegalArgumentException{
        if(grade >= 0 && grade <=100) {
            this.grading = Grading.A_TO_F;
            this.grade = (float) grade;
        } else {
            throw new IllegalArgumentException();
        }
    }
    public static String format(Grading grading, Float grade) throws IllegalArgumentException{
        DecimalFormat formatter = new DecimalFormat("0");
        DecimalFormat decimalFormatter = new DecimalFormat("0.0");
        if(grading != null) {
            switch (grading) {
                case ONE_TO_HUNDRED:
                    if (grade >= 0 && grade <= 100) {
                        if (grade % 1 == 0) {
                            return formatter.format(grade);
                        } else {
                            return decimalFormatter.format(grade);
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                case ONE_TO_TEN:
                    if (grade >= 0 && grade <= 10) {
                        if (grade % 1 == 0) {
                            return formatter.format(grade);
                        } else {
                            return decimalFormatter.format(grade);
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                case A_TO_F:
                    if (grade >= 0 && grade <= 100) {
                        return a_to_f_grades_names().get(getClosestIndex(Math.round(grade)));
                    } else {
                        throw new IllegalArgumentException();
                    }
                default:
                    throw new IllegalArgumentException();
            }
        }
        if (grade >= 0 && grade <= 100) {
            if (grade % 1 == 0) {
                return formatter.format(grade);
            } else {
                return decimalFormatter.format(grade);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static int getClosestIndex(int grade){
        int index = 0;
        int difference=100;
        for(int i=0; i<a_to_f_grades_values().size(); i++){
            if(Math.abs(a_to_f_grades_values().get(i) - grade) < difference){
                difference = Math.abs(a_to_f_grades_values().get(i) - grade);
                index = i;
            }
        }
        return index;
    }

    private static ArrayList<Integer> a_to_f_grades_values(){
        ArrayList<Integer> a_to_f_grades_values = new ArrayList<>();
        a_to_f_grades_values.add(100);
        a_to_f_grades_values.add(92);
        a_to_f_grades_values.add(83);
        a_to_f_grades_values.add(75);
        a_to_f_grades_values.add(67);
        a_to_f_grades_values.add(58);
        a_to_f_grades_values.add(50);
        a_to_f_grades_values.add(42);
        a_to_f_grades_values.add(33);
        a_to_f_grades_values.add(25);
        a_to_f_grades_values.add(17);
        a_to_f_grades_values.add(8);
        a_to_f_grades_values.add(0);
        return a_to_f_grades_values;

    }

    private static ArrayList<String> a_to_f_grades_names(){
        ArrayList<String> a_to_f_grades_values = new ArrayList<>();
        a_to_f_grades_values.add("A+");
        a_to_f_grades_values.add("A");
        a_to_f_grades_values.add("A-");
        a_to_f_grades_values.add("B+");
        a_to_f_grades_values.add("B");
        a_to_f_grades_values.add("B-");
        a_to_f_grades_values.add("C+");
        a_to_f_grades_values.add("C");
        a_to_f_grades_values.add("C-");
        a_to_f_grades_values.add("D+");
        a_to_f_grades_values.add("D");
        a_to_f_grades_values.add("D-");
        a_to_f_grades_values.add("F");
        return a_to_f_grades_values;

    }
}

