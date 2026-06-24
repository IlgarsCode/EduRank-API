package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.dto.exam.ExamStartResponseDto;
import com.ilgarscode.edurank.dto.exam.ExamSubmitDto;
import com.ilgarscode.edurank.entity.*;
import com.ilgarscode.edurank.repository.ExamSessionAnswerRepository;
import com.ilgarscode.edurank.repository.ExamSessionQuestionRepository;
import com.ilgarscode.edurank.repository.ExamSessionRepository;
import com.ilgarscode.edurank.repository.QuestionRepository;
import com.ilgarscode.edurank.entity.ExamSessionAnswer;
import com.ilgarscode.edurank.service.AuditLogService;
import com.ilgarscode.edurank.service.ExamConfigService;
import com.ilgarscode.edurank.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.ilgarscode.edurank.entity.ExamWindowDto;
import java.time.Duration;
import java.util.stream.Collectors;
import com.ilgarscode.edurank.dto.student.StudentAnswerHistoryDto;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/exam")
public class ExamController {

    private final QuestionRepository questionRepository;
    private final ExamSessionRepository examSessionRepository;
    private final StudentService studentService;
    private final AuditLogService auditLogService;
    private final ExamSessionQuestionRepository examSessionQuestionRepository;
    private final ExamSessionAnswerRepository examSessionAnswerRepository;
    private final ExamConfigService examConfigService;

    public ExamController(
            QuestionRepository questionRepository,
            ExamSessionRepository examSessionRepository,
            StudentService studentService,
            AuditLogService auditLogService,
            ExamSessionQuestionRepository examSessionQuestionRepository,
            ExamSessionAnswerRepository examSessionAnswerRepository,
            ExamConfigService examConfigService
    ) {
        this.questionRepository = questionRepository;
        this.examSessionRepository = examSessionRepository;
        this.studentService = studentService;
        this.auditLogService = auditLogService;
        this.examSessionQuestionRepository = examSessionQuestionRepository;
        this.examSessionAnswerRepository = examSessionAnswerRepository;
        this.examConfigService = examConfigService;
    }
    @PostMapping("/start")
    public ExamStartResponseDto startExam() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String studentId =
                authentication.getName();

        Student student =
                studentService.getById(studentId);

        if (student.getScore() != null) {

            throw new RuntimeException(
                    "Bu tələbə artıq imtahan verib"
            );
        }

        ExamSession session =
                new ExamSession();

        session.setStudent(student);
        session.setStatus(ExamStatus.ACTIVE);
        session.setScore(0);

        session.setStartedAt(
                LocalDateTime.now()
        );

        session.setEndsAt(
                LocalDateTime.now().plusMinutes(
                        examConfigService
                                .getConfig()
                                .getDurationMinutes()
                )
        );

        session =
                examSessionRepository.save(session);

        auditLogService.log(
                studentId,
                "EXAM_STARTED"
        );

        List<Question> questions =
                questionRepository.findAll();

        Collections.shuffle(questions);

        List<Question> selectedQuestions =
                questions.stream()
                        .limit(
                                examConfigService
                                        .getConfig()
                                        .getQuestionCount()
                        )
                        .toList();

        for (Question question : selectedQuestions) {

            ExamSessionQuestion sq =
                    new ExamSessionQuestion();

            sq.setSession(session);
            sq.setQuestion(question);

            examSessionQuestionRepository.save(sq);
        }

