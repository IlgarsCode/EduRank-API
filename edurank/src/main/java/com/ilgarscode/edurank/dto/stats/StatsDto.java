package com.ilgarscode.edurank.dto.stats;

public class StatsDto {

    private long totalStudents;
    private long examParticipants;
    private long admittedStudents;

    private double averageScore;
    private int highestScore;
    private int lowestScore;

    public StatsDto(
            long totalStudents,
            long examParticipants,
            long admittedStudents,
            double averageScore,
            int highestScore,
            int lowestScore
    ) {
        this.totalStudents = totalStudents;
        this.examParticipants = examParticipants;
        this.admittedStudents = admittedStudents;
        this.averageScore = averageScore;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public long getExamParticipants() {
        return examParticipants;
    }

    public long getAdmittedStudents() {
        return admittedStudents;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getLowestScore() {
        return lowestScore;
    }
}