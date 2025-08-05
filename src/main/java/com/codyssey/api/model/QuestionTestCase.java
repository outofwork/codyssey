package com.codyssey.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * QuestionTestCase Entity
 * <p>
 * Represents test cases for coding questions with input, expected output, and metadata.
 */
@Entity
@Table(name = "question_test_cases",
        indexes = {
                @Index(name = "idx_test_case_question", columnList = "question_id"),
                @Index(name = "idx_test_case_sequence", columnList = "question_id, sequence"),
                @Index(name = "idx_test_case_sample", columnList = "is_sample")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTestCase extends BaseEntity {

    /**
     * Primary key for the entity - Test Case ID (TST-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "test-case-id")
    @GenericGenerator(name = "test-case-id", strategy = "com.codyssey.api.util.TestCaseIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Associated coding question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @NotNull
    private CodingQuestion question;

    /**
     * Order of test cases
     */
    @Column(name = "sequence", nullable = false)
    @NotNull
    private Integer sequence;

    /**
     * Test case input data
     */
    @NotBlank
    @Column(name = "input_data", nullable = false, columnDefinition = "TEXT")
    private String inputData;

    /**
     * Expected output for this test case
     */
    @NotBlank
    @Column(name = "expected_output", nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;

    /**
     * Is this a sample/example test case shown to users
     */
    @Column(name = "is_sample", nullable = false)
    private Boolean isSample = false;

    /**
     * Explanation of this test case
     */
    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    /**
     * Constructor with required fields
     */
    public QuestionTestCase(CodingQuestion question, Integer sequence, String inputData, String expectedOutput) {
        this.question = question;
        this.sequence = sequence;
        this.inputData = inputData;
        this.expectedOutput = expectedOutput;
        this.isSample = false;
    }

    /**
     * Constructor with sample flag
     */
    public QuestionTestCase(CodingQuestion question, Integer sequence, String inputData, 
                           String expectedOutput, Boolean isSample, String explanation) {
        this.question = question;
        this.sequence = sequence;
        this.inputData = inputData;
        this.expectedOutput = expectedOutput;
        this.isSample = isSample != null ? isSample : false;
        this.explanation = explanation;
    }
}