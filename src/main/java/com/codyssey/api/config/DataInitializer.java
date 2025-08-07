package com.codyssey.api.config;

import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.util.UrlSlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data initializer to create default roles on application startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final SourceRepository sourceRepository;
    private final LabelRepository labelRepository;
    private final LabelCategoryRepository labelCategoryRepository;
    private final CodingQuestionRepository codingQuestionRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        createDefaultRoles();
        generateMissingUrlSlugs();
    }

    private void createDefaultRoles() {
        // Create ROLE_USER if it doesn't exist
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setDescription("Default user role");
            roleRepository.save(userRole);
            log.info("Created default role: ROLE_USER");
        }

        // Create ROLE_ADMIN if it doesn't exist
        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("Administrator role");
            roleRepository.save(adminRole);
            log.info("Created default role: ROLE_ADMIN");
        }

        log.info("Data initialization completed");
    }

    @Transactional
    private void generateMissingUrlSlugs() {
        log.info("Generating missing URL slugs for existing entities");
        
        generateSourceUrlSlugs();
        generateLabelCategoryUrlSlugs();
        generateLabelUrlSlugs();
        generateUserUrlSlugs();
        generateCodingQuestionUrlSlugs();
        
        log.info("URL slug generation completed");
    }

    private void generateSourceUrlSlugs() {
        List<Source> sourcesWithoutSlugs = sourceRepository.findByDeletedFalse()
                .stream()
                .filter(source -> source.getUrlSlug() == null || source.getUrlSlug().trim().isEmpty())
                .toList();

        if (!sourcesWithoutSlugs.isEmpty()) {
            log.info("Generating URL slugs for {} sources", sourcesWithoutSlugs.size());
            
            Set<String> existingSlugs = new HashSet<>();
            sourceRepository.findByDeletedFalse().forEach(source -> {
                if (source.getUrlSlug() != null && !source.getUrlSlug().trim().isEmpty()) {
                    existingSlugs.add(source.getUrlSlug());
                }
            });

            for (Source source : sourcesWithoutSlugs) {
                String baseSlug = UrlSlugGenerator.generateSourceSlug(source.getName());
                String uniqueSlug = UrlSlugGenerator.generateUniqueSlug(baseSlug, existingSlugs);
                source.setUrlSlug(uniqueSlug);
                existingSlugs.add(uniqueSlug);
                sourceRepository.save(source);
                log.debug("Generated URL slug '{}' for source '{}'", uniqueSlug, source.getName());
            }
        }
    }

    private void generateLabelCategoryUrlSlugs() {
        List<LabelCategory> categoriesWithoutSlugs = labelCategoryRepository.findByDeletedFalse()
                .stream()
                .filter(category -> category.getUrlSlug() == null || category.getUrlSlug().trim().isEmpty())
                .toList();

        if (!categoriesWithoutSlugs.isEmpty()) {
            log.info("Generating URL slugs for {} label categories", categoriesWithoutSlugs.size());
            
            Set<String> existingSlugs = new HashSet<>();
            labelCategoryRepository.findByDeletedFalse().forEach(category -> {
                if (category.getUrlSlug() != null && !category.getUrlSlug().trim().isEmpty()) {
                    existingSlugs.add(category.getUrlSlug());
                }
            });

            for (LabelCategory category : categoriesWithoutSlugs) {
                String baseSlug = UrlSlugGenerator.generateCategorySlug(category.getName());
                String uniqueSlug = UrlSlugGenerator.generateUniqueSlug(baseSlug, existingSlugs);
                category.setUrlSlug(uniqueSlug);
                existingSlugs.add(uniqueSlug);
                labelCategoryRepository.save(category);
                log.debug("Generated URL slug '{}' for category '{}'", uniqueSlug, category.getName());
            }
        }
    }

    private void generateLabelUrlSlugs() {
        // Use a query that eagerly fetches the category to avoid lazy loading issues
        List<Label> labelsWithoutSlugs = labelRepository.findByDeletedFalseWithCategory()
                .stream()
                .filter(label -> label.getUrlSlug() == null || label.getUrlSlug().trim().isEmpty())
                .toList();

        if (!labelsWithoutSlugs.isEmpty()) {
            log.info("Generating URL slugs for {} labels", labelsWithoutSlugs.size());
            
            Set<String> existingSlugs = new HashSet<>();
            labelRepository.findByDeletedFalseWithCategory().forEach(label -> {
                if (label.getUrlSlug() != null && !label.getUrlSlug().trim().isEmpty()) {
                    existingSlugs.add(label.getUrlSlug());
                }
            });

            for (Label label : labelsWithoutSlugs) {
                String categoryName = label.getCategory() != null ? label.getCategory().getName() : "";
                String baseSlug = UrlSlugGenerator.generateLabelSlug(label.getName(), categoryName);
                String uniqueSlug = UrlSlugGenerator.generateUniqueSlug(baseSlug, existingSlugs);
                label.setUrlSlug(uniqueSlug);
                existingSlugs.add(uniqueSlug);
                labelRepository.save(label);
                log.debug("Generated URL slug '{}' for label '{}'", uniqueSlug, label.getName());
            }
        }
    }

    private void generateUserUrlSlugs() {
        List<User> usersWithoutSlugs = userRepository.findByDeletedFalse()
                .stream()
                .filter(user -> user.getUrlSlug() == null || user.getUrlSlug().trim().isEmpty())
                .toList();

        if (!usersWithoutSlugs.isEmpty()) {
            log.info("Generating URL slugs for {} users", usersWithoutSlugs.size());
            
            Set<String> existingSlugs = new HashSet<>();
            userRepository.findByDeletedFalse().forEach(user -> {
                if (user.getUrlSlug() != null && !user.getUrlSlug().trim().isEmpty()) {
                    existingSlugs.add(user.getUrlSlug());
                }
            });

            for (User user : usersWithoutSlugs) {
                String baseSlug = UrlSlugGenerator.generateUserSlug(user.getUsername());
                String uniqueSlug = UrlSlugGenerator.generateUniqueSlug(baseSlug, existingSlugs);
                user.setUrlSlug(uniqueSlug);
                existingSlugs.add(uniqueSlug);
                userRepository.save(user);
                log.debug("Generated URL slug '{}' for user '{}'", uniqueSlug, user.getUsername());
            }
        }
    }

    private void generateCodingQuestionUrlSlugs() {
        List<CodingQuestion> questionsWithoutSlugs = codingQuestionRepository.findByDeletedFalseWithSource()
                .stream()
                .filter(question -> question.getUrlSlug() == null || question.getUrlSlug().trim().isEmpty())
                .toList();

        if (!questionsWithoutSlugs.isEmpty()) {
            log.info("Generating URL slugs for {} coding questions", questionsWithoutSlugs.size());
            
            Set<String> existingSlugs = new HashSet<>();
            codingQuestionRepository.findByDeletedFalseWithSource().forEach(question -> {
                if (question.getUrlSlug() != null && !question.getUrlSlug().trim().isEmpty()) {
                    existingSlugs.add(question.getUrlSlug());
                }
            });

            for (CodingQuestion question : questionsWithoutSlugs) {
                String sourceCode = question.getSource() != null ? question.getSource().getCode() : "";
                String baseSlug = UrlSlugGenerator.generateQuestionSlug(question.getTitle(), sourceCode);
                String uniqueSlug = UrlSlugGenerator.generateUniqueSlug(baseSlug, existingSlugs);
                question.setUrlSlug(uniqueSlug);
                existingSlugs.add(uniqueSlug);
                codingQuestionRepository.save(question);
                log.debug("Generated URL slug '{}' for question '{}'", uniqueSlug, question.getTitle());
            }
        }
    }
}