        return new ExamStartResponseDto(
                session.getId(),
                selectedQuestions
        );
        }

    @PostMapping("/submit/{sessionId}")
    public int submitExam(@PathVariable Long sessionId,
                          @RequestBody ExamSubmitDto dto) {

        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session tapılmadı"));

        if (session.getStatus() == ExamStatus.FINISHED) {
            throw new RuntimeException("İmtahan artıq bitib");
        }

        if (LocalDateTime.now().isAfter(session.getEndsAt())) {
            session.setStatus(ExamStatus.FINISHED);
            examSessionRepository.save(session);

            auditLogService.log(
                    session.getStudent().getId(),
                    "TIME_EXPIRED"
            );
            throw new RuntimeException("Vaxt bitib");
        }
        List<ExamSessionQuestion> sessionQuestions =
                examSessionQuestionRepository
                        .findBySession(session);

        for (ExamSessionQuestion sq : sessionQuestions) {

            Long questionId =
                    sq.getQuestion().getId();

            String answer =
                    dto.getAnswers()
                            .get(questionId);

            if (answer == null) {
                continue;
            }

            ExamSessionAnswer sessionAnswer =
                    new ExamSessionAnswer();

            sessionAnswer.setSession(session);

            sessionAnswer.setQuestion(
                    sq.getQuestion()
            );

            sessionAnswer.setSelectedAnswer(
                    answer
            );

            examSessionAnswerRepository.save(
                    sessionAnswer
            );
        }

        int score =
                calculateScore(
                        session,
                        dto
                );

        session.setScore(score);
        session.setStatus(ExamStatus.FINISHED);
        examSessionRepository.save(session);

        studentService.updateStudentScore(
                session.getStudent().getId(),
                score
        );

        auditLogService.log(
                session.getStudent().getId(),
                "EXAM_SUBMITTED"
        );

        return score;
    }

    private int calculateScore(
            ExamSession session,
            ExamSubmitDto dto
    ) {

        int score = 0;

        List<ExamSessionQuestion> sessionQuestions =
                examSessionQuestionRepository
                        .findBySession(session);

        for (ExamSessionQuestion sq : sessionQuestions) {

            Long questionId =
                    sq.getQuestion().getId();

            String answer =
                    dto.getAnswers()
                            .get(questionId);

            if (answer == null) {
                continue;
            }

            if (sq.getQuestion()
                    .getCorrectAnswer()
                    .equalsIgnoreCase(answer)) {

                score += 20;
            }
        }

        return score;
    }

    @GetMapping("/status/{sessionId}")
    public ExamSession status(
            @PathVariable Long sessionId
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String currentUserId =
                authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

        if (isAdmin) {

            return examSessionRepository
                    .findById(sessionId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Session tapılmadı"
                            ));
        }

        return examSessionRepository
                .findByIdAndStudent_Id(
                        sessionId,
                        currentUserId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bu sessiyaya giriş icazəniz yoxdur"
                        ));
    }
    @GetMapping("/my-last-session")
    public ExamSession myLastSession() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String studentId =
                authentication.getName();

        return examSessionRepository
                .findTopByStudent_IdOrderByIdDesc(
                        studentId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Session tapılmadı"
                        ));
    }
    @GetMapping("/history")
    public List<ExamSession> myHistory() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String studentId =
                authentication.getName();

        return examSessionRepository
                .findByStudent_IdOrderByIdDesc(
                        studentId
                );
    }
    @GetMapping("/history/{studentId}")
    public List<ExamSession> studentHistory(
            @PathVariable String studentId
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException(
                    "Bu əməliyyat yalnız admin üçündür"
            );
        }

        return examSessionRepository
                .findByStudent_IdOrderByIdDesc(
                        studentId
                );
    }
    @GetMapping("/answers/{sessionId}")
    public List<ExamSessionAnswer> getAnswers(
            @PathVariable Long sessionId
    ) {

        ExamSession session =
                examSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Session tapılmadı"
                                ));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String currentUserId =
                authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

        if (!isAdmin &&
                !session.getStudent()
                        .getId()
                        .equals(currentUserId)) {

            throw new RuntimeException(
                    "Bu cavablara baxa bilməzsiniz"
            );
        }

        return examSessionAnswerRepository
                .findBySession(session);
    }
    @GetMapping("/window/{sessionId}")
    public ExamWindowDto getExamWindow(
            @PathVariable Long sessionId
    ) {

        ExamSession session =
                examSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Session tapılmadı"
                                ));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String currentUserId =
                authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

        if (!isAdmin &&
                !session.getStudent()
                        .getId()
                        .equals(currentUserId)) {

            throw new RuntimeException(
                    "Bu sessiyaya giriş icazəniz yoxdur"
            );
        }

        List<Question> questions =
                examSessionQuestionRepository
                        .findBySession(session)
                        .stream()
                        .map(ExamSessionQuestion::getQuestion)
                        .collect(Collectors.toList());

        long remainingSeconds = 0;

        if (session.getStatus() == ExamStatus.ACTIVE) {

            remainingSeconds =
                    Duration.between(
                            LocalDateTime.now(),
                            session.getEndsAt()
                    ).getSeconds();

            if (remainingSeconds < 0) {
                remainingSeconds = 0;
            }
        }

        return new ExamWindowDto(
                session.getId(),
                session.getStatus(),
                session.getStartedAt(),
                session.getEndsAt(),
                remainingSeconds,
                questions.size(),
                questions
        );
    }
    @GetMapping("/review/{sessionId}")
    public List<StudentAnswerHistoryDto> reviewExam(
            @PathVariable Long sessionId
    ) {

        ExamSession session =
                examSessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Session tapılmadı"
                                ));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String currentUserId =
                authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

        if (!isAdmin &&
                !session.getStudent()
                        .getId()
                        .equals(currentUserId)) {

            throw new RuntimeException(
                    "Bu nəticəyə baxa bilməzsiniz"
            );
        }

        List<ExamSessionAnswer> answers =
                examSessionAnswerRepository
                        .findBySession(session);

        List<StudentAnswerHistoryDto> result =
                new ArrayList<>();

        for (ExamSessionAnswer answer : answers) {

            Question question =
                    answer.getQuestion();

            boolean correct =
                    question.getCorrectAnswer()
                            .equalsIgnoreCase(
                                    answer.getSelectedAnswer()
                            );

            result.add(
                    new StudentAnswerHistoryDto(
                            question.getId(),
                            question.getText(),
                            answer.getSelectedAnswer(),
                            question.getCorrectAnswer(),
                            correct
                    )
            );
        }

        return result;
    }
}