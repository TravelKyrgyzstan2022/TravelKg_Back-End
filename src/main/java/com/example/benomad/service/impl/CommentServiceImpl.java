package com.example.benomad.service.impl;

import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.entity.User;
import com.example.benomad.enums.CommentReference;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.*;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;
import com.example.benomad.logger.LogWriterServiceImpl;
import com.example.benomad.mapper.CommentMapper;
import com.example.benomad.mapper.DeletionInfoMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.CommentRepository;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;
    private final PlaceRepository placeRepository;
    private final BlogRepository blogRepository;
    private final CommentMapper commentMapper;
    private final DeletionInfoMapper deletionInfoMapper;
    private final LogWriterServiceImpl logWriter;

    @Override
    public List<CommentDTO> getAllComments() {
        List<CommentDTO> commentDTOS = commentMapper.entityListToDtoList(commentRepository.findAll());
        logWriter.get(String.format("%s - Returned %d comments", authService.getCurrentEmail(), commentDTOS.size()));
        return commentDTOS;
    }

    @Override
    public List<CommentDTO> getReferenceCommentsById(Long referenceId, CommentReference reference,
                                                     PageRequest pageRequest) {
        Page<Comment> page;
        if(reference == CommentReference.BLOG){
            if(!blogRepository.existsById(referenceId)){
                throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, "id", String.valueOf(referenceId));
            }
            page = commentRepository.getBlogCommentsById(referenceId, pageRequest);
        }else{
            if(!placeRepository.existsById(referenceId)){
                throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(referenceId));
            }
            page = commentRepository.getPlaceCommentsById(referenceId, pageRequest);
        }
        List<CommentDTO> commentDTOS = commentMapper.entityListToDtoList(page.stream().collect(Collectors.toList()));
        logWriter.get(String.format("%s - Returned %d comments for %s with id = %d", authService.getCurrentEmail(), commentDTOS.size(),
                reference.toString(), referenceId));
        return commentDTOS;
    }

    private void checkComment(Long commentId){
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        Comment comment = getCommentEntityById(commentId);
        if(!comment.getUser().getId().equals(user.getId())){
            throw new NoAccessException();
        }
    }

    public CommentDTO deleteMyComment(Long commentId){
        checkUserActivation();
        checkComment(commentId);
        Comment comment = getCommentEntityById(commentId);
        comment.setDeleted(true);
        commentRepository.save(comment);
        return commentMapper.entityToDto(comment);
    }

    public CommentDTO updateMyComment(CommentDTO commentDTO, Long commentId){
        checkUserActivation();
        checkComment(commentId);
        commentDTO.setId(authService.getCurrentUserId());
        Comment comment = commentMapper.dtoToEntity(commentDTO);
        comment.setUpdateDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        comment.setId(commentId);
        commentDTO = commentMapper.entityToDto(commentRepository.save(comment));
        return commentDTO;
    }

    @Override
    public CommentDTO getCommentById(Long commentId) throws ContentNotFoundException {
        CommentDTO commentDTO = commentMapper.entityToDto(commentRepository.findById(commentId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.COMMENT, "id", String.valueOf(commentId));
                })
        );
        logWriter.get(String.format("%s - Returned comment with id = %d", authService.getCurrentEmail(), commentId));
        return commentDTO;
    }
    private void checkUserActivation(){
        Long userId = authService.getCurrentUserId();
        User user = userService.getUserEntityById(userId);
        if(!user.getIsActivated()){
            throw new UserNotActivatedException();
        }
    }


    @Override
    public CommentDTO likeDislikeComment(Long commentId, boolean isDislike){
        Long userId = authService.getCurrentUserId();
        checkUserActivation();

        if(!commentRepository.existsById(commentId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.COMMENT, "id", String.valueOf(commentId));
        }
        if(!userRepository.existsById(userId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
        }

        boolean isAlreadyLiked = commentRepository.isCommentLikedByUser(commentId, userId);

        if(isDislike){
            if(!isAlreadyLiked){
                throw new ContentIsNotLikedException(ContentNotFoundEnum.COMMENT);
            }
            commentRepository.dislikeCommentById(commentId, userId);
        }else{
            if(isAlreadyLiked){
                throw new ContentIsAlreadyLikedException(ContentNotFoundEnum.COMMENT);
            }
            commentRepository.likeCommentById(commentId, userId);
        }
        logWriter.update(String.format("%s - %s comment with id = %d", authService.getCurrentEmail(),
                isDislike ? "Disliked" : "Liked", commentId));
        return commentMapper.entityToDto(
                commentRepository.findById(commentId).orElseThrow(
                        () -> {
                            throw new ContentNotFoundException(ContentNotFoundEnum.COMMENT, "id",
                                    String.valueOf(commentId));
                        })
        );
    }

    @Override
    public CommentDTO insertComment(CommentReference reference, Long referenceId, CommentDTO commentDTO) {
        commentDTO.setId(null);
        commentDTO.setReference(reference);
        commentDTO.setReferenceId(referenceId);
        checkUserActivation();

        Comment comment = commentMapper.dtoToEntity(commentDTO);
        comment.setCreationDate(LocalDate.now());
        commentRepository.save(comment);
        commentDTO.setId(commentRepository.getLastValueOfSequence());

        if(reference == CommentReference.PLACE){
            commentRepository.insertPlaceComment(commentDTO.getId(), referenceId);
        }else if(reference == CommentReference.BLOG){
            commentRepository.insertBlogComment(commentDTO.getId(), referenceId);
        }
        logWriter.insert(String.format("%s - commented %s with id = %d", authService.getCurrentEmail(), reference.toString(),
                referenceId));
        return commentDTO;
    }

    @Override
    public CommentDTO deleteCommentById(Long commentId, DeletionInfoDTO infoDTO) throws ContentNotFoundException {
        Comment comment = getCommentEntityById(commentId);
        comment.setDeleted(true);
        infoDTO.setDeletionDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        comment.setDeletionInfo(deletionInfoMapper.dtoToEntity(infoDTO));
        logWriter.delete(String.format("%s - Deleted comment with id = %d", authService.getCurrentEmail(), commentId));
        return commentMapper.entityToDto(comment);
    }

    @Override
    public CommentDTO updateCommentById(Long commentId, CommentDTO commentDTO) throws ContentNotFoundException {
        commentDTO.setId(commentId);
        Comment comment = commentMapper.dtoToEntity(commentDTO);
        comment.setUpdateDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        comment.setCreationDate(null);
        commentRepository.save(comment);
        logWriter.update(String.format("%s - Updated comment with id = %d", authService.getCurrentEmail(), commentId));
        return commentDTO;
    }

    private Comment getCommentEntityById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.COMMENT, "id", String.valueOf(commentId));
                });
    }
}
