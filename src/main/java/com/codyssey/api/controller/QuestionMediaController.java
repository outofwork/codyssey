package com.codyssey.api.controller;

import com.codyssey.api.dto.media.*;
import com.codyssey.api.service.QuestionMediaService;
import com.codyssey.api.dto.media.MediaSequenceDto;
import com.codyssey.api.validation.ValidId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST Controller for QuestionMedia operations
 * <p>
 * Provides endpoints for media file management including creation, retrieval,
 * updating, deletion, upload/download, and bulk operations for coding question media.
 */
@RestController
@RequestMapping("/v1/media")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Question Media Management", description = "APIs for managing coding question media files (images, diagrams, videos)")
public class QuestionMediaController {

    private final QuestionMediaService mediaService;

    @Operation(summary = "Create a new media file", description = "Creates a new media file reference for a coding question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Media file created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "409", description = "Sequence number already exists for the question")
    })
    @PostMapping
    public ResponseEntity<QuestionMediaDto> createMedia(
            @Parameter(description = "Media creation data", required = true)
            @Valid @RequestBody QuestionMediaCreateDto createDto) {

        log.info("POST /v1/media - Creating media for question: {}", createDto.getQuestionId());
        QuestionMediaDto createdMedia = mediaService.createMedia(createDto);
        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple media files in bulk", description = "Creates multiple media file references for one or more coding questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Media files created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "One or more questions not found"),
            @ApiResponse(responseCode = "409", description = "One or more sequence conflicts")
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<QuestionMediaDto>> createMediaBulk(
            @Parameter(description = "Bulk media creation data", required = true)
            @Valid @RequestBody QuestionMediaBulkCreateDto bulkCreateDto) {

        log.info("POST /v1/media/bulk - Creating {} media files in bulk", bulkCreateDto.getMediaList().size());
        List<QuestionMediaDto> createdMedia = mediaService.createMediaBulk(bulkCreateDto);
        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
    }

    @Operation(summary = "Upload media file", description = "Uploads a media file and creates a reference for a coding question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Media file uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file or data"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @PostMapping("/upload")
    public ResponseEntity<QuestionMediaDto> uploadMedia(
            @Parameter(description = "Question ID", required = true)
            @RequestParam @ValidId String questionId,
            @Parameter(description = "Sequence number", required = true)
            @RequestParam Integer sequence,
            @Parameter(description = "Media type (IMAGE, DIAGRAM, VIDEO)", required = true)
            @RequestParam String mediaType,
            @Parameter(description = "Media file", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Description", required = false)
            @RequestParam(required = false) String description) {

        log.info("POST /v1/media/upload - Uploading media file for question: {}", questionId);
        
        try {
            QuestionMediaDto uploadedMedia = mediaService.uploadMedia(
                    questionId, sequence, mediaType, file.getOriginalFilename(), 
                    file.getBytes(), description);
            return new ResponseEntity<>(uploadedMedia, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to upload media file: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all media files", description = "Retrieves all media files (non-deleted)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media files retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<QuestionMediaDto>> getAllMedia() {
        log.info("GET /v1/media - Retrieving all media files");
        List<QuestionMediaDto> mediaFiles = mediaService.getAllMedia();
        return ResponseEntity.ok(mediaFiles);
    }

    @Operation(summary = "Get media file by ID", description = "Retrieves a specific media file by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media file retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Media file not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuestionMediaDto> getMediaById(
            @Parameter(description = "Media ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/media/{} - Retrieving media by ID", id);
        return mediaService.getMediaById(id)
                .map(media -> ResponseEntity.ok(media))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Download media file", description = "Downloads the actual media file content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media file downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Media file not found")
    })
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadMedia(
            @Parameter(description = "Media ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("GET /v1/media/{}/download - Downloading media file", id);
        
        try {
            byte[] fileData = mediaService.downloadMedia(id);
            QuestionMediaDto media = mediaService.getMediaById(id)
                    .orElseThrow(() -> new RuntimeException("Media not found"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", media.getFileName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData);
        } catch (Exception e) {
            log.error("Failed to download media file: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update media file", description = "Updates an existing media file reference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media file updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Media file not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuestionMediaDto> updateMedia(
            @Parameter(description = "Media ID", required = true)
            @PathVariable @ValidId String id,
            @Parameter(description = "Media update data", required = true)
            @Valid @RequestBody com.codyssey.api.dto.media.QuestionMediaUpdateDto updateDto) {

        log.info("PUT /v1/media/{} - Updating media", id);
        QuestionMediaDto updatedMedia = mediaService.updateMedia(id, updateDto);
        return ResponseEntity.ok(updatedMedia);
    }

    @Operation(summary = "Delete media file", description = "Soft deletes a media file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Media file deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Media file not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(
            @Parameter(description = "Media ID", required = true)
            @PathVariable @ValidId String id) {

        log.info("DELETE /v1/media/{} - Deleting media", id);
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get media files by question", description = "Retrieves all media files for a specific question ordered by sequence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media files retrieved successfully")
    })
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<QuestionMediaDto>> getMediaByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/media/question/{} - Retrieving media by question ID", questionId);
        List<QuestionMediaDto> mediaFiles = mediaService.getMediaByQuestionId(questionId);
        return ResponseEntity.ok(mediaFiles);
    }

    @Operation(summary = "Get media files by type", description = "Retrieves media files filtered by media type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media files retrieved successfully")
    })
    @GetMapping("/type/{mediaType}")
    public ResponseEntity<List<QuestionMediaDto>> getMediaByType(
            @Parameter(description = "Media type (IMAGE, DIAGRAM, VIDEO)", required = true)
            @PathVariable String mediaType) {

        log.info("GET /v1/media/type/{} - Retrieving media by type", mediaType);
        List<QuestionMediaDto> mediaFiles = mediaService.getMediaByType(mediaType);
        return ResponseEntity.ok(mediaFiles);
    }

    @Operation(summary = "Get media files by question and type", description = "Retrieves media files for a question filtered by media type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media files retrieved successfully")
    })
    @GetMapping("/question/{questionId}/type/{mediaType}")
    public ResponseEntity<List<QuestionMediaDto>> getMediaByQuestionIdAndType(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "Media type", required = true)
            @PathVariable String mediaType) {

        log.info("GET /v1/media/question/{}/type/{} - Retrieving media by question and type", questionId, mediaType);
        List<QuestionMediaDto> mediaFiles = mediaService.getMediaByQuestionIdAndType(questionId, mediaType);
        return ResponseEntity.ok(mediaFiles);
    }

    @Operation(summary = "Check sequence availability", description = "Checks if a sequence number is available for a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability check completed")
    })
    @GetMapping("/check-sequence")
    public ResponseEntity<Boolean> checkSequenceAvailability(
            @Parameter(description = "Question ID", required = true)
            @RequestParam @ValidId String questionId,
            @Parameter(description = "Sequence number", required = true)
            @RequestParam Integer sequence) {

        log.info("GET /v1/media/check-sequence?questionId={}&sequence={} - Checking sequence availability", questionId, sequence);
        boolean isAvailable = mediaService.checkSequenceAvailability(questionId, sequence);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "Get next sequence number", description = "Gets the next available sequence number for a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Next sequence number retrieved successfully")
    })
    @GetMapping("/next-sequence/{questionId}")
    public ResponseEntity<Integer> getNextSequenceNumber(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/media/next-sequence/{} - Getting next sequence number", questionId);
        Integer nextSequence = mediaService.getNextSequenceNumber(questionId);
        return ResponseEntity.ok(nextSequence);
    }

    @Operation(summary = "Reorder media files", description = "Reorders media files for a question by updating their sequences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Media files reordered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid reorder data"),
            @ApiResponse(responseCode = "404", description = "Question or media files not found")
    })
    @PutMapping("/reorder/{questionId}")
    public ResponseEntity<Void> reorderMedia(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "List of media IDs with new sequences", required = true)
            @Valid @RequestBody List<MediaSequenceDto> mediaSequences) {

        log.info("PUT /v1/media/reorder/{} - Reordering {} media files", questionId, mediaSequences.size());
        mediaService.reorderMedia(questionId, mediaSequences);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search media by filename", description = "Searches media files by filename containing specific text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<List<QuestionMediaDto>> searchMediaByFileName(
            @Parameter(description = "Search term", required = true)
            @RequestParam String q) {

        log.info("GET /v1/media/search?q={} - Searching media by filename", q);
        List<QuestionMediaDto> mediaFiles = mediaService.searchMediaByFileName(q);
        return ResponseEntity.ok(mediaFiles);
    }

    @Operation(summary = "Delete all media for question", description = "Deletes all media files for a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All media files deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<Void> deleteAllMediaForQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("DELETE /v1/media/question/{} - Deleting all media for question", questionId);
        mediaService.deleteAllMediaForQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get media count", description = "Gets the count of media files for a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media count retrieved successfully")
    })
    @GetMapping("/question/{questionId}/count")
    public ResponseEntity<Long> getMediaCountByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/media/question/{}/count - Getting media count", questionId);
        Long count = mediaService.getMediaCountByQuestionId(questionId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Find orphaned media", description = "Finds media files that exist but their questions are deleted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orphaned media files retrieved successfully")
    })
    @GetMapping("/orphaned")
    public ResponseEntity<List<QuestionMediaDto>> findOrphanedMediaFiles() {
        log.info("GET /v1/media/orphaned - Finding orphaned media files");
        List<QuestionMediaDto> orphanedMedia = mediaService.findOrphanedMediaFiles();
        return ResponseEntity.ok(orphanedMedia);
    }

    @Operation(summary = "Clone media files", description = "Clones media files from one question to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Media files cloned successfully"),
            @ApiResponse(responseCode = "404", description = "Source or target question not found")
    })
    @PostMapping("/clone")
    public ResponseEntity<List<QuestionMediaDto>> cloneMedia(
            @Parameter(description = "Source question ID", required = true)
            @RequestParam @ValidId String sourceQuestionId,
            @Parameter(description = "Target question ID", required = true)
            @RequestParam @ValidId String targetQuestionId) {

        log.info("POST /v1/media/clone?sourceQuestionId={}&targetQuestionId={} - Cloning media", sourceQuestionId, targetQuestionId);
        List<QuestionMediaDto> clonedMedia = mediaService.cloneMedia(sourceQuestionId, targetQuestionId);
        return new ResponseEntity<>(clonedMedia, HttpStatus.CREATED);
    }
